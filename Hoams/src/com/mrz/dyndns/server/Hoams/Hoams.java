package com.mrz.dyndns.server.Hoams;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.mrz.dyndns.server.Hoams.commands.*;
import com.mrz.dyndns.server.Hoams.management.HomeManager;
import com.mrz.dyndns.server.Hoams.management.HomeResult;
import com.mrz.dyndns.server.Hoams.management.LoadFailureType;
import com.mrz.dyndns.server.Hoams.zorascommandsystem.bukkitcompat.BukkitCommandSystem;
import static com.mrz.dyndns.server.Hoams.Permissions.*;

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
			this.getServer().getPluginManager().registerEvents(new Listener() 
			{
				@EventHandler
				public void onPlayerRespawn(PlayerRespawnEvent event) 
				{
					final org.bukkit.entity.Player player = event.getPlayer();
					
					if(WILL_RESPAWN_HOME.verify(player))
					{
						HomeResult result = homeManager.loadHome(player.getName());
						if(result.getLoadFailureType().equals(LoadFailureType.NONE))
						{
							event.setRespawnLocation(result.getHome());
						}
					}
				}
			}, this);
		}
	}
}
