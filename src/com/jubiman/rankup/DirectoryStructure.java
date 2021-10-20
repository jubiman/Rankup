package com.jubiman.rankup;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class DirectoryStructure {
	private static File pluginDir = new File("plugins", "Rankup");
	private static File dataDir, databaseDir;
	private static final String dataFoldername = "data";
	private static final String databaseFoldername = "db";

	private static final String persistFilename = "persist.yml";

	public static void setup(Rankup plugin) {
		pluginDir = Rankup.getInstance().getDataFolder();

		setupDirectoryStructure();
	}

	public static File getDatabaseDir() {
		return databaseDir;
	}

	private static void setupDirectoryStructure() {
		// directories
		dataDir = new File(pluginDir, dataFoldername);
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

	public static File getResourceFileForLoad(File dir, String filename) {
		// try the lower-cased form first, if that fails try the exact filename
		File f = new File(dir, "custom" + File.separator + filename.toLowerCase() + ".yml");
		if (!f.exists()) {
			f = new File(dir, "custom" + File.separator + filename + ".yml");
		}
		if (!f.exists()) {
			f = new File(dir, filename.toLowerCase() + ".yml");
		}
		return f;
	}

	public static boolean isCustom(File path) {
		return path.getParentFile().getName().equalsIgnoreCase("custom");
	}

	public static File getResourceFileForSave(File dir, String filename) {
		return new File(dir, "custom" + File.separator + filename.toLowerCase() + ".yml");
	}

	public static final FilenameFilter ymlFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".yml");
		}
	};
}
