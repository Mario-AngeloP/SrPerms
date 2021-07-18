package de.suders.srperms.system;

import java.util.Map;

import lombok.NonNull;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 17.07.2021
* Project: srperms
*/
public interface AdaptiveSrPlayer {

	public String getUniqueId();
	public Map<String, SrPlayerGroup> getGroups();
	public boolean equals(Object obj);
	public void addPermision(final String permission);
	public boolean hasPermission(final String permission);
	public void removePermission(final String permission);
	public void addGroup(@NonNull final String name, long expriationTime);
	public void removeGroup(@NonNull final String name);
	public void changeExpirationTimeOfGroup(@NonNull final String name, long expriationTime);
	public void save();
	public void init();

}
