package com.mrz.dyndns.server.Hoams.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.mrz.dyndns.server.Hoams.evilmidget38.UUIDFetcher;

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
	
	public boolean convertToUuids()
	{
		class Home
		{
			String worldName;
			double x, y, z, yaw, pitch;
		}
		
		Map<String, Home> tempHomes = new HashMap<String, Home>();
		
		Set<String> keys = config.getConfigurationSection("Homes").getKeys(false);
		for(String key : keys)
		{
			if(!isUuid(key))
			{
				//time to convert!
				
				Home home = new Home();
				home.worldName = config.getString("Homes." + key + ".World");
				home.x = config.getDouble("Homes." + key + ".X");
				home.y = config.getDouble("Homes." + key + ".Y");
				home.z = config.getDouble("Homes." + key + ".Z");
				home.yaw = config.getDouble("Homes." + key + ".Yaw");
				home.pitch = config.getDouble("Homes." + key + ".Pitch");
				
				tempHomes.put(key, home);
				
				//delete bad entry
				config.set("Homes." + key + ".World", null);
				config.set("Homes." + key + ".X", null);
				config.set("Homes." + key + ".Y", null);
				config.set("Homes." + key + ".Z", null);
				config.set("Homes." + key + ".Yaw", null);
				config.set("Homes." + key + ".Pitch", null);
				config.set("Homes." + key, null);
			}
		}
		
		if(!tempHomes.isEmpty())
		{
			plugin.getLogger().info("Converting to uuid system...");
			UUIDFetcher fetcher = new UUIDFetcher(new ArrayList<String>(tempHomes.keySet()));
			Map<String, UUID> result = null;
			try
			{
				result = fetcher.call();
			}
			catch (Exception e)
			{
				plugin.getLogger().severe("Failed to convert to uuid system!");
				e.printStackTrace();
				return false;
			}
			
			for(Map.Entry<String, UUID> items : result.entrySet())
			{
				String uuidString = items.getValue().toString();
				Home home = tempHomes.get(items.getKey());
				
				config.set("Homes." + uuidString + ".World", home.worldName);
				config.set("Homes." + uuidString + ".X", home.x);
				config.set("Homes." + uuidString + ".Y", home.y);
				config.set("Homes." + uuidString + ".Z", home.z);
				config.set("Homes." + uuidString + ".Yaw", home.yaw);
				config.set("Homes." + uuidString + ".Pitch", home.pitch);
				
				plugin.saveConfig();
			}
		}
		
		return true;
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
