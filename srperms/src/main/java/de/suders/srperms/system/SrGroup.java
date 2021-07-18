package de.suders.srperms.system;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.NotSaved;

import de.suders.srperms.storage.Data;
import lombok.Getter;
import lombok.Setter;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 17.07.2021
* Project: srperms
*/
@Entity(value = "SrGroups", noClassnameStored = true)
public class SrGroup implements AdaptiveSrGroup {

	@Getter @Id
	private String identifier;
	@Getter @NotSaved
	private List<String> members;
	@Getter @Embedded
	private List<String> permissions;
	@Getter @Embedded
	private List<AdaptiveSrGroup> adaptiveSrGroups;
	@Getter @Setter
	private boolean isDefault;
	
	public SrGroup(final String identifier) { 
		this.identifier = identifier;
		this.members = new ArrayList<>();
		this.permissions = new ArrayList<String>();
		this.adaptiveSrGroups = new ArrayList<>();
	}
	
	public void save() {
		Data.getInstance().getMongoDB().getGroupsData().save(this);
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object instanceof AdaptiveSrGroup) {
			AdaptiveSrGroup group = (AdaptiveSrGroup) object;
			return group.getIdentifier().equals(this.identifier);
		}
		return false;
	}

	@Override
	public boolean hasPermission(final String name, final boolean loop) {
		Boolean[] permission = new Boolean[1];
		permission[0] = false;
		
		if(name.startsWith("-")) 
			permission[0] = permissions.contains(name.toLowerCase());
		
		if(permissions.contains("-" + name.toLowerCase())) return false;
			
		if(permissions.contains(name.toLowerCase())) permission[0] = true;
		
		if(!loop)
		adaptiveSrGroups.forEach(group -> {
			if(group.hasPermission(name, true)) 
				permission[0] = true;
		});
		
		return permission[0];
	}

	@Override
	public void addPermission(String name) {
		if(!permissions.contains(name.toLowerCase())) 
			permissions.add(name.toLowerCase());
	}

	@Override
	public void removePermission(String name) {
		if(permissions.contains(name.toLowerCase()))
			permissions.add(name.toLowerCase());
	}

	@Override
	public List<AdaptiveSrGroup> getSubGroups() {
		return this.adaptiveSrGroups;
	}

	@Override
	public void addSubGroup(AdaptiveSrGroup adaptiveSrGroup) {
		if(!this.adaptiveSrGroups.contains(adaptiveSrGroup)) 
			this.adaptiveSrGroups.add(adaptiveSrGroup);
	}

	@Override
	public void removeSubGroup(AdaptiveSrGroup adaptiveSrGroup) {
		if(this.adaptiveSrGroups.contains(adaptiveSrGroup))
			this.adaptiveSrGroups.remove(adaptiveSrGroup);
	}
}
