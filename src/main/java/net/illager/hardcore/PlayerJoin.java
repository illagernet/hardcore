package net.illager.hardcore;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
	private HardcorePlugin plugin;
	private RandomSpawner spawner;
	private Welcomer welcomer;

	public PlayerJoin(HardcorePlugin plugin) {
		this.plugin = plugin;
		this.spawner = new RandomSpawner(plugin);
		this.welcomer = new Welcomer(this.plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(!player.hasPlayedBefore()) {
			Location spawnpoint = spawner.generate();
			player.teleport(spawnpoint);
			welcomer.giveKit(player);
			welcomer.remindWelcome(player);
		}
	}
}
