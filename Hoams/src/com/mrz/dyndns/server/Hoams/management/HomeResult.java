package com.mrz.dyndns.server.Hoams.management;

import org.bukkit.Location;

public class HomeResult {
	HomeResult(LoadFailureType type, Location home) {
		this.type = type;
		this.home = home;
	}
	
	HomeResult() {
		this.home = null;
		this.type = null;
	}
	
	private LoadFailureType type;
	private Location home;
	
	public LoadFailureType getLoadFailureType() {
		return type;
	}
	
	public Location getHome() {
		return home;
	}
	
	public void setLoadFailureType(LoadFailureType type) {
		this.type = type;
	}
	
	public void setHome(Location home) {
		this.home = home;
	}
}
