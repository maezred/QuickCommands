package net.moltendorf.Bukkit.QuickCommands.storage;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by moltendorf on 15/07/27.
 *
 * @author moltendorf
 */
abstract public class AbstractStorage {
	public void setSpawnForPlayer(final Player player) {
		setSpawnForPlayer(player, player.getLocation());
	}

	abstract public void setSpawnForPlayer(final Player player, final Location location);

	public Location getSpawnForPlayer(final Player player) {
		return getSpawnForPlayer(player, player.getLocation());
	}

	abstract public Location getSpawnForPlayer(final Player player, final Location location);
}
