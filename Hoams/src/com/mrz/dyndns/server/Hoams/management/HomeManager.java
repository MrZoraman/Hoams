package com.mrz.dyndns.server.Hoams.management;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import static com.mrz.dyndns.server.Hoams.management.LoadFailureType.*;

public class HomeManager
{
	public HomeManager(JavaPlugin plugin)
	{
		this.config = plugin.getConfig();
		this.plugin = plugin;
	}
	
	private final FileConfiguration config;
	private final JavaPlugin plugin;

//	public void saveHome(Player player)
//	{
//		UUID playerUuid = player.getUniqueId();
//		Location loc = player.getLocation();
//		config.set("Homes." + playerUuid.toString() + ".World", loc.getWorld().getName());
//		config.set("Homes." + playerUuid.toString() + ".X", loc.getX());
//		config.set("Homes." + playerUuid.toString() + ".Y", loc.getY());
//		config.set("Homes." + playerUuid.toString() + ".Z", loc.getZ());
//		config.set("Homes." + playerUuid.toString() + ".Yaw", loc.getYaw());
//		config.set("Homes." + playerUuid.toString() + ".Pitch", loc.getPitch());
//		plugin.saveConfig();
//	}

	public void saveHome(UUID playerUuid, Location loc)
	{
		config.set("Homes." + playerUuid.toString() + ".World", loc.getWorld().getName());
		config.set("Homes." + playerUuid.toString() + ".X", loc.getX());
		config.set("Homes." + playerUuid.toString() + ".Y", loc.getY());
		config.set("Homes." + playerUuid.toString() + ".Z", loc.getZ());
		config.set("Homes." + playerUuid.toString() + ".Yaw", loc.getYaw());
		config.set("Homes." + playerUuid.toString() + ".Pitch", loc.getPitch());
		plugin.saveConfig();
	}

	public HomeResult loadHome(UUID playerUuid)
	{
		if (homeExists(playerUuid))
		{
			World world = Bukkit.getWorld(config.getString("Homes." + playerUuid.toString() + ".World"));
			if (world == null)
			{
				return new HomeResult(NO_MAP, null);
			}
			else
			{
				Location loc = new Location(world, 
						config.getDouble("Homes." + playerUuid + ".X"), 
						config.getDouble("Homes." + playerUuid + ".Y"), 
						config.getDouble("Homes." + playerUuid + ".Z"), 
						(float) config.getDouble("Homes." + playerUuid + ".Yaw"), 
						(float) config.getDouble("Homes." + playerUuid + ".Pitch"));
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
		return config.contains("Homes." + playerUuid.toString() + ".World");
	}
	
	public void convertToUuids()
	{
		Set<String> keys = config.getConfigurationSection("Homes").getKeys(false);
		for(String key : keys)
		{
			if(!isUuid(key))
			{
				//time to convert!
				String worldName = config.getString("Homes." + key + ".World");
				double x = config.getDouble("Homes." + key + ".X");
				double y = config.getDouble("Homes." + key + ".Y");
				double z = config.getDouble("Homes." + key + ".Z");
				double yaw = config.getDouble("Homes." + key + ".Yaw");
				double pitch = config.getDouble("Homes." + key + ".Pitch");
				
				//delete bad entry
				config.set("Homes." + key + ".World", null);
				config.set("Homes." + key + ".X", null);
				config.set("Homes." + key + ".Y", null);
				config.set("Homes." + key + ".Z", null);
				config.set("Homes." + key + ".Yaw", null);
				config.set("Homes." + key + ".Pitch", null);
				config.set("Homes." + key, null);
				
				
				
				plugin.saveConfig();
			}
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
