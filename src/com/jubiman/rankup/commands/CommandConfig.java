package com.jubiman.rankup.commands;

import com.jubiman.rankup.Rankup;
import me.desht.dhutils.ConfigurationManager;
import me.desht.dhutils.DHUtilsException;
import me.desht.dhutils.MiscUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandConfig implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 2) {
			return false;
		}
		String key = args[0], val = args[1];

		ConfigurationManager configManager = Rankup.getInstance().getConfigManager();

		try {
			if (args.length > 2) {
				List<String> list = new ArrayList<>(args.length - 1);
				list.addAll(Arrays.asList(args).subList(1, args.length));
				configManager.set(key, list);
			} else {
				configManager.set(key, val);
			}
			Object res = configManager.get(key);
			MiscUtil.statusMessage(sender, key + " is now set to '&e" + res + "&-'");
		} catch (DHUtilsException e) {
			MiscUtil.errorMessage(sender, e.getMessage());
			MiscUtil.errorMessage(sender, "Use /getcfg to list all valid keys");
		} catch (IllegalArgumentException e) {
			MiscUtil.errorMessage(sender, e.getMessage());
		}
		return true;
	}
}
