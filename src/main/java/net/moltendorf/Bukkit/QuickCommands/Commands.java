package net.moltendorf.Bukkit.QuickCommands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Listener register.
 *
 * @author moltendorf
 */
public class Commands {

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

			final QuickCommands instance = QuickCommands.getInstance();

			Player player = (Player)commandSender;

			ItemStack item = player.getItemInHand();
			Material type = item.getType();

			if (instance.settings.equipment.contains(type)) {
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
						commandSender.sendMessage(instance.getCommand(s).getPermissionMessage());

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
					if (instance.settings.tools.contains(type)) {
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
		final QuickCommands instance = QuickCommands.getInstance();
		final Server        server   = instance.getServer();

		final Player player1, player2;

		if (strings.length == 1) {
			if (commandSender instanceof Player) {
				player1 = (Player)commandSender;
				player2 = server.getPlayerExact(strings[0]);
			} else {
				commandSender.sendMessage("You must be a player to use this command with only one argument.");

				return false;
			}
		} else if (strings.length == 2) {
			player1 = server.getPlayerExact(strings[0]);
			player2 = server.getPlayerExact(strings[1]);
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
		final QuickCommands instance = QuickCommands.getInstance();
		final Server        server   = instance.getServer();

		final Player player1, player2;

		if (strings.length == 1) {
			if (commandSender instanceof Player) {
				player1 = (Player)commandSender;
				player2 = server.getPlayerExact(strings[0]);
			} else {
				commandSender.sendMessage("You must be a player to use this command with only one argument.");

				return false;
			}
		} else if (strings.length == 2) {
			player1 = server.getPlayerExact(strings[0]);
			player2 = server.getPlayerExact(strings[1]);
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

		final QuickCommands instance = QuickCommands.getInstance();
		final Server        server   = instance.getServer();

		final Collection<? extends Player> players;

		if (strings[0].equals("*")) {
			players = server.getOnlinePlayers();
		} else {
			final Player player = server.getPlayer(strings[0]);

			if (player == null) {
				commandSender.sendMessage("Could not find player " + strings[0] + ".");

				return true;
			}

			players = Collections.singletonList(player);
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
		final QuickCommands instance = QuickCommands.getInstance();
		final Server        server   = instance.getServer();

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
			player = server.getPlayerExact(strings[0]);

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
		final QuickCommands instance = QuickCommands.getInstance();
		final Server        server   = instance.getServer();

		if (strings.length == 2 || strings.length == 3) {
			final Player player = server.getPlayer(strings[0]);

			if (player == null) {
				commandSender.sendMessage("Could not find player " + strings[0] + ".");

				return true;
			}

			final BukkitScheduler scheduler = server.getScheduler();

			final Runnable teleport;

			if (strings.length == 2) {
				final OfflinePlayer toPlayer = server.getOfflinePlayer(strings[1]);

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
		final QuickCommands instance = QuickCommands.getInstance();
		final Server        server   = instance.getServer();

		boolean success = false;

		final List<String> arguments = new LinkedList<>(Arrays.asList(strings));

		final Iterable<World>    worlds;
		final Collection<Entity> entities;

		if (commandSender instanceof Player) {
			final int radius;

			if (strings.length > 0 && strings[0].matches("^(?:-1|[0-9]+)$")) {
				radius = Integer.parseInt(arguments.remove(0));
			} else {
				radius = 25;
			}

			if (radius == -1) {
				worlds = server.getWorlds();
				entities = null;
			} else {
				final Player player = ((Player)commandSender);

				worlds = Collections.singletonList(player.getWorld());
				entities = player.getNearbyEntities(radius, radius, radius);
			}
		} else {
			worlds = server.getWorlds();
			entities = null;
		}

		if (arguments.size() == 0) {
			return false;
		}

		for (final World world : worlds) {
			for (final String type : arguments) {
				final Class clazz;

				Collection<Entity> collection = null;
				String display = null;

				switch (type.toLowerCase()) {
					case "arrow":
					case "arrows":
					case "pin":
					case "pins":
					case "fleshprobedevices":
						clazz = Arrow.class;
						break;

					case "bat":
					case "batties":
					case "bats":
					case "batsies":
					case "batman":
					case "batmobile":
					case "batwoman":
					case "batcycle":
						clazz = Bat.class;
						break;

					case "pig":
					case "pigs":
					case "piggies":
					case "pigsies":
						clazz = Pig.class;
						break;

					case "sheep":
					case "sheeps":
					case "sheepies":
					case "sheepsies":
						clazz = Sheep.class;
						break;

					case "cow":
					case "cows":
					case "cowsies":
					case "calf":
					case "calves":
						clazz = Cow.class;
						break;

					case "chicken":
					case "chickens":
					case "chick":
					case "chicks":
					case "duck":
					case "ducks":
					case "duckies":
						clazz = Chicken.class;
						break;

					case "squid":
					case "squids":
					case "octopus":
					case "octopuses":
					case "octopi":
					case "tentacles":
						clazz = Squid.class;
						break;

					case "wolf":
					case "wolves":
					case "wolfs":
					case "wolfy":
					case "wolfies":
					case "dawolfsclaw":
					case "dawoofsclaw":
						clazz = Wolf.class;
						break;

					case "mooshroom":
					case "mooshrooms":
					case "mushroomcow":
					case "mushroomcows":
					case "mushroom":
					case "mushrooms":
					case "shroom":
					case "shrooms":
					case "trip":
						clazz = MushroomCow.class;
						break;

					case "snowgolem":
					case "snowman":
					case "snowmen":
					case "snowwoman":
					case "snowwomen":
					case "snowlady":
					case "snoshadow":
					case "sno":
					case "snow":
						clazz = Snowman.class;
						break;

					case "ocelot":
					case "ocelots":
					case "cat":
					case "cats":
					case "kitty":
					case "kitties":
					case "kittens":
					case "pussy":
					case "pussycat":
					case "pussys":
					case "pussies":
						clazz = Ocelot.class;
						break;

					case "irongolem":
					case "iron":
					case "ironman":
					case "flipper":
					case "flippin":
					case "flipyou":
					case "rose":
					case "rosebud":
					case "redflower":
					case "red":
					case "peaceofficer":
					case "police":
					case "popo":
					case "po":
						clazz = IronGolem.class;
						break;

					case "horse":
					case "horses":
					case "horsies":
					case "raisin":
					case "neigh":
						clazz = Horse.class;
						break;

					case "rabbit":
					case "rabbits":
					case "bunny":
					case "bunnies":
					case "wabbit":
					case "wabbits":
						clazz = Rabbit.class;
						break;

					case "villager":
					case "villagers":
					case "squidward":
					case "squidwards":
						clazz = Villager.class;
						break;

					default:
						clazz = null;

						commandSender.sendMessage("§cNo type known by: " + type + ".");
				}

				if (clazz != null) {
					if (entities == null) {
						collection = world.getEntitiesByClass(clazz);
					} else {
						collection = entities.stream().filter(clazz::isInstance).collect(Collectors.toList());
					}

					collection.forEach(Entity::remove);

					if (display == null) {
						display = clazz.getSimpleName().toLowerCase() + (collection.size() > 1 ? "s" : "");
					}
				}

				if (collection != null && collection.size() > 0) {
					commandSender.sendMessage("§2Found and cleaned up " + collection.size() + " " + display + " in " + world.getName() + ".");

					success = true;
				}
			}
		}

		if (!success) {
			commandSender.sendMessage("§cDid not find anything to cleanup.");
		}

		return true;
	}

	public boolean alert(CommandSender commandSender, Command command, String s, String[] strings) {
		final QuickCommands instance = QuickCommands.getInstance();
		final Server        server   = instance.getServer();

		if (strings.length > 0) {
			server.broadcastMessage("§8<§4Alert§8> §4" + String.join(" ", strings));

			return true;
		}

		return false;
	}

	public boolean broadcast(CommandSender commandSender, Command command, String s, String[] strings) {
		final QuickCommands instance = QuickCommands.getInstance();
		final Server        server   = instance.getServer();

		if (strings.length > 0) {
			server.broadcastMessage(String.join(" ", strings));

			return true;
		}

		return false;
	}

	public boolean say(CommandSender commandSender, Command command, String s, String[] strings) {
		final QuickCommands instance = QuickCommands.getInstance();
		final Server        server   = instance.getServer();

		if (strings.length > 0) {
			server.broadcastMessage("§8<§3Console§8> §3" + String.join(" ", strings));

			return true;
		}

		return false;
	}

	public boolean stuff(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			// Armor.

			final ItemStack helm = new ItemStack(Material.DIAMOND_HELMET);
			helm.addEnchantments(new LinkedHashMap<Enchantment, Integer>() {{
				put(Enchantment.WATER_WORKER, 1);
				put(Enchantment.OXYGEN, 3);
				put(Enchantment.DURABILITY, 3);
				put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			}});
			helm.setDurability((short)(helm.getDurability()/2));

			final ItemMeta helmMeta = helm.getItemMeta();
			helmMeta.setDisplayName("Das Helm");
			helm.setItemMeta(helmMeta);

			final ItemStack vest = new ItemStack(Material.DIAMOND_CHESTPLATE);
			vest.addEnchantments(new LinkedHashMap<Enchantment, Integer>() {{
				put(Enchantment.THORNS, 3);
				put(Enchantment.DURABILITY, 3);
				put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			}});
			vest.setDurability((short)(vest.getDurability()/2));

			final ItemMeta vestMeta = vest.getItemMeta();
			vestMeta.setDisplayName("Das Vest");
			vest.setItemMeta(vestMeta);

			final ItemStack pant = new ItemStack(Material.DIAMOND_LEGGINGS);
			pant.addEnchantments(new LinkedHashMap<Enchantment, Integer>() {{
				put(Enchantment.DURABILITY, 3);
				put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			}});
			pant.setDurability((short)(pant.getDurability()/2));

			final ItemMeta pantMeta = pant.getItemMeta();
			pantMeta.setDisplayName("Das Pant");
			pant.setItemMeta(pantMeta);

			final ItemStack boot = new ItemStack(Material.DIAMOND_BOOTS);
			boot.addEnchantments(new LinkedHashMap<Enchantment, Integer>() {{
				put(Enchantment.DEPTH_STRIDER, 3);
				put(Enchantment.PROTECTION_FALL, 4);
				put(Enchantment.DURABILITY, 3);
				put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			}});
			boot.setDurability((short)(boot.getDurability()/2));

			final ItemMeta bootMeta = boot.getItemMeta();
			bootMeta.setDisplayName("Das Boot");
			boot.setItemMeta(bootMeta);

			// Tools.

			final ItemStack silk = new ItemStack(Material.DIAMOND_PICKAXE);
			silk.addEnchantments(new LinkedHashMap<Enchantment, Integer>() {{
				put(Enchantment.SILK_TOUCH, 1);
				put(Enchantment.DIG_SPEED, 5);
				put(Enchantment.DURABILITY, 3);
			}});
			silk.setDurability((short)(silk.getDurability()/2));

			final ItemMeta silkMeta = silk.getItemMeta();
			silkMeta.setDisplayName("Das Silk");
			silk.setItemMeta(silkMeta);

			final ItemStack luck = new ItemStack(Material.DIAMOND_PICKAXE);
			luck.addEnchantments(new LinkedHashMap<Enchantment, Integer>() {{
				put(Enchantment.LOOT_BONUS_BLOCKS, 3);
				put(Enchantment.DIG_SPEED, 5);
				put(Enchantment.DURABILITY, 3);
			}});
			luck.setDurability((short)(luck.getDurability()/2));

			final ItemMeta luckMeta = luck.getItemMeta();
			luckMeta.setDisplayName("Das Luck");
			luck.setItemMeta(luckMeta);

			final ItemStack hole = new ItemStack(Material.DIAMOND_SPADE);
			hole.addEnchantments(new LinkedHashMap<Enchantment, Integer>() {{
				put(Enchantment.DIG_SPEED, 5);
				put(Enchantment.DURABILITY, 3);
			}});
			hole.setDurability((short)(hole.getDurability()/2));

			final ItemMeta holeMeta = hole.getItemMeta();
			holeMeta.setDisplayName("Das Hole");
			hole.setItemMeta(holeMeta);

			final ItemStack chop = new ItemStack(Material.DIAMOND_AXE);
			chop.addEnchantments(new LinkedHashMap<Enchantment, Integer>() {{
				put(Enchantment.DAMAGE_ALL, 5);
				put(Enchantment.DIG_SPEED, 5);
				put(Enchantment.DURABILITY, 3);
			}});
			chop.setDurability((short)(chop.getDurability()/2));

			final ItemMeta chopMeta = chop.getItemMeta();
			chopMeta.setDisplayName("Das Chop");
			chop.setItemMeta(chopMeta);

			final ItemStack loot = new ItemStack(Material.DIAMOND_SWORD);
			loot.addEnchantments(new LinkedHashMap<Enchantment, Integer>() {{
				put(Enchantment.LOOT_BONUS_MOBS, 2);
				put(Enchantment.FIRE_ASPECT, 2);
				put(Enchantment.KNOCKBACK, 2);
				put(Enchantment.DAMAGE_ALL, 5);
				put(Enchantment.DURABILITY, 3);
			}});
			loot.setDurability((short)(loot.getDurability()/2));

			final ItemMeta lootMeta = loot.getItemMeta();
			lootMeta.setDisplayName("Das Loot");
			loot.setItemMeta(lootMeta);

			final ItemStack arch = new ItemStack(Material.BOW);
			arch.addEnchantments(new LinkedHashMap<Enchantment, Integer>() {{
				put(Enchantment.ARROW_INFINITE, 1);
				put(Enchantment.ARROW_FIRE, 1);
				put(Enchantment.ARROW_KNOCKBACK, 2);
				put(Enchantment.ARROW_DAMAGE, 5);
				put(Enchantment.DURABILITY, 3);
			}});
			arch.setDurability((short)(arch.getDurability()/2));

			final ItemMeta archMeta = arch.getItemMeta();
			archMeta.setDisplayName("Das Arch");
			arch.setItemMeta(archMeta);

			final ItemStack poke = new ItemStack(Material.ARROW);
			poke.setAmount(1);

			final ItemMeta pokeMeta = poke.getItemMeta();
			pokeMeta.setDisplayName("Das Poke");
			poke.setItemMeta(pokeMeta);

			// Misc.

			final ItemStack fire = new ItemStack(Material.FLINT_AND_STEEL);
			fire.addEnchantments(new LinkedHashMap<Enchantment, Integer>() {{
				put(Enchantment.DURABILITY, 3);
			}});
			fire.setDurability((short)(fire.getDurability()/2));

			final ItemMeta fireMeta = fire.getItemMeta();
			fireMeta.setDisplayName("Das Fire");
			fire.setItemMeta(fireMeta);

			final ItemStack boom = new ItemStack(Material.TNT);
			boom.setAmount(-1);

			final ItemMeta boomMeta = boom.getItemMeta();
			boomMeta.setDisplayName("Das Boom");
			boom.setItemMeta(boomMeta);

			final ItemStack snip = new ItemStack(Material.SHEARS);
			snip.addEnchantments(new LinkedHashMap<Enchantment, Integer>() {{
				put(Enchantment.DURABILITY, 3);
			}});
			snip.setDurability((short)(snip.getDurability()/2));

			final ItemMeta snipMeta = snip.getItemMeta();
			snipMeta.setDisplayName("Das Snip");
			snip.setItemMeta(snipMeta);

			final ItemStack fish = new ItemStack(Material.FISHING_ROD);
			fish.addEnchantments(new LinkedHashMap<Enchantment, Integer>() {{
				put(Enchantment.LUCK, 3);
				put(Enchantment.LURE, 3);
			}});
			fish.setDurability((short)(fish.getDurability()/2));

			final ItemMeta fishMeta = fish.getItemMeta();
			fishMeta.setDisplayName("Das Fish");
			fish.setItemMeta(fishMeta);

			final ItemStack cool = new ItemStack(Material.WATER_BUCKET);

			final ItemMeta coolMeta = cool.getItemMeta();
			coolMeta.setDisplayName("Das Cool");
			cool.setItemMeta(coolMeta);

			// Ender pearls.

			final ItemStack perl = new ItemStack(Material.ENDER_PEARL);
			perl.setAmount(-1);

			final ItemMeta perlMeta = perl.getItemMeta();
			perlMeta.setDisplayName("Das Perl");
			perl.setItemMeta(perlMeta);

			// Food.

			final ItemStack food = new ItemStack(Material.GOLDEN_CARROT);
			food.setAmount(-1);

			final ItemMeta foodMeta = food.getItemMeta();
			foodMeta.setDisplayName("Das Food");
			food.setItemMeta(foodMeta);

			// Blocks.

			final ItemStack dirt = new ItemStack(Material.DIRT);
			dirt.setAmount(-1);

			final ItemMeta dirtMeta = dirt.getItemMeta();
			dirtMeta.setDisplayName("Das Dirt");
			dirt.setItemMeta(dirtMeta);

			// Assign things to slots.

			final Player player = (Player)commandSender;

			final PlayerInventory inventory = player.getInventory();

			inventory.clear();

			inventory.setHelmet(helm);
			inventory.setChestplate(vest);
			inventory.setLeggings(pant);
			inventory.setBoots(boot);

			inventory.setItem(0, silk);
			inventory.setItem(27, luck);
			inventory.setItem(1, hole);
			inventory.setItem(28, chop);
			inventory.setItem(2, food);
			inventory.setItem(3, loot);
			inventory.setItem(4, arch);
			inventory.setItem(9, poke);
			inventory.setItem(5, perl);
			inventory.setItem(6, fire);
			inventory.setItem(7, boom);
			inventory.setItem(8, cool);
			inventory.setItem(19, fish);
			inventory.setItem(10, snip);
			inventory.setItem(17, dirt);

			player.setLevel(1337);

			commandSender.sendMessage("§3Gave you some basic equipment to help you start on the quest to find diamonds to build diamond gear!");

		} else {
			commandSender.sendMessage("§cYou must be a player to use this command.");
		}

		return true;
	}

	public boolean inventory_backup(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			final Player player = (Player)commandSender;

			PlayerBackupManager.backup(player);
		} else {
			commandSender.sendMessage("§cYou must be a player to use this command.");
		}

		return true;
	}

	public boolean inventory_restore(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			final Player player = (Player)commandSender;

			if (!PlayerBackupManager.restore(player)) {
				commandSender.sendMessage("§cNo backups.");
			}
		} else {
			commandSender.sendMessage("§cYou must be a player to use this command.");
		}

		return true;
	}

	public boolean example(CommandSender commandSender, Command command, String s, String[] strings) {
		return false;
	}
}
