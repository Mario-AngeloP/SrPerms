package de.suders.srperms.system.object;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import de.suders.srperms.system.AdaptiveSrPlayerGroup;
import lombok.Getter;
import lombok.Setter;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 18.07.2021
* Project: srperms
*/
@Entity(value = "SrPlayerGroup", noClassnameStored = true)
public class SrPlayerGroup implements AdaptiveSrPlayerGroup {
	@Id @Getter
	private String identifier;
	@Getter @Setter
	private long startTime, expirationTime;
	@Reference @Getter
	private SrGroup group;
	@Reference @Getter
	private SrPlayer player;

	public SrPlayerGroup() {
		
	}
	
	public SrPlayerGroup(final String identifier, final long startTime, final long expirationTime, final SrGroup group, final SrPlayer player) {
		this.identifier = identifier;
		this.startTime = startTime;
		this.expirationTime = expirationTime;
		this.player = player;
		this.group = group;
	}
	
	public boolean equals(final Object object) {
		if(object instanceof SrPlayerGroup) {
			final SrPlayerGroup group = (SrPlayerGroup) object;
			return group.getIdentifier().equals(this.identifier) && 
					group.getStartTime() == this.startTime &&
					group.getExpirationTime() == this.expirationTime && 
					group.getGroup().equals(this.group) &&
					group.getPlayer().equals(this.player);
		}
		return false;
	}
}
