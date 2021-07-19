package de.suders.srperms.system;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.mongodb.morphia.Datastore;

import de.suders.srperms.storage.Data;
import de.suders.srperms.system.object.SrGroup;
import de.suders.srperms.system.object.SrPlayer;
import lombok.NonNull;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 17.07.2021
* Project: srperms
*/
public class GroupManager {

	private static final Map<String, AdaptiveSrGroup> groups = new HashMap<>();
	private static final Map<String, AdaptiveSrPlayer> players = new HashMap<>();
	private static String defaultGroup = "player";
	
	public static void saveDatas() {
		groups.values().forEach(group -> {
			group.save();
		});
		players.values().forEach(player -> {
			player.save();
		});
	}
 	
	public static AdaptiveSrGroup getGroup(@NonNull final String identifier) {
		if(groups.containsKey(identifier.toLowerCase())) 
			return groups.get(identifier.toLowerCase());
		return null;
	}
	
	public static AdaptiveSrPlayer getPlayer(@NonNull final UUID uuid) {
		if(players.containsKey(uuid.toString())) 
			return players.get(uuid.toString());
		else {
			final SrPlayer player = new SrPlayer(uuid);
			player.addGroup(defaultGroup, -1L);
			players.put(uuid.toString(), player);
			return player;
		}
	}
	
	protected static void addNotSavedGroup(final AdaptiveSrGroup group) {
		if(!groups.containsKey(group.getIdentifier().toLowerCase())) {
			groups.put(group.getIdentifier().toLowerCase(), group);
		}
	}
	
	protected static void addNotSavedPlayer(@NonNull final AdaptiveSrPlayer player) {
		if(!players.containsKey(player.getUniqueId())) {
			players.put(player.getUniqueId(), player);
		}
	}
	
	public static void addPlayer(@NonNull final AdaptiveSrPlayer player) {
		if(!players.containsKey(player.getUniqueId())) {
			players.put(player.getUniqueId(), player);
			player.save();
		}
	}
	
	public static void addGroup(@NonNull final AdaptiveSrGroup group) {
		if(!groups.containsKey(group.getIdentifier().toLowerCase())) {
			groups.put(group.getIdentifier().toLowerCase(), group);
			group.save();
		}
	}
	
	public static void deleteGroup(@NonNull final AdaptiveSrGroup group) {
		Datastore datastore = Data.getInstance().getMongoDB().getDatastore();
		if(groups.containsKey(group.getIdentifier().toLowerCase())) {
			for(String uuid : group.getMembers()) {
				final AdaptiveSrPlayer player = players.get(uuid);
				if(player != null) {
					player.removeGroup(group.getIdentifier().toLowerCase());
					player.save();
				}
			}
			groups.remove(group.getIdentifier().toLowerCase());
			datastore.delete((SrGroup) group);
		}
	}
	
	public static String getDefaultGroupName() {
		if(defaultGroup == null) {
			final AdaptiveSrGroup adaptiveSrGroup = getGroup("player");
			if(adaptiveSrGroup == null) return null;
			defaultGroup = "player";
			adaptiveSrGroup.save();
			return defaultGroup;
		}
		return defaultGroup;
	}
	
	protected static void setDefaultGroupNotSaved(@NonNull final AdaptiveSrGroup group) {
		group.setDefault(true);
		defaultGroup = group.getIdentifier();
	}
	
	public static void setDefaultGroup(@NonNull final AdaptiveSrGroup group) {
		final AdaptiveSrGroup oldGroup = groups.get(defaultGroup);
		if(group.equals(oldGroup)) return;
		oldGroup.setDefault(false);
		group.setDefault(true);
		defaultGroup = group.getIdentifier();
		group.save();
		oldGroup.save();
	}
}
