package com.jubiman.rankup;

import java.io.File;

public class DirectoryStructure {
	private static File pluginDir = new File("plugins", "Rankup");
	private static File databaseDir;
	private static final String dataFoldername = "data";
	private static final String databaseFoldername = "db";

	public static void setup() {
		pluginDir = Rankup.getInstance().getDataFolder();

		setupDirectoryStructure();
	}

	public static File getDatabaseDir() {
		return databaseDir;
	}

	private static void setupDirectoryStructure() {
		// directories
		File dataDir = new File(pluginDir, dataFoldername);
		databaseDir = new File(dataDir, databaseFoldername);

		// <plugins>/Rankup
		createDir(pluginDir);
		// <plugins>/Rankup/data
		createDir(dataDir);
		// <plugins>/Rankup/data/db
		createDir(databaseDir);
	}

	private static void createDir(File dir) {
		if (dir.isDirectory()) {
			return;
		}
		if (!dir.mkdir()) {
			Rankup.getInstance().getLogger().warning("Can't make directory " + dir.getName()); //$NON-NLS-1$
		}
	}
}
