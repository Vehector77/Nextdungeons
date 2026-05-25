package com.nextdungeons.listeners;

import com.nextdungeons.plugin.NextDungeons;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    
    private final NextDungeons plugin;
    
    public PlayerJoinListener(NextDungeons plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    }
}