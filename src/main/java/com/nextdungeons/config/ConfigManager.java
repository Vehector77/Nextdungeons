package com.nextdungeons.config;

import com.nextdungeons.plugin.NextDungeons;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    
    private final NextDungeons plugin;
    private final Map<String, FileConfiguration> configs;
    private final Map<String, File> configFiles;
    
    public ConfigManager(NextDungeons plugin) {
        this.plugin = plugin;
        this.configs = new HashMap<>();
        this.configFiles = new HashMap<>();
    }
    
    public void loadConfigs() {
        plugin.saveDefaultConfig();
        configs.put("config", plugin.getConfig());
        
        createCustomConfig("dungeons.yml");
        createCustomConfig("loot.yml");
        createCustomConfig("messages.yml");
    }
    
    private void createCustomConfig(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        configs.put(fileName.replace(".yml", ""), config);
        configFiles.put(fileName.replace(".yml", ""), file);
    }
    
    public FileConfiguration getConfig() {
        return configs.get("config");
    }
    
    public FileConfiguration getConfig(String name) {
        return configs.getOrDefault(name, null);
    }
    
    public void saveConfig(String name) {
        FileConfiguration config = configs.get(name);
        File file = configFiles.get(name);
        
        if (config != null && file != null) {
            try {
                config.save(file);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not save " + name + ".yml");
                e.printStackTrace();
            }
        }
    }
    
    public void reloadConfigs() {
        plugin.reloadConfig();
        configs.put("config", plugin.getConfig());
        
        for (Map.Entry<String, File> entry : configFiles.entrySet()) {
            configs.put(entry.getKey(), YamlConfiguration.loadConfiguration(entry.getValue()));
        }
    }
}