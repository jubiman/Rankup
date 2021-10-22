package com.jubiman.rankup;

import com.jubiman.rankup.commands.*;
import com.jubiman.rankup.events.listener.ClickEventListener;
import com.jubiman.rankup.events.listener.MoneyChangeListener;
import com.jubiman.rankup.events.listener.PlayerJoinListener;

import me.desht.dhutils.ConfigurationManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Rankup extends JavaPlugin {
	private static Rankup instance;
	private static Database db;

	private ConfigurationManager configManager;

	@Override
	public void onEnable() {
		setInstance(this);

		getLogger().info("Registering commands...");
		registerCommands();
		getLogger().info("Finished registering commands.");

		getLogger().info("Registering events...");
		registerEvents();
		getLogger().info("Finished registering events.");

		// DHUtils stuff
		DirectoryStructure.setup();
		configManager = new ConfigurationManager(this);

		// Connecting to database
		try {
			db = new Database();
		} catch (SQLException | ClassNotFoundException e) {
			getLogger().severe("Failed to initialize database: " + e.getMessage());
		}


		getLogger().info("Finished loading Rankup plugin!");
	}

	@Override
	public void onDisable() {
		getLogger().info("Goodbye World!");

		// Close connection to database
		try {
			if (db.getConnection()!=null && !db.getConnection().isClosed()) {
				db.shutdown();
				getLogger().info("Shut down ");
			}
		} catch (SQLException e) {
			getLogger().severe("Got an error while trying to shutdown database: " + e.getMessage());
		}
	}

	private void registerCommands() {
		getCommand("rankup").setExecutor(new CommandRankup());
		getCommand("money").setExecutor(new CommandMoney());
		getCommand("rank").setExecutor(new CommandRank());
		getCommand("prestige").setExecutor(new CommandPrestige());
		getCommand("config").setExecutor(new CommandConfig());
		getCommand("getconfig").setExecutor(new CommandGetConfig());
	}

	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new ClickEventListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new MoneyChangeListener(), this);
	}

	private void setInstance(Rankup inst) {
		instance = inst;
	}
	public static Rankup getInstance() {
		return instance;
	}

	public static Database getDB() {
		return db;
	}
	public ConfigurationManager getConfigManager() {
		return configManager;
	}
}
