package de.suders.srperms.storage;

import java.util.HashMap;
import java.util.UUID;

import de.suders.srperms.io.ConfigReader;
import de.suders.srperms.io.MongoDB;
import lombok.Getter;
import lombok.Setter;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 17.07.2021
* Project: srperms
*/
public class Data {

	@Getter
	private static Data instance;
	@Getter @Setter
	private boolean bungee = true;
	@Getter
	private final HashMap<UUID, String> uuidCache;
	@Getter
	private final ConfigReader configReader;
	@Getter
	private final MongoDB mongoDB;
	
	public Data() {
		instance = this;
		this.uuidCache = new HashMap<>();
		this.configReader = new ConfigReader();
		this.mongoDB = new MongoDB(configReader);
	}
}
