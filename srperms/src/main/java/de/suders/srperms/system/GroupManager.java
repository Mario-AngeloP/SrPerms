package de.suders.srperms.system;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.NonNull;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 17.07.2021
* Project: srperms
*/
public class GroupManager {

	private static final Map<String, AdaptiveSrGroup> groups = new HashMap<>();
	private static final Map<String, AdaptiveSrPlayer> players = new HashMap<>();
	private static String defaultGroup = null;
	
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
	
	public static void addPlayer(@NonNull final AdaptiveSrPlayer player) {
		if(!players.containsKey(player.getUniqueId())) 
			players.put(player.getUniqueId(), player);
	}
	
	public static void addGroup(@NonNull final AdaptiveSrGroup group) {
		if(!groups.containsKey(group.getIdentifier().toLowerCase())) 
			groups.put(group.getIdentifier().toLowerCase(), group);
	}
	
	public static void deleteGroup(@NonNull final AdaptiveSrGroup group) {
		if(groups.containsKey(group.getIdentifier().toLowerCase())) {
			for(String uuid : group.getMembers()) {
				final AdaptiveSrPlayer player = players.get(uuid);
				if(player != null) 
					player.removeGroup(uuid);
			}
			groups.remove(group.getIdentifier().toLowerCase());
		}
	}
	
	public static String getDefaultGroup() {
		if(defaultGroup == null) {
			if(groups.containsKey("player")) {
				getGroup("player").setDefault(true);
				defaultGroup = "player";
			}
		}
		return defaultGroup;
	}
	
	public static void setDefaultGroup(final AdaptiveSrGroup group) {
		final AdaptiveSrGroup oldGroup = groups.get(defaultGroup);
		if(group.equals(oldGroup)) return;
		oldGroup.setDefault(false);
		group.setDefault(true);
		defaultGroup = group.getIdentifier();
	}
}
