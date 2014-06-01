package com.mrz.dyndns.server.Hoams.commands;

import static com.mrz.dyndns.server.Hoams.Permissions.*;

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
import com.mrz.dyndns.server.Hoams.zorascommandsystem.bukkitcompat.CSBukkitCommand;

public class SetHomeCommand implements CSBukkitCommand
{
	public SetHomeCommand(Hoams plugin)
	{
		this.plugin = plugin;
	}

	private final Hoams plugin;

	@Override
	public boolean execute(final CommandSender sender, final Player player, String cmdName, String[] preArgs, String[] args)
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
				plugin.getHomeManager().saveHome(player.getUniqueId(), player.getLocation());
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
									
									Player target = Bukkit.getPlayer(targetUuid);
									
									if (target == null)
									{
										player.sendMessage(ChatColor.RED + "Player '" + ChatColor.GOLD + targetName + ChatColor.RED + "' is not online!");
										return;
									}
									else
									{
										if (IS_IMMUNE.verify(target) && !OVERRIDES.verify(sender))
										{
											player.sendMessage(ChatColor.RED + "You are not allowed to set " + ChatColor.GOLD + target.getName() + 
													(target.getName().endsWith("s") ? "\'" : "'s") + ChatColor.RED + " home!");
											return;
										}
										plugin.getHomeManager().saveHome(target.getUniqueId(), player.getLocation());
										player.sendMessage(ChatColor.GREEN + "Home for player '" + ChatColor.GOLD + targetName + ChatColor.GREEN + "' set!");
										target.sendMessage(ChatColor.GREEN + "Your home has been set!");
										return;
									}
								}
							});//end sync
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
							}); //end sync
						}
					}
				});//end async
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
				return true;
			}
		}
		
		return true;
	}
}
