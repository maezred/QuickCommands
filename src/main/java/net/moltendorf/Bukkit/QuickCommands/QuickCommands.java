package net.moltendorf.Bukkit.QuickCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

		// Construct new settings.
		settings = new Settings();

		// Are we enabled?
		if (!settings.global.enabled) {
			return;
		}

		// Create our commands instance.
		commands = new Commands();

		// Register our commands.
		for (final String methodName : getDescription().getCommands().keySet()) {
			try {
				getLogger().info("Enabling command: /" + methodName + ".");

				final Method method = commands.getClass().getMethod(methodName, CommandSender.class, Command.class, String.class, String[].class);

				final CommandExecutor executor = (final CommandSender a, final Command b, final String c, final String[] d) -> {
					try {
						return (boolean)method.invoke(commands, a, b, c, d);
					} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException exception) {
						getLogger().info("Error executing command: /" + methodName + ".");
						exception.printStackTrace();
					}

					return false;
				};

				getCommand(methodName).setExecutor(executor);
			} catch (NoSuchMethodException exception) {
				getLogger().info("Failed to enable command: /" + methodName + ".");
				exception.printStackTrace();
			}
		}
	}

	@Override
	public synchronized void onDisable() {
		instance = null;
	}
}
