package com.jubiman.rankup.events.listener;

import com.jubiman.rankup.Rankup;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerJoinListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		// Add the user to the database
		try {
			Rankup.getDB().addNewUserToDatabase(e.getPlayer().getUniqueId().toString());
			Rankup.getInstance().getLogger().info("Added user '" + e.getPlayer().getDisplayName() +
					"' ('" + e.getPlayer().getUniqueId().toString() + "') to the database.");
		} catch (SQLException ex) {
			Rankup.getInstance().getLogger().severe("Failed to add user '" + e.getPlayer().getDisplayName() +
					"' ('" + e.getPlayer().getUniqueId().toString() + "') to the database: " + ex.getMessage());
		}
	}
}
