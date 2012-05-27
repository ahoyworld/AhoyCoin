package uk.co.ahoyworld;

public class Phrases {

	private static AhoyCoin plugin;
	
	public Phrases(AhoyCoin plugin)
	{
		Phrases.plugin = plugin;
	}
	
	/*
	 * The command "/ac reload" or "/ac reload phrases", run this command.
	 */
	
	public static void getPhrases()
	{
		plugin.loadYamls();
		//if unsuccessful, return error and stop. else...
		
		for (String nodeName : AhoyCoin.config.getConfigurationSection("phrases").getKeys(false))
		{
			plugin.phrases.clear();
			plugin.phrases.put(nodeName, AhoyCoin.config.get("phrases." + nodeName).toString());
		}
	}
}
