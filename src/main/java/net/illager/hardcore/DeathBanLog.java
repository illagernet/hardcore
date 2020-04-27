package net.illager.hardcore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonParser;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * A yaml log file to store deathbans
 */
public class DeathBanLog {
	private Logger logger;
	private File file;
	private FileConfiguration cache;

	/**
	 * @param plugin Hardcore plugin instance 
	 * @param filepath Log filepath
	 */
	public DeathBanLog(HardcorePlugin plugin, String filepath) {
		this.logger = plugin.getLogger();
		this.file = new File(plugin.getDataFolder(), filepath);
		this.load();
	}

	/**
	 * Load the log from file
	 */
	private void load() {
		try {
			this.cache = YamlConfiguration.loadConfiguration(this.file);
		} catch(IllegalArgumentException exception) {
			this.logger.log(Level.SEVERE, exception.getMessage());
		}
	}

	/**
	 * Save the log to file
	 */
	private void save() {
		try {
			this.cache.save(this.file);
		} catch(IOException exception) {
			this.logger.log(Level.SEVERE, exception.getMessage());
		}
	}

	/**
	 * Add a log entry
	 * @param uuid The player UUID
	 * @param deathBan The death ban to serialize
	 */
	public void add(UUID uuid, DeathBan deathBan) {
		this.cache.set(uuid.toString(), deathBan);
		this.save();
	}

	/**
	 * Add a log entry
	 * @param username The player username
	 * @param deathBan The death ban to serialize
	 */
	public void add(String username, DeathBan deathBan) {
		UUID uuid = this.fetchUUID(username);
		this.add(uuid, deathBan);
	}

	/**
	 * Remove a log entry
	 * @param uuid The player UUID
	 */
	public void remove(UUID uuid) {
		this.cache.set(uuid.toString(), null);
		this.save();
	}

	/**
	 * Remove a log entry
	 * @param username The player username
	 */
	public void remove(String username) {
		UUID uuid = this.fetchUUID(username);
		this.remove(uuid);
	}

	/**
	 * Checks a log entry existence
	 * @param uuid The player UUID
	 * @return Whether or not the entry exists
	 */
	public boolean has(UUID uuid) {
		return this.cache.contains(uuid.toString());
	}

	/**
	 * Checks a log entry existence
	 * @param username The player username
	 * @return Whether or not the entry exists
	 */
	public boolean has(String username) {
		UUID uuid = this.fetchUUID(username);
		return this.has(uuid);
	}

	/**
	 * Get a log entry by UUID
	 * @param uuid The player UUID
	 */
	public DeathBan get(UUID uuid) {
		return (DeathBan) this.cache.get(uuid.toString());
	}

	/**
	 * Get a log entry by username
	 * @param username The player username
	 */
	public DeathBan get(String username) {
		UUID uuid = this.fetchUUID(username);
		return this.get(uuid);
	}

	/**
	 * Fetch a player UUID from the Mojang API
	 * @param username The player username
	 */
	private UUID fetchUUID(String username) {
		JsonParser parser = new JsonParser();
		URLConnection request;
		try {
			request = new URL("https://api.mojang.com/users/profiles/minecraft/" + username).openConnection();
			request.connect();
			InputStreamReader reader = new InputStreamReader((InputStream) request.getContent());
			String id = parser.parse(reader).getAsJsonObject().get("id").getAsString();
			String delineatedId = id.replaceFirst(
				"(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
				"$1-$2-$3-$4-$5"
			);
			return UUID.fromString(delineatedId);
		} catch(Exception exception) {
			this.logger.log(Level.SEVERE, exception.getMessage());
			return null;
		}
	}
}
