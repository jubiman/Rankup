package com.jubiman.rankup;

import com.jubiman.rankup.events.event.MoneyChangeEvent;
import com.jubiman.rankup.exeptions.unsupportedDatabaseTypeException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;

import java.io.File;
import java.sql.*;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.UUID;

public class Database {
	private SupportedDrivers activeDriver;

	enum SupportedDrivers {
		MYSQL,
		SQLITE
	}

	private Connection connection;

	Database() throws ClassNotFoundException, SQLException {
		makeDBConnection();
	}

	public Connection getConnection() {
		return connection;
	}

	void shutdown() {
		try {
			if (!connection.getAutoCommit()) {
				connection.rollback();
			}
			connection.close();
			Rankup.getInstance().getLogger().info("Closing DB connection to " + connection.getMetaData().getDatabaseProductName());
		} catch (SQLException e) {
			Rankup.getInstance().getLogger().warning("Can't cleanly shut down DB connection: " + e.getMessage());
		}
	}

	void makeDBConnection() throws SQLException, ClassNotFoundException {
		connection = null;
		String dbType = Rankup.getInstance().getConfig().getString("database.driver", "sqlite");
		SupportedDrivers driver = SupportedDrivers.valueOf(dbType.toUpperCase());
		switch (driver) {
			case MYSQL:
				connection = connectMySQL();
				setupTablesMySQL();
				break;
			case SQLITE:
				connection = connectSQLite();
				setupTablesSQLite();
				break;
			default:
				throw new unsupportedDatabaseTypeException(dbType);
		}
		this.activeDriver = driver;
		Rankup.getInstance().getLogger().info("Connected to DB: " + connection.getMetaData().getDatabaseProductName());
	}

