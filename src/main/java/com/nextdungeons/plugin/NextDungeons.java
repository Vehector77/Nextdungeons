package com.nextdungeons.plugin;

import com.nextdungeons.commands.*;
import com.nextdungeons.config.ConfigManager;
import com.nextdungeons.dungeon.DungeonManager;
import com.nextdungeons.party.PartyManager;
import com.nextdungeons.chest.ChestManager;
import com.nextdungeons.hooks.*;
import com.nextdungeons.listeners.*;
import com.nextdungeons.gui.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class NextDungeons extends JavaPlugin {
    
    private static NextDungeons instance;
    private ConfigManager configManager;
    private DungeonManager dungeonManager;
    private PartyManager partyManager;
    private ChestManager chestManager;
    private GUIManager guiManager;
    
    private VaultHook vaultHook;
    private PlaceholderHook placeholderHook;
    private MythicMobsHook mythicMobsHook;
    private CMIHook cmiHook;
    private FAWEHook faweHook;
    
    @Override
    public void onEnable() {
        instance = this;
        
        getLogger().info("========================================" );
        getLogger().info("  NextDungeons v1.0.0 - Starting...");
        getLogger().info("========================================");
        
        configManager = new ConfigManager(this);
        configManager.loadConfigs();
        
        setupWorld();
        
        dungeonManager = new DungeonManager(this);
        partyManager = new PartyManager(this);
        chestManager = new ChestManager(this);
        guiManager = new GUIManager(this);
        
        setupHooks();
        
        registerCommands();
        registerListeners();
        
        getLogger().info("========================================");
        getLogger().info("  NextDungeons successfully enabled!");
        getLogger().info("========================================");
    }
    
    @Override
    public void onDisable() {
        if (dungeonManager != null) {
            dungeonManager.shutdown();
        }
        
        getLogger().info("NextDungeons has been disabled!");
    }
    
    private void setupWorld() {
        String worldName = configManager.getConfig().getString("general.dungeon-world", "dungeons_world");
        boolean autoCreate = configManager.getConfig().getBoolean("general.auto-create-world", true);
        
        World world = Bukkit.getWorld(worldName);
        if (world == null && autoCreate) {
            getLogger().info("Creating dungeon world: " + worldName);
            WorldCreator creator = new WorldCreator(worldName);
            creator.environment(World.Environment.NORMAL);
            creator.generateStructures(false);
            world = creator.createWorld();
            
            if (world != null) {
                world.setAutoSave(false);
                getLogger().info("Dungeon world created successfully!");
            }
        }
    }
    
    private void setupHooks() {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            vaultHook = new VaultHook(this);
            if (vaultHook.setup()) {
                getLogger().info("Hooked into Vault!");
            }
        }
        
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderHook = new PlaceholderHook(this);
            placeholderHook.register();
            getLogger().info("Hooked into PlaceholderAPI!");
        }
        
        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            mythicMobsHook = new MythicMobsHook(this);
            getLogger().info("Hooked into MythicMobs!");
        }
        
        if (Bukkit.getPluginManager().getPlugin("CMI") != null) {
            cmiHook = new CMIHook(this);
            getLogger().info("Hooked into CMI!");
        }
        
        if (Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit") != null) {
            faweHook = new FAWEHook(this);
            getLogger().info("Hooked into FastAsyncWorldEdit!");
        }
    }
    
    private void registerCommands() {
        getCommand("dungeons").setExecutor(new DungeonCommand(this));
        getCommand("dungeonadmin").setExecutor(new DungeonAdminCommand(this));
        getCommand("party").setExecutor(new PartyCommand(this));
    }
    
    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ChestListener(this), this);
        Bukkit.getPluginManager().registerEvents(new DungeonListener(this), this);
    }
    
    public static NextDungeons getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public DungeonManager getDungeonManager() {
        return dungeonManager;
    }
    
    public PartyManager getPartyManager() {
        return partyManager;
    }
    
    public ChestManager getChestManager() {
        return chestManager;
    }
    
    public GUIManager getGuiManager() {
        return guiManager;
    }
    
    public VaultHook getVaultHook() {
        return vaultHook;
    }
    
    public PlaceholderHook getPlaceholderHook() {
        return placeholderHook;
    }
    
    public MythicMobsHook getMythicMobsHook() {
        return mythicMobsHook;
    }
    
    public CMIHook getCmiHook() {
        return cmiHook;
    }
    
    public FAWEHook getFaweHook() {
        return faweHook;
    }
}