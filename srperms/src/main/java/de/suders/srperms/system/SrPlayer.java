package de.suders.srperms.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import de.suders.srperms.storage.Data;
import lombok.Getter;
import lombok.NonNull;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 17.07.2021
* Project: srperms
*/
@Entity(value = "SrPlayers", noClassnameStored = true)
public class SrPlayer implements AdaptiveSrPlayer {
	@Id
	private String uuid;
	@Getter @Embedded 
	private Map<String, SrPlayerGroup> groups;
	@Getter @Embedded
	private List<String> permissions;
	
	public SrPlayer(@NonNull final UUID uuid) {
		this.uuid = uuid.toString();
		this.groups = new HashMap<>();
		this.permissions = new ArrayList<>();
	}
	
	public void init() {
		
	}
	
	public void save() {
		Data.getInstance().getMongoDB().getPlayersData().save(this);
	}
	
	public void addGroup(@NonNull final String name, long expriationTime) {
		if(groups.containsKey(name.toLowerCase())) return;
		final AdaptiveSrGroup group = GroupManager.getGroup(name.toLowerCase());
		final SrPlayer player = this;
		if(!group.getMembers().contains(this.uuid)) 
			group.getMembers().add(this.uuid);
		groups.put(name.toLowerCase(), new SrPlayerGroup(name, System.currentTimeMillis(), expriationTime, group, player));
	}
	
	public void changeExpirationTimeOfGroup(@NonNull final String name, long expirationTime) {
		if(!groups.containsKey(name.toLowerCase())) return;
		final AdaptiveSrPlayerGroup group = groups.get(name.toLowerCase());
		if(!group.getGroup().getMembers().contains(this.uuid)) 
			group.getGroup().getMembers().add(this.uuid);
		group.setExpirationTime(expirationTime);
	}
	
	public void removeGroup(@NonNull final String name) {
		if(groups.containsKey(name.toLowerCase())) 
			groups.remove(name.toLowerCase());
	}

	@Override
	public String getUniqueId() {
		return uuid;
	}

	@Override
	public void addPermision(String permission) {
		if(!permissions.contains(permission.toLowerCase())) 
			permissions.add(permission.toLowerCase());
	}

	@Override
	public boolean hasPermission(String perm) {
		Boolean[] permission = new Boolean[1];
		permission[0] = false;
		groups.forEach((name, group) -> {
			permission[0] = permission[0] ? false : group.getGroup().hasPermission(perm, false);
		});
		if(permissions.contains("-" + perm.toLowerCase())) 
			return permission[0] ? false : true;
		return permission[0] ? true : permissions.contains(perm.toLowerCase());
	}

	@Override
	public void removePermission(String permission) {
		if(permissions.contains(permission.toLowerCase())) 
			permissions.remove(permission.toLowerCase());
	}
}
