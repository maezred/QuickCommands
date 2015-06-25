package net.moltendorf.Bukkit.QuickCommands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by moltendorf on 15/05/17.
 *
 * @author moltendorf
 */
public class PlayerBackup {
	public int experience;

	public int heldItemSlot;

	public ItemStack[] armorContents;
	public ItemStack[] contents;

	protected PlayerBackup(final Player player, final boolean clear) {
		// Create wand item.

		final ItemStack wand = new ItemStack(Material.STICK);

		// Store items.

		final PlayerInventory inventory = player.getInventory();

		heldItemSlot = inventory.getHeldItemSlot();
		armorContents = inventory.getArmorContents();
		contents = inventory.getContents();

		experience = (new ExperienceManager(player)).getCurrentExp();

		// Set default items.

		if (clear) {
			inventory.clear();
			inventory.setItem(0, wand);
			inventory.setHeldItemSlot(0);

			player.sendMessage("§2Your inventory has been backed up, and was replaced with the creative inventory.");
		} else {
			player.sendMessage("§2Your inventory has been backed up.");
		}
	}

	protected void restore(final Player player) {
		final PlayerInventory inventory = player.getInventory();

		inventory.clear();

		inventory.setHeldItemSlot(heldItemSlot);
		inventory.setArmorContents(armorContents);
		inventory.setContents(contents);

		(new ExperienceManager(player)).setExp(experience);

		player.updateInventory();

		player.sendMessage("§2Your inventory has been restored.");
	}
}
