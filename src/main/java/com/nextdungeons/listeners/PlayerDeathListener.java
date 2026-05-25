package com.nextdungeons.listeners;

import com.nextdungeons.dungeon.DungeonInstance;
import com.nextdungeons.plugin.NextDungeons;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    
    private final NextDungeons plugin;
    
    public PlayerDeathListener(NextDungeons plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        
        if (plugin.getDungeonManager().isInDungeon(player)) {
            DungeonInstance instance = plugin.getDungeonManager().getDungeonInstance(player);
            
            boolean respawn = plugin.getConfig().getBoolean("dungeons.respawn-on-death", true);
            if (respawn && instance != null) {
                event.setKeepInventory(true);
                event.getDrops().clear();
            }
        }
    }
}