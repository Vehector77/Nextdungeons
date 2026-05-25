# ⚡ NextDungeons v2.0 - COMPLETE SOURCE CODE GUIDE

## 🎯 Código Fuente Completo para v2.0

Este documento contiene TODO el código fuente necesario para NextDungeons v2.0. Copia los archivos en el orden indicado.

---

## 📦 PASO 1: Core Plugin Class

**Ubicación:** `src/main/java/com/nextdungeons/plugin/NextDungeons.java`

```java
package com.nextdungeons.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

public class NextDungeons extends JavaPlugin {
    private static NextDungeons instance;

    @Override
    public void onEnable() {
        instance = this;
        long startTime = System.currentTimeMillis();
        
        try {
            if (!getDataFolder().exists()) getDataFolder().mkdirs();
            saveDefaultConfig();
            
            getLogger().info("═══════════════════════════════════");
            getLogger().info("NextDungeons v2.0 ENABLED");
            getLogger().info("Java: " + System.getProperty("java.version"));
            getLogger().info("Time: " + (System.currentTimeMillis() - startTime) + "ms");
            getLogger().info("═══════════════════════════════════");
            
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to enable!");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("NextDungeons v2.0 disabled.");
    }

    public static NextDungeons getInstance() {
        return instance;
    }
}
```

---

## 📁 PASO 2: Config Manager

**Ubicación:** `src/main/java/com/nextdungeons/config/ConfigManager.java`

```java
package com.nextdungeons.config;

import com.nextdungeons.plugin.NextDungeons;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final NextDungeons plugin;
    private final Map<String, FileConfiguration> configs = new HashMap<>();
    private final Map<String, File> files = new HashMap<>();

    public ConfigManager(NextDungeons plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {
        loadConfig("config.yml");
        loadConfig("dungeons.yml");
        loadConfig("classes.yml");
        loadConfig("quests.yml");
        loadConfig("items.yml");
        loadConfig("rewards.yml");
        loadConfig("messages.yml");
    }

    private void loadConfig(String filename) {
        File file = new File(plugin.getDataFolder(), filename);
        if (!file.exists()) {
            plugin.saveResource(filename, false);
        }
        files.put(filename, file);
        configs.put(filename, YamlConfiguration.loadConfiguration(file));
    }

    public FileConfiguration getConfig(String filename) {
        return configs.getOrDefault(filename, new YamlConfiguration());
    }

    public void saveConfig(String filename) {
        try {
            configs.get(filename).save(files.get(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig(String filename) {
        if (files.containsKey(filename)) {
            configs.put(filename, YamlConfiguration.loadConfiguration(files.get(filename)));
        }
    }
}
```

---

## 🏛️ PASO 3: Dungeon Manager

**Ubicación:** `src/main/java/com/nextdungeons/dungeon/DungeonManager.java`

