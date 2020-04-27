package net.illager.hardcore;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.permission.ChildPermission;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.permission.Permissions;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion.Target;

/**
 * Illager Net plugin that adds hardcore mechanics
 */
@Plugin(name = "Hardcore", version = "1.1")
@Description(value = "Hardcore mechanics")
@ApiVersion(Target.v1_15)
@Permissions({
	@Permission(
		name = "hardcore.revive",
		desc = "Allows use of the revive command",
		defaultValue = PermissionDefault.OP
	),
	@Permission(
		name = "hardcore.*",
		desc = "All hardcore special permissions",
		defaultValue = PermissionDefault.OP,
		children = {
			@ChildPermission( name = "hardcore.revive")
		}
	)
})
@Commands({
	@Command(
		name = "revive",
		desc = "Revive deathbanned player",
		usage = "/revive <username> [<username>...]",
		permission = "hardcore.revive"
	)
})
public class HardcorePlugin extends JavaPlugin {
	private DeathBanLog log;

	@Override
	public void onEnable() {
		ConfigurationSerialization.registerClass(DeathBan.class);
		this.log = new DeathBanLog(this, "deathbans.yml");
		this.getCommand("revive").setExecutor(new ReviveCommand(this));
		this.getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerLogin(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerRespawn(this), this);
	}
	
	@Override
	public void onDisable() {}

	/**
	 * Get the deathban log of this plugin instance
	 */
	public DeathBanLog getDeathBanLog() {
		return this.log;
	}
}
