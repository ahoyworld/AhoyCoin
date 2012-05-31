package uk.co.ahoyworld;

public class Phrases {

	private AhoyCoin plugin;
	
	public Phrases(AhoyCoin plugin)
	{
		this.plugin = plugin;
	}
	
	/*
	 * The command "/ac reload" or "/ac reload phrases", run this command.
	 */
	
	public void getPhrases()
	{
		plugin.loadYamls();
		//if unsuccessful, return error and stop. else...
		
		for (String nodeName : plugin.config.getConfigurationSection("phrases").getKeys(false))
		{
			plugin.phrasesMap.clear();
			plugin.phrasesMap.put(nodeName, plugin.config.get("phrases." + nodeName).toString());
		}
	}
	
	
	
	public void clear()
	{
		plugin.phrasesMap.clear();
	}
}
