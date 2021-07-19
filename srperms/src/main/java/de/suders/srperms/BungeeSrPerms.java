package de.suders.srperms;

import de.suders.srperms.command.SrPerms;
import de.suders.srperms.listener.BungeeSrPermsListener;
import de.suders.srperms.storage.Data;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 17.07.2021
* Project: srperms
*/
public class BungeeSrPerms extends Plugin implements MainSrPerms {

	@Getter
	private static BungeeSrPerms instance;
	@Getter
	private Data data;
	
	public void onEnable() {
		instance = this;
		ProxyServer.getInstance().getPluginManager().registerCommand(instance, new SrPerms());
		data = new Data();
		ProxyServer.getInstance().getPluginManager().registerListener(instance, new BungeeSrPermsListener());
	}

	@Override
	public boolean isApiEnabled() {
		return false;
	}
}
