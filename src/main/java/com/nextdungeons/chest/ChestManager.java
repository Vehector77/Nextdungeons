package com.nextdungeons.chest;

import com.nextdungeons.plugin.NextDungeons;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ChestManager {
    
    private final NextDungeons plugin;
    private final Map<String, LootTable> lootTables;
    
    public ChestManager(NextDungeons plugin) {
        this.plugin = plugin;
        this.lootTables = new HashMap<>();
        
        loadLootTables();
    }
    
    private void loadLootTables() {
        var lootConfig = plugin.getConfigManager().getConfig("loot");
        if (lootConfig != null && lootConfig.contains("loot-tables")) {
            ConfigurationSection section = lootConfig.getConfigurationSection("loot-tables");
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    LootTable table = new LootTable(key);
                    
                    ConfigurationSection tableSection = section.getConfigurationSection(key);
                    if (tableSection != null && tableSection.contains("items")) {
                        List<Map<?, ?>> itemsList = tableSection.getMapList("items");
                        for (Map<?, ?> itemMap : itemsList) {
                            table.addItem(itemMap);
                        }
                    }
                    
                    lootTables.put(key, table);
                }
            }
        }
        
        if (lootTables.isEmpty()) {
            createDefaultLootTable();
        }
        
        plugin.getLogger().info("Loaded " + lootTables.size() + " loot tables");
    }
    
    private void createDefaultLootTable() {
        LootTable defaultTable = new LootTable("default");
        lootTables.put("default", defaultTable);
    }
    
    public List<ItemStack> generateLoot(String tableId) {
        LootTable table = lootTables.getOrDefault(tableId, lootTables.get("default"));
        return table != null ? table.generateLoot() : new ArrayList<>();
    }
    
    public LootTable getLootTable(String id) {
        return lootTables.get(id);
    }
    
    public void registerLootTable(LootTable table) {
        lootTables.put(table.getId(), table);
    }
    
    public Map<String, LootTable> getAllLootTables() {
        return new HashMap<>(lootTables);
    }
}