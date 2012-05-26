package uk.co.ahoyworld;

public class replenish implements Runnable
{
	private AhoyCoin plugin;
	public static int taskID;
	public replenish(AhoyCoin plugin)
	{
		this.plugin = plugin;
	}
	
	//String [] signText = plugin.signText;
	String townName = AhoyCoin.signText[1];
	String itemName = AhoyCoin.signText[2];
	
	public void run()
	{
		//String townName = "Drammar";
		//String itemName = "apple";
		
		String theTaskID = Integer.valueOf(taskID).toString();
		
		Integer replenishAmount = -1;
		// get specified or default replenishment amount
		if (AhoyCoin.towns.getKeys(true).contains(townName + ".items." + itemName + ".replenishamount"))
		{
			replenishAmount = AhoyCoin.towns.getInt(townName + ".items." + itemName + ".replenishamount");
		} else {
			replenishAmount = AhoyCoin.basePrices.getInt(itemName + ".replenishamount");
		}
		
		// get specified or default maximum stock
		Integer maxStock = -1;
		if (AhoyCoin.towns.getKeys(true).contains(townName + ".items." + itemName + ".maxstock"))
		{
			maxStock = AhoyCoin.towns.getInt(townName + ".items." + itemName + ".maxstock");
		} else {
			maxStock = AhoyCoin.basePrices.getInt(itemName + ".maxstock");
		}											
		
		// calculate new stock count
		Integer oldCurStock = AhoyCoin.towns.getInt(townName + ".items." + itemName + ".curstock");
		Integer newCurStock = -1;
		if ((oldCurStock + replenishAmount) > maxStock)
		{
			// will go over max stock
			newCurStock = maxStock;
		} else {
			// will not go over max stock
			newCurStock = oldCurStock + replenishAmount;
		}
		
		//if (!AhoyCoin.replenishStartUp) { AhoyCoin.towns.set(townName + ".items." + itemName + ".replenishtimer", 0); }
		Integer startTime = (int) (System.currentTimeMillis() / 1000L);
		System.out.println("[AhoyCoin] Task ran with an ID of " + theTaskID + ".");
		String townNameTime = townName + "," + itemName + "," + startTime.toString();
		if (!AhoyCoin.replenishThreads.containsKey(taskID))
		{
			AhoyCoin.replenishThreads.put(theTaskID, townNameTime);
		}
		System.out.println("Item \"" + itemName + "\" in town \"" + townName + "\" replenished " + replenishAmount.toString() + " stock and now has " + newCurStock.toString() + " stock.");
		AhoyCoin.towns.set(townName + ".items." + itemName + ".curstock", newCurStock);
		
		AhoyCoin.saveYamls();
	}
}