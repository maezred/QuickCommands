package net.moltendorf.Bukkit.QuickCommands;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by moltendorf on 15/04/03.
 *
 * @author moltendorf
 */
public class Plugin extends JavaPlugin {

	// Main instance.
	public static Plugin instance = null;

	// Variable data.
	protected Configuration configuration = null;
	protected Commands commands = null;

	@Override
	public synchronized void onDisable() {
		instance = null;
	}

	@Override
	public synchronized void onEnable() {
		// Store reference to this.
		instance = this;

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
		getCommand("hide").setExecutor(commands::hide);
		getCommand("show").setExecutor(commands::show);
		getCommand("health").setExecutor(commands::health);
		getCommand("drop").setExecutor(commands::drop);
		getCommand("cleanup").setExecutor(commands::cleanup);
	}
}
