package net.moltendorf.Bukkit.QuickCommands.storage;

import net.moltendorf.Bukkit.QuickCommands.QuickCommands;
import net.moltendorf.Bukkit.QuickCommands.Settings;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.sql.*;

/**
 * Created by moltendorf on 15/07/27.
 *
 * @author moltendorf
 */
public class MySQL extends AbstractStorage {
	String host     = "localhost";
	int    port     = 3306;
	String prefix   = "qc__";
	String database = "database";
	String username = "username";
	String password = "password";

	Connection connection;

	public MySQL(final ConfigurationSection storageSection) {
		if (storageSection.isString("host")) {
			host = storageSection.getString("host", host);
		} else {
			set("host", host);
		}

		if (storageSection.isInt("port")) {
			port = storageSection.getInt("port", port);
		} else {
			set("port", port);
		}

		if (storageSection.isString("prefix")) {
			prefix = storageSection.getString("prefix", prefix);
		} else {
			set("prefix", prefix);
		}

		if (storageSection.isString("database")) {
			database = storageSection.getString("database", database);
		} else {
			set("database", database);
		}

		if (storageSection.isString("username")) {
			username = storageSection.getString("username", username);
		} else {
			set("username", username);
		}

		if (storageSection.isString("password")) {
			password = storageSection.getString("password", password);
		} else {
			set("password", password);
		}

		try {
			String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&dontTrackOpenResources=true";
			connection = DriverManager.getConnection(url, username, password);
		} catch (final Exception exception) {
			QuickCommands.getInstance().getLogger().warning("Could not connect to MySQL database with provided information.");
		}
	}

	private void set(final String path, final Object value) {
		Settings.getInstance().set("storage." + path, value);
	}

	@Override
	public void setSpawnForPlayer(final Player player, final Location location) {
		final String id = player.getUniqueId().toString().replace("-", "");

		double x, y, z;

		x = location.getX();
		y = location.getY();
		z = location.getZ();

		float yaw = location.getYaw();

		try {
			final PreparedStatement statement = connection.prepareStatement(
				"insert into " + prefix + "spawns " +
					"(id, x, y, z, yaw, world) " +
					"values (UNHEX(?), ?, ?, ?, ?, ?) " +
					"on duplicate key update " +
					"x = ? " +
					"y = ? " +
					"z = ? " +
					"yaw = ?"
			);

			int i = 0;

			statement.setString(++i, id);
			statement.setDouble(++i, x);
			statement.setDouble(++i, y);
			statement.setDouble(++i, z);
			statement.setFloat(++i, yaw);
			statement.setString(++i, location.getWorld().getName());

			// Update values.
			statement.setDouble(++i, x);
			statement.setDouble(++i, y);
			statement.setDouble(++i, z);
			statement.setFloat(++i, yaw);

			statement.executeUpdate();
		} catch (final SQLException exception) {
			QuickCommands.getInstance().getLogger().warning("Failed to set spawn for player " + player.getName() + ".");
		}
	}

	@Override
	public Location getSpawnForPlayer(final Player player, final Location location) {
		final String id    = player.getUniqueId().toString().replace("-", "");
		final World  world = location.getWorld();

		try {
			final PreparedStatement statement = connection.prepareStatement(
				"select x, y, z, yaw " +
					"from " + prefix + "spawns " +
					"where id = UNHEX(?) and world = ?"
			);

			int i = 0;

			statement.setString(++i, id);
			statement.setString(++i, world.getName());

			final ResultSet result = statement.executeQuery();

			i = 0;

			final double x, y, z;
			x = result.getDouble(++i);
			y = result.getDouble(++i);
			z = result.getDouble(++i);

			final float yaw, pitch;
			yaw = result.getFloat(++i);
			pitch = player.getLocation().getPitch();

			return new Location(world, x, y, z, yaw, pitch);
		} catch (final SQLException exception) {
			QuickCommands.getInstance().getLogger().warning("Failed to get spawn for player " + player.getName() + ".");
		}

		return null;
	}
}
