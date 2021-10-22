package com.jubiman.rankup.commands;

import com.jubiman.rankup.Rankup;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Objects;
import java.util.OptionalInt;

public class CommandRank implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				try {
					OptionalInt rank = Rankup.getDB().getRankFromUUID(player.getUniqueId().toString());
					if (!rank.isPresent()) {
						player.sendMessage("§cSomething went wrong while getting your rank.");
						return true;
					}
					player.sendMessage("§eRank: §6" + rank.getAsInt());
				} catch (SQLException e) {
					Rankup.getInstance().getLogger().severe("Failed to get rank from user '" +
							player.getDisplayName() +
							"' ('" + player.getUniqueId().toString() + "') to the database: " + e.getMessage());
				}
			} else if (args.length == 1) {
				Player p2 = Bukkit.getPlayer(args[0]);
				if (p2 == null) {
					player.sendMessage("§cCouldn't find " + args[0] + ". Please check your spelling and try again.");
					return true;
				}
				try {
					OptionalInt rank = Rankup.getDB().getRankFromUUID(p2.getUniqueId().toString());
					if (!rank.isPresent()) {
						player.sendMessage("§cSomething went wrong while getting " + args[0] + "'s rank.");
						return true;
					}
					player.sendMessage("§e" + p2.getDisplayName() + "'s rank: §6" + rank.getAsInt());
				} catch (SQLException e) {
					Rankup.getInstance().getLogger().severe("Failed to get rank from user '" +
							player.getDisplayName() +
							"' ('" + player.getUniqueId().toString() + "') to the database: " + e.getMessage());
				}
			} else {
				if (Objects.equals(args[0].toLowerCase(), "add") || Objects.equals(args[0].toLowerCase(), "remove") || Objects.equals(args[0].toLowerCase(), "set")) {
					try {
						switch (args[0].toLowerCase()) {
							case "set": {
								try {
									Rankup.getDB().setRankFromUUID(player.getUniqueId().toString(), Integer.parseInt(args[1]));
								} catch (SQLException e) {
									player.sendMessage("§cFailed to set your rank to §6" + args[1]);
									return true;
								}
								player.sendMessage("§eSet your rank to §6" + args[1]);
								break;
							}
							case "add": {
								try {
									Rankup.getDB().addRankFromUUID(player.getUniqueId().toString(), Integer.parseInt(args[1]));
								} catch (SQLException e) {
									player.sendMessage("§cFailed to add §6" + args[1] + "§c to your rank.");
									return true;
								}
								player.sendMessage("§eAdded §6" + args[1] + "§e to your rank.");
								break;
							}
							case "remove": {
								try {
									Rankup.getDB().removeRankFromUUID(player.getUniqueId().toString(), Integer.parseInt(args[1]));
								} catch (SQLException e) {
									player.sendMessage("§cFailed to remove §6" + args[1] + "§c from your rank.");
									return true;
								}
								player.sendMessage("§eRemoved §6" + args[1] + "§e from your rank.");
								break;
							}
						}
					} catch (NumberFormatException e) {
						player.sendMessage("§cCannot convert §6" + args[1] + "§c to a number. Please check your number for letters, or it might be too big.");
					}
				} else {
					Player p2 = Bukkit.getPlayer(args[0]);
					if (p2 == null) {
						player.sendMessage("§cCouldn't find " + args[0] + ". Please check your spelling and try again.");
						return true;
					}
					try {
						switch (args[1].toLowerCase()) {
							case "set": {
								try {
									Rankup.getDB().setRankFromUUID(p2.getUniqueId().toString(), Integer.parseInt(args[2]));
								} catch (SQLException e) {
									player.sendMessage("§cFailed to set your rank to §6" + args[1]);
									return true;
								}
								player.sendMessage("§eSet " + args[1] + "'s rank to §6" + args[2]);
								break;
							}
							case "add": {
								try {
									Rankup.getDB().addRankFromUUID(p2.getUniqueId().toString(), Integer.parseInt(args[2]));
								} catch (SQLException e) {
									player.sendMessage("§cFailed to add §6" + args[2] + "§c to " + args[1] + "'s rank.");
									return true;
								}
								player.sendMessage("§eAdded §6" + args[2] + "§e to " + args[1] + "'s rank.");
								break;
							}
							case "remove": {
								try {
									Rankup.getDB().removeRankFromUUID(p2.getUniqueId().toString(), Integer.parseInt(args[2]));
								} catch (SQLException e) {
									player.sendMessage("§cFailed to remove §6" + args[2] + "§c from your rank.");
									return true;
								}
								player.sendMessage("§eRemoved §6" + args[2] + "§e from " + args[1] + "'s rank.");
								break;
							}
						}
					} catch (NumberFormatException e) {
						player.sendMessage("§cCannot convert §6" + args[1] + "§c to a number. Please check your number for letters, or it might be too big.");
					}
				}
			}
			return true;
		}
		return false;
	}
}
