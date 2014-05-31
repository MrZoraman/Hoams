package com.mrz.dyndns.server.Hoams.commands;

import static com.mrz.dyndns.server.Hoams.Permissions.CAN_SET_HOME;
import static com.mrz.dyndns.server.Hoams.Permissions.CAN_SET_OTHERS_HOME;
import static com.mrz.dyndns.server.Hoams.Permissions.IS_IMMUNE;
import static com.mrz.dyndns.server.Hoams.Permissions.OVERRIDES;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mrz.dyndns.server.Hoams.Hoams;
import com.mrz.dyndns.server.Hoams.zorascommandsystem.bukkitcompat.CSBukkitCommand;

public class SetHomeCommand implements CSBukkitCommand
{
	public SetHomeCommand(Hoams plugin)
	{
		this.plugin = plugin;
	}

	private final Hoams plugin;

	@Override
	public boolean execute(CommandSender sender, Player player, String cmdName, String[] preArgs, String[] args)
	{
		if(player == null)
		{
			sender.sendMessage(ChatColor.RED + "You must be a player to set a home!");
			return true;
		}

		if (args.length == 0)
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
				@SuppressWarnings("deprecation")//TODO
				Player target = Bukkit.getPlayer(args[0]);
				if (target == null)
				{
					player.sendMessage(ChatColor.RED + "Player '" + ChatColor.GOLD + args[0] + ChatColor.RED + "' is not online!");
					return true;
				}
				else
				{
					if (IS_IMMUNE.verify(target) && !OVERRIDES.verify(sender))
					{
						player.sendMessage(ChatColor.RED + "You are not allowed to set " + ChatColor.GOLD + target.getName() + 
								(target.getName().endsWith("s") ? "\'" : "'s") + ChatColor.RED + " home!");
						return true;
					}
					plugin.getHomeManager().saveHome(target, player.getLocation());
					player.sendMessage(ChatColor.GREEN + "Home for player '" + ChatColor.GOLD + args[0] + ChatColor.GREEN + "' set!");
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
