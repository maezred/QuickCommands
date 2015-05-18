package net.moltendorf.Bukkit.QuickCommands;

import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

/**
 * Created by moltendorf on 15/04/03.
 *
 * @author moltendorf
 */
public class QuickCommands extends JavaPlugin {

	// Main instance.
	private static QuickCommands instance = null;

	// Variable data.
	protected Settings settings = null;
	protected Commands commands = null;

	public static QuickCommands getInstance() {
		return instance;
	}

	@Override
	public synchronized void onEnable() {
		instance = this;

		saveDefaultConfig();

		// Construct new settings.
		settings = new Settings();

		// Are we enabled?
		if (!settings.enabled) {
			return;
		}

		// Create our commands instance.
		commands = new Commands();

		// Register our commands.
		for (final String commandName : getDescription().getCommands().keySet()) {
			try {
				getLogger().info("Enabling command: /" + commandName + ".");

				final String methodName = commandName.replace('-', '_');
				final Method method = commands.getClass().getMethod(methodName, CommandSender.class, Command.class, String.class, String[].class);

				final CommandExecutor executor = (final CommandSender a, final Command b, final String c, final String[] d) -> {
					try {
						return (boolean)method.invoke(commands, a, b, c, d);
					} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException exception) {
						getLogger().info("Error executing command: /" + commandName + ".");
						exception.printStackTrace();
					}

					return false;
				};

				getCommand(commandName).setExecutor(executor);
			} catch (NoSuchMethodException | NullPointerException exception) {
				getLogger().info("Failed to enable command: /" + commandName + ".");
				exception.printStackTrace();
			}
		}

		final Server server = getServer();

		// Register our listeners.
		server.getPluginManager().registerEvents(new Listeners(), this);

		server.getOnlinePlayers().stream().filter(player -> player.getGameMode() == GameMode.CREATIVE).forEach(PlayerBackupManager::backup);
	}

	@Override
	public synchronized void onDisable() {
		for (final Map.Entry<UUID, Stack<PlayerBackup>> entry : PlayerBackupManager.inventories.entrySet()) {
			final Stack<PlayerBackup> backups = entry.getValue();

			PlayerBackup backup = null;

			while (!backups.empty()) {
				backup = backups.pop();
			}

			if (backup != null) {
				backup.restore(getServer().getPlayer(entry.getKey()));
			}
		}

		PlayerBackupManager.inventories.clear();

		instance = null;
	}
}
