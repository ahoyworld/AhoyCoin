package uk.co.ahoyworld;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Event_onBlockClick implements Listener 
{	
	private AhoyCoin plugin;
	
<<<<<<< HEAD
	//public ArrayList<String> townNameTime = new ArrayList<String>();
	//String townNameTime = "";
	//Long startTime = -1L;
	
=======
>>>>>>> f0cdb11a47cc4b17f0d6430b8de97ebe1ba87bf6
	public Event_onBlockClick(AhoyCoin plugin)
	{
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
		
<<<<<<< HEAD
	@SuppressWarnings("deprecation")
=======
>>>>>>> f0cdb11a47cc4b17f0d6430b8de97ebe1ba87bf6
	@EventHandler
	public void blockLeftClick (PlayerInteractEvent event)
	{		
		if (event.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			if (event.getClickedBlock().getState() instanceof Sign)
			{
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).equalsIgnoreCase("[Vendor]"))
				{
					Player player = event.getPlayer();
					// player.sendMessage("Creating sign...");
					final String [] signText = sign.getLines();
					if (!(signText.length < 4))
					{
						String townName = signText[1];
						String itemName = signText[2].toLowerCase();
						Integer quantity = Integer.parseInt(signText[3]);
<<<<<<< HEAD
						if (AhoyCoin.towns.getKeys(false).contains(townName))
						{
							// player.sendMessage("Town \"" + townName + "\" exists.");
							if (AhoyCoin.basePrices.getKeys(false).contains(itemName))
							{
								// player.sendMessage("Item \"" + itemName + "\" exists.");
								if (quantity <= 64 && quantity <= AhoyCoin.basePrices.getInt(itemName + ".maxstock"))
=======
						if (plugin.towns.getKeys(false).contains(townName))
						{
							// player.sendMessage("Town \"" + townName + "\" exists.");
							if (plugin.basePrices.getKeys(false).contains(itemName))
							{
								// player.sendMessage("Item \"" + itemName + "\" exists.");
								if (quantity <= 64 && quantity <= plugin.basePrices.getInt(itemName + ".maxstock"))
>>>>>>> f0cdb11a47cc4b17f0d6430b8de97ebe1ba87bf6
								{
									// player.sendMessage("Quantity \"" + quantity.toString() + "\" valid.");
									
									// Create the sign.
									sign.setLine(0, (ChatColor.BLUE + sign.getLine(0)));
									sign.update();
									// Ugly, UGLY code.
									// Change this to get the specifically-set maxstock settings (in towns.yml) if available
									// Also, use "if (!plugin.towns.getKeys(true).contains(townName + ".items." + itemName + ".curstock"))
<<<<<<< HEAD
									if (AhoyCoin.towns.getConfigurationSection(townName).getKeys(false).contains("items"))
									{
										if (AhoyCoin.towns.getConfigurationSection(townName + ".items").getKeys(false).contains(itemName))
										{
											if (!AhoyCoin.towns.getConfigurationSection(townName + ".items." + itemName).getKeys(false).contains("curstock"))
											{
												Integer maxstock = AhoyCoin.basePrices.getInt(itemName + ".maxstock");
												AhoyCoin.towns.set(townName + ".items." + itemName + ".curstock", maxstock);
												AhoyCoin.saveYamls();
=======
									if (plugin.towns.getConfigurationSection(townName).getKeys(false).contains("items"))
									{
										if (plugin.towns.getConfigurationSection(townName + ".items").getKeys(false).contains(itemName))
										{
											if (!plugin.towns.getConfigurationSection(townName + ".items." + itemName).getKeys(false).contains("curstock"))
											{
												Integer maxstock = plugin.basePrices.getInt(itemName + ".maxstock");
												plugin.towns.set(townName + ".items." + itemName + ".curstock", maxstock);
												plugin.saveYamls();
>>>>>>> f0cdb11a47cc4b17f0d6430b8de97ebe1ba87bf6
												player.sendMessage(plugin.pre + "Sign created!");
											} else {
												player.sendMessage(plugin.pre + "Apparently current stock is already assigned.");
											}
										} else {
<<<<<<< HEAD
											Integer maxstock = AhoyCoin.basePrices.getInt(itemName + ".maxstock");
											AhoyCoin.towns.set(townName + ".items." + itemName + ".curstock", maxstock);
											AhoyCoin.saveYamls();
											player.sendMessage(plugin.pre + "Sign created!");
										}
									} else {
										Integer maxstock = AhoyCoin.basePrices.getInt(itemName + ".maxstock");
										AhoyCoin.towns.set(townName + ".items." + itemName + ".curstock", maxstock);
										AhoyCoin.saveYamls();
										player.sendMessage(plugin.pre + "Sign created!");
									}
									Integer replenishTime = -1;
									if (AhoyCoin.towns.getKeys(true).contains(townName + ".items." + itemName + ".replenishTime"))
									{
										replenishTime = (AhoyCoin.towns.getInt(townName + ".items." + itemName + ".replenishtime") * 24000);
									} else {
										replenishTime = (AhoyCoin.basePrices.getInt(itemName + ".replenishtime") * 24000);
									}
									
									if (!AhoyCoin.towns.getKeys(true).contains(townName + ".items." + itemName + ".replenishtimer"))
									{
										AhoyCoin.signText[1] = townName;
										AhoyCoin.signText[2] = itemName;
										plugin.createReplenishTimer(townName, itemName, 0, replenishTime);
										AhoyCoin.towns.set(townName + ".items." + itemName + ".replenishtimer", 0);
										/*
										plugin.towns.set(townName + ".items." + itemName + ".replenishtimer", 0);
										final int taskId = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable()
										{											
											String townName = signText[1];
											String itemName = signText[2];
											 
											public void run()
											{
												String theTaskID = Integer.valueOf(taskId).toString();
																								
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
												
												Long startTime = plugin.now.getTime();
												System.out.println("[AhoyCoin] Task created with an ID of " + theTaskID + ".");
												String townNameTime = townName + "," + itemName + "," + startTime.toString();
												plugin.replenishThreads.put(theTaskID, townNameTime);
												
												System.out.println("Item \"" + itemName + "\" in town \"" + townName + "\" replenished " + replenishAmount.toString() + " stock and now has " + newCurStock.toString() + " stock.");
												plugin.towns.set(townName + ".items." + itemName + ".curstock", newCurStock);

												plugin.saveYamls();
											}
										}, 0L, (replenishTime)); */
									}								
=======
											Integer maxstock = plugin.basePrices.getInt(itemName + ".maxstock");
											plugin.towns.set(townName + ".items." + itemName + ".curstock", maxstock);
											plugin.saveYamls();
											player.sendMessage(plugin.pre + "Sign created!");
										}
									} else {
										Integer maxstock = plugin.basePrices.getInt(itemName + ".maxstock");
										plugin.towns.set(townName + ".items." + itemName + ".curstock", maxstock);
										plugin.saveYamls();
										player.sendMessage(plugin.pre + "Sign created!");
									}
									Integer replenishTime = -1;
									if (plugin.towns.getKeys(true).contains(townName + ".items." + itemName + ".replenishTime"))
									{
										replenishTime = (plugin.towns.getInt(townName + ".items." + itemName + ".replenishtime") * 20);
									} else {
										replenishTime = (plugin.basePrices.getInt(itemName + ".replenishtime") * 20);
									}
									
									plugin.towns.set(townName + ".items." + itemName + ".replenishtimer", 0);
									plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable()
									{
										String townName = signText[1];
										String itemName = signText[2];
										 
										public void run()
										{
											Integer replenishAmount = -1;
											if (plugin.towns.getKeys(true).contains(townName + ".items." + itemName + ".replenishamount"))
											{
												replenishAmount = plugin.towns.getInt(townName + ".items." + itemName + ".replenishamount");
											} else {
												replenishAmount = plugin.basePrices.getInt(itemName + ".replenishamount");
											}
											
											Integer maxStock = -1;
											if (plugin.towns.getKeys(true).contains(townName + ".items." + itemName + ".maxstock"))
											{
												maxStock = plugin.towns.getInt(townName + ".items." + itemName + ".maxstock");
											} else {
												maxStock = plugin.basePrices.getInt(itemName + ".maxstock");
											}											
											
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
											
											plugin.towns.set(townName + ".items." + itemName + ".curstock", newCurStock);
											System.out.println("Item \"" + itemName + "\" in town \"" + townName + "\" replenished " + replenishAmount.toString() + " stock and now has " + newCurStock.toString() + " stock.");
											plugin.saveYamls();
										}
									}, 0L, (replenishTime));
									
>>>>>>> f0cdb11a47cc4b17f0d6430b8de97ebe1ba87bf6
								} else {
									player.sendMessage(plugin.pre + "Quantity \"" + quantity.toString() + "\" invalid. Please specify a value below the maximum stock level (NO. HERE).");
								}
							} else {
								player.sendMessage(plugin.pre + "Item \"" + itemName + "\" does not exist.");
							}
						} else {
							player.sendMessage(plugin.pre + "Town \"" + townName + "\" does not exist.");
						}
						sign.setLine(0, (ChatColor.BLUE + "[Vendor]"));
					} else {
						player.sendMessage(plugin.pre + "Invalid number of parameters!");
					}
				} else if (sign.getLine(0).equalsIgnoreCase(ChatColor.BLUE + "[Vendor]")) {
					Player player = event.getPlayer();
					String [] signText = sign.getLines();
					String townName = signText[1];
					String itemName = signText[2].toLowerCase();
					Integer quantity = Integer.parseInt(signText[3]);
<<<<<<< HEAD
					double tax = AhoyCoin.towns.getInt(townName + ".tax");
					double preTax = -1;
					double finalPrice = -1;
					
					if (!AhoyCoin.towns.getKeys(true).contains(townName + ".items." + itemName)) // if item isn't created
					{
						AhoyCoin.towns.set(townName + ".items." + itemName + ".curstock", AhoyCoin.basePrices.getInt(itemName + ".maxstock"));
						AhoyCoin.saveYamls();
						preTax = AhoyCoin.basePrices.getInt(itemName + ".price") * quantity;
						finalPrice = preTax + ((preTax / 100) * tax);
					} else if (AhoyCoin.towns.getConfigurationSection(townName + ".items." + itemName).getKeys(false).contains("price")) {
						preTax = AhoyCoin.towns.getInt(townName + ".items." + itemName + ".price") * quantity;
						finalPrice = preTax + ((preTax / 100) * tax);
					} else {
						preTax = AhoyCoin.basePrices.getInt(itemName + ".price") * quantity;
=======
					double tax = plugin.towns.getInt(townName + ".tax");
					double preTax = -1;
					double finalPrice = -1;
					
					if (!plugin.towns.getKeys(true).contains(townName + ".items." + itemName)) // if item isn't created
					{
						plugin.towns.set(townName + ".items." + itemName + ".curstock", plugin.basePrices.getInt(itemName + ".maxstock"));
						plugin.saveYamls();
						preTax = plugin.basePrices.getInt(itemName + ".price") * quantity;
						finalPrice = preTax + ((preTax / 100) * tax);
					} else if (plugin.towns.getConfigurationSection(townName + ".items." + itemName).getKeys(false).contains("price")) {
						preTax = plugin.towns.getInt(townName + ".items." + itemName + ".price") * quantity;
						finalPrice = preTax + ((preTax / 100) * tax);
					} else {
						preTax = plugin.basePrices.getInt(itemName + ".price") * quantity;
>>>>>>> f0cdb11a47cc4b17f0d6430b8de97ebe1ba87bf6
						finalPrice = preTax + ((preTax / 100) * tax);
					}
					player.sendMessage(plugin.pre + "Buy " + quantity.toString() + " " + itemName + "(s) from " + townName + " for " + finalPrice + "?");
				}
			}
		} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock().getState() instanceof Sign)
			{
				Sign sign = (Sign) event.getClickedBlock().getState();
				Player player = event.getPlayer();
				// player.sendMessage(plugin.pre + "You right clicked a sign. Well-fucking-done.");
				String [] signText = sign.getLines();
				String townName = signText[1];
				String itemName = signText[2];
				Integer quantity = Integer.parseInt(signText[3]);
<<<<<<< HEAD
				Integer curstock = AhoyCoin.towns.getInt(townName + ".items." + itemName + ".curstock");
=======
				Integer curstock = plugin.towns.getInt(townName + ".items." + itemName + ".curstock");
>>>>>>> f0cdb11a47cc4b17f0d6430b8de97ebe1ba87bf6
				if (quantity > curstock)
				{
					// Sorry - we're currently out of stock! Our next shipment of X item(s) comes in X days.
					player.sendMessage(plugin.pre + "Buying quantity larger than current stock.");
					player.sendMessage(plugin.pre + "I'll sort this out later.");
				} else {
					ItemStack items = new ItemStack(Material.getMaterial(itemName.toUpperCase()), quantity);
					player.getInventory().addItem(items);
					player.updateInventory();
					Integer newStock = curstock - quantity;
<<<<<<< HEAD
					AhoyCoin.towns.set(signText[1] + ".items." + signText[2] + ".curstock", newStock);
					AhoyCoin.saveYamls();
=======
					plugin.towns.set(signText[1] + ".items." + signText[2] + ".curstock", newStock);
					plugin.saveYamls();
>>>>>>> f0cdb11a47cc4b17f0d6430b8de97ebe1ba87bf6
					// player.sendMessage(plugin.pre + "In seriousness, you took " + quantity.toString() + " of the available " + curstock.toString() + " stock.");
					// player.sendMessage(plugin.pre + "Current stock level is now " + newStock.toString() + ".");
				}
			}
		}
	}
}