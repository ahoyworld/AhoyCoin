package uk.co.ahoyworld;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProjectsCommand implements CommandExecutor {

	private AhoyCoin plugin;
	
	public ProjectsCommand(AhoyCoin plugin)
	{
		this.plugin = plugin;
	}
	
	/*
	 * 1 - list - DONE
	 * 2 - add [name]
	 * 2 - info [name]
	 * 2 - remove [name]
	 * 4 - resource remove [projectname] [resourcename]
	 * 5 - resource add [projectname] [resourcename] [quantity]
	 */
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		Player player = (Player) sender;
		if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("list"))
			{
				if (!plugin.projects.getKeys(false).isEmpty())
				{
					String projectsList = "";
					for (String str : plugin.projects.getKeys(false))
					{
						projectsList = projectsList + str + ", ";
					}
					player.sendMessage(plugin.pre + projectsList);
					return true;
				} else {
					player.sendMessage(plugin.pre + "No projects have been created yet!");
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase("add"))
			{
				player.sendMessage(plugin.pre + "No project name provided!");
				player.sendMessage(plugin.pre + "Usage: /project add [projectname]");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("info"))
			{
				player.sendMessage(plugin.pre + "No project name provided!");
				player.sendMessage(plugin.pre + "Usage: /project info [projectname]");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("remove"))
			{
				player.sendMessage(plugin.pre + "No project name provided!");
				player.sendMessage(plugin.pre + "Usage: /project remove [projectname]");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("resource"))
			{
				player.sendMessage(plugin.pre + "Not enough parameters provided.");
				player.sendMessage(plugin.pre + "Usage: /project resource [add, remove]");
				return true;
			}
		}
		
		if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("add"))
			{
				String newProjectName = args[1];
				if (!plugin.projects.getKeys(false).contains(newProjectName))
				{
					TimeZone tz = TimeZone.getTimeZone("GMT:00");
					DateFormat dfgMT = DateFormat.getTimeInstance(DateFormat.LONG);
					dfgMT.setTimeZone(tz);
					
					plugin.projects.set(newProjectName + ".started", dfgMT.format(new Date()));
					plugin.projects.set(newProjectName + ".status", "inactive");

					plugin.saveYamls();
		    		
					player.sendMessage(plugin.pre + "Project \"" + newProjectName + "\" created!");
					return true;
				} else {
					player.sendMessage(plugin.pre + "Project \"" + newProjectName + "\" already exists!");
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase("info"))
			{
				String project = args[1];
				if (plugin.projects.getKeys(false).contains(project))
				{
					String status = plugin.projects.getString(project + ".status");
					String startTime = plugin.projects.getString(project + ".started");
					player.sendMessage(plugin.pre + "Project Name: " + project);
					//player.sendMessage(plugin.pre + "Town: " + "");
					player.sendMessage(plugin.pre + "Status: " + status);
					player.sendMessage(plugin.pre + "Started: " + startTime);
					player.sendMessage(plugin.pre + "Resources: " + "Not yet implemeted");
					return true;
				} else {
					player.sendMessage(plugin.pre + "Project \"" + project + "\" does not exist!");
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase("remove"))
			{
				String project = args[1];
				if (plugin.projects.getKeys(false).contains(project))
				{
					plugin.projects.set(project, null);
					
					plugin.saveYamls();

					player.sendMessage(plugin.pre + "Project \"" + project + "\" deleted.");
					return true;
				} else {
					player.sendMessage(plugin.pre + "Project \"" + project + "\" does not exist!");
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase("resource"))
			{
				if (args[1].equalsIgnoreCase("add"))
				{
					player.sendMessage(plugin.pre + "Not enough parameters provided.");
					player.sendMessage(plugin.pre + "Usage: /project resource add [projectname] [resourcename] [quantity]");
					return true;
				}
				
				if (args[1].equalsIgnoreCase("remove"))
				{
					player.sendMessage(plugin.pre + "Not enough parameters provided.");
					player.sendMessage(plugin.pre + "Usage: /project resource remove [projectname] [resourcename]");
					return true;
				}
			}
		}
		
		if (args.length == 3)
		{
			if (args[0].equalsIgnoreCase("resource"))
			{
				if (args[1].equalsIgnoreCase("add"))
				{
					player.sendMessage(plugin.pre + "Not enough parameters provided.");
					player.sendMessage(plugin.pre + "Usage: /project resource add [projectname] [resourcename] [quantity]");
					return true;
				}
				
				if (args[1].equalsIgnoreCase("remove"))
				{
					player.sendMessage(plugin.pre + "Not enough parameters provided.");
					player.sendMessage(plugin.pre + "Usage: /project resource remove [projectname] [resourcename]");
					return true;
				}
			}
		}
		
		if (args.length == 4)
		{
			if (args[0].equalsIgnoreCase("resource"))
			{
				if (args[1].equalsIgnoreCase("add"))
				{
					player.sendMessage(plugin.pre + "Not enough parameters provided.");
					player.sendMessage(plugin.pre + "Usage: /project resource add [projectname] [resourcename] [quantity]");
					return true;
				}
				
				if (args[1].equalsIgnoreCase("remove"))
				{
					String project = args[2];
					String resource = args[3];
					
					if (plugin.projects.getKeys(false).contains(project))
					{
						if (plugin.projects.getConfigurationSection(project + ".resources").getKeys(false).contains(resource))
						{
							plugin.projects.set(project + ".resources." + resource, null);
							
							plugin.saveYamls();

							player.sendMessage(plugin.pre + "Resource \"" + resource + "\" removed for project \"" + project + "\".");
							return true;
						} else {
							player.sendMessage(plugin.pre + "No record found of resource \"" + resource + "\".");
							return true;
						}
					} else {
						player.sendMessage(plugin.pre + "Project \"" + project + "\" does not exist!");
						return true;
					}
				}
			}
		}
		
		/*if (args.length == 5)
		{
			if (args[0].equalsIgnoreCase("resource"))
			{
				if (args[1].equalsIgnoreCase("add"))
				{
					String project = args[2];
					String resource = args[3];
					Integer quantity = Integer.parseInt(args[4]);
					
					//player.sendMessage(plugin.pre + "Usage: /project resource add [projectname] [resourcename] [quantity]");
					if (plugin.projects.getKeys(false).contains(project))
					{
						plugin.projects.set(project + ".resources." + resource + ".target", quantity);
						player.sendMessage(plugin.pre + "Project \"" + project + "\" now needs " + quantity.toString() + " " + resource + ".");
						return true;
					} else {
						player.sendMessage(plugin.pre + "Project \"" + project + "\" does not exist!");
						return true;
					}
				}
			}
		}*/
		
		return false;
	}
	
}
