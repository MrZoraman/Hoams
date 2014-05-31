package com.mrz.dyndns.server.Hoams;

import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.mrz.dyndns.server.Hoams.commands.*;
import com.mrz.dyndns.server.Hoams.listeners.PlayerRespawnListener;
import com.mrz.dyndns.server.Hoams.management.HomeManager;
import com.mrz.dyndns.server.Hoams.zorascommandsystem.bukkitcompat.BukkitCommandSystem;

public class Hoams extends JavaPlugin 
{
	private BukkitCommandSystem cs;
	private HomeManager homeManager;
	
	private boolean useSetHome;
	private boolean goHomeOnDeath;
	
	private GoHomeCommand goHomeCommand;
	private SetHomeCommand setHomeCommand;
	
	@Override
	public void onEnable() 
	{
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		homeManager = new HomeManager(this);
		
		cs = new BukkitCommandSystem(this);
		
		goHomeCommand = new GoHomeCommand(this);
		setHomeCommand = new SetHomeCommand(this);
		
		reload();
	}
	
	@Override
	public void onDisable() 
	{
	}
	
	public HomeManager getHomeManager() 
	{
		return homeManager;
	}
	
	public void reload() 
	{
		PlayerRespawnEvent.getHandlerList().unregister(this);
		reloadConfig();
		
		useSetHome = getConfig().getBoolean("Use_Sethome");
		goHomeOnDeath = getConfig().getBoolean("Go_home_on_death");
		
		cs.registerCommand("home", goHomeCommand);
		cs.registerCommand("home set", setHomeCommand);
		
		if(useSetHome)
			cs.registerCommand("sethome", setHomeCommand);
		
		cs.registerCommand("home reload", new ReloadCommand(this));
		cs.registerCommand("home help", new HelpCommand(useSetHome));
		
		if(goHomeOnDeath) 
		{
			this.getServer().getPluginManager().registerEvents(new PlayerRespawnListener(homeManager), this);
		}
	}
}
