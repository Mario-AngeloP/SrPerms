package de.suders.srperms.listener;

import de.suders.srperms.system.AdaptiveSrPlayer;
import de.suders.srperms.system.GroupManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 18.07.2021
* Project: srperms
*/
public class BungeeSrPermsListener implements Listener {

	@EventHandler
	public void hasPermission(final PermissionCheckEvent e) {
		e.setHasPermission(false);
		if(e.getSender() instanceof ProxiedPlayer) {
			final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) e.getSender();
			final AdaptiveSrPlayer player = GroupManager.getPlayer(proxiedPlayer.getUniqueId());
			if(player.hasPermission("*")) {
				e.setHasPermission(true);
				return;
			}
			if(player.hasPermission(e.getPermission())) e.setHasPermission(true);
		}
	}
	
}