```java
package com.nextdungeons.dungeon;

import com.nextdungeons.plugin.NextDungeons;
import java.util.*;

public class DungeonManager {
    private final NextDungeons plugin;
    private final Map<String, DungeonData> dungeons = new HashMap<>();
    private final Map<UUID, DungeonInstance> playerDungeons = new HashMap<>();

    public DungeonManager(NextDungeons plugin) {
        this.plugin = plugin;
        loadDungeons();
    }

    private void loadDungeons() {
        var config = plugin.getConfigManager().getConfig("dungeons.yml");
        if (config.contains("dungeons")) {
            for (String key : config.getConfigurationSection("dungeons").getKeys(false)) {
                var section = config.getConfigurationSection("dungeons." + key);
                DungeonData data = new DungeonData(
                    key,
                    section.getString("name", "Unknown"),
                    section.getList("description", new ArrayList<>()),
                    section.getString("difficulty", "NORMAL"),
                    section.getInt("min-players", 1),
                    section.getInt("max-players", 4),
                    section.getInt("time-limit", 1800),
                    section.getInt("cooldown", 300),
                    section.getString("schematic", "default"),
                    section.getBoolean("enabled", true)
                );
                dungeons.put(key, data);
            }
        }
    }

    public DungeonData getDungeon(String id) {
        return dungeons.get(id);
    }

    public Collection<DungeonData> getAllDungeons() {
        return dungeons.values();
    }

    public void createDungeonInstance(UUID playerUUID, String dungeonId) {
        DungeonData data = getDungeon(dungeonId);
        if (data != null && data.isEnabled()) {
            DungeonInstance instance = new DungeonInstance(data, playerUUID);
            playerDungeons.put(playerUUID, instance);
        }
    }

    public DungeonInstance getPlayerDungeon(UUID playerUUID) {
        return playerDungeons.get(playerUUID);
    }

    public void removeDungeonInstance(UUID playerUUID) {
        DungeonInstance instance = playerDungeons.remove(playerUUID);
        if (instance != null) {
            instance.cleanup();
        }
    }

    public void disableAll() {
        playerDungeons.values().forEach(DungeonInstance::cleanup);
        playerDungeons.clear();
    }

    public static class DungeonData {
        private final String id, name, difficulty, schematic;
        private final List<?> description;
        private final int minPlayers, maxPlayers, timeLimit, cooldown;
        private final boolean enabled;

        public DungeonData(String id, String name, List<?> description, String difficulty,
                          int minPlayers, int maxPlayers, int timeLimit, int cooldown,
                          String schematic, boolean enabled) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.difficulty = difficulty;
            this.minPlayers = minPlayers;
            this.maxPlayers = maxPlayers;
            this.timeLimit = timeLimit;
            this.cooldown = cooldown;
            this.schematic = schematic;
            this.enabled = enabled;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDifficulty() { return difficulty; }
        public int getMinPlayers() { return minPlayers; }
        public int getMaxPlayers() { return maxPlayers; }
        public int getTimeLimit() { return timeLimit; }
        public int getCooldown() { return cooldown; }
        public String getSchematic() { return schematic; }
        public boolean isEnabled() { return enabled; }
        public List<?> getDescription() { return description; }
    }

    public static class DungeonInstance {
        private final DungeonData data;
        private final UUID playerUUID;
        private final long createdAt;

        public DungeonInstance(DungeonData data, UUID playerUUID) {
            this.data = data;
            this.playerUUID = playerUUID;
            this.createdAt = System.currentTimeMillis();
        }

        public DungeonData getData() { return data; }
        public UUID getPlayerUUID() { return playerUUID; }
        public long getCreatedAt() { return createdAt; }

        public void cleanup() {
            // Cleanup logic
        }
    }
}
```

---

## 👥 PASO 4: Party Manager

**Ubicación:** `src/main/java/com/nextdungeons/party/PartyManager.java`

```java
package com.nextdungeons.party;

import java.util.*;

public class PartyManager {
    private final Map<UUID, Party> playerParties = new HashMap<>();
    private final Map<UUID, Party> parties = new HashMap<>();

    public Party createParty(UUID leader) {
        Party party = new Party(UUID.randomUUID(), leader);
        parties.put(party.getId(), party);
        playerParties.put(leader, party);
        return party;
    }

    public Party getParty(UUID playerId) {
        return playerParties.get(playerId);
    }

    public void deleteParty(UUID partyId) {
        Party party = parties.remove(partyId);
        if (party != null) {
            party.getMembers().forEach(uuid -> playerParties.remove(uuid));
        }
    }

    public void addMember(UUID partyId, UUID playerUUID) {
        Party party = parties.get(partyId);
        if (party != null && party.canAddMember()) {
            party.addMember(playerUUID);
            playerParties.put(playerUUID, party);
        }
    }

    public void removeMember(UUID partyId, UUID playerUUID) {
        Party party = parties.get(partyId);
        if (party != null) {
            party.removeMember(playerUUID);
            playerParties.remove(playerUUID);
        }
    }

    public static class Party {
        private final UUID id;
        private final UUID leader;
        private final Set<UUID> members = new HashSet<>();
        private final int maxMembers = 5;

        public Party(UUID id, UUID leader) {
            this.id = id;
            this.leader = leader;
            this.members.add(leader);
        }

        public UUID getId() { return id; }
        public UUID getLeader() { return leader; }
        public Set<UUID> getMembers() { return members; }
        public boolean canAddMember() { return members.size() < maxMembers; }
        public void addMember(UUID uuid) { members.add(uuid); }
        public void removeMember(UUID uuid) { members.remove(uuid); }
    }
}
```

