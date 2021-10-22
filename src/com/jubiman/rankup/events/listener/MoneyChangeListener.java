package com.jubiman.rankup.events.listener;

import com.jubiman.rankup.Rankup;
import com.jubiman.rankup.events.event.MoneyChangeEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class MoneyChangeListener implements Listener {
	@EventHandler
	public void onMoneyChange(MoneyChangeEvent e) {
		// Get coins from player
		try {
			OptionalLong coins = Rankup.getDB().getCoinsFromUUID(e.getPlayer().getUniqueId().toString());
			OptionalInt rank = Rankup.getDB().getRankFromUUID(e.getPlayer().getUniqueId().toString());
			OptionalInt prestige = Rankup.getDB().getPrestigeFromUUID(e.getPlayer().getUniqueId().toString());
			if (!coins.isPresent() || !rank.isPresent() || !prestige.isPresent()) {
				throw new SQLException();
			}
			long tmp_coins = coins.getAsLong();
			int tmp_rank = rank.getAsInt();
			int tmp_prestige = prestige.getAsInt();
			// Formula: upgrade_cost = $2500 * new_rank * (1 + (prestige * .25f))
			// per rank, rank up costs increases by $2500
			// per prestige level, rank up costs increase by 25%
			// We want to calculate for the next rank, so (1 + rank)
			long upgrade_cost = (long)(2500 * (1 + tmp_rank) * (1 + (tmp_prestige * .25f)));
			while (tmp_coins > upgrade_cost) {
				e.getPlayer().sendMessage(ChatColor.GREEN + "You ranked up to rank " + ChatColor.GOLD + (tmp_rank + 1) + ChatColor.GREEN + "! (" + ChatColor.GOLD + -upgrade_cost + ChatColor.GREEN + ")");
				if (++tmp_rank > 10) {
					tmp_rank = 0;
					tmp_prestige++;
					e.getPlayer().sendMessage(ChatColor.RED + "You hit prestige level " + ChatColor.GOLD + (tmp_prestige) + ChatColor.RED + "! (" + ChatColor.GOLD + -upgrade_cost + ChatColor.RED + ")");
				}
				tmp_coins -= upgrade_cost;
				upgrade_cost = (long)(2500 * (1 + tmp_rank) * (1 + (tmp_prestige * .25f)));
			}
			// Set new values to database
			Rankup.getDB().updateAllFromUUID(e.getPlayer().getUniqueId().toString(), tmp_coins, tmp_rank, tmp_prestige);
		} catch (SQLException ex) {
			Rankup.getInstance().getLogger().severe("Failed to get player data from " + e.getPlayer().getDisplayName());
		}
	}
}