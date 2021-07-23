package de.suders.srperms.system;

import java.util.function.Consumer;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.gson.JsonObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import de.suders.srperms.io.ConfigReader;
import de.suders.srperms.system.object.SrGroup;
import de.suders.srperms.system.object.SrPlayer;
import lombok.Getter;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 17.07.2021
* Project: srperms
*/
public class MongoDB {

	@Getter
	private Datastore datastore;
	
	public MongoDB(final ConfigReader reader) {
		
		JsonObject cfg = reader.getMongoDB();
		MongoClient client =  new MongoClient(new MongoClientURI(
				"mongodb://" + cfg.get("user").getAsString() + ":" + cfg.get("password").getAsString() + "@"
						+ cfg.get("host").getAsString() + ":" + cfg.get("port").getAsString()/*
						+ "/?authSource=admin&readPreference=primary&appname=MongoDB%20Compass&ssl=false")*/));
		datastore = new Morphia().mapPackage("de.suders.srperms.object").createDatastore(client, "admin");
		datastore.find(SrGroup.class).forEach((Consumer<? super SrGroup>) group -> {
			GroupManager.addNotSavedGroup(group);
			if(group.isDefault()) 
				GroupManager.setDefaultGroupNotSaved(group);
		});
		datastore.find(SrPlayer.class).forEach((Consumer<? super SrPlayer>) player -> {
			GroupManager.addNotSavedPlayer(player);
			for(AdaptiveSrPlayerGroup group : player.getGroups().values()) 
				group.getGroup().getMembers().add(player.getUniqueId());
		});
		if(GroupManager.getDefaultGroupName() == null) {
			final SrGroup group = new SrGroup("player");
			GroupManager.addNotSavedGroup(group);
			GroupManager.setDefaultGroupNotSaved(group);
		} else if(GroupManager.getDefaultGroupName().equalsIgnoreCase("player")) {
			AdaptiveSrGroup group = GroupManager.getGroup("player");
			if(group == null) {
				group = new SrGroup("player");
				group.setDefault(true);
				GroupManager.addNotSavedGroup(group);
			}
		}
	}
}
