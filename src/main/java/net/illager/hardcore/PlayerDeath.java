package net.illager.hardcore;

import java.util.Date;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {
	private HardcorePlugin plugin;
	private DeathBanLog log;

	public PlayerDeath(HardcorePlugin plugin) {
		this.plugin = plugin;
		this.log = plugin.getDeathBanLog();
	}

	@EventHandler
	public void onPlayerLogin(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Date time = new Date();
		Location location = player.getLocation();
		String deathMessage = event.getDeathMessage();
		DeathBan ban = new DeathBan(time, location, deathMessage, 0L);
		log.add(player.getUniqueId(), ban);
		log.save();
		this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable() {
			@Override
			public void run() {
				player.kickPlayer(ban.kickMessage());
			}
		});
	}
}
