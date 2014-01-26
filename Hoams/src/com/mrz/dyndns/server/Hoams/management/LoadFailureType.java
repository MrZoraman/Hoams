package com.mrz.dyndns.server.Hoams.management;

/**
 * If shit's gonna hit the fan, I wanna know why!
 * @author Zora
 *
 */
public enum LoadFailureType 
{
	/**
	 * Everything went according to plan :)
	 */
	NONE,
	
	/**
	 * The map the home is on is not loaded, or doesn't exist (therefore, not loaded!)
	 */
	NO_MAP,
	
	/**
	 * The player doesn't have his/her home set
	 */
	NO_HOME;
}
