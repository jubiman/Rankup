package com.jubiman.rankup.commands;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagShort;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
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
			Inventory gui = Bukkit.createInventory(null, 54, ChatColor.AQUA + "RANK UP");

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
			// TODO: placeholders
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
							"§5The tenth rank, what a scrub are you..."
					}
			};

			// Set name and lore
			for (int i=0; i<ranks.length; i++) {
				itemMetas[i].setDisplayName(names[i]);
				itemMetas[i].setLore(Arrays.asList(lores[i]));
				itemMetas[i].addItemFlags(ItemFlag.HIDE_ENCHANTS);
				ranks[i].setItemMeta(itemMetas[i]);
			}

			//for (int i=(27-(ranks.length/2)); i<(27+(ranks.length/2)); i++)
			//	inv.setItem(i, ranks[i-(27-ranks.length/2)]);
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
			ItemStack prestige = new ItemStack(Material.BEDROCK);

			// Item meta
			ItemMeta closeMeta = close.getItemMeta();
			ItemMeta prestigeMeta = prestige.getItemMeta();

			// Close button
			closeMeta.setDisplayName("Close");

			// Prestige button
			String[] prestigeLore = new String[]{
					"§4Prestige. YOU FINALLY MADE IT HAHA."
			};

			prestigeMeta.setDisplayName("§dPrestige");
			prestigeMeta.setLore(Arrays.asList(prestigeLore));

			close.setItemMeta(closeMeta);
			prestige.setItemMeta(prestigeMeta);

			gui.setItem(49, close);
			gui.setItem(50, prestige);

			// Set the inventory
			player.openInventory(gui);
			return true;
		}
		return false;
	}
}