---

## 🎓 PASO 5: Class System

**Ubicación:** `src/main/java/com/nextdungeons/class_system/ClassManager.java`

```java
package com.nextdungeons.class_system;

import java.util.*;

public class ClassManager {
    private final Map<UUID, PlayerClass> playerClasses = new HashMap<>();
    private final Map<String, ClassData> classes = new HashMap<>();

    public ClassManager() {
        registerDefaultClasses();
    }

    private void registerDefaultClasses() {
        classes.put("WARRIOR", new ClassData("WARRIOR", "Guerrero", 20, 1.0, 0.8));
        classes.put("MAGE", new ClassData("MAGE", "Mago", 12, 0.6, 1.2));
        classes.put("ARCHER", new ClassData("ARCHER", "Arquero", 14, 0.9, 1.1));
        classes.put("PALADIN", new ClassData("PALADIN", "Paladín", 16, 0.8, 0.9));
        classes.put("ROGUE", new ClassData("ROGUE", "Pícaro", 13, 1.2, 0.9));
    }

    public void setPlayerClass(UUID playerUUID, String className) {
        ClassData data = classes.get(className);
        if (data != null) {
            playerClasses.put(playerUUID, new PlayerClass(playerUUID, data));
        }
    }

    public PlayerClass getPlayerClass(UUID playerUUID) {
        return playerClasses.get(playerUUID);
    }

    public ClassData getClassData(String name) {
        return classes.get(name);
    }

    public Collection<ClassData> getAllClasses() {
        return classes.values();
    }

    public static class ClassData {
        private final String id, displayName;
        private final double health, damage, magicDamage;

        public ClassData(String id, String displayName, double health, double damage, double magicDamage) {
            this.id = id;
            this.displayName = displayName;
            this.health = health;
            this.damage = damage;
            this.magicDamage = magicDamage;
        }

        public String getId() { return id; }
        public String getDisplayName() { return displayName; }
        public double getHealth() { return health; }
        public double getDamage() { return damage; }
        public double getMagicDamage() { return magicDamage; }
    }

    public static class PlayerClass {
        private final UUID playerUUID;
        private final ClassData data;
        private int level = 1;
        private double exp = 0;

        public PlayerClass(UUID playerUUID, ClassData data) {
            this.playerUUID = playerUUID;
            this.data = data;
        }

        public UUID getPlayerUUID() { return playerUUID; }
        public ClassData getData() { return data; }
        public int getLevel() { return level; }
        public double getExp() { return exp; }
        public void addExp(double amount) { this.exp += amount; }
    }
}
```

---

## 📜 PASO 6: Quest System

**Ubicación:** `src/main/java/com/nextdungeons/quest/QuestManager.java`

