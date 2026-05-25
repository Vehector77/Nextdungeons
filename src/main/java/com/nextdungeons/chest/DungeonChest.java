package com.nextdungeons.chest;

import com.nextdungeons.dungeon.DungeonInstance;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DungeonChest {
    
    private final Location location;
    private final DungeonInstance instance;
    private boolean opened;
    private long lastRestockTime;
    private String lootTableId;
    
    public DungeonChest(Location location, DungeonInstance instance) {
        this.location = location;
        this.instance = instance;
        this.opened = false;
        this.lastRestockTime = 0;
        this.lootTableId = "default";
    }
    
    public void fill(List<ItemStack> items) {
        if (location.getBlock().getState() instanceof Chest chest) {
            Inventory inv = chest.getInventory();
            inv.clear();
            
            for (ItemStack item : items) {
                if (item != null && item.getType() != Material.AIR) {
                    inv.addItem(item);
                }
            }
            
            chest.update();
        }
    }
    
    public void open() {
        this.opened = true;
    }
    
    public void reset() {
        this.opened = false;
        this.lastRestockTime = System.currentTimeMillis();
    }
    
    public Location getLocation() {
        return location;
    }
    
    public boolean isOpened() {
        return opened;
    }
    
    public String getLootTableId() {
        return lootTableId;
    }
    
    public void setLootTableId(String lootTableId) {
        this.lootTableId = lootTableId;
    }
    
    public DungeonInstance getInstance() {
        return instance;
    }
}