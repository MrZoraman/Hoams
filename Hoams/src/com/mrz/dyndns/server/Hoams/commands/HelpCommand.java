package com.mrz.dyndns.server.Hoams.commands;

import static com.mrz.dyndns.server.Hoams.Permissions.*;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mrz.dyndns.server.Hoams.zorascommandsystem.bukkitcompat.CSBukkitCommand;

public class HelpCommand implements CSBukkitCommand
{
	private final boolean useSetHome;
	
	public HelpCommand(boolean useSetHome)
	{
		this.useSetHome = useSetHome;
	}
	
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
}
