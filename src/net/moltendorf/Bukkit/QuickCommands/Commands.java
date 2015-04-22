package net.moltendorf.Bukkit.QuickCommands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Collection;

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

	public boolean colors(CommandSender commandSender, Command command, String s, String[] strings) {
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

	public boolean durability(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			if (strings.length > 1) {
				commandSender.sendMessage("Invalid number of arguments.");

				return false;
			}

			Player player = (Player)commandSender;

			ItemStack item = player.getItemInHand();
			Material type = item.getType();

			if (plugin.configuration.global.equipment.contains(type)) {
				short max = item.getType().getMaxDurability();

				if (strings.length == 1) {
					if (commandSender.hasPermission("quickcommands.durability.set")) {
						try {
							short durability = Short.parseShort(strings[0]);

							if (durability == -1) {
								durability = 0;
							} else {
								durability = (short)(max - durability);
							}

							item.setDurability(durability);
						} catch (final NumberFormatException exception) {
							commandSender.sendMessage("§cInvalid durability specified.");

							return false;
						}
					} else {
						commandSender.sendMessage(plugin.getCommand(s).getPermissionMessage());

						return true;
					}
				}

				int durability = max - item.getDurability();

				double percentage = (double)durability/(double)max*100.;

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
						durability = (int)((double)durability/((100./((double)unbreaking + 1.))/100.));
					} else {
						// Armor unbreaking.
						durability = (int)((double)durability/((60. + (40./((double)unbreaking + 1.)))/100.));
					}
					percentage = (double)durability/(double)max*100.;

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

	public boolean hide(CommandSender commandSender, Command command, String s, String[] strings) {
		final Player player1, player2;

		if (strings.length == 1) {
			if (commandSender instanceof Player) {
				player1 = (Player)commandSender;
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

	public boolean show(CommandSender commandSender, Command command, String s, String[] strings) {
		final Player player1, player2;

		if (strings.length == 1) {
			if (commandSender instanceof Player) {
				player1 = (Player)commandSender;
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

	public boolean health(CommandSender commandSender, Command command, String s, String[] strings) {
		if (strings.length < 2 || strings.length > 4) {
			commandSender.sendMessage("Invalid number of arguments.");

			return false;
		}

		final Player[] players;

		if (strings[0].equals("*")) {
			players = plugin.getServer().getOnlinePlayers();
		} else {
			final Player player = plugin.getServer().getPlayer(strings[0]);

			if (player == null) {
				commandSender.sendMessage("Could not find player " + strings[0] + ".");

				return true;
			}

			players = new Player[]{player};
		}

		double max;

		try {
			max = Double.parseDouble(strings[1]);

			if (max > 2048) {
				max = 2048;
			}
		} catch (final NumberFormatException exception) {
			commandSender.sendMessage("Invalid max health specified.");

			return false;
		}

		for (Player player : players) {
			player.setMaxHealth(max);
			player.sendMessage("Set max health to " + max);
		}

		if (strings.length > 2) {
			double scale;

			try {
				scale = Double.parseDouble(strings[2]);

				if (scale > 100) {
					scale = 100;
				}
			} catch (final NumberFormatException exception) {
				commandSender.sendMessage("Invalid health scale specified.");

				return false;
			}

			for (Player player : players) {
				player.setHealthScale(scale);
				player.sendMessage("Set health scale to " + scale);
			}
		}

		if (strings.length > 3) {
			double health;

			try {
				health = Double.parseDouble(strings[3]);

				if (health > 2048) {
					health = 2048;
				}
			} catch (final NumberFormatException exception) {
				commandSender.sendMessage("Invalid health specified.");

				return false;
			}

			for (Player player : players) {
				player.setHealth(health);
				player.sendMessage("Set health to " + health);
			}
		}

		return true;
	}

	public boolean name(CommandSender commandSender, Command command, String s, String[] strings) {
		final Player player;
		final String name;

		if (strings.length == 1) {
			if (commandSender instanceof Player) {
				player = (Player)commandSender;
				name = strings[0];
			} else {
				commandSender.sendMessage("You must be a player to use this command with only one argument.");

				return false;
			}
		} else if (strings.length == 2) {
			player = plugin.getServer().getPlayerExact(strings[0]);

			if (player == null) {
				commandSender.sendMessage("Could not find player " + strings[0] + ".");

				return true;
			}

			name = strings[1];
		} else {
			commandSender.sendMessage("Invalid number of arguments.");

			return false;
		}

		player.setDisplayName(name);

		return true;
	}

	public boolean drop(CommandSender commandSender, Command command, String s, String[] strings) {
		if (strings.length == 2 || strings.length == 3) {
			final Player player = plugin.getServer().getPlayer(strings[0]);

			if (player == null) {
				commandSender.sendMessage("Could not find player " + strings[0] + ".");

				return true;
			}

			final Plugin instance = Plugin.instance;
			final Server server = instance.getServer();
			final BukkitScheduler scheduler = server.getScheduler();

			final Runnable teleport;

			if (strings.length == 2) {
				final OfflinePlayer toPlayer = plugin.getServer().getOfflinePlayer(strings[1]);

				if (toPlayer == null || !toPlayer.hasPlayedBefore()) {
					commandSender.sendMessage("Could not find player " + strings[1] + ".");

					return true;
				}

				teleport = () -> {
					final Location previous = player.getLocation();
					final Location location;

					if (!toPlayer.getUniqueId().equals(player.getUniqueId()) && toPlayer.isOnline() && toPlayer.getPlayer().getWorld().getName().equals
						("world")) {
						location = toPlayer.getPlayer().getLocation();
					} else {
						location = toPlayer.getBedSpawnLocation();
					}

					location.setY(600);

					player.teleport(location);

					scheduler.runTaskLater(instance, () -> {
						player.sendMessage("§2Phase jump to drop pod successful.");

						previous.getWorld().strikeLightningEffect(previous);
						location.getWorld().strikeLightningEffect(location);
					}, 5);
				};
			} else {
				final int x;

				try {
					x = Integer.parseInt(strings[1]);
				} catch (final NumberFormatException exception) {
					commandSender.sendMessage("Invalid x specified.");

					return false;
				}

				final int z;

				try {
					z = Integer.parseInt(strings[2]);
				} catch (final NumberFormatException exception) {
					commandSender.sendMessage("Invalid z specified.");

					return false;
				}

				teleport = () -> {
					final Location previous = player.getLocation();
					final Location location = new Location(server.getWorld("world"), x, 600, z);

					player.teleport(location);

					scheduler.runTaskLater(instance, () -> {
						player.sendMessage("§2Phase jump to drop pod successful.");

						previous.getWorld().strikeLightningEffect(previous);
						location.getWorld().strikeLightningEffect(location);
					}, 5);
				};
			}

			player.sendMessage("§4Preparing phase jump to drop pod.");
			player.sendMessage("§2Phase jump in 5.");

			scheduler.runTaskLater(instance, () -> {
				player.sendMessage("§2Phase jump in 4.");

				scheduler.runTaskLater(instance, () -> {
					player.sendMessage("§2Phase jump in 3.");

					scheduler.runTaskLater(instance, () -> {
						player.sendMessage("§2Phase jump in 2.");

						scheduler.runTaskLater(instance, () -> {
							player.sendMessage("§2Phase jump in 1.");

							scheduler.runTaskLater(instance, teleport, 20);
						}, 20);
					}, 20);
				}, 20);
			}, 20);

			return true;
		}

		return false;
	}

	public boolean cleanup(CommandSender commandSender, Command command, String s, String[] strings) {
		for (final World world : Plugin.instance.getServer().getWorlds()) {
			final Collection<Arrow> arrows = world.getEntitiesByClass(Arrow.class);

			int removed_arrows = 0;

			for (final Arrow arrow : arrows) {
				if (arrow.getTicksLived() > 15*20) {
					arrow.remove();

					++removed_arrows;
				}
			}

			if (removed_arrows > 0) {
				final String plural = removed_arrows > 1 ? "s" : "";

				commandSender.sendMessage("§2Found and cleaned up " + arrows.size() + " arrow" + plural + " in " + world.getName() + ".");
			}
		}

		return true;
	}

	public boolean example(CommandSender commandSender, Command command, String s, String[] strings) {
		return false;
	}
}
