package com.jubiman.rankup.commands;

import com.jubiman.rankup.Rankup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.OptionalInt;

public class CommandRankup implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			Inventory gui = Bukkit.createInventory(null, 54, ChatColor.AQUA + "RANK UP");

			OptionalInt r, p;
			int rank, prestige;
			try {
				r = Rankup.getDB().getRankFromUUID(player.getUniqueId().toString());
				p = Rankup.getDB().getPrestigeFromUUID(player.getUniqueId().toString());
				if(!r.isPresent() || !p.isPresent()) throw new SQLException();
				rank = r.getAsInt();
				prestige = p.getAsInt();
			} catch (SQLException e) {
				player.sendMessage("§cFailed to get player data.");
				return true;
			}
			long upgrade_cost = (long)(2500 * (1 + rank) * (1 + (prestige * .25f)));

			// Create ranks
			ItemStack[] ranks = new ItemStack[]{
					// Ranks 0-10
					new ItemStack(Material.DIRT),
					new ItemStack(Material.COBBLESTONE),
					new ItemStack(Material.COAL_BLOCK),
					new ItemStack(Material.IRON_BLOCK),
					new ItemStack(Material.LAPIS_BLOCK),
					new ItemStack(Material.QUARTZ_BLOCK),
					new ItemStack(Material.REDSTONE_BLOCK),
					new ItemStack(Material.GOLD_BLOCK),
					new ItemStack(Material.DIAMOND_BLOCK),
					new ItemStack(Material.OBSIDIAN),
			};

			// Create item metas
			ItemMeta[] itemMetas = new ItemMeta[11];
			for (int i=0; i<ranks.length; i++) {
				itemMetas[i] = ranks[i].getItemMeta();
			}

			// Create names and lores
			String[] names = new String[]{
					"§dRank 1",
					"§dRank 2",
					"§dRank 3",
					"§dRank 4",
					"§dRank 5",
					"§dRank 6",
					"§dRank 7",
					"§dRank 8",
					"§dRank 9",
					"§dRank 10"
			};

			String[][] lores = new String[][]{
					new String[]{
							"§5The first rank, what a scrub are you...",
							"§5Gain access to:",
							"§5    Bla bla bla.",
							"",
							"§b§lTHE FIRST RANK (IT'S FREE)"
					},
					new String[]{
							"§5The second rank, what a scrub are you...",
							"§5Gain access to:",
							"§5    Bla bla bla.",
							"",
							"§5Upgrade cost: " + (rank < 2 ? (1-rank) == 0 ? "§b§lCURRENT RANK" : ("§6$" + (upgrade_cost + (2500L * (1-rank)))) : "§b§lALREADY PURCHASED")
					},
					new String[]{
							"§5The third rank, what a scrub are you...",
							"§5Gain access to:",
							"§5    Bla bla bla.",
							"",
							"§5Upgrade cost: " + (rank < 3 ? (2-rank) == 0 ? "§b§lCURRENT RANK" : ("§6$" + (upgrade_cost + (2500L * (2-rank)))) : "§b§lALREADY PURCHASED")
					},
					new String[]{
							"§5The fourth rank, what a scrub are you...",
							"§5Gain access to:",
							"§5    Bla bla bla.",
							"",
							"§5Upgrade cost: " + (rank < 4 ? (3-rank) == 0 ? "§b§lCURRENT RANK" : ("§6$" + (upgrade_cost + (2500L * (3-rank)))) : "§b§lALREADY PURCHASED")
					},
					new String[]{
							"§5The fifth rank, what a scrub are you...",
							"§5Gain access to:",
							"§5    Bla bla bla.",
							"",
							"§5Upgrade cost: " + (rank < 5 ? (4-rank) == 0 ? "§b§lCURRENT RANK" : ("§6$" + (upgrade_cost + (2500L * (4-rank)))) : "§b§lALREADY PURCHASED")
					},
					new String[]{
							"§5The sixth rank, what a scrub are you...",
							"§5Gain access to:",
							"§5    Bla bla bla.",
							"",
							"§5Upgrade cost: " + (rank < 6 ? (5-rank) == 0 ? "§b§lCURRENT RANK" : ("§6$" + (upgrade_cost + (2500L * (5-rank)))) : "§b§lALREADY PURCHASED")
					},
					new String[]{
							"§5The seventh rank, what a scrub are you...",
							"§5Gain access to:",
							"§5    Bla bla bla.",
							"",
							"§5Upgrade cost: " + (rank < 7 ? (6-rank) == 0 ? "§b§lCURRENT RANK" : ("§6$" + (upgrade_cost + (2500L * (6-rank)))) : "§b§lALREADY PURCHASED")
					},
					new String[]{
							"§5The eighth rank, what a scrub are you...",
							"§5Gain access to:",
							"§5    Bla bla bla.",
							"",
							"§5Upgrade cost: " + (rank < 8 ? (7-rank) == 0 ? "§b§lCURRENT RANK" : ("§6$" + (upgrade_cost + (2500L * (7-rank)))) : "§b§lALREADY PURCHASED")
					},
					new String[]{
							"§5The ninth rank, what a scrub are you...",
							"§5Gain access to:",
							"§5    Bla bla bla.",
							"",
							"§5Upgrade cost: " + (rank < 9 ? (8-rank) == 0 ? "§b§lCURRENT RANK" : ("§6$" + (upgrade_cost + (2500L * (8-rank)))) : "§b§lALREADY PURCHASED")
					},
					new String[]{
							"§5The tenth rank, what a scrub are you...",
							"§5Gain access to:",
							"§5    Bla bla bla.",
							"",
							"§5Upgrade cost: " + (rank < 10 ? (9-rank) == 0 ? "§b§lCURRENT RANK" : ("§6$" + (upgrade_cost + (2500L * (9-rank)))) : "§b§lALREADY PURCHASED")
					}
			};

			// Make current rank glowing and update name
			names[rank] = names[rank] + " §b§l(CURRENT)";
			itemMetas[rank].addEnchant(Enchantment.WATER_WORKER, 1, true);
			itemMetas[rank].addItemFlags(ItemFlag.HIDE_ENCHANTS);

			// Set name and lore
			for (int i=0; i<ranks.length; i++) {
				itemMetas[i].setDisplayName(names[i]);
				itemMetas[i].setLore(Arrays.asList(lores[i]));
				itemMetas[i].addItemFlags(ItemFlag.HIDE_ENCHANTS);
				ranks[i].setItemMeta(itemMetas[i]);
			}

			ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE);
			filler.setDurability((short) 0xF); // Black

			for (int i=0; i<54; ++i) {
				if (i < 20) gui.setItem(i, filler);
				else if (i < 25) gui.setItem(i, ranks[i-20]);
				else if (i < 29) gui.setItem(i, filler);
				else if (i < 34) gui.setItem(i, ranks[i-24]);
				else gui.setItem(i, filler);
			}

			// Functional buttons
			ItemStack close = new ItemStack(Material.BARRIER);
			// Prestige
			ItemStack prestigeStack = new ItemStack(Material.BEDROCK);

			// Item meta
			ItemMeta closeMeta = close.getItemMeta();
			ItemMeta prestigeMeta = prestigeStack.getItemMeta();

			// Close button
			String[] closeLore = new String[]{
					"Click here to close this menu"
			};
			closeMeta.setDisplayName("§4Close");
			closeMeta.setLore(Arrays.asList(closeLore));

			// Prestige button
			String[] prestigeLore = new String[]{
					"§4You are prestige: §6" + prestige
			};

			prestigeMeta.setDisplayName("§dPrestige");
			prestigeMeta.setLore(Arrays.asList(prestigeLore));

			close.setItemMeta(closeMeta);
			prestigeStack.setItemMeta(prestigeMeta);

			gui.setItem(49, close);
			gui.setItem(50, prestigeStack);

			// Set the inventory
			player.openInventory(gui);
			return true;
		}
		return false;
	}
}
