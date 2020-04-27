package net.illager.hardcore;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * A yaml log file to store deathbans
 */
public class DeathBanLog {
	private HardcorePlugin plugin;
	private File file;
	private FileConfiguration cache;

	/**
	 * @param plugin Hardcore plugin instance 
	 * @param filepath Log filepath
	 */
	public DeathBanLog(HardcorePlugin plugin, String filepath) {
		this.plugin = plugin;
		this.file = new File(this.plugin.getDataFolder(), filepath);
		this.load();
	}

	/**
	 * Load the log from file
	 */
	public void load() {
		try {
			this.cache = YamlConfiguration.loadConfiguration(this.file);
		} catch(IllegalArgumentException exception) {
			this.plugin.getLogger().log(Level.SEVERE, exception.getMessage());
		}
	}

	/**
	 * Save the log to file
	 */
	public void save() {
		try {
			this.cache.save(this.file);
		} catch(IOException exception) {
			this.plugin.getLogger().log(Level.SEVERE, exception.getMessage());
		}
	}

	/**
	 * Add a log entry
	 * @param uuid The player UUID
	 * @param deathBan The death ban to serialize
	 */
	public void add(UUID uuid, DeathBan deathBan) {
		this.cache.set(uuid.toString(), deathBan);
	}

	/**
	 * Remove a log entry
	 * @param uuid The player UUID
	 */
	public void remove(UUID uuid) {
		this.cache.set(uuid.toString(), null);
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
	 * Get a log entry
	 * @param uuid The player UUID
	 */
	public DeathBan get(UUID uuid) {
		return (DeathBan) this.cache.get(uuid.toString());
	}
}
