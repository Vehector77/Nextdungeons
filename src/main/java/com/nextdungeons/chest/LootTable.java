package com.nextdungeons.chest;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class LootTable {
    
    private final String id;
    private final List<LootEntry> entries;
    
    public LootTable(String id) {
        this.id = id;
        this.entries = new ArrayList<>();
    }
    
    public void addItem(Map<?, ?> itemData) {
        try {
            String materialName = itemData.get("material").toString();
            Material material = Material.valueOf(materialName.toUpperCase());
            
            int amount = itemData.containsKey("amount") ? 
                Integer.parseInt(itemData.get("amount").toString()) : 1;
            double chance = itemData.containsKey("chance") ? 
                Double.parseDouble(itemData.get("chance").toString()) : 100.0;
            
            ItemStack item = new ItemStack(material, amount);
            entries.add(new LootEntry(item, chance));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addEntry(LootEntry entry) {
        entries.add(entry);
    }
    
    public List<ItemStack> generateLoot() {
        List<ItemStack> loot = new ArrayList<>();
        Random random = new Random();
        
        for (LootEntry entry : entries) {
            if (random.nextDouble() * 100 < entry.getChance()) {
                loot.add(entry.getItem().clone());
            }
        }
        
        return loot;
    }
    
    public String getId() {
        return id;
    }
    
    public List<LootEntry> getEntries() {
        return new ArrayList<>(entries);
    }
    
    public static class LootEntry {
        private final ItemStack item;
        private final double chance;
        
        public LootEntry(ItemStack item, double chance) {
            this.item = item;
            this.chance = chance;
        }
        
        public ItemStack getItem() {
            return item;
        }
        
        public double getChance() {
            return chance;
        }
    }
}