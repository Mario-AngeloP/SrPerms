package de.suders.srperms.system;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 17.07.2021
* Project: srperms
*/
public interface AdaptiveSrPlayerGroup {

	public AdaptiveSrPlayer getPlayer();
	public AdaptiveSrGroup getGroup();
	/**
	 * the time, where the player got the group
	 * @return
	 */
	public long getStartTime();
	/**
	 * the time, where the player lose the group
	 * when set to -1, the is no expiration
	 * @return
	 */
	public long getExpirationTime();
	public void setExpirationTime(long expirationTime);
	
}
