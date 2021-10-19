package com.jubiman.rankup;

import com.jubiman.rankup.commands.CommandRankup;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Rankup extends JavaPlugin {
	@Override
	public void onEnable() {
		getLogger().info("Registering commands...");
		registerCommands();
		getLogger().info("Finished registering commands.");
	}

	@Override
	public void onDisable() {
		getLogger().info("Goodbye World!");
	}

	private void registerCommands() {
		this.getCommand("rankup").setExecutor(new CommandRankup());
	}
}
