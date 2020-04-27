package net.illager.hardcore;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

public class Welcome {
    HardcorePlugin plugin;

    public Welcome(HardcorePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Give the welcome kit to a player.
     * @param player The player to receive the kit.
     */
    public void giveKit(Player player) {
        ItemStack[] kit = { new ItemStack(Material.TOTEM_OF_UNDYING) };
        player.getInventory().addItem(kit);
	}

    /**
     * Tell every other player in the server to say hello to a new player.
     * @param newPlayer The player other players will say hello to.
     */
	public void remindWelcome(Player newPlayer) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!player.getUniqueId().toString().equals(newPlayer.getUniqueId().toString())) {
                player.sendMessage(ChatColor.GOLD + "Welcome " + ChatColor.WHITE + newPlayer.getDisplayName() + ChatColor.GOLD + " to the server!");
            }
        }
    }
    
    /*
    private ItemStack composeBook() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        plugin.reloadConfig();

        meta.setTitle("");
        meta.setAuthor("");
        meta.setGeneration(BookMeta.Generation.TATTERED);

        // Add the pages ...
        for (String page : (List<String>) plugin.getConfig().getList("welcome-book.generation")) {
            meta.addPage(page);
        }

        return book;
    }
    */
}
