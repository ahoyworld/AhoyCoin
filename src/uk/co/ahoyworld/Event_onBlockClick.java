package uk.co.ahoyworld;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Event_onBlockClick implements Listener 
{	
	private AhoyCoin plugin;
	
	//public ArrayList<String> townNameTime = new ArrayList<String>();
	//String townNameTime = "";
	//Long startTime = -1L;
	
	public Event_onBlockClick(AhoyCoin plugin)
	{
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@SuppressWarnings("deprecation")
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
					String [] signText = sign.getLines();
					if (!(signText[3].equals("")))
					{
						String townName = signText[1];
						String itemName = signText[2].toLowerCase();
						Integer quantity = Integer.parseInt(signText[3]);
						if (AhoyCoin.towns.getKeys(false).contains(townName))
						{
							// player.sendMessage("Town \"" + townName + "\" exists.");
							if (AhoyCoin.basePrices.getKeys(false).contains(itemName))
							{
								// player.sendMessage("Item \"" + itemName + "\" exists.");
								if (quantity <= 64 && quantity <= AhoyCoin.basePrices.getInt(itemName + ".maxstock"))
								{
									// player.sendMessage("Quantity \"" + quantity.toString() + "\" valid.");
									
									// Create the sign.
									sign.setLine(0, (ChatColor.BLUE + sign.getLine(0)));
									sign.update();
									// Ugly, UGLY code.
									// Change this to get the specifically-set maxstock settings (in towns.yml) if available
									// Also, use "if (!plugin.towns.getKeys(true).contains(townName + ".items." + itemName + ".curstock"))
									if (AhoyCoin.towns.getConfigurationSection(townName).getKeys(false).contains("items"))
									{
										if (AhoyCoin.towns.getConfigurationSection(townName + ".items").getKeys(false).contains(itemName))
										{
											if (!AhoyCoin.towns.getConfigurationSection(townName + ".items." + itemName).getKeys(false).contains("curstock"))
											{
												Integer maxstock = AhoyCoin.basePrices.getInt(itemName + ".maxstock");
												AhoyCoin.towns.set(townName + ".items." + itemName + ".curstock", maxstock);
												AhoyCoin.saveYamls();
												player.sendMessage(plugin.pre + "Sign created!");
											} else {
												player.sendMessage(plugin.pre + "Apparently current stock is already assigned.");
											}
										} else {
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
									}								
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
					String playerName = player.getName();
					String [] signText = sign.getLines();
					String townName = signText[1];
					String itemName = signText[2].toLowerCase();
					Integer quantity = Integer.parseInt(signText[3]);
					double tax = AhoyCoin.towns.getInt(townName + ".tax");
					double preTax = -1;
					double finalPrice = -1;
					
					if (AhoyCoin.playerList.getKeys(false).contains(player.getName()))
					{
						if (AhoyCoin.playerList.getString(playerName + ".trademode").equalsIgnoreCase("buy"))
						{
							//set to sell mode
							AhoyCoin.playerList.set(playerName + ".trademode", "sell");
						} else {
							//set to buy mode
							AhoyCoin.playerList.set(playerName + ".trademode", "buy");
						}
					} else {
						AhoyCoin.playerList.set(playerName + ".trademode", "buy");
						//player does not exist - create new buy mode
					}
					
					AhoyCoin.saveYamls();
					
					boolean buymode = AhoyCoin.playerList.getString(player.getName() + ".trademode").equalsIgnoreCase("buy");
					
					if (!AhoyCoin.towns.getKeys(true).contains(townName + ".items." + itemName)) // if item isn't created
					{
						AhoyCoin.towns.set(townName + ".items." + itemName + ".curstock", AhoyCoin.basePrices.getInt(itemName + ".maxstock"));
						AhoyCoin.saveYamls();
						preTax = AhoyCoin.basePrices.getInt(itemName + ".price") * quantity;
						finalPrice = preTax + ((preTax / 100) * tax);
					} else if (AhoyCoin.towns.getConfigurationSection(townName + ".items." + itemName).getKeys(false).contains("price")) { //if price is specified
						preTax = AhoyCoin.towns.getInt(townName + ".items." + itemName + ".price") * quantity;
						finalPrice = preTax + ((preTax / 100) * tax);
					} else { //use default price
						preTax = AhoyCoin.basePrices.getInt(itemName + ".price") * quantity;
						finalPrice = preTax + ((preTax / 100) * tax);
					}
					
					if (!buymode)
					{
						finalPrice = (finalPrice / 100) * AhoyCoin.config.getInt("defaults.sell_percent");
					}
					
					//round finalPrice UP to the nearest Integer
					finalPrice = Math.ceil(finalPrice);
					
					if (AhoyCoin.playerList.getString(playerName + ".trademode").equalsIgnoreCase("buy"))
					{
						//query buy price
						player.sendMessage(plugin.pre + "Buy " + quantity.toString() + " " + itemName + "(s) from " + townName + " for " + finalPrice + "?");
					} else if (AhoyCoin.playerList.getString(playerName + ".trademode").equalsIgnoreCase("sell")) {
						//query sell price
						player.sendMessage(plugin.pre + "Sell " + quantity.toString() + " " + itemName + "(s) to " + townName + " for " + finalPrice + "?");
					}
				}
			}
		} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock().getState() instanceof Sign)
			{
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).equalsIgnoreCase(ChatColor.BLUE + "[Vendor]"))
				{
					Player player = event.getPlayer();
					// player.sendMessage(plugin.pre + "You right clicked a sign. Well-fucking-done.");
					String [] signText = sign.getLines();
					String townName = signText[1];
					String itemName = signText[2];
					Integer quantity = Integer.parseInt(signText[3]);
					Integer curstock = AhoyCoin.towns.getInt(townName + ".items." + itemName + ".curstock");
					Integer maxstock = -1;
					double tax = AhoyCoin.towns.getInt(townName + ".tax");
					double preTax = -1;
					double finalPrice = -1;
					
					if (!AhoyCoin.playerList.getKeys(false).contains(player.getName()))
					{
						AhoyCoin.playerList.set(player.getName() + ".trademode", "buy");
						//player does not exist - create new buy mode
					}
					
					AhoyCoin.saveYamls();
					
					boolean buymode = AhoyCoin.playerList.getString(player.getName() + ".trademode").equalsIgnoreCase("buy");
					
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
						finalPrice = preTax + ((preTax / 100) * tax);
					}
										
					if (AhoyCoin.towns.getConfigurationSection(townName + ".items." + itemName).getKeys(false).contains("maxstock"))
					{
						maxstock = AhoyCoin.towns.getInt(townName + ".items." + itemName + ".maxstock");
					} else {
						maxstock = AhoyCoin.basePrices.getInt(itemName + ".maxstock");
					}
					
					if (!buymode)
					{
						finalPrice = (finalPrice / 100) * AhoyCoin.config.getInt("defaults.sell_percent");
					}
					
					//round finalPrice UP to the nearest Integer
					finalPrice = Math.ceil(finalPrice);
					
					Economy econ = null;
					RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
					if (economyProvider != null)
					{
						econ = economyProvider.getProvider();
					}
					
					if (quantity > curstock && buymode)
					{
						player.sendMessage(plugin.pre + "Sorry! We're currently out of stock!");
					} else if (buymode) {
						if ((double) econ.getBalance(player.getName()) >= finalPrice)
						{
							//NEED TO TAKE INTO ACCOUNT IF INVEVNTORY IS FULL!
							ItemStack items = new ItemStack(Material.getMaterial(itemName.toUpperCase()), quantity);
							econ.withdrawPlayer(player.getName(), finalPrice);
							player.getInventory().addItem(items);
							Integer newStock = curstock - quantity;
							AhoyCoin.towns.set(signText[1] + ".items." + signText[2] + ".curstock", newStock);
							AhoyCoin.saveYamls();
							player.sendMessage(plugin.pre + "You bought " + quantity.toString() + " " + itemName.toString() + "(s) from " + townName + " for " + String.valueOf(finalPrice) + ".");
							player.sendMessage(plugin.pre + "Your new balance is " + econ.getBalance(player.getName()) + ".");
						} else {
							player.sendMessage(plugin.pre + "You don't have enough money! You've only got " + econ.getBalance(player.getName()) + "!");
						}
					} else if (!buymode) {
						if (player.getItemInHand().getType().toString().equalsIgnoreCase(itemName))
						{
							Integer amountHeld = player.getItemInHand().getAmount();
							
							if (!(amountHeld < quantity))
							{
								//player has enough of item to sell - so sell it!
								Integer newPlayerAmount = amountHeld - quantity;
								
								//set the shop's stock
								if (!((curstock + quantity) >= maxstock))
								{
									AhoyCoin.towns.set(townName + ".items." + itemName + ".curstock", (curstock + quantity));
								} else {
									AhoyCoin.towns.set(townName + ".items." + itemName + ".curstock", maxstock);
								}
								
								AhoyCoin.saveYamls();
								
								//remove the item(s) from the player
								if (newPlayerAmount == 0)
								{
									ItemStack replaceItemWith = new ItemStack(Material.AIR, 1);
									player.setItemInHand(replaceItemWith);
									
									//int itemSlot = player.getInventory().getHeldItemSlot();
									//player.getInventory().setItem(itemSlot, null);
									
									//player.setItemInHand(null);
									
									//player.getInventory().remove(itemToRemove);
								} else {
									player.getInventory().removeItem(new ItemStack(Material.getMaterial(itemName.toUpperCase()), quantity));
									//player.getItemInHand().setAmount(newPlayerAmount);
								}
								
								player.updateInventory();
								
								//give the player the money
								econ.depositPlayer(player.getName(), finalPrice);
								
								player.sendMessage(plugin.pre + "You sold " + quantity.toString() + " " + itemName + "(s) to " + townName + " for " + String.valueOf(finalPrice) + ".");
								player.sendMessage(plugin.pre + "Your new balance is " + econ.getBalance(player.getName()) + ".");
							} else {
								player.sendMessage(plugin.pre + "You don't have enough " + itemName + "s to sell to this shop! You need at least " + quantity.toString() + ".");
							}
						} else {
							player.sendMessage(plugin.pre + "You can't sell that here! Hold what you want to sell in your hand!");
						}
					}
				} else if (sign.getLine(0).equalsIgnoreCase("[Vendor]")) {
					Player player = event.getPlayer();
					player.sendMessage(plugin.pre + "Left-click the sign first to create it!");
				}
			}
		}
	}
}