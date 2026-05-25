package com.nextdungeons.dungeon;

import com.nextdungeons.plugin.NextDungeons;
import com.nextdungeons.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DungeonManager {
    
    private final NextDungeons plugin;
    private final Map<UUID, DungeonInstance> activeDungeons;
    private final Map<UUID, Long> cooldowns;
    private final Map<String, DungeonData> dungeonTemplates;
    
    public DungeonManager(NextDungeons plugin) {
        this.plugin = plugin;
        this.activeDungeons = new ConcurrentHashMap<>();
        this.cooldowns = new ConcurrentHashMap<>();
        this.dungeonTemplates = new HashMap<>();
        
        loadDungeonTemplates();
        startTimerTask();
    }
    
    private void loadDungeonTemplates() {
        var dungeonsConfig = plugin.getConfigManager().getConfig("dungeons");
        if (dungeonsConfig != null && dungeonsConfig.contains("dungeons")) {
            var section = dungeonsConfig.getConfigurationSection("dungeons");
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    DungeonData data = new DungeonData(key);
                    data.setDisplayName(section.getString(key + ".name", key));
                    data.setDescription(section.getStringList(key + ".description"));
                    data.setDifficulty(section.getString(key + ".difficulty", "NORMAL"));
                    data.setMinPlayers(section.getInt(key + ".min-players", 1));
                    data.setMaxPlayers(section.getInt(key + ".max-players", 5));
                    data.setTimeLimit(section.getInt(key + ".time-limit", 1800));
                    data.setCooldown(section.getInt(key + ".cooldown", 300));
                    data.setSchematicName(section.getString(key + ".schematic", key));
                    data.setEnabled(section.getBoolean(key + ".enabled", true));
                    
                    dungeonTemplates.put(key, data);
                }
            }
        }
        
        plugin.getLogger().info("Loaded " + dungeonTemplates.size() + " dungeon templates");
    }
    
    public boolean createDungeonInstance(Player player, String dungeonId) {
        if (activeDungeons.containsKey(player.getUniqueId())) {
            MessageUtils.sendMessage(player, plugin.getConfig().getString("messages.already-in-dungeon"));
            return false;
        }
        
        if (!dungeonTemplates.containsKey(dungeonId)) {
            MessageUtils.sendMessage(player, plugin.getConfig().getString("messages.dungeon-not-found"));
            return false;
        }
        
        if (isOnCooldown(player, dungeonId)) {
            long remaining = getRemainingCooldown(player, dungeonId);
            String message = plugin.getConfig().getString("messages.cooldown")
                .replace("%time%", formatTime(remaining));
            MessageUtils.sendMessage(player, message);
            return false;
        }
        
        DungeonData template = dungeonTemplates.get(dungeonId);
        DungeonInstance instance = new DungeonInstance(plugin, player, template);
        
        if (instance.generate()) {
            activeDungeons.put(player.getUniqueId(), instance);
            instance.teleportPlayer(player);
            MessageUtils.sendMessage(player, plugin.getConfig().getString("messages.dungeon-enter"));
            
            setCooldown(player, dungeonId);
            return true;
        }
        
        return false;
    }
    
    public void exitDungeon(Player player) {
        DungeonInstance instance = activeDungeons.remove(player.getUniqueId());
        if (instance != null) {
            instance.cleanup();
            MessageUtils.sendMessage(player, plugin.getConfig().getString("messages.dungeon-exit"));
        }
    }
    
    public DungeonInstance getDungeonInstance(Player player) {
        return activeDungeons.get(player.getUniqueId());
    }
    
    public boolean isInDungeon(Player player) {
        return activeDungeons.containsKey(player.getUniqueId());
    }
    
    private void startTimerTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (DungeonInstance instance : new ArrayList<>(activeDungeons.values())) {
                    instance.tick();
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }
    
    private boolean isOnCooldown(Player player, String dungeonId) {
        if (!plugin.getConfig().getBoolean("cooldowns.enabled", true)) {
            return false;
        }
        
        String key = player.getUniqueId() + ":" + dungeonId;
        Long cooldownEnd = cooldowns.get(UUID.nameUUIDFromBytes(key.getBytes()));
        return cooldownEnd != null && System.currentTimeMillis() < cooldownEnd;
    }
    
    private long getRemainingCooldown(Player player, String dungeonId) {
        String key = player.getUniqueId() + ":" + dungeonId;
        Long cooldownEnd = cooldowns.get(UUID.nameUUIDFromBytes(key.getBytes()));
        if (cooldownEnd == null) return 0;
        return Math.max(0, cooldownEnd - System.currentTimeMillis());
    }
    
    private void setCooldown(Player player, String dungeonId) {
        DungeonData template = dungeonTemplates.get(dungeonId);
        int cooldownSeconds = template != null ? template.getCooldown() : 
            plugin.getConfig().getInt("cooldowns.global", 300);
        
        String key = player.getUniqueId() + ":" + dungeonId;
        cooldowns.put(UUID.nameUUIDFromBytes(key.getBytes()), 
            System.currentTimeMillis() + (cooldownSeconds * 1000L));
    }
    
    private String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
    
    public Map<String, DungeonData> getDungeonTemplates() {
        return new HashMap<>(dungeonTemplates);
    }
    
    public void registerDungeon(DungeonData data) {
        dungeonTemplates.put(data.getId(), data);
        
        var config = plugin.getConfigManager().getConfig("dungeons");
        if (config != null) {
            config.set("dungeons." + data.getId() + ".name", data.getDisplayName());
            config.set("dungeons." + data.getId() + ".schematic", data.getSchematicName());
            config.set("dungeons." + data.getId() + ".enabled", data.isEnabled());
            plugin.getConfigManager().saveConfig("dungeons");
        }
    }
    
    public void shutdown() {
        for (DungeonInstance instance : new ArrayList<>(activeDungeons.values())) {
            instance.cleanup();
        }
        activeDungeons.clear();
    }
}