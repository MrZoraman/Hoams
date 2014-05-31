package com.mrz.dyndns.server.Hoams.commands;

import static com.mrz.dyndns.server.Hoams.Permissions.CAN_RELOAD;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mrz.dyndns.server.Hoams.Hoams;
import com.mrz.dyndns.server.Hoams.zorascommandsystem.bukkitcompat.CSBukkitCommand;

public class ReloadCommand implements CSBukkitCommand
{
	private final Hoams hoams;
	
	public ReloadCommand(Hoams hoams)
	{
		this.hoams = hoams;
	}
	
	@Override
	public boolean execute(CommandSender sender, Player player, String cmdName, String[] preArgs, String[] args)
	{
		if(CAN_RELOAD.verify(sender))
		{
			hoams.reload();
			sender.sendMessage(ChatColor.GREEN + "Homes reloaded");
			if(player != null) 
			{
				hoams.getLogger().info("Homes reloaded");
			}
		} 
		else 
		{
			sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
		}
		return true;
	}
}
