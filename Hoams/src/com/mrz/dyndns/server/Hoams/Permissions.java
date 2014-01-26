package com.mrz.dyndns.server.Hoams;

import org.bukkit.command.CommandSender;

public enum Permissions
{
	CAN_RELOAD				("hoams.reload"),
	CAN_SEE_HELP			("hoams.help"),
	WILL_RESPAWN_HOME		("hoams.respawnhome"),
	
	CAN_GO_HOME				("hoams.gohome", 
							 "hoams.gohome.self"),
							 
	CAN_SET_HOME			("hoams.sethome", 
							 "hoams.set.self"),
	
	CAN_GO_TO_OTHERS_HOME	("hoams.gohome.other"),
	CAN_SET_OTHERS_HOME		("hoams.set.other"),
	
	IS_IMMUNE				("hoams.immune"),
	OVERRIDES				("hoams.override");
	
	private Permissions(String... nodes)
	{
		this.nodes = nodes;
	}
	
	private final String[] nodes;
	
	public boolean verify(CommandSender sender)
	{
		boolean hasPermission = false;
		for(String node : nodes)
		{
			hasPermission |= sender.hasPermission(node);
		}
		return hasPermission;
	}
}
