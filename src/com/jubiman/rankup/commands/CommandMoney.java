package com.jubiman.rankup.commands;

import com.jubiman.rankup.Rankup;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Objects;
import java.util.OptionalLong;

public class CommandMoney implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				try {
					OptionalLong coins = Rankup.getDB().getCoinsFromUUID(player.getUniqueId().toString());
					if (!coins.isPresent()) {
						player.sendMessage("§cSomething went wrong while getting your balance.");
						return true;
					}
					player.sendMessage("§eBalance: §6" + coins.getAsLong());
				} catch (SQLException e) {
					Rankup.getInstance().getLogger().severe("Failed to get coins from user '" +
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
					OptionalLong coins = Rankup.getDB().getCoinsFromUUID(p2.getUniqueId().toString());
					if (!coins.isPresent()) {
						player.sendMessage("§cSomething went wrong while getting " + args[0] + "'s balance.");
						return true;
					}
					player.sendMessage("§e" + p2.getDisplayName() + "'s balance: §6" + coins.getAsLong());
				} catch (SQLException e) {
					Rankup.getInstance().getLogger().severe("Failed to get coins from user '" +
							player.getDisplayName() +
							"' ('" + player.getUniqueId().toString() + "') to the database: " + e.getMessage());
				}
			} else {
				if (Objects.equals(args[0].toLowerCase(), "add") || Objects.equals(args[0].toLowerCase(), "remove") || Objects.equals(args[0].toLowerCase(), "set")) {
					try {
						switch (args[0].toLowerCase()) {
							case "set": {
								try {
									Rankup.getDB().setCoinsFromUUID(player.getUniqueId().toString(), Long.parseLong(args[1]));
								} catch (SQLException e) {
									player.sendMessage("§cFailed to set your balance to §6" + args[1]);
									return true;
								}
								player.sendMessage("§eSet your balance to §6" + args[1]);
								break;
							}
							case "add": {
								try {
									Rankup.getDB().addCoinsFromUUID(player.getUniqueId().toString(), Long.parseLong(args[1]));
								} catch (SQLException e) {
									player.sendMessage("§cFailed to add §6" + args[1] + "§c to your balance.");
									return true;
								}
								player.sendMessage("§eAdded §6" + args[1] + "§e to your balance.");
								break;
							}
							case "remove": {
								try {
									Rankup.getDB().removeCoinsFromUUID(player.getUniqueId().toString(), Long.parseLong(args[1]));
								} catch (SQLException e) {
									player.sendMessage("§cFailed to remove §6" + args[1] + "§c from your balance.");
									return true;
								}
								player.sendMessage("§eRemoved §6" + args[1] + "§e from your balance.");
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
									Rankup.getDB().setCoinsFromUUID(p2.getUniqueId().toString(), Long.parseLong(args[2]));
								} catch (SQLException e) {
									player.sendMessage("§cFailed to set your balance to §6" + args[1]);
									return true;
								}
								player.sendMessage("§eSet " + args[1] + "'s balance to §6" + args[2]);
								break;
							}
							case "add": {
								try {
									Rankup.getDB().addCoinsFromUUID(p2.getUniqueId().toString(), Long.parseLong(args[2]));
								} catch (SQLException e) {
									player.sendMessage("§cFailed to add §6" + args[2] + "§c to " + args[1] + "'s balance.");
									return true;
								}
								player.sendMessage("§eAdded §6" + args[2] + "§e to " + args[1] + "'s balance.");
								break;
							}
							case "remove": {
								try {
									Rankup.getDB().removeCoinsFromUUID(p2.getUniqueId().toString(), Long.parseLong(args[2]));
								} catch (SQLException e) {
									player.sendMessage("§cFailed to remove §6" + args[2] + "§c from your balance.");
									return true;
								}
								player.sendMessage("§eRemoved §6" + args[2] + "§e from " + args[1] + "'s balance.");
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
