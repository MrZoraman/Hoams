package com.mrz.dyndns.server.Hoams.commands;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mrz.dyndns.server.Hoams.Hoams;
import com.mrz.dyndns.server.Hoams.evilmidget38.UUIDFetcher;
import com.mrz.dyndns.server.Hoams.management.HomeResult;
import com.mrz.dyndns.server.Hoams.zorascommandsystem.bukkitcompat.CSBukkitCommand;

import static com.mrz.dyndns.server.Hoams.Permissions.*;

public class GoHomeCommand implements CSBukkitCommand 
{
	public GoHomeCommand(Hoams plugin) 
	{
		this.plugin = plugin;
	}
	
	private final Hoams plugin;
	
	@Override
	public boolean execute(final CommandSender sender, final Player player, String cmdName, String[] preArgs, String[] args)
	{
		if(player == null)
		{
			sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
			return true;
		}
		
		if(args.length == 0) 
		{
			if(CAN_GO_HOME.verify(sender))
			{
				HomeResult result = plugin.getHomeManager().loadHome(player.getUniqueId());
				switch(result.getLoadFailureType()) 
				{
				case NONE:
					player.teleport(result.getHome());
					return true;
				case NO_HOME:
					player.sendMessage(ChatColor.RED + "You don't have a home set!");
					if(CAN_SET_HOME.verify(sender))
					{
						player.sendMessage(ChatColor.AQUA + "Use " + ChatColor.DARK_AQUA + "/home set" + ChatColor.AQUA + " or " 
								+ ChatColor.DARK_AQUA + "/sethome" + ChatColor.AQUA + " to set your home.");
					}
					return true;
				case NO_MAP:
					player.sendMessage(ChatColor.RED + "That world your home is in either isn't loaded by the server or no longer exists :(");
					return true;
				default:
					return true;
				}
			} 
			else 
			{
				player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
				return true;
			}
		} 
		else 
		{
			if(CAN_GO_TO_OTHERS_HOME.verify(sender))
			{
				final String targetName = args[0];
				
				UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(args[0]));
				final Future<Map<String, UUID>> f = Bukkit.getScheduler().callSyncMethod(plugin, fetcher);
				sender.sendMessage(ChatColor.YELLOW + "Starting teleport...");
				
				Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							final UUID targetUuid = f.get().get(targetName);
							
							Bukkit.getScheduler().runTask(plugin, new Runnable()
							{
								@Override
								public void run()
								{
									if(targetUuid == null)
									{
										sender.sendMessage(ChatColor.RED + "Failed to find uuid for that player name! Perhaps they changed their name...");
										return;
									}
									
									HomeResult result = plugin.getHomeManager().loadHome(targetUuid);
									switch(result.getLoadFailureType()) {
									case NONE:
										player.teleport(result.getHome());
										return;
									case NO_HOME:
										player.sendMessage(ChatColor.GOLD + targetName + ChatColor.RED + " doesn't have a home set!");
										if(CAN_SET_OTHERS_HOME.verify(sender))
										{
											player.sendMessage(ChatColor.AQUA + "Use " + ChatColor.DARK_AQUA + "/home set " + targetName 
													+ (plugin.getConfig().getBoolean("Use_Sethome") ? ChatColor.AQUA + "or " + ChatColor.DARK_AQUA + "/sethome " : "") + ChatColor.AQUA 
													+ " to set " + ChatColor.GOLD + targetName + ChatColor.AQUA + "'s home.");
										}
										return;
									case NO_MAP:
										player.sendMessage(ChatColor.RED + "That world that " + ChatColor.GOLD + targetName + ChatColor.RED 
												+ " has their home in either isn't loaded by the server or no longer exists :(");
										return;
									}
								}
							}); //end runnable
						}
						catch (Exception e)
						{
							Bukkit.getScheduler().runTask(plugin, new Runnable()
							{
								@Override
								public void run()
								{
									sender.sendMessage(ChatColor.RED + "Failed to retrieve uuid for player " + targetName);
								}
							}); //end runnable
						}
					}
				});// end async
			} 
			else 
			{
				player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
				return true;
			}
		}
		
		return true;
	}
}
