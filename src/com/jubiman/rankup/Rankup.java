package com.jubiman.rankup;

import com.jubiman.rankup.commands.CommandRankup;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Objects;

public class Rankup extends JavaPlugin {
	private static Rankup instance;

	private Database db;

	@Override
	public void onEnable() {
		setInstance(this);

		getLogger().info("Registering commands...");
		registerCommands();
		getLogger().info("Finished registering commands.");

		DirectoryStructure.setup(this);

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
		this.getCommand("rankup").setExecutor(new CommandRankup());
	}

	private void setInstance(Rankup inst) {
		instance = inst;
	}
	public static Rankup getInstance() {
		return instance;
	}
}
