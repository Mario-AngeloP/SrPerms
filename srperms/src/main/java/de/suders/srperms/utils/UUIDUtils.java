package de.suders.srperms.utils;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.suders.srperms.BungeeSrPerms;
import de.suders.srperms.storage.Data;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public final class UUIDUtils {

	private static final Map<UUID, String> cache;

	static {
		cache = Data.getInstance().getUuidCache();
	}

	/**
	 * Bestimme die UUID, mit des Namens, mit dem sich der Spieler der gesuchten
	 * UUID zuletzt verbunden hat.
	 *
	 * @param playerName letzer bekannter Name des Spielers
	 * @return Universally Unique Identifier des Spielers
	 */
	public static UUID getUUID(String playerName) {
		if(!Data.getInstance().isBungee()) {
			final Player player = Bukkit.getPlayer(playerName);
			if (player != null) {
				return player.getUniqueId();
			}
		} else {
			final ProxiedPlayer proxiedPlayer = BungeeSrPerms.getInstance().getProxy().getPlayer(playerName);
			if(proxiedPlayer != null) 
				return proxiedPlayer.getUniqueId();
		}
		if (cache != null) {
			for (final UUID id : cache.keySet()) {
				final String nameFromUUID = cache.get(id);
				if (nameFromUUID.equalsIgnoreCase(playerName)) {
					return id;
				}
			}
		}

		return NameFetcher.getUUID(playerName);
	}

	/**
	 * Bestimme den Namen, mithilfe der UUID, mit dem sich der Spieler des gesuchten
	 * Namen zuletzt verbunden hat.
	 *
	 * @param uuid Universally Unique Identifier des Spielers
	 * @return letzter bekannter Name des Spielers
	 */
	public static String getName(UUID uuid) {
		if(cache != null && cache.containsKey(uuid)) return cache.get(uuid);
		if(!Data.getInstance().isBungee()) {
			final Player player = Bukkit.getPlayer(uuid);
			if (player != null) {
				cache.put(uuid, player.getName());
				return player.getName();
			}
		} else {
			final ProxiedPlayer proxiedPlayer = BungeeSrPerms.getInstance().getProxy().getPlayer(uuid);
			if(proxiedPlayer != null)  {
				cache.put(uuid, proxiedPlayer.getName());
				return proxiedPlayer.getName();
			}
		}
		if (cache != null) {
			if (cache.containsKey(uuid)) {
				return cache.get(uuid);
			}
		}
		return NameFetcher.getName(uuid);
	}
}