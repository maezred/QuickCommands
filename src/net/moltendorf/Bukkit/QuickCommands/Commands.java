package net.moltendorf.Bukkit.QuickCommands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Listener register.
 *
 * @author moltendorf
 */
public class Commands {

	final protected Plugin plugin;

	protected Commands(final Plugin instance) {
		plugin = instance;
	}

	protected boolean colors(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			commandSender.sendMessage("All chat colors:\n" +
				"§r&0: §0BLACK §r&1: §1DARK BLUE §r&2: §2DARK GREEN §r&3: §3DARK AQUA\n" +
				"§r&4: §4DARK RED §r&5: §5DARK PURPLE §r&6: §6GOLD §r&7: §7GRAY\n" +
				"§r&8: §8DARK GRAY §r&9: §9BLUE §r&a: §aGREEN §r&b: §bAQUA\n" +
				"§r&c: §cRED §r&d: §dLIGHT PURPLE §r&e: §eYELLOW §r&f: §fWHITE");
		} else {
			commandSender.sendMessage("All chat colors:\n" +
				"§r§§r0: §0BLACK §r§§r1: §1DARK BLUE §r§§r2: §2DARK GREEN §r§§r3: §3DARK AQUA\n" +
				"§r§§r4: §4DARK RED §r§§r5: §5DARK PURPLE §r§§r6: §6GOLD §r§§r7: §7GRAY\n" +
				"§r§§r8: §8DARK GRAY §r§§r9: §9BLUE §r§§ra: §aGREEN §r§§rb: §bAQUA\n" +
				"§r§§rc: §cRED §r§§rd: §dLIGHT PURPLE §r§§re: §eYELLOW §r§§rf: §fWHITE");
		}

		return true;
	}

	protected boolean durability(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			if (strings.length > 1) {
				commandSender.sendMessage("Invalid number of arguments.");

				return false;
			}

			Player player = (Player) commandSender;

			ItemStack item = player.getItemInHand();
			Material type = item.getType();

			if (plugin.configuration.global.equipment.contains(type)) {
				short max = item.getType().getMaxDurability();

				if (strings.length == 1) {
					if (commandSender.isOp()) {
						try {
							short durability = Short.parseShort(strings[0]);

							if (durability == -1) {
								durability = 0;
							} else {
								durability = (short) (max - durability);
							}

							item.setDurability(durability);
						} catch (final NumberFormatException exception) {
							commandSender.sendMessage("Invalid durability specified.");

							return false;
						}
					} else {
						commandSender.sendMessage("You must be an operator to set an item's durability.");

						return true;
					}
				}

				int durability = max - item.getDurability();

				double percentage = (double) durability / (double) max * 100.;

				String message;

				String name = item.getItemMeta().getDisplayName();

				if (name == null) {
					message = type.toString();
				} else {
					message = "§b" + name + "§r";
				}

				String color;

				if (percentage > 66.666) {
					color = "§a";
				} else if (percentage > 33.333) {
					color = "§e";
				} else {
					color = "§c";
				}

				message += " has " + color + durability + "/" + max + "§r durability";

				int unbreaking = item.getEnchantmentLevel(Enchantment.DURABILITY);

				if (unbreaking > 0) {
					if (plugin.configuration.global.tools.contains(type)) {
						// Tool unbreaking.
						durability = (int) ((double) durability / ((100. / ((double) unbreaking + 1.)) / 100.));
					} else {
						// Armor unbreaking.
						durability = (int) ((double) durability / ((60. + (40. / ((double) unbreaking + 1.))) / 100.));
					}
					percentage = (double) durability / (double) max * 100.;

					if (percentage > 66.666) {
						color = "§a";
					} else if (percentage > 33.333) {
						color = "§e";
					} else {
						color = "§c";
					}

					message += " (about " + color + durability + "§r uses left).";
				} else {
					message += ".";
				}

				commandSender.sendMessage(message);

				return true;
			}

			commandSender.sendMessage("§7Item has no durability.");

			return true;
		}

		commandSender.sendMessage("You must be a player to use this command.");

		return true;
	}

	protected boolean hide(CommandSender commandSender, Command command, String s, String[] strings) {
		if (!commandSender.isOp()) {
			commandSender.sendMessage("You must be an operator to use this command.");

			return true;
		}

		final Player player1, player2;

		if (strings.length == 1) {
			if (commandSender instanceof Player) {
				player1 = (Player) commandSender;
				player2 = plugin.getServer().getPlayerExact(strings[0]);
			} else {
				commandSender.sendMessage("You must be a player to use this command with only one argument.");

				return false;
			}
		} else if (strings.length == 2) {
			player1 = plugin.getServer().getPlayerExact(strings[0]);
			player2 = plugin.getServer().getPlayerExact(strings[1]);
		} else {
			commandSender.sendMessage("Invalid number of arguments.");

			return false;
		}

		if (player1 == null) {
			commandSender.sendMessage("Could not find player " + strings[0] + ".");

			return true;
		}

		if (player2 == null) {
			commandSender.sendMessage("Could not find player " + strings[1] + ".");

			return true;
		}

		player2.hidePlayer(player1);

		return true;
	}

	protected boolean show(CommandSender commandSender, Command command, String s, String[] strings) {
		if (!commandSender.isOp()) {
			commandSender.sendMessage("You must be an operator to use this command.");

			return true;
		}

		final Player player1, player2;

		if (strings.length == 1) {
			if (commandSender instanceof Player) {
				player1 = (Player) commandSender;
				player2 = plugin.getServer().getPlayerExact(strings[0]);
			} else {
				commandSender.sendMessage("You must be a player to use this command with only one argument.");

				return false;
			}
		} else if (strings.length == 2) {
			player1 = plugin.getServer().getPlayerExact(strings[0]);
			player2 = plugin.getServer().getPlayerExact(strings[1]);
		} else {
			commandSender.sendMessage("Invalid number of arguments.");

			return false;
		}

		if (player1 == null) {
			commandSender.sendMessage("Could not find player " + strings[0] + ".");

			return true;
		}

		if (player2 == null) {
			commandSender.sendMessage("Could not find player " + strings[1] + ".");

			return true;
		}

		player2.showPlayer(player1);

		return true;
	}

	protected boolean example(CommandSender commandSender, Command command, String s, String[] strings) {
		return false;
	}
}
