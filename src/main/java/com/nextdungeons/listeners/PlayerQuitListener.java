package com.nextdungeons.listeners;

import com.nextdungeons.plugin.NextDungeons;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    
    private final NextDungeons plugin;
    
    public PlayerQuitListener(NextDungeons plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getDungeonManager().isInDungeon(event.getPlayer())) {
            plugin.getDungeonManager().exitDungeon(event.getPlayer());
        }
        
        if (plugin.getPartyManager().isInParty(event.getPlayer())) {
            plugin.getPartyManager().leaveParty(event.getPlayer());
        }
    }
}