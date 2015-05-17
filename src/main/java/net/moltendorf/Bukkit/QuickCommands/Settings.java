package net.moltendorf.Bukkit.QuickCommands;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Settings class.
 * Created by moltendorf on 14/09/03.
 *
 * @author moltendorf
 */
public class Settings {

	static protected class Global {

		// Final data.
		final protected boolean enabled = true; // Whether or not the plugin is enabled at all; interface mode.

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
	}

	// Final data.
	final protected Global global = new Global();

	public Settings() {
		// Placeholder.
	}
}
