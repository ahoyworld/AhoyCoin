package uk.co.ahoyworld;
 
import java.util.logging.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
 
public class AhoyCoin extends JavaPlugin {
	
	//create the variables for our config files
    File configFile;
    File townsFile;
    File basePriceFile;
    FileConfiguration config;
    FileConfiguration towns;
    FileConfiguration basePrices;
    
    //create a logger called "log" so we can output to the console
    Logger log;
    
    //set up an "[AhoyCoin]" prefix that'll go at the beginning of all our messages
    String pre = ChatColor.GOLD + "[AhoyCoin]" + ChatColor.WHITE + " ";
    
    public void onEnable()
    {
    	// get "replenishtimer" from items in towns.yml
    	// if > 0, start a thread running with the value as a start offset
    	// 		Say an item replenishes every 200 ticks. After 50 ticks, the server restarts and 50 is saved as replenishtimer
    	//		onEnable, set the initial delay (initial, regular repeat time) to (replenishtime - replenishtimer (i.e. 200 - 50)).
    	//		Using this technique, the replenishment will keep the position of longer replenishment times
    	
    	//register our event that triggers when a play clicks a block
    	new Event_onBlockClick(this);
    	
    	//ensure the config files are created
        configFile = new File(getDataFolder(), "config.yml");
        townsFile = new File(getDataFolder(), "towns.yml");
        basePriceFile = new File(getDataFolder(), "basePrice.yml");
     
        //error when first running? show us!
        try
        {
            firstRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
     
        //define our config files as "YamlConfigurations"
        config = new YamlConfiguration();
        towns = new YamlConfiguration();
        basePrices = new YamlConfiguration();

        //run the function "loadYamls"
        loadYamls();
        
        log = this.getLogger();
        //tell us that the plugin has now been successfully enabled
        log.info("Plugin enabled.");
    }
 
    //function that saves the config files
    public void saveYamls()
    {
        try
        {
        	//save our config files
            config.save(configFile);
            towns.save(townsFile);
            basePrices.save(basePriceFile);
        //problem? tell us!
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    //function that loads the config files
    public void loadYamls()
    {
        try
        {
            config.load(configFile);
            towns.load(townsFile);
            basePrices.load(basePriceFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    private void firstRun() throws Exception
    {
        if (!configFile.exists())
        {
            configFile.getParentFile().mkdirs();
            copy(getResource("config.yml"), configFile);
        }
        if (!townsFile.exists())
        {
            townsFile.getParentFile().mkdirs();
            copy(getResource("towns.yml"), townsFile);
        }
        if (!basePriceFile.exists())
        {
            basePriceFile.getParentFile().mkdirs();
            copy(getResource("basePrice.yml"), basePriceFile);
        }
    }
 
    private void copy(InputStream in, File file)
    {
        try
        {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len=in.read(buf))>0)
            {
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public void onDisable()
    {
    	// interrupt all running async threads and get their current time (split) in ticks
    	// save this value in towns.yml as "replenishtimer" under the appropriate item
    	// refer to onEnable for the rest...
    	
        saveYamls();
        log.info("Plugin disabled.");
    }
 
    public void loadConfiguration()
    {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
    
    //this is a boolean (true/false) that manages when a player sends a /command
    //if it returns true, the command is treated as recognised and so doesn't do anything special (apart from what we tell it to do)
    //if it returns false, the help text is displayed. We specify this in the plugin.yml file under the "commands" node
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
    	//this just creates a variable named "player" that will be the player that sent the command
    	Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("ac"))
        {
            if (args.length == 1)
            {
                if (args[0].equalsIgnoreCase("town"))
                {
                    player.sendMessage(pre + "Usage: /AC town [list, add, remove]");
                    return true;
                }
                
                if (args[0].equalsIgnoreCase("reload"))
                {
                	this.reloadConfig();
                	return true;
                }
            }
            if (args.length == 2)
            {
                if (args[0].equalsIgnoreCase("town"))
                {
                    if (args[1].equalsIgnoreCase("list"))
                    {
                    	if (!towns.getKeys(false).isEmpty())
                    	{
                    		String townsListStr = "";
                        	for (String str : towns.getKeys(false))
                        	{
                        		townsListStr = townsListStr + str + ", ";
                        	}
                        	player.sendMessage(pre + "List of created towns:");
                        	player.sendMessage(pre + "" + townsListStr);
                    	} else {
                    		player.sendMessage(pre + "No towns have been created.");
                    	}
                    	
                        return true;	
                    }
                    if (args[1].equalsIgnoreCase("add"))
                    {
                        player.sendMessage(pre + "Type the name of the town you would like to add");
                        player.sendMessage(pre + "Usage: /AC town add [name]");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("remove"))
                    {
                    	player.sendMessage(pre + "Name of town to remove not provided.");
                    	return true;
                    }
                }
            }
            
            if (args.length == 3)
            {
            	if (args[0].equalsIgnoreCase("town"))
            	{
            		if (args[1].equalsIgnoreCase("add"))
            		{
            			int defaultTax = config.getInt("defaults.tax");
            			
            			towns.set(args[2].toString() + ".tax", defaultTax);
            			saveYamls();
            			player.sendMessage(pre + "Town \"" + args[2].toString() + "\" added.");
            			
            			return true;
            		}
            		
                	if (args[1].equalsIgnoreCase("remove"))
                	{
                		if (!towns.getKeys(false).isEmpty())
                		{
                			for (String str : towns.getKeys(false))
                			{
                				if (str.equalsIgnoreCase(args[2].toString()))
                				{
                					towns.set(str, null);
                					saveYamls();
                        			player.sendMessage(pre + "Town \"" + args[2].toString() + "\" removed.");
                        			return true;
                				}
                			}
	        	    		player.sendMessage(pre + "Town \"" + args[2].toString() + "\" does not exist.");
	        	    		return true;
                		} else {
                	    	player.sendMessage(pre + "There are no towns to remove.");
                	    	return true;
                		}
                	}
            	}
            	
            	if (args[0].equalsIgnoreCase("pricereset"))
            	{
            		String town = args[1].toString();
            		String item = args[2].toString().toLowerCase();
            		
            		if (towns.getKeys(true).contains(town + ".items." + item))
            		{
            			// item exists
            			towns.set(town + ".items." + item, null);
            			saveYamls();
            			player.sendMessage(pre + "Item \"" + item + "\" for town \"" + town + "\" reset to defaults.");
            			return true;
            		} else {
            			// item doesn't exist
            			player.sendMessage(pre + "Item \"" + item + "\" for town \"" + town + "\" already set to defaults.");
            			return true;
            		}
            	}
            	
            	if (args[0].equalsIgnoreCase("taxset"))
            	{
            		boolean townExists = false;
            		String town = "";
            		for (String str : towns.getKeys(false))
            		{
            			if (str.equalsIgnoreCase(args[1]))
            			{
            				townExists = true;
            				town = str;
            				break;
            			}
            		}
            		if (townExists)
            		{
            			Integer tax = Integer.parseInt(args[2]);
            			towns.set(town + ".tax", tax);
            			saveYamls();
            			player.sendMessage(pre + "Tax rate for town \"" + town + "\" set to " + tax.toString() + "%.");
            			return true;
            		} else {
            			player.sendMessage(pre + "Town \"" + town + "\" does not exist.");
            			return true;
            		}
            	}
            }
            
            if (args.length >= 4)
            {
            	if (args[0].equalsIgnoreCase("priceset"))
            	{
            		String town = "";
            		String item = "";
            		
            		//Check town exists
            		boolean townExists = false;
            		for (String str : towns.getKeys(false))
            		{
            			if (args[1].toString().equalsIgnoreCase(str))
            			{
            				townExists = true;
            				town = str;
            				break;
            			}
            		}
            		if (townExists)
            		{
            			//Check item exists
            			boolean itemExists = false;
            			for (String str : basePrices.getKeys(false))
            			{
            				if (args[2].toString().equalsIgnoreCase(str))
            				{
            					itemExists = true;
            					item = str;
            					break;
            				}
            			}
            			if (itemExists)
            			{
            				//Submit!
            				player.sendMessage(pre + "Setting custom item for " + town + ":");
            				String reportStr = "Item \"" + item + "\"";
            				if (args.length > 3)
            				{
            					//Price
            					towns.set(town + ".items." + item + ".price", Integer.parseInt(args[3]));
            					reportStr = reportStr + " now sells for " + args[3].toString();
            				}
            				if (args.length > 4)
            				{
            					//Max Stock
            					towns.set(town + ".items." + item + ".maxstock", Integer.parseInt(args[4]));
            					reportStr = reportStr + " with a maximum stock of " + args[4].toString();
            				} else if (towns.getConfigurationSection(town + ".items." + item).getKeys(false).contains("maxstock")) {
            					reportStr = reportStr + " with a maximum stock of " + towns.getString(town + ".items." + item + ".maxstock");
            				} else {
            					reportStr = reportStr + " with a maximum stock of " + basePrices.getString(item + ".maxstock");
            				}
            				if (args.length > 5)
            				{
            					//Replenish every X days
            					towns.set(town + ".items." + item + ".replenishtime", Integer.parseInt(args[5]));
            					reportStr = reportStr + ", replenishing every " + args[5].toString() + " days";
            				} else if (towns.getConfigurationSection(town + ".items." + item).getKeys(false).contains("replenishtime")) {
            					reportStr = reportStr + ", replenishing every " + towns.getString(town + ".items." + item + ".replenishtime");
            				} else {
            					reportStr = reportStr + ", replenishing every " + basePrices.getString(item + ".replenishtime") + " days";
            				}
            				if (args.length > 6)
            				{
            					//Replenish Amount
            					towns.set(town + ".items." + item + ".replenishamount", Integer.parseInt(args[6]));
            					reportStr = reportStr + " for " + args[6].toString() + ".";
            				} else if (towns.getConfigurationSection(town + ".items." + item).getKeys(false).contains("replenishamount")) {
            					reportStr = reportStr + " for " + towns.getString(town + ".items." + item + ".replenishamount");
            				} else {
            					reportStr = reportStr + " for " + basePrices.getString(item + ".replenishamount") + ".";
            				}
            				saveYamls();
            				player.sendMessage(reportStr);
            				return true;
            			} else {
            				player.sendMessage(pre + "Item \"" + args[2].toString() + "\" does not exist.");
            				return true;
            			}
            		} else {
            			player.sendMessage(pre + "Town \"" + args[1].toString() + "\" does not exist.");
            			return true;
            		}
            	}
            }
        }
        return false;
    }
}