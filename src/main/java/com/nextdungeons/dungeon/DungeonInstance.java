package com.nextdungeons.dungeon;

import com.nextdungeons.plugin.NextDungeons;
import com.nextdungeons.chest.DungeonChest;
import com.nextdungeons.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.*;

public class DungeonInstance {
    
    private final NextDungeons plugin;
    private final UUID ownerId;
    private final DungeonData template;
    private final UUID instanceId;
    private Location spawnLocation;
    private Location exitLocation;
    private long startTime;
    private int timeLimit;
    private boolean completed;
    private final Set<UUID> participants;
    private final List<DungeonChest> chests;
    private final Map<Location, RoomData> rooms;
    
    public DungeonInstance(NextDungeons plugin, Player owner, DungeonData template) {
        this.plugin = plugin;
        this.ownerId = owner.getUniqueId();
        this.template = template;
        this.instanceId = UUID.randomUUID();
        this.exitLocation = owner.getLocation().clone();
        this.startTime = System.currentTimeMillis();
        this.timeLimit = template.getTimeLimit();
        this.completed = false;
        this.participants = new HashSet<>();
        this.chests = new ArrayList<>();
        this.rooms = new HashMap<>();
        
        participants.add(owner.getUniqueId());
    }
    
    public boolean generate() {
        try {
            World world = Bukkit.getWorld(plugin.getConfig().getString("general.dungeon-world", "dungeons_world"));
            if (world == null) {
                plugin.getLogger().severe("Dungeon world not found!");
                return false;
            }
            
            Random random = new Random();
            int x = random.nextInt(10000) - 5000;
            int z = random.nextInt(10000) - 5000;
            int y = 100;
            
            spawnLocation = new Location(world, x, y, z);
            
            if (plugin.getFaweHook() != null && plugin.getFaweHook().isEnabled()) {
                boolean pasted = plugin.getFaweHook().pasteSchematic(template.getSchematicName(), spawnLocation);
                if (!pasted) {
                    plugin.getLogger().warning("Failed to paste schematic for dungeon: " + template.getId());
                    return false;
                }
                
                scanForChests();
                scanForRooms();
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void scanForChests() {
        if (spawnLocation == null) return;
        
        World world = spawnLocation.getWorld();
        int radius = 100;
        
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location loc = spawnLocation.clone().add(x, y, z);
                    Block block = world.getBlockAt(loc);
                    
                    if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
                        if (block.getState() instanceof Chest) {
                            DungeonChest dChest = new DungeonChest(loc, this);
                            chests.add(dChest);
                        }
                    }
                }
            }
        }
        
        plugin.getLogger().info("Found " + chests.size() + " chests in dungeon instance");
    }
    
    private void scanForRooms() {
    }
    
    public void teleportPlayer(Player player) {
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
            participants.add(player.getUniqueId());
        }
    }
    
    public void tick() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        long remaining = timeLimit - elapsed;
        
        if (remaining <= 0) {
            timeout();
            return;
        }
        
        int warningTime = plugin.getConfig().getInt("time-limits.warning-time", 300);
        if (remaining == warningTime) {
            broadcastMessage(plugin.getConfig().getString("messages.time-warning")
                .replace("%time%", formatTime(remaining)));
        }
    }
    
    private void timeout() {
        broadcastMessage(plugin.getConfig().getString("messages.time-expired"));
        cleanup();
    }
    
    public void complete() {
        if (completed) return;
        
        completed = true;
        broadcastMessage(plugin.getConfig().getString("messages.dungeon-complete"));
        
        for (UUID uuid : participants) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                giveRewards(player);
            }
        }
        
        Bukkit.getScheduler().runTaskLater(plugin, this::cleanup, 100L);
    }
    
    private void giveRewards(Player player) {
        if (plugin.getVaultHook() != null && plugin.getVaultHook().hasEconomy()) {
            double amount = 100.0;
            plugin.getVaultHook().getEconomy().depositPlayer(player, amount);
        }
        
        int exp = 500;
        player.giveExp(exp);
    }
    
    public void cleanup() {
        for (UUID uuid : new ArrayList<>(participants)) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.teleport(exitLocation);
            }
        }
        
        if (plugin.getFaweHook() != null && spawnLocation != null) {
            clearArea(spawnLocation, 100);
        }
        
        participants.clear();
        chests.clear();
        rooms.clear();
    }
    
    private void clearArea(Location center, int radius) {
        World world = center.getWorld();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location loc = center.clone().add(x, y, z);
                    world.getBlockAt(loc).setType(Material.AIR);
                }
            }
        }
    }
    
    private void broadcastMessage(String message) {
        for (UUID uuid : participants) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                MessageUtils.sendMessage(player, message);
            }
        }
    }
    
    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }
    
    public UUID getInstanceId() {
        return instanceId;
    }
    
    public UUID getOwnerId() {
        return ownerId;
    }
    
    public DungeonData getTemplate() {
        return template;
    }
    
    public Set<UUID> getParticipants() {
        return new HashSet<>(participants);
    }
    
    public List<DungeonChest> getChests() {
        return new ArrayList<>(chests);
    }
    
    public Location getSpawnLocation() {
        return spawnLocation;
    }
    
    public boolean isCompleted() {
        return completed;
    }
}