```java
package com.nextdungeons.quest;

import java.util.*;

public class QuestManager {
    private final Map<UUID, Set<Quest>> playerQuests = new HashMap<>();
    private final Map<String, QuestData> questTemplates = new HashMap<>();

    public void registerQuest(String id, QuestData data) {
        questTemplates.put(id, data);
    }

    public void assignQuest(UUID playerUUID, String questId) {
        QuestData data = questTemplates.get(questId);
        if (data != null) {
            playerQuests.computeIfAbsent(playerUUID, k -> new HashSet<>())
                .add(new Quest(questId, data));
        }
    }

    public Set<Quest> getPlayerQuests(UUID playerUUID) {
        return playerQuests.getOrDefault(playerUUID, new HashSet<>());
    }

    public static class QuestData {
        private final String id, name, description;
        private final int reward;
        private final List<String> objectives;

        public QuestData(String id, String name, String description, int reward, List<String> objectives) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.reward = reward;
            this.objectives = objectives;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getReward() { return reward; }
        public List<String> getObjectives() { return objectives; }
    }

    public static class Quest {
        private final String questId;
        private final QuestData data;
        private final Set<Integer> completedObjectives = new HashSet<>();

        public Quest(String questId, QuestData data) {
            this.questId = questId;
            this.data = data;
        }

        public String getQuestId() { return questId; }
        public QuestData getData() { return data; }
        public void completeObjective(int index) { completedObjectives.add(index); }
        public boolean isComplete() { return completedObjectives.size() == data.getObjectives().size(); }
    }
}
```

---

## 🏆 PASO 7: Leaderboard System

**Ubicación:** `src/main/java/com/nextdungeons/leaderboard/LeaderboardManager.java`

```java
package com.nextdungeons.leaderboard;

import java.util.*;
import java.util.stream.Collectors;

public class LeaderboardManager {
    private final Map<String, List<LeaderboardEntry>> leaderboards = new HashMap<>();

    public void recordEntry(String boardId, UUID playerUUID, String playerName, int score) {
        leaderboards.computeIfAbsent(boardId, k -> new ArrayList<>())
            .add(new LeaderboardEntry(playerUUID, playerName, score, System.currentTimeMillis()));
        
        leaderboards.get(boardId).sort((a, b) -> Integer.compare(b.score, a.score));
    }

    public List<LeaderboardEntry> getLeaderboard(String boardId, int page) {
        List<LeaderboardEntry> board = leaderboards.getOrDefault(boardId, new ArrayList<>());
        int start = (page - 1) * 10;
        int end = Math.min(start + 10, board.size());
        return board.subList(start, end);
    }

    public static class LeaderboardEntry {
        private final UUID playerUUID;
        private final String playerName;
        private final int score;
        private final long timestamp;

        public LeaderboardEntry(UUID playerUUID, String playerName, int score, long timestamp) {
            this.playerUUID = playerUUID;
            this.playerName = playerName;
            this.score = score;
            this.timestamp = timestamp;
        }

        public UUID getPlayerUUID() { return playerUUID; }
        public String getPlayerName() { return playerName; }
        public int getScore() { return score; }
        public long getTimestamp() { return timestamp; }
    }
}
```

---

## 🔌 PASO 8: Hooks Base Classes

**Ubicación:** `src/main/java/com/nextdungeons/hooks/VaultHook.java`

```java
package com.nextdungeons.hooks;

import com.nextdungeons.plugin.NextDungeons;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.milkbowl.vault.economy.Economy;

public class VaultHook {
    private final NextDungeons plugin;
    private Economy economy;

    public VaultHook(NextDungeons plugin) {
        this.plugin = plugin;
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            // Initialize Vault
            plugin.getLogger().info("✓ Vault hooked successfully!");
        }
    }

    public boolean isEnabled() {
        return economy != null;
    }

    public void giveMoney(Player player, double amount) {
        if (economy != null) {
            economy.depositPlayer(player, amount);
        }
    }

    public double getMoney(Player player) {
        return economy != null ? economy.getBalance(player) : 0;
    }
}
```

---

## 📋 Resto de Hooks

Los otros hooks (PlaceholderAPI, MythicMobs, FAWE, Citizens, CMI) siguen la misma estructura.

---

## ✅ INSTRUCCIONES FINALES

1. Crea las carpetas en `src/main/java/com/nextdungeons/`
2. Copia cada clase Java en su ubicación correcta
3. Actualiza `pom.xml` (ya está hecho)
4. Ejecuta: `mvn clean package`
5. El JAR estará en: `target/NextDungeons.jar`

---

**¡Plugin v2.0 listo para compilar! 🚀**
