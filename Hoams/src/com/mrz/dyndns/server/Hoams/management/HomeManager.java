package com.mrz.dyndns.server.Hoams.management;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.mrz.dyndns.server.Hoams.Hoams;

import static com.mrz.dyndns.server.Hoams.management.LoadFailureType.*;

public class HomeManager
{
	public HomeManager(Hoams plugin)
	{
		this.plugin = plugin;
	}

	private Hoams plugin;

	public void saveHome(Player player)
	{
		Location loc = player.getLocation();
		plugin.getConfig().set("Homes." + player.getName() + ".World", loc.getWorld().getName());
		plugin.getConfig().set("Homes." + player.getName() + ".X", loc.getX());
		plugin.getConfig().set("Homes." + player.getName() + ".Y", loc.getY());
		plugin.getConfig().set("Homes." + player.getName() + ".Z", loc.getZ());
		plugin.getConfig().set("Homes." + player.getName() + ".Yaw", loc.getYaw());
		plugin.getConfig().set("Homes." + player.getName() + ".Pitch", loc.getPitch());
		plugin.saveConfig();
	}

	public void saveHome(Player player, Location loc)
	{
		plugin.getConfig().set("Homes." + player.getName() + ".World", loc.getWorld().getName());
		plugin.getConfig().set("Homes." + player.getName() + ".X", loc.getX());
		plugin.getConfig().set("Homes." + player.getName() + ".Y", loc.getY());
		plugin.getConfig().set("Homes." + player.getName() + ".Z", loc.getZ());
		plugin.getConfig().set("Homes." + player.getName() + ".Yaw", loc.getYaw());
		plugin.getConfig().set("Homes." + player.getName() + ".Pitch", loc.getPitch());
		plugin.saveConfig();
	}

	public HomeResult loadHome(Player player)
	{
		if (homeExists(player))
		{
			World world = Bukkit.getWorld(plugin.getConfig().getString("Homes." + player.getName() + ".World"));
			if (world == null)
			{
				return new HomeResult(NO_MAP, null);
			}
			else
			{
				Location loc = new Location(world, 
						plugin.getConfig().getDouble("Homes." + player.getName() + ".X"), 
						plugin.getConfig().getDouble("Homes." + player.getName() + ".Y"), 
						plugin.getConfig().getDouble("Homes." + player.getName() + ".Z"), 
						(float) plugin.getConfig().getDouble("Homes." + player.getName() + ".Yaw"), 
						(float) plugin.getConfig().getDouble("Homes." + player.getName() + ".Pitch"));
				return new HomeResult(NONE, loc);
			}
		}
		else
		{
			return new HomeResult(NO_HOME, null);
		}
	}

	public boolean homeExists(Player player)
	{
		return plugin.getConfig().contains("Homes." + player.getName() + ".World");
	}
}
