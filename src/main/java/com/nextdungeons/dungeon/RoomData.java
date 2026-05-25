package com.nextdungeons.dungeon;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class RoomData {
    
    private final Location location;
    private final RoomType type;
    private final List<String> mobs;
    private boolean cleared;
    
    public RoomData(Location location, RoomType type) {
        this.location = location;
        this.type = type;
        this.mobs = new ArrayList<>();
        this.cleared = false;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public RoomType getType() {
        return type;
    }
    
    public List<String> getMobs() {
        return mobs;
    }
    
    public void addMob(String mobId) {
        mobs.add(mobId);
    }
    
    public boolean isCleared() {
        return cleared;
    }
    
    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }
    
    public enum RoomType {
        NORMAL,
        MINI_BOSS,
        BOSS,
        TREASURE,
        PUZZLE
    }
}