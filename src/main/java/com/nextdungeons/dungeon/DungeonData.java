package com.nextdungeons.dungeon;

import java.util.List;

public class DungeonData {
    
    private final String id;
    private String displayName;
    private List<String> description;
    private String difficulty;
    private int minPlayers;
    private int maxPlayers;
    private int timeLimit;
    private int cooldown;
    private String schematicName;
    private boolean enabled;
    
    public DungeonData(String id) {
        this.id = id;
        this.displayName = id;
        this.difficulty = "NORMAL";
        this.minPlayers = 1;
        this.maxPlayers = 5;
        this.timeLimit = 1800;
        this.cooldown = 300;
        this.enabled = true;
    }
    
    public String getId() {
        return id;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public List<String> getDescription() {
        return description;
    }
    
    public void setDescription(List<String> description) {
        this.description = description;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public int getMinPlayers() {
        return minPlayers;
    }
    
    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }
    
    public int getMaxPlayers() {
        return maxPlayers;
    }
    
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
    
    public int getTimeLimit() {
        return timeLimit;
    }
    
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }
    
    public int getCooldown() {
        return cooldown;
    }
    
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
    
    public String getSchematicName() {
        return schematicName;
    }
    
    public void setSchematicName(String schematicName) {
        this.schematicName = schematicName;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}