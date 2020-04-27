package net.illager.hardcore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("DeathBan")
public class DeathBan implements ConfigurationSerializable {
	private Date time;
	private Location location;
	private String deathMessage;
	private long discount;

	/**
	 * @param time Time of death
	 * @param location Location of death
	 * @param deathMessage Death message containing cause of death
	 * @param discount Milliseconds discounted from ban duration
	 */
	public DeathBan(Date time, Location location, String deathMessage, long discount) {
		this.time = time;
		this.location = location;
		this.deathMessage = deathMessage;
		this.discount = discount;
	}

	/**
	 * Serialize object for Bukkit configuration file
	 */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("time", this.time.getTime());
		result.put("location", this.location);
		result.put("message", this.deathMessage);
		if(this.discount != 0L) result.put("discount", this.discount);
		return result;
	}

	/**
	 * Deserialize object from Bukkit configuration file
	 */
	public static DeathBan deserialize(Map<String, Object> args) {
		Date time;
		Location location;
		String deathMessage;
		long discount = 0L;
		
		time = new Date((long) args.get("time"));
		location = (Location) args.get("location");
		deathMessage = (String) args.get("message");
		if(args.containsKey("discount")) discount = (long) args.get("discount");
		return new DeathBan(time, location, deathMessage, discount);
	}

	/**
	 * Generate kick message from ban 
	 */
	public String kickMessage() {
		return String.format(
			"%s%s\n%sat (%d, %d, %d) in %s\nRespawning in %s",
			ChatColor.YELLOW,
			this.deathMessage,
			ChatColor.RESET,
			this.location.getBlockX(),
			this.location.getBlockY(),
			this.location.getBlockZ(),
			environmentDisplayName(this.location),
			formatTimeout()
		);
	}

	/**
	 * Get time remaining on death ban
	 */
	public long timeRemaining() {
		return this.time.getTime() + 259200000 - new Date().getTime();
	}

	/**
	 * Get human readable environment name
	 */
	private String environmentDisplayName(Location location) {
		switch(location.getWorld().getEnvironment()) {
			case NORMAL: return "the Overworld";
			case NETHER: return "the Nether";
			case THE_END: return "the End";
			default: return "an unknown dimension";
		}
	}

	/**
	 * Format time remaining as human readable string
	 */
	private String formatTimeout() {
		long time = timeRemaining();
		long days = time / 86400000;
		long hours = time % 86400000 / 3600000;
		long minutes = time % 3600000 / 60000;
		long seconds = time % 60000 / 1000;
		String daysUnit = days > 1 ? "days" : "day";
		String hoursUnit = hours > 1 ? "hours" : "hour";
		String minutesUnit = minutes > 1 ? "minutes" : "minute";
		String secondsUnit = seconds > 1 ? "seconds" : "second";
		if(days > 0) return String.format("%d %s %d %s", days, daysUnit, hours, hoursUnit);
		if(hours > 0) return String.format("%d %s %d %s", hours, hoursUnit, minutes, minutesUnit);
		if(minutes > 0) return String.format("%d %s %d %s", minutes, minutesUnit, seconds, secondsUnit);
		return String.format("%d %s", seconds, secondsUnit);
	}
}
