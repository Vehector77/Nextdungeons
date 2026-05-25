package com.nextdungeons.gui;

import com.nextdungeons.dungeon.DungeonData;
import com.nextdungeons.plugin.NextDungeons;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIManager implements Listener {
    
    private final NextDungeons plugin;
    private final Map<Player, String> openGuis;
    
    public GUIManager(NextDungeons plugin) {
        this.plugin = plugin;
        this.openGuis = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    public void openDungeonMenu(Player player) {
        String title = plugin.getConfig().getString("gui.title", "Dungeons Menu");
        title = org.bukkit.ChatColor.translateAlternateColorCodes('&', title);
        
        int rows = plugin.getConfig().getInt("gui.rows", 3);
        Inventory inv = Bukkit.createInventory(null, rows * 9, title);
        
        boolean fillEmpty = plugin.getConfig().getBoolean("gui.fill-empty", true);
        if (fillEmpty) {
            ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta fillerMeta = filler.getItemMeta();
            if (fillerMeta != null) {
                fillerMeta.setDisplayName(" ");
                filler.setItemMeta(fillerMeta);
            }
            
            for (int i = 0; i < inv.getSize(); i++) {
                inv.setItem(i, filler);
            }
        }
        
        Map<String, DungeonData> dungeons = plugin.getDungeonManager().getDungeonTemplates();
        int slot = 10;
        
        for (DungeonData data : dungeons.values()) {
            if (!data.isEnabled()) continue;
            
            ItemStack item = new ItemStack(Material.NETHER_STAR);
            ItemMeta meta = item.getItemMeta();
            
            if (meta != null) {
                meta.setDisplayName(org.bukkit.ChatColor.GOLD + data.getDisplayName());
                
                List<String> lore = new ArrayList<>();
                if (data.getDescription() != null) {
                    for (String line : data.getDescription()) {
                        lore.add(org.bukkit.ChatColor.GRAY + line);
                    }
                }
                lore.add("");
                lore.add(org.bukkit.ChatColor.YELLOW + "Difficulty: " + org.bukkit.ChatColor.WHITE + data.getDifficulty());
                lore.add(org.bukkit.ChatColor.YELLOW + "Players: " + org.bukkit.ChatColor.WHITE + 
                    data.getMinPlayers() + "-" + data.getMaxPlayers());
                lore.add(org.bukkit.ChatColor.YELLOW + "Time Limit: " + org.bukkit.ChatColor.WHITE + 
                    data.getTimeLimit() + "s");
                lore.add("");
                lore.add(org.bukkit.ChatColor.GREEN + "Click to enter!");
                
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            
            inv.setItem(slot, item);
            slot++;
            
            if (slot % 9 >= 7) {
                slot += 3;
            }
        }
        
        player.openInventory(inv);
        openGuis.put(player, "dungeon_menu");
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!openGuis.containsKey(player)) return;
        
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
        if (clicked.getType() == Material.GRAY_STAINED_GLASS_PANE) return;
        
        if (clicked.getType() == Material.NETHER_STAR) {
            ItemMeta meta = clicked.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                String displayName = org.bukkit.ChatColor.stripColor(meta.getDisplayName());
                
                Map<String, DungeonData> dungeons = plugin.getDungeonManager().getDungeonTemplates();
                for (DungeonData data : dungeons.values()) {
                    if (data.getDisplayName().equals(displayName)) {
                        player.closeInventory();
                        plugin.getDungeonManager().createDungeonInstance(player, data.getId());
                        break;
                    }
                }
            }
        }
        
        openGuis.remove(player);
    }
}