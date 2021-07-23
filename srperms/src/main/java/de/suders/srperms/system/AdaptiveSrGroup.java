package de.suders.srperms.system;

import java.util.List;

import de.suders.srperms.system.object.SrGroup;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 17.07.2021
* Project: srperms
*/
public interface AdaptiveSrGroup {

	public String getIdentifier();
	public boolean hasPermission(final String name, boolean loop);
	public void addPermission(final String name);
	public void removePermission(final String name);
	public List<SrGroup> getSubGroups();
	public void addSubGroup(final AdaptiveSrGroup adaptiveSrGroup);
	public void removeSubGroup(final AdaptiveSrGroup adaptiveSrGroup);
	public List<String> getMembers();
	public boolean equals(Object obj);
	public void save();
	public boolean isDefault();
	public void setDefault(final boolean isDefault);
	
}
