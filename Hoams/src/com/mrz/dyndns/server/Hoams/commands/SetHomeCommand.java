package com.mrz.dyndns.server.Hoams.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mrz.dyndns.server.EnhancedCommandSystem.SimpleCommand;
import com.mrz.dyndns.server.Hoams.Hoams;

import static com.mrz.dyndns.server.Hoams.Permissions.*;

public class SetHomeCommand implements SimpleCommand
{
	public SetHomeCommand(Hoams plugin)
	{
		this.plugin = plugin;
	}

	private final Hoams plugin;

	@Override
	public boolean Execute(String commandName, CommandSender sender, List<String> args, List<String> variables)
	{
		Player player = null;
		if (sender instanceof Player)
		{
			player = (Player) sender;
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "You must be a player to set a home!");
			return true;
		}

		if (args.size() == 0)
		{
			if (CAN_SET_HOME.verify(sender))
			{
				plugin.getHomeManager().saveHome(player);
				player.sendMessage(ChatColor.GREEN + "Home set!");
				return true;
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
				return true;
			}
		}
		else
		{
			if (CAN_SET_OTHERS_HOME.verify(sender))
			{
				Player target = Bukkit.getPlayer(args.get(0));
				if (target == null)
				{
					player.sendMessage(ChatColor.RED + "Player '" + ChatColor.GOLD + args.get(0) + ChatColor.RED + "' is not online!");
					return true;
				}
				else
				{
					if (IS_IMMUNE.verify(target) && !OVERRIDES.verify(sender))
					{
						player.sendMessage(ChatColor.RED + "You are not allowed to set \'" + ChatColor.GOLD + target.getName() + 
								(target.getName().endsWith("s") ? "\'" : "'s") + ChatColor.RED + "\' home!");
						return true;
					}
					plugin.getHomeManager().saveHome(target, player.getLocation());
					player.sendMessage(ChatColor.GREEN + "Home for player '" + ChatColor.GOLD + args.get(0) + ChatColor.GREEN + "' set!");
					target.sendMessage(ChatColor.GREEN + "Your home has been set!");
					return true;
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
				return true;
			}
		}
	}
}
