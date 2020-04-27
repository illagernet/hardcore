package net.illager.hardcore;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class RandomSpawner {
	private HardcorePlugin plugin;
	private Logger logger;
	private World mainWorld;
	private Random random = new Random();

	public RandomSpawner(HardcorePlugin plugin) {
		this.plugin = plugin;
		this.logger = plugin.getLogger();
		this.mainWorld = plugin.getServer().getWorlds().get(0);
	}

	/**
	 * Generate a random spawn location
	 */
	public Location generate() {
		for(Integer i = 1; i <= 100; ++i) {
			Location spawnpoint = new Location(
				this.mainWorld,
				random.nextInt(20000) - 9999.5,
				0,
				random.nextInt(20000) - 9999.5
			);
			Chunk chunk = spawnpoint.getChunk();
			if(!chunk.isLoaded()) chunk.load(true);
			spawnpoint.setY(this.mainWorld.getHighestBlockYAt(spawnpoint));
			if(isSafeBlock(spawnpoint.getBlock().getRelative(0, -1, 0))) {
				this.logger.log(
					Level.INFO,
					"Found safe spawnpoint after " + i + (i > 1 ? " iterations" : " iteration")
				);
				return spawnpoint;
			};
		}
		this.logger.log(Level.SEVERE, "Failed to find safe random spawnpoint! Defaulting to world spawn");
		return this.plugin.getServer().getWorlds().get(0).getSpawnLocation();
	}

	/**
	 * Check if block is safe to spawn on
	 */
	private boolean isSafeBlock(Block block) {
		switch(block.getType()) {
			case BROWN_TERRACOTTA:
			case COARSE_DIRT:
			case COBBLESTONE:
			case DIRT:
			case GRASS_BLOCK:
			case GRAVEL:
			case ICE:
			case LIGHT_GRAY_TERRACOTTA:
			case ORANGE_TERRACOTTA:
			case PACKED_ICE:
			case PODZOL:
			case RED_SAND:
			case RED_TERRACOTTA:
			case SAND:
			case SNOW:
			case STONE:
			case TERRACOTTA:
			case WHITE_TERRACOTTA:
			case YELLOW_TERRACOTTA: return true;
			default: return false;
		}
	}
}
