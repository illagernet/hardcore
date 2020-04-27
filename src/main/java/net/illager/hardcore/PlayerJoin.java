package net.illager.hardcore;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
	HardcorePlugin plugin;
	RandomSpawner spawner;

	public PlayerJoin(HardcorePlugin plugin) {
		this.plugin = plugin;
		this.spawner = new RandomSpawner(plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(!player.hasPlayedBefore()) {
			Location spawnpoint = spawner.generate();
			player.teleport(spawnpoint);

			Welcome welcome = new Welcome(this.plugin);
			welcome.giveKit(player);
			welcome.remindWelcome(player);
		}
	}
}
