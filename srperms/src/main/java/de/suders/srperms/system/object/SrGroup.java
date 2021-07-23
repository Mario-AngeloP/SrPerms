package de.suders.srperms.system.object;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.NotSaved;
import org.mongodb.morphia.annotations.Reference;

import de.suders.srperms.storage.Data;
import de.suders.srperms.system.AdaptiveSrGroup;
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
	@Getter @Embedded @Reference
	private List<SrGroup> adaptiveSrGroups;
	@Getter @Setter
	private boolean isDefault;
	
	public SrGroup() {
		this.members = new ArrayList<>();
		this.permissions = new ArrayList<>();
		this.adaptiveSrGroups = new ArrayList<>();
	}
	
	public SrGroup(final String identifier) { 
		this.identifier = identifier;
		this.members = new ArrayList<>();
		this.permissions = new ArrayList<String>();
		this.adaptiveSrGroups = new ArrayList<>();
	}
	
	public void save() {
		Data.getInstance().getMongoDB().getDatastore().findAndDelete(Data.getInstance().getMongoDB().getDatastore().createQuery(SrGroup.class).field("_id").equalIgnoreCase(identifier));
		Data.getInstance().getMongoDB().getDatastore().save(this);
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
		if(!permissions.contains(name.toLowerCase())) {
			permissions.add(name.toLowerCase());
			save();
		}
	}

	@Override
	public void removePermission(String name) {
		if(permissions.contains(name.toLowerCase())) {
			permissions.remove(name.toLowerCase());
			save();
		}
	}

	@Override
	public List<SrGroup> getSubGroups() {
		return this.adaptiveSrGroups;
	}

	@Override
	public void addSubGroup(AdaptiveSrGroup adaptiveSrGroup) {
		if(!this.adaptiveSrGroups.contains((SrGroup) adaptiveSrGroup)) {
			this.adaptiveSrGroups.add((SrGroup) adaptiveSrGroup);
			save();
			adaptiveSrGroup.save();
		}
	}

	@Override
	public void removeSubGroup(AdaptiveSrGroup adaptiveSrGroup) {
		if(this.adaptiveSrGroups.contains((SrGroup) adaptiveSrGroup)) {
			this.adaptiveSrGroups.remove((SrGroup) adaptiveSrGroup);
			save();
			adaptiveSrGroup.save();
		}
	}
}
