package net.moltendorf.Bukkit.QuickCommands;

import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by moltendorf on 15/05/17.
 *
 * @author moltendorf
 */
public class PlayerBackupManager {
	final protected static Map<UUID, Stack<PlayerBackup>> inventories = new LinkedHashMap<>();

	protected static boolean contains(final Player player) {
		return inventories.containsKey(player.getUniqueId());
	}

	protected static void backup(final Player player) {
		backup(player, false);
	}

	protected static void backup(final Player player, final boolean clear) {
		final UUID id = player.getUniqueId();

		Stack<PlayerBackup> backups = inventories.get(id);

		if (backups == null) {
			backups = new Stack<>();

			inventories.put(id, backups);
		}

		backups.push(new PlayerBackup(player, clear));
	}

	protected static boolean restore(final Player player) {
		final UUID id = player.getUniqueId();
		final Stack<PlayerBackup> backups = inventories.get(id);

		if (backups != null) {
			final PlayerBackup backup = backups.pop();

			backup.restore(player);

			if (backups.empty()) {
				inventories.remove(id);
			}

			return true;
		}

		return false;
	}

	protected static boolean remove(final Player player) {
		final Stack<PlayerBackup> backups = inventories.remove(player.getUniqueId());

		if (backups != null) {
			PlayerBackup backup = null;

			while (!backups.empty()) {
				backup = backups.pop();
			}

			if (backup != null) {
				backup.restore(player);

				return true;
			}
		}

		return false;
	}
}
