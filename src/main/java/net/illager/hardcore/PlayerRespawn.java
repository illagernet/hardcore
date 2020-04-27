package net.illager.hardcore;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener {
	HardcorePlugin plugin;
	RandomSpawner spawner;

	public PlayerRespawn(HardcorePlugin plugin) {
		this.plugin = plugin;
		this.spawner = new RandomSpawner(plugin);
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if(!event.isBedSpawn()) {
			Location spawnpoint = spawner.generate();
			event.setRespawnLocation(spawnpoint);
		}
	}
}
