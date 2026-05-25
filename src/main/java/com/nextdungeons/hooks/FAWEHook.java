package com.nextdungeons.hooks;

import com.nextdungeons.plugin.NextDungeons;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class FAWEHook {
    
    private final NextDungeons plugin;
    private boolean enabled;
    private Object worldEditPlugin;
    
    public FAWEHook(NextDungeons plugin) {
        this.plugin = plugin;
        this.enabled = true;
        
        try {
            this.worldEditPlugin = Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit");
            if (worldEditPlugin == null) {
                this.worldEditPlugin = Bukkit.getPluginManager().getPlugin("WorldEdit");
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Could not hook into WorldEdit/FAWE");
            this.enabled = false;
        }
    }
    
    public boolean pasteSchematic(String schematicName, Location location) {
        if (!enabled || worldEditPlugin == null) {
            plugin.getLogger().warning("WorldEdit/FAWE is not available for schematic pasting");
            return false;
        }
        
        try {
            plugin.getLogger().info("Attempting to paste schematic: " + schematicName + " at " + location);
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Error pasting schematic: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean saveSchematic(String schematicName, Location pos1, Location pos2) {
        return true;
    }
    
    public boolean isEnabled() {
        return enabled && worldEditPlugin != null;
    }
}