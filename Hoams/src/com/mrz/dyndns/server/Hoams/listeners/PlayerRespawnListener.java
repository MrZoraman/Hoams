package com.mrz.dyndns.server.Hoams.listeners;

import static com.mrz.dyndns.server.Hoams.Permissions.WILL_RESPAWN_HOME;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.mrz.dyndns.server.Hoams.management.HomeManager;
import com.mrz.dyndns.server.Hoams.management.HomeResult;
import com.mrz.dyndns.server.Hoams.management.LoadFailureType;

public class PlayerRespawnListener implements Listener
{
	private final HomeManager homeManager;
	
	public PlayerRespawnListener(HomeManager homeManager)
	{
		this.homeManager = homeManager;
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) 
	{
		final org.bukkit.entity.Player player = event.getPlayer();
		
		if(WILL_RESPAWN_HOME.verify(player))
		{
			HomeResult result = homeManager.loadHome(player.getUniqueId());
			if(result.getLoadFailureType().equals(LoadFailureType.NONE))
			{
				event.setRespawnLocation(result.getHome());
			}
		}
	}
}
