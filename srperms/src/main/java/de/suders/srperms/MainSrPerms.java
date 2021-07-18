package de.suders.srperms;

import de.suders.srperms.storage.Data;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 17.07.2021
* Project: srperms
*/
public interface MainSrPerms {

	public Data getData();
	/**
	 * Is the API for Player or Groups enabled
	 * @return
	 */
	public boolean isApiEnabled();
	
}
