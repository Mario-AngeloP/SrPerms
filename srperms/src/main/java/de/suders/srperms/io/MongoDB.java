package de.suders.srperms.io;

import java.util.function.Consumer;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.gson.JsonObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import de.suders.srperms.system.AdaptiveSrPlayerGroup;
import de.suders.srperms.system.GroupManager;
import de.suders.srperms.system.SrGroup;
import de.suders.srperms.system.SrPlayer;
import lombok.Getter;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 17.07.2021
* Project: srperms
*/
public class MongoDB {

	@Getter
	private final Datastore playersData, groupsData;
	
	public MongoDB(final ConfigReader reader) {
		final Morphia morphia = new Morphia();
		morphia.mapPackage("de.suders.srperms.system");
		JsonObject cfg = reader.getMongoDB();
		MongoClient client =  new MongoClient(new MongoClientURI(
				"mongodb://" + cfg.get("user").getAsString() + ":" + cfg.get("password").getAsString() + "@"
						+ cfg.get("host").getAsString() + ":" + cfg.get("port").getAsString()
						+ "/?authSource=admin&readPreference=primary&appname=MongoDB%20Compass&ssl=false"));
		playersData = morphia.createDatastore(client, cfg.get("Database").getAsString());
		playersData.ensureIndexes();
		groupsData = morphia.createDatastore(client, cfg.get("Database").getAsString());
		groupsData.ensureIndexes();
		
		groupsData.find(SrGroup.class).forEach((Consumer<? super SrGroup>) group -> {
			GroupManager.addGroup(group);
			if(group.isDefault()) 
				GroupManager.setDefaultGroup(group);
		});
		playersData.find(SrPlayer.class).forEach((Consumer<? super SrPlayer>) player -> {
			GroupManager.addPlayer(player);
			for(AdaptiveSrPlayerGroup group : player.getGroups().values()) 
				group.getGroup().getMembers().add(player.getUniqueId());
		});
		if(GroupManager.getDefaultGroup() == null) {
			final SrGroup group = new SrGroup("player");
			GroupManager.addGroup(group);
			GroupManager.setDefaultGroup(group);
		}
	}
}
