package net.illager.hardcore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class ReviveCommand implements CommandExecutor {
	private DeathBanLog log;

	public ReviveCommand(HardcorePlugin plugin) {
		this.log = plugin.getDeathBanLog();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equals("revive") && args.length > 0) {
			for(String username : args) {
				if(!this.log.has(username)) {
					sender.sendMessage(ChatColor.RED + "Count not revive " + username);
					continue;
				}
				this.log.remove(username);
				sender.sendMessage(username + " was revived");
			}
			return true;
		}
		return false;
	}
}