	private Connection connectSQLite() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		File dbFile = new File(DirectoryStructure.getDatabaseDir(), "rankup.db");
		return DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
	}

	private Connection connectMySQL() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Configuration config = Rankup.getInstance().getConfig();
		String user = config.getString("database.user", "rankup");
		String pass = config.getString("database.password", "");
		String host = config.getString("database.host", "localhost");
		String dbName = config.getString("database.name", "rankup");
		int port = config.getInt("database.port", 3306);
		String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
		return DriverManager.getConnection(url, user, pass);
	}

	private void setupTablesSQLite() throws SQLException {
		createTableIfNotExists("info",
				"uuid CHAR(36) NOT NULL PRIMARY KEY UNIQUE," +
						"coins BIGINT DEFAULT 0 NOT NULL," +
						"rank INT DEFAULT 0 NOT NULL," +
						"prestige INT DEFAULT 0 NOT NULL");
	}

	private void setupTablesMySQL() throws SQLException {
		createTableIfNotExists("info",
				"uuid CHAR(36) NOT NULL," +
						"coins BIGINT DEFAULT 0 NOT NULL," +
						"rank INT DEFAULT 0 NOT NULL," +
						"prestige INT DEFAULT 0 NOT NULL," +
						"PRIMARY KEY (uuid)");
	}

	private void createTableIfNotExists(String tableName, String ddl) throws SQLException {
		String fullName = Rankup.getInstance().getConfig().getString("database.table_prefix", "rankup_") + tableName;
		Statement stmt = connection.createStatement();
		try {
			if (tableExists(tableName)) {
				stmt.executeUpdate("ALTER TABLE " + tableName + " RENAME TO " + fullName);
				Rankup.getInstance().getLogger().info("Renamed DB table " + tableName + " to " + fullName);
			} else if (!tableExists(fullName)) {
				stmt.executeUpdate("CREATE TABLE " + fullName + "(" + ddl + ")");
			}
		} catch (SQLException e) {
			Rankup.getInstance().getLogger().severe("Can't execute " + stmt + ": " + e.getMessage());
		}
	}

	private boolean tableExists(String table) throws SQLException {
		DatabaseMetaData dbm = connection.getMetaData();
		ResultSet tables = dbm.getTables(null , null, table, null);
		return tables.next();
	}

	// Database functions
	public void addNewUserToDatabase(String uuid) throws SQLException {
		String tableName = Rankup.getInstance().getConfig().getString("database.table_prefix") + "info";
		PreparedStatement stmt = connection.prepareStatement(
				"INSERT INTO " + tableName + " (uuid, coins, rank, prestige)" +
						" VALUES (?, 0, 0, 0)");
		stmt.setString(1, uuid);
		stmt.executeUpdate();
	}

	public OptionalInt getRankFromUUID(String uuid) throws SQLException {
		String tableName = Rankup.getInstance().getConfig().getString("database.table_prefix") + "info";
		PreparedStatement stmt = connection.prepareStatement(
				"SELECT rank FROM " + tableName + " WHERE uuid=?");
		stmt.setString(1, uuid);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			return OptionalInt.of(rs.getInt("rank"));
		}
		return OptionalInt.empty();
	}

	public void setRankFromUUID(String uuid, int rank) throws SQLException {
		String tableName = Rankup.getInstance().getConfig().getString("database.table_prefix") + "info";
		PreparedStatement stmt = connection.prepareStatement(
				"UPDATE " + tableName + " SET rank=? WHERE uuid=?");
		stmt.setInt(1, rank);
		stmt.setString(2, uuid);
		stmt.executeUpdate();
	}

	public void addRankFromUUID(String uuid, int rank) throws SQLException {
		String tableName = Rankup.getInstance().getConfig().getString("database.table_prefix") + "info";
		PreparedStatement stmt = connection.prepareStatement(
				"UPDATE " + tableName + " SET rank=rank+? WHERE uuid=?");
		stmt.setInt(1, rank);
		stmt.setString(2, uuid);
		stmt.executeUpdate();
	}

	public void removeRankFromUUID(String uuid, int rank) throws SQLException {
		String tableName = Rankup.getInstance().getConfig().getString("database.table_prefix") + "info";
		PreparedStatement stmt = connection.prepareStatement(
				"UPDATE " + tableName + " SET rank=rank-? WHERE uuid=?");
		stmt.setInt(1, rank);
		stmt.setString(2, uuid);
		stmt.executeUpdate();
	}

	public OptionalInt getPrestigeFromUUID(String uuid) throws SQLException {
		String tableName = Rankup.getInstance().getConfig().getString("database.table_prefix") + "info";
		PreparedStatement stmt = connection.prepareStatement(
				"SELECT prestige FROM " + tableName + " WHERE uuid=?");
		stmt.setString(1, uuid);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			return OptionalInt.of(rs.getInt("prestige"));
		}
		return OptionalInt.empty();
	}

	public void setPrestigeFromUUID(String uuid, int prestige) throws SQLException {
		String tableName = Rankup.getInstance().getConfig().getString("database.table_prefix") + "info";
		PreparedStatement stmt = connection.prepareStatement(
				"UPDATE " + tableName + " SET prestige=? WHERE uuid=?");
		stmt.setInt(1, prestige);
		stmt.setString(2, uuid);
		stmt.executeUpdate();
	}

	public void addPrestigeFromUUID(String uuid, int prestige) throws SQLException {
		String tableName = Rankup.getInstance().getConfig().getString("database.table_prefix") + "info";
		PreparedStatement stmt = connection.prepareStatement(
				"UPDATE " + tableName + " SET prestige=prestige+? WHERE uuid=?");
		stmt.setInt(1, prestige);
		stmt.setString(2, uuid);
		stmt.executeUpdate();
	}

	public void removePrestigeFromUUID(String uuid, int prestige) throws SQLException {
		String tableName = Rankup.getInstance().getConfig().getString("database.table_prefix") + "info";
		PreparedStatement stmt = connection.prepareStatement(
				"UPDATE " + tableName + " SET prestige=prestige-? WHERE uuid=?");
		stmt.setInt(1, prestige);
		stmt.setString(2, uuid);
		stmt.executeUpdate();
	}

	public OptionalLong getCoinsFromUUID(String uuid) throws SQLException {
		String tableName = Rankup.getInstance().getConfig().getString("database.table_prefix") + "info";
		PreparedStatement stmt = connection.prepareStatement(
				"SELECT coins FROM " + tableName + " WHERE uuid=?");
		stmt.setString(1, uuid);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			return OptionalLong.of(rs.getLong("coins"));
		}
		return OptionalLong.empty();
	}

	public void setCoinsFromUUID(String uuid, long coins) throws SQLException {
		String tableName = Rankup.getInstance().getConfig().getString("database.table_prefix") + "info";
		PreparedStatement stmt = connection.prepareStatement(
				"UPDATE " + tableName + " SET coins=? WHERE uuid=?");
		stmt.setLong(1, coins);
		stmt.setString(2, uuid);
		stmt.executeUpdate();
		Bukkit.getServer().getPluginManager().callEvent(new MoneyChangeEvent(Bukkit.getPlayer(UUID.fromString(uuid))));
	}

	public void addCoinsFromUUID(String uuid, long coins) throws SQLException {
		String tableName = Rankup.getInstance().getConfig().getString("database.table_prefix") + "info";
		PreparedStatement stmt = connection.prepareStatement(
				"UPDATE " + tableName + " SET coins=coins+? WHERE uuid=?");
		stmt.setLong(1, coins);
		stmt.setString(2, uuid);
		stmt.executeUpdate();
		Bukkit.getServer().getPluginManager().callEvent(new MoneyChangeEvent(Bukkit.getPlayer(UUID.fromString(uuid))));
	}

	public void removeCoinsFromUUID(String uuid, long coins) throws SQLException {
		String tableName = Rankup.getInstance().getConfig().getString("database.table_prefix") + "info";
		PreparedStatement stmt = connection.prepareStatement(
				"UPDATE " + tableName + " SET coins=coins-? WHERE uuid=?");
		stmt.setLong(1, coins);
		stmt.setString(2, uuid);
		stmt.executeUpdate();
		Bukkit.getServer().getPluginManager().callEvent(new MoneyChangeEvent(Bukkit.getPlayer(UUID.fromString(uuid))));
	}

	public void updateAllFromUUID(String uuid, long coins, int rank, int prestige) throws SQLException {
		String tableName = Rankup.getInstance().getConfig().getString("database.table_prefix") + "info";
		PreparedStatement stmt = connection.prepareStatement(
				"UPDATE " + tableName + " SET coins=?,rank=?,prestige=? WHERE uuid=?");
		stmt.setLong(1, coins);
		stmt.setInt(2, rank);
		stmt.setInt(3, prestige);
		stmt.setString(4, uuid);
		stmt.executeUpdate();
	}
}
