package net.moltendorf.Bukkit.QuickCommands;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by moltendorf on 15/06/20.
 *
 * @author moltendorf
 */
public class SpectatorManager {
	static {
		QuickCommands.LoadedSpectatorManager = true;
	}

	final protected static Map<UUID, Location> spectators = new LinkedHashMap<>();

	protected static boolean contains(final Player player) {
		return spectators.containsKey(player.getUniqueId());
	}

	protected static Location put(final Player player) {
		player.setGameMode(GameMode.SPECTATOR);

		return spectators.put(player.getUniqueId(), player.getLocation());
	}

	protected static Location remove(final Player player) {
		final Location location = spectators.remove(player.getUniqueId());

		if (location != null) {
			player.teleport(location);
			player.setGameMode(GameMode.SURVIVAL);
		}

		return location;
	}
}
