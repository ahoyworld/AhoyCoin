package uk.co.ahoyworld;
 
import java.util.logging.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
 
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
 
public class AhoyCoin extends JavaPlugin {
 
    File configFile;
    File townsFile;
    File basePriceFile;
    FileConfiguration config;
    FileConfiguration towns;
    FileConfiguration basePrices;
    Logger log;
    
    public void onEnable()
    {        
    	new Event_onBlockClick(this);
    	
        configFile = new File(getDataFolder(), "config.yml");
        townsFile = new File(getDataFolder(), "towns.yml");
        basePriceFile = new File(getDataFolder(), "basePrice.yml");
     
        try
        {
            firstRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
     
        config = new YamlConfiguration();
        towns = new YamlConfiguration();
        basePrices = new YamlConfiguration();
     
        loadYamls();
     
        log = this.getLogger();
        log.info("Plugin enabled.");
    }
 
    public void saveYamls()
    {
        try
        {
            config.save(configFile);
            towns.save(townsFile);
            basePrices.save(basePriceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
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
        saveYamls();
        log.info("Plugin disabled.");
    }
 
    public void loadConfiguration()
    {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
    	Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("ac"))
        {
            if (args.length == 1)
            {
                if (args[0].equalsIgnoreCase("town"))
                {
                    player.sendMessage("[AhoyCoin] Usage: /AC town [list, add, remove]");
                    return true;
                }
            }
            if (args.length == 2)
            {
                if (args[0].equalsIgnoreCase("town"))
                {
                    if (args[1].equalsIgnoreCase("list"))
                    {
                		List<String> townslist = towns.getStringList("towns");
                		String townsListStr = "";
                		
                	    if (!townslist.isEmpty())
                	    {
                	    	for (int i = 0; i < townslist.size(); i++)
                	    	{
                	    		townsListStr = townsListStr + townslist.get(i) + ", ";
                	    	}
                	    	player.sendMessage("[AhoyCoin] List of created towns:");
                	    	player.sendMessage("[AhoyCoin] " + townsListStr);
                	    } else {
                	    	player.sendMessage("[AhoyCoin] No towns have been created.");
                	    }
                	    	
                        return true;
                		
                    }
                    if (args[1].equalsIgnoreCase("add"))
                    {
                        player.sendMessage("[AhoyCoin] Type the name of the town you would like to add");
                        player.sendMessage("[AhoyCoin] Usage: /AC town add [name]");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("remove"))
                    {
                    	player.sendMessage("[AhoyCoin] Name of town to remove not provided.");
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
            			List<String> townslist = towns.getStringList("towns");
            			townslist.add(args[2].toString());
            			towns.set("towns", townslist);
            			saveYamls();
            			player.sendMessage("[AhoyCoin] Town \"" + args[2].toString() + "\" added.");
            			
            			return true;
            		}
            		
                	if (args[1].equalsIgnoreCase("remove"))
                	{
                		List<String> townslist = towns.getStringList("towns");
                		if (!townslist.isEmpty())
                	    {
                			String townToRemove = args[2];
                	    	for (int i = 0; i < townslist.size(); i++)
                	    	{
                	    		if (townToRemove.equalsIgnoreCase(townslist.get(i)))
                	    		{
                	    			String townRemoved = townslist.get(i).toString();
                	    			townslist.remove(i);
                	    			towns.set("towns", townslist);
                	    			saveYamls();
                	    			player.sendMessage("[AhoyCoin] Town \"" + townRemoved + "\" removed.");
                	    			return true;
                	    		}
                	    	}
	        	    		player.sendMessage("[AhoyCoin] Town \"" + townToRemove + "\" does not exist.");
	        	    		return true;
                	    } else {
                	    	player.sendMessage("[AhoyCoin] There are no towns to remove.");
                	    	return true;
                	    }
                	}
            	}
            }
            
            if (args.length > 3)
            {
            	if (args[0].equalsIgnoreCase("town"))
            	{
            		if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove"))
            		{
            			player.sendMessage("[AhoyCoin] Invalid arguments!");
            			player.sendMessage("[AhoyCoin] Town names can not contain spaces!");
            			
            			return true;
            		}
            	}
            }
        }
        return false;
    }
}
 