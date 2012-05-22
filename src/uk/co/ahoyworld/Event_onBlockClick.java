package uk.co.ahoyworld;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Event_onBlockClick implements Listener 
{	
	public Event_onBlockClick(AhoyCoin plugin)
	{
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void blockLeftClick (PlayerInteractEvent event)
	{
		if (event.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			if (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN)
			{
				Player player = event.getPlayer();
				player.sendMessage("Left clicked on a sign.");
			}
		} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN)
			{
				Player player = event.getPlayer();
				player.sendMessage("Right clicked on a sign.");
			}
		}
	}
}
