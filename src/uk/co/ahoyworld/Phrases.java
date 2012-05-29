package uk.co.ahoyworld;

public class Phrases {

	private final AhoyCoin plugin;
	
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
			plugin.phrases.clear();
			plugin.phrases.put(nodeName, plugin.config.get("phrases." + nodeName).toString());
		}
	}
}
