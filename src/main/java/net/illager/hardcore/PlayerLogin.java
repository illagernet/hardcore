package net.illager.hardcore;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLogin implements Listener {
	DeathBanLog log;
	
	public PlayerLogin(HardcorePlugin plugin) {
		this.log = plugin.getDeathBanLog();
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		DeathBan ban;
		if(!this.log.has(uuid)) return;
		ban = this.log.get(uuid);
		if(ban.timeRemaining() <= 0) {
			log.remove(uuid);
			return;
		}
		event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ban.kickMessage());
	}
}
