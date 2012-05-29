package uk.co.ahoyworld;

public class Replenish implements Runnable
{
	private final AhoyCoin plugin;
	String townNameTime; // What is this even good for? o.O
	private int taskID;
	
	//String [] signText = plugin.signText;
	private final String townName;
	private final String itemName;
	
	public Replenish(AhoyCoin plugin, String townNameTime)
	{
		this.plugin = plugin;
		this.townNameTime = townNameTime;
		townName = plugin.signText[1];
		itemName = plugin.signText[2];
	}
	
	public void setPid(int pid)
	{
		taskID = pid;
		System.out.println("[AhoyCoin] Task ran with an ID of " + pid + ".");
	}
	
	public void run()
	{
		//String townName = "Drammar";
		//String itemName = "apple";
		
		Integer replenishAmount = -1;
		// get specified or default replenishment amount
		if (plugin.towns.getKeys(true).contains(townName + ".items." + itemName + ".replenishamount"))
		{
			replenishAmount = plugin.towns.getInt(townName + ".items." + itemName + ".replenishamount");
		} else {
			replenishAmount = plugin.basePrices.getInt(itemName + ".replenishamount");
		}
		
		// get specified or default maximum stock
		Integer maxStock = -1;
		if (plugin.towns.getKeys(true).contains(townName + ".items." + itemName + ".maxstock"))
		{
			maxStock = plugin.towns.getInt(townName + ".items." + itemName + ".maxstock");
		} else {
			maxStock = plugin.basePrices.getInt(itemName + ".maxstock");
		}											
		
		// calculate new stock count
		Integer oldCurStock = plugin.towns.getInt(townName + ".items." + itemName + ".curstock");
		Integer newCurStock = -1;
		if ((oldCurStock + replenishAmount) > maxStock)
		{
			// will go over max stock
			newCurStock = maxStock;
		} else {
			// will not go over max stock
			newCurStock = oldCurStock + replenishAmount;
		}
		
		//if (!plugin.replenishStartUp) { plugin.towns.set(townName + ".items." + itemName + ".replenishtimer", 0); }
		Integer startTime = (int) (System.currentTimeMillis() / 1000L);
		System.out.println("[AhoyCoin] Task ran with an ID of " + taskID + ". < This is true");
		townNameTime = townName + "," + itemName + "," + startTime.toString();
		System.out.println("Item \"" + itemName + "\" in town \"" + townName + "\" replenished " + replenishAmount.toString() + " stock and now has " + newCurStock.toString() + " stock.");
		plugin.towns.set(townName + ".items." + itemName + ".curstock", newCurStock);
		
		plugin.saveYamls();
	}
}