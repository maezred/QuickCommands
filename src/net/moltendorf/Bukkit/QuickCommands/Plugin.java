package net.moltendorf.Bukkit.QuickCommands;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by moltendorf on 15/04/03.
 *
 * @author moltendorf
 */
public class Plugin extends JavaPlugin {

	// Variable data.
	protected Configuration configuration = null;
	protected Commands commands = null;

	@Override
	public synchronized void onDisable() {
		// Do nothing.
	}

	@Override
	public synchronized void onEnable() {

		// Construct new configuration.
		configuration = new Configuration();

		// Are we enabled?
		if (!configuration.global.enabled) {
			return;
		}

		// Create our commands instance.
		commands = new Commands(this);

		// Register our commands.
		getCommand("colors").setExecutor(commands::colors);
		getCommand("durability").setExecutor(commands::durability);
	}
}
