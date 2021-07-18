package de.suders.srperms;

import org.bukkit.plugin.java.JavaPlugin;

import de.suders.srperms.storage.Data;
import lombok.Getter;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 17.07.2021
* Project: srperms
*/
public class SpigotSrPerms extends JavaPlugin implements MainSrPerms {
	@Getter
	private static SpigotSrPerms instance;
	@Getter
	private Data data;
	
	public void onEnable() {
		instance = this;
		data = new Data();
		data.setBungee(false);
	}

	@Override
	public boolean isApiEnabled() {
		return false;
	}
}
