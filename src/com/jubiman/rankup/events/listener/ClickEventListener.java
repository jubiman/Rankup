package com.jubiman.rankup.events.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickEventListener implements Listener {
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if(e.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.AQUA + "RANK UP")) {
			switch (e.getCurrentItem().getType()) {
				case DIRT:
				case COBBLESTONE:
				case COAL_BLOCK:
				case IRON_BLOCK:
				case LAPIS_BLOCK:
				case QUARTZ_BLOCK:
				case REDSTONE_BLOCK:
				case GOLD_BLOCK:
				case DIAMOND_BLOCK:
				case OBSIDIAN:
					break;
				case BEDROCK: {
					// TODO: Prestige
					// rank = 0
					// Coins -= ahwy8ubfyuabf
					// SQL stuff as well :(
					break;
				}
				case BARRIER: {
					player.closeInventory();
					break;
				}
			}
			e.setCancelled(true);
		}
	}
}
