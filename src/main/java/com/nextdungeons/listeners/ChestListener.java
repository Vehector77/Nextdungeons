package com.nextdungeons.listeners;

import com.nextdungeons.chest.DungeonChest;
import com.nextdungeons.dungeon.DungeonInstance;
import com.nextdungeons.plugin.NextDungeons;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ChestListener implements Listener {
    
    private final NextDungeons plugin;
    
    public ChestListener(NextDungeons plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onChestOpen(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (!(event.getClickedBlock().getState() instanceof Chest)) return;
        
        Player player = event.getPlayer();
        if (!plugin.getDungeonManager().isInDungeon(player)) return;
        
        DungeonInstance instance = plugin.getDungeonManager().getDungeonInstance(player);
        if (instance == null) return;
        
        Location chestLoc = event.getClickedBlock().getLocation();
        
        for (DungeonChest dChest : instance.getChests()) {
            if (dChest.getLocation().equals(chestLoc)) {
                if (!dChest.isOpened()) {
                    List<ItemStack> loot = plugin.getChestManager().generateLoot(dChest.getLootTableId());
                    dChest.fill(loot);
                    dChest.open();
                }
                break;
            }
        }
    }
}