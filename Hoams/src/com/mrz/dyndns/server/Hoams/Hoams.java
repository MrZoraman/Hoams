package com.mrz.dyndns.server.Hoams;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.mrz.dyndns.server.Hoams.commands.*;
import com.mrz.dyndns.server.Hoams.management.HomeManager;
import com.mrz.dyndns.server.Hoams.management.HomeResult;
import com.mrz.dyndns.server.Hoams.management.LoadFailureType;
import com.mrz.dyndns.server.Hoams.zorascommandsystem.bukkitcompat.BukkitCommandSystem;
import com.mrz.dyndns.server.Hoams.zorascommandsystem.bukkitcompat.CSBukkitCommand;

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
		
		cs.registerCommand("home reload", new CSBukkitCommand() 
		{
			@Override
			public boolean execute(CommandSender sender, Player player, String cmdName, String[] preArgs, String[] args)
			{
				if(CAN_RELOAD.verify(sender))
				{
					reload();
					sender.sendMessage(ChatColor.GREEN + "Homes reloaded");
					if(sender instanceof org.bukkit.entity.Player) 
					{
						getLogger().info("Homes reloaded");
					}
				} 
				else 
				{
					sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
				}
				return true;
			}
		});
		
		cs.registerCommand("home help", new CSBukkitCommand() 
		{
			@Override
			public boolean execute(CommandSender sender, Player player, String cmdName, String[] preArgs, String[] args)
			{
				if(CAN_SEE_HELP.verify(sender))
				{
					sender.sendMessage(ChatColor.GOLD + "-----Homes help-----");
					if(CAN_GO_HOME.verify(sender))
						sender.sendMessage(ChatColor.DARK_AQUA + "/home " + ChatColor.GOLD + "-" + ChatColor.AQUA + " Takes you to your home");
					
					if(CAN_SET_HOME.verify(sender))
						sender.sendMessage(ChatColor.DARK_AQUA + "/home set " + (useSetHome ? ChatColor.AQUA + "or " + ChatColor.DARK_AQUA + "/sethome " : "") + ChatColor.GOLD + "-" + ChatColor.AQUA + " Sets your home");
					
					if(CAN_GO_TO_OTHERS_HOME.verify(sender))
						sender.sendMessage(ChatColor.DARK_AQUA + "/home [PlayerName] " + ChatColor.GOLD + "-" + ChatColor.AQUA + " Takes you to a player's home");
					
					if(CAN_SET_OTHERS_HOME.verify(sender))
						sender.sendMessage(ChatColor.DARK_AQUA + "/home set [PlayerName] " + (useSetHome ? ChatColor.AQUA + "or " + ChatColor.DARK_AQUA + "/sethome [PlayerName] " : "") + ChatColor.GOLD + "-" + ChatColor.AQUA + " Sets a player's home");
					
					if(CAN_RELOAD.verify(sender))
						sender.sendMessage(ChatColor.DARK_AQUA + "/home reload " + ChatColor.GOLD + "-" + ChatColor.AQUA + " Reloads homes file");
					
					if(player == null) 
					{
						sender.sendMessage(ChatColor.RED + "NOTICE: You are not a player so the only commands you can use are " + ChatColor.DARK_AQUA + "/home help" + ChatColor.RED  + " and " + ChatColor.DARK_AQUA + "/home reload" + ChatColor.RED + "!");
					}
				} 
				else 
				{
					sender.sendMessage(ChatColor.RED + "You don't have permission to do that!"); 
				}
				return true;
			}
		});
		
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
