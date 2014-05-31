package com.mrz.dyndns.server.Hoams;

import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.mrz.dyndns.server.Hoams.commands.*;
import com.mrz.dyndns.server.Hoams.listeners.PlayerRespawnListener;
import com.mrz.dyndns.server.Hoams.management.HomeManager;
import com.mrz.dyndns.server.Hoams.zorascommandsystem.bukkitcompat.BukkitCommandSystem;

public class Hoams extends JavaPlugin 
{
	private HomeManager homeManager;
	
	@Override
	public void onEnable() 
	{
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		homeManager = new HomeManager(this);
		
		reload();
	}
	
	public HomeManager getHomeManager() 
	{
		return homeManager;
	}
	
	public void reload() 
	{
		reloadConfig();
		
		boolean useSetHome = getConfig().getBoolean("Use_Sethome");
		boolean goHomeOnDeath = getConfig().getBoolean("Go_home_on_death");
		
		homeManager.convertToUuids();
		
		BukkitCommandSystem cs = new BukkitCommandSystem(this);
		
		SetHomeCommand setHomeCommand = new SetHomeCommand(this);
		cs.registerCommand("home", new GoHomeCommand(this));
		cs.registerCommand("home set", setHomeCommand);
		
		if(useSetHome)
		{
			cs.registerCommand("sethome", setHomeCommand);
		}
		
		cs.registerCommand("home reload", new ReloadCommand(this));
		cs.registerCommand("home help", new HelpCommand(useSetHome));

		PlayerRespawnEvent.getHandlerList().unregister(this);
		
		if(goHomeOnDeath) 
		{
			this.getServer().getPluginManager().registerEvents(new PlayerRespawnListener(homeManager), this);
		}
	}
}
