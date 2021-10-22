package com.jubiman.rankup.commands;

import com.jubiman.rankup.Rankup;
import me.desht.dhutils.MessagePager;
import me.desht.dhutils.MiscUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class CommandGetConfig implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		List<String> lines = getPluginConfiguration(args.length >= 1 ? args[0] : null);
		if (lines.size() > 1) {
			MessagePager pager = MessagePager.getPager(sender).clear().setParseColours(true);
			for (String line : lines) {
				pager.add(line);
			}
			pager.showPage();
		} else if (lines.size() == 1) {
			MiscUtil.statusMessage(sender, lines.get(0));
		} else {
			MiscUtil.errorMessage(sender, "No such configuration key.");
		}
		return true;
	}

	public List<String> getPluginConfiguration(String section) {
		ArrayList<String> res = new ArrayList<>();
		Configuration config = Rankup.getInstance().getConfig();
		ConfigurationSection cs = config.getRoot();

		Set<String> items;
		if (section == null) {
			items = config.getDefaults().getKeys(true);
		} else {
			if (config.getDefaults().isConfigurationSection(section)) {
				cs = config.getConfigurationSection(section);
				items = config.getDefaults().getConfigurationSection(section).getKeys(true);
			} else {
				items = new HashSet<>();
				if (config.getDefaults().contains(section))
					items.add(section);
			}
		}

		for (String k : items) {
			if (cs.isConfigurationSection(k))
				continue;
			res.add("&f" + k + "&- = '&e" + cs.get(k) + "&-'");
		}
		Collections.sort(res);
		return res;
	}
}
