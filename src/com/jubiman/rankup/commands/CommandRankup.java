package com.jubiman.rankup.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CommandRankup implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE +  "RANK UP");

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
					new ItemStack(Material.EMERALD_BLOCK),
					// Prestige
					new ItemStack(Material.BEDROCK)
			};

			// Create item metas
			ItemMeta[] itemMetas = new ItemMeta[12];
			for (int i=0; i<12; i++) {
				itemMetas[i] = ranks[i].getItemMeta();
			}

			// Create names and lores
			// TODO: placeholders
			String[] names = new String[]{
					"Rank 1",
					"Rank 2",
					"Rank 3",
					"Rank 4",
					"Rank 5",
					"Rank 6",
					"Rank 7",
					"Rank 8",
					"Rank 9",
					"Rank 10",
					"Rank 11",
					"Prestige"
			};

			String[][] lores = new String[][]{
					new String[]{
							"§5The first rank, what a scrub are you..."
					},
					new String[]{
							"§5The second rank, what a scrub are you..."
					},
					new String[]{
							"§5The third rank, what a scrub are you..."
					},
					new String[]{
							"§5The fourth rank, what a scrub are you..."
					},
					new String[]{
							"§5The fifth rank, what a scrub are you..."
					},
					new String[]{
							"§5The sixth rank, what a scrub are you..."
					},
					new String[]{
							"§5The seventh rank, what a scrub are you..."
					},
					new String[]{
							"§5The eighth rank, what a scrub are you..."
					},
					new String[]{
							"§5The ninth rank, what a scrub are you..."
					},
					new String[]{
							"§5The eleventh rank, what a scrub are you..."
					},
					new String[]{
							"§5The twelfth rank, what a scrub are you..."
					},
					new String[]{
							"§4Prestige. YOU FINALLY MADE IT HAHA"
					}
			};

			// Set name and lore
			for (int i=0; i<ranks.length; i++) {
				itemMetas[i].setDisplayName(names[i]);
				itemMetas[i].setLore(Arrays.asList(lores[i]));
				itemMetas[i].addItemFlags(ItemFlag.HIDE_ENCHANTS);
				ranks[i].setItemMeta(itemMetas[i]);
			}

			for (int i=(27-(ranks.length/2)); i<(27+(ranks.length/2)); i++)
				inv.setItem(i, ranks[i-(27-ranks.length/2)]);

			// Set the inventory
			player.openInventory(inv);
			return true;
		}
		return false;
	}
}
