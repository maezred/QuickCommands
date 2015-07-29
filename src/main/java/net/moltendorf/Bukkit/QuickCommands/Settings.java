package net.moltendorf.Bukkit.QuickCommands;

import net.moltendorf.Bukkit.QuickCommands.storage.AbstractStorage;
import net.moltendorf.Bukkit.QuickCommands.storage.MySQL;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Settings class.
 * Created by moltendorf on 14/09/03.
 *
 * @author moltendorf
 */
public class Settings {
	public static Settings getInstance() {
		return QuickCommands.getInstance().settings;
	}

	final private FileConfiguration config;
	private       boolean           dirty;

	public boolean isEnabled() {
		return enabled;
	}

	private boolean enabled = true; // Whether or not the plugin is enabled at all; interface mode.

	public boolean isCreativeInventory() {
		return creativeInventory;
	}

	private boolean creativeInventory = true;

	public AbstractStorage getStorage() {
		return storage;
	}

	private AbstractStorage storage = null;

	final protected Set<Material> equipment = new HashSet<>(Arrays.asList(
		Material.DIAMOND_HELMET,
		Material.DIAMOND_CHESTPLATE,
		Material.DIAMOND_LEGGINGS,
		Material.DIAMOND_BOOTS,

		Material.IRON_HELMET,
		Material.IRON_CHESTPLATE,
		Material.IRON_LEGGINGS,
		Material.IRON_BOOTS,

		Material.CHAINMAIL_HELMET,
		Material.CHAINMAIL_CHESTPLATE,
		Material.CHAINMAIL_LEGGINGS,
		Material.CHAINMAIL_BOOTS,

		Material.GOLD_HELMET,
		Material.GOLD_CHESTPLATE,
		Material.GOLD_LEGGINGS,
		Material.GOLD_BOOTS,

		Material.LEATHER_HELMET,
		Material.LEATHER_CHESTPLATE,
		Material.LEATHER_LEGGINGS,
		Material.LEATHER_BOOTS,

		Material.DIAMOND_AXE,
		Material.DIAMOND_PICKAXE,
		Material.DIAMOND_SPADE,
		Material.DIAMOND_SWORD,
		Material.DIAMOND_HOE,

		Material.IRON_AXE,
		Material.IRON_PICKAXE,
		Material.IRON_SPADE,
		Material.IRON_SWORD,
		Material.IRON_HOE,

		Material.STONE_AXE,
		Material.STONE_PICKAXE,
		Material.STONE_SPADE,
		Material.STONE_SWORD,
		Material.STONE_HOE,

		Material.WOOD_AXE,
		Material.WOOD_PICKAXE,
		Material.WOOD_SPADE,
		Material.WOOD_SWORD,
		Material.WOOD_HOE,

		Material.GOLD_AXE,
		Material.GOLD_PICKAXE,
		Material.GOLD_SPADE,
		Material.GOLD_SWORD,
		Material.GOLD_HOE,

		Material.BOW,

		Material.FISHING_ROD,

		Material.SHEARS,

		Material.FLINT_AND_STEEL
	));

	final protected Set<Material> tools = new HashSet<>(Arrays.asList(
		Material.DIAMOND_AXE,
		Material.DIAMOND_PICKAXE,
		Material.DIAMOND_SPADE,
		Material.DIAMOND_SWORD,
		Material.DIAMOND_HOE,

		Material.IRON_AXE,
		Material.IRON_PICKAXE,
		Material.IRON_SPADE,
		Material.IRON_SWORD,
		Material.IRON_HOE,

		Material.STONE_AXE,
		Material.STONE_PICKAXE,
		Material.STONE_SPADE,
		Material.STONE_SWORD,
		Material.STONE_HOE,

		Material.WOOD_AXE,
		Material.WOOD_PICKAXE,
		Material.WOOD_SPADE,
		Material.WOOD_SWORD,
		Material.WOOD_HOE,

		Material.GOLD_AXE,
		Material.GOLD_PICKAXE,
		Material.GOLD_SPADE,
		Material.GOLD_SWORD,
		Material.GOLD_HOE,

		Material.BOW,

		Material.FISHING_ROD,

		Material.SHEARS,

		Material.FLINT_AND_STEEL
	));

	public Settings() {
		final QuickCommands instance = QuickCommands.getInstance();
		final Logger        log      = instance.getLogger();

		// Make sure the default configuration is saved.
		instance.saveDefaultConfig();

		config = instance.getConfig();

		if (config.isBoolean("enabled")) {
			enabled = config.getBoolean("enabled", enabled);
		} else {
			set("enabled", enabled);
		}

		if (config.isBoolean("creative-inventory")) {
			creativeInventory = config.getBoolean("creative-inventory", creativeInventory);
		} else {
			set("creative-inventory", creativeInventory);
		}

		if (config.isConfigurationSection("storage")) {
			final ConfigurationSection storageSection = config.getConfigurationSection("storage");

			if (storageSection.isBoolean("enabled") && storageSection.getBoolean("storage.enabled", false) && storageSection.isString("type")) {
				switch (storageSection.getString("type", "")) {
					case "mysql":
						storage = new MySQL(storageSection);
				}
			}
		}

		save();
	}

	private void save() {
		if (dirty) {
			QuickCommands.getInstance().saveConfig();
			dirty = false;
		}
	}

	public void set(final String path, final Object value) {
		config.set(path, value);
		dirty = true;
	}
}
