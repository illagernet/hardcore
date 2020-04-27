package net.illager.hardcore;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.logging.Level;

import com.google.gson.JsonParser;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReviveCommand implements CommandExecutor {
	private DeathBanLog log;

	public ReviveCommand(HardcorePlugin plugin) {
		this.log = plugin.getDeathBanLog();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equals("revive") && args.length > 0) {
			for(String username : args) {
				UUID uuid = fetchUUID(username);
				if(uuid == null) {
					sender.sendMessage("Unable to fetch UUID of " + username);
					continue;
				}
				if(!this.log.has(uuid)) {
					sender.sendMessage(username + " is not death banned");
					continue;
				}
				this.log.remove(uuid);
				sender.sendMessage(username + " was revived");
			}
			return true;
		}
		return false;
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
			Bukkit.getLogger().log(Level.SEVERE, exception.getMessage());
			return null;
		}
	}
}
