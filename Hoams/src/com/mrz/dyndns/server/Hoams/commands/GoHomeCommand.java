package com.mrz.dyndns.server.Hoams.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mrz.dyndns.server.EnhancedCommandSystem.SimpleCommand;
import com.mrz.dyndns.server.Hoams.Hoams;
import com.mrz.dyndns.server.Hoams.management.HomeResult;

import static com.mrz.dyndns.server.Hoams.Permissions.*;

public class GoHomeCommand implements SimpleCommand 
{
	public GoHomeCommand(Hoams plugin) 
	{
		this.plugin = plugin;
	}
	
	private final Hoams plugin;
	
	@Override
	public boolean Execute(String commandName, CommandSender sender, List<String> args, List<String> variables)
	{
		Player player = null;
		if(sender instanceof Player) 
		{
			player = (Player) sender;
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
			return true;
		}
		
		if(args.size() == 0) 
		{
			if(CAN_GO_HOME.verify(sender))
			{
				HomeResult result = plugin.getHomeManager().loadHome(player);
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
				Player target = Bukkit.getPlayer(args.get(0));
				if(target == null) 
				{
					player.sendMessage(ChatColor.RED + "Player '" + ChatColor.GOLD + args.get(0) + ChatColor.RED + "' is not online!");
					return true;
				} 
				else 
				{
					HomeResult result = plugin.getHomeManager().loadHome(target);
					switch(result.getLoadFailureType()) {
					case NONE:
						player.teleport(result.getHome());
						return true;
					case NO_HOME:
						player.sendMessage(ChatColor.GOLD + target.getName() + ChatColor.RED + " doesn't have a home set!");
						if(CAN_SET_OTHERS_HOME.verify(sender))
						{
							player.sendMessage(ChatColor.AQUA + "Use " + ChatColor.DARK_AQUA + "/home set " + target.getName() 
									+ ChatColor.AQUA + " or " + ChatColor.DARK_AQUA + "/sethome " + target.getName() + ChatColor.AQUA 
									+ " to set " + ChatColor.GOLD + target.getName() + ChatColor.AQUA + "'s home.");
						}
						return true;
					case NO_MAP:
						player.sendMessage(ChatColor.RED + "That world that " + ChatColor.GOLD + target.getName() + ChatColor.RED 
								+ " is in either isn't loaded by the server or no longer exists :(");
						return true;
					default:
						return true;
					}
				}
			} 
			else 
			{
				player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
				return true;
			}
		}
	}
}
