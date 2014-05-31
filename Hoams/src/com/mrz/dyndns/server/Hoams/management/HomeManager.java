package com.mrz.dyndns.server.Hoams.management;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.mrz.dyndns.server.Hoams.Hoams;

import static com.mrz.dyndns.server.Hoams.management.LoadFailureType.*;

public class HomeManager
{
	public HomeManager(Hoams plugin)
	{
		this.plugin = plugin;
	}

	private Hoams plugin;

//	public void saveHome(Player player)
//	{
//		UUID playerUuid = player.getUniqueId();
//		Location loc = player.getLocation();
//		plugin.getConfig().set("Homes." + playerUuid.toString() + ".World", loc.getWorld().getName());
//		plugin.getConfig().set("Homes." + playerUuid.toString() + ".X", loc.getX());
//		plugin.getConfig().set("Homes." + playerUuid.toString() + ".Y", loc.getY());
//		plugin.getConfig().set("Homes." + playerUuid.toString() + ".Z", loc.getZ());
//		plugin.getConfig().set("Homes." + playerUuid.toString() + ".Yaw", loc.getYaw());
//		plugin.getConfig().set("Homes." + playerUuid.toString() + ".Pitch", loc.getPitch());
//		plugin.saveConfig();
//	}

	public void saveHome(UUID playerUuid, Location loc)
	{
		plugin.getConfig().set("Homes." + playerUuid.toString() + ".World", loc.getWorld().getName());
		plugin.getConfig().set("Homes." + playerUuid.toString() + ".X", loc.getX());
		plugin.getConfig().set("Homes." + playerUuid.toString() + ".Y", loc.getY());
		plugin.getConfig().set("Homes." + playerUuid.toString() + ".Z", loc.getZ());
		plugin.getConfig().set("Homes." + playerUuid.toString() + ".Yaw", loc.getYaw());
		plugin.getConfig().set("Homes." + playerUuid.toString() + ".Pitch", loc.getPitch());
		plugin.saveConfig();
	}

	public HomeResult loadHome(UUID playerUuid)
	{
		if (homeExists(playerUuid))
		{
			World world = Bukkit.getWorld(plugin.getConfig().getString("Homes." + playerUuid.toString() + ".World"));
			if (world == null)
			{
				return new HomeResult(NO_MAP, null);
			}
			else
			{
				Location loc = new Location(world, 
						plugin.getConfig().getDouble("Homes." + playerUuid.toString() + ".X"), 
						plugin.getConfig().getDouble("Homes." + playerUuid.toString() + ".Y"), 
						plugin.getConfig().getDouble("Homes." + playerUuid.toString() + ".Z"), 
						(float) plugin.getConfig().getDouble("Homes." + playerUuid.toString() + ".Yaw"), 
						(float) plugin.getConfig().getDouble("Homes." + playerUuid.toString() + ".Pitch"));
				return new HomeResult(NONE, loc);
			}
		}
		else
		{
			return new HomeResult(NO_HOME, null);
		}
	}

	public boolean homeExists(UUID playerUuid)
	{
		return plugin.getConfig().contains("Homes." + playerUuid.toString() + ".World");
	}
	
	public void convertToUuids()
	{
		Set<String> keys = plugin.getConfig().getConfigurationSection("Homes").getKeys(false);
		for(String key : keys)
		{
			System.out.println("found key: " + key + ", and is it a uuid?: " + isUuid(key));
		}
	}
	
	private static boolean isUuid(String uuidString)
	{
		try
		{
			UUID uuid = UUID.fromString(uuidString);
			
			return uuid.toString().equals(uuidString);
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
