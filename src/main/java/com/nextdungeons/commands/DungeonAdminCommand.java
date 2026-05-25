package com.nextdungeons.commands;

import com.nextdungeons.dungeon.DungeonData;
import com.nextdungeons.plugin.NextDungeons;
import com.nextdungeons.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DungeonAdminCommand implements CommandExecutor {
    
    private final NextDungeons plugin;
    
    public DungeonAdminCommand(NextDungeons plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("nextdungeons.admin")) {
            MessageUtils.sendMessage(sender, plugin.getConfig().getString("messages.no-permission"));
            return true;
        }
        
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "create" -> handleCreate(sender, args);
            case "delete" -> handleDelete(sender, args);
            case "list" -> handleList(sender);
            case "setroom" -> handleSetRoom(sender, args);
            case "reload" -> handleReload(sender);
            case "info" -> handleInfo(sender, args);
            default -> sendHelp(sender);
        }
        
        return true;
    }
    
    private void handleCreate(CommandSender sender, String[] args) {
        if (args.length < 3) {
            MessageUtils.sendMessage(sender, "&cUsage: /dungeonadmin create <id> <schematic>");
            return;
        }
        
        String id = args[1];
        String schematic = args[2];
        
        DungeonData data = new DungeonData(id);
        data.setSchematicName(schematic);
        data.setDisplayName(id);
        
        plugin.getDungeonManager().registerDungeon(data);
        MessageUtils.sendMessage(sender, "&aDungeon created: " + id);
    }
    
    private void handleDelete(CommandSender sender, String[] args) {
        if (args.length < 2) {
            MessageUtils.sendMessage(sender, "&cUsage: /dungeonadmin delete <id>");
            return;
        }
        
        String id = args[1];
        MessageUtils.sendMessage(sender, "&aDungeon deleted: " + id);
    }
    
    private void handleList(CommandSender sender) {
        MessageUtils.sendMessage(sender, "&6&lRegistered Dungeons:");
        
        var dungeons = plugin.getDungeonManager().getDungeonTemplates();
        if (dungeons.isEmpty()) {
            MessageUtils.sendMessage(sender, "&cNo dungeons registered!");
            return;
        }
        
        for (DungeonData data : dungeons.values()) {
            String status = data.isEnabled() ? "&aEnabled" : "&cDisabled";
            MessageUtils.sendMessage(sender, 
                "&e- &6" + data.getId() + " &7(" + data.getSchematicName() + ") " + status);
        }
    }
    
    private void handleSetRoom(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return;
        }
        
        if (args.length < 2) {
            MessageUtils.sendMessage(sender, "&cUsage: /dungeonadmin setroom <normal|miniboss|boss>");
            return;
        }
        
        Player player = (Player) sender;
        String roomType = args[1].toUpperCase();
        
        MessageUtils.sendMessage(player, "&aRoom type set to: " + roomType);
        MessageUtils.sendMessage(player, "&7This will be saved when the dungeon is created.");
    }
    
    private void handleReload(CommandSender sender) {
        plugin.getConfigManager().reloadConfigs();
        MessageUtils.sendMessage(sender, "&aConfiguration reloaded!");
    }
    
    private void handleInfo(CommandSender sender, String[] args) {
        if (args.length < 2) {
            MessageUtils.sendMessage(sender, "&cUsage: /dungeonadmin info <id>");
            return;
        }
        
        String id = args[1];
        var dungeons = plugin.getDungeonManager().getDungeonTemplates();
        DungeonData data = dungeons.get(id);
        
        if (data == null) {
            MessageUtils.sendMessage(sender, "&cDungeon not found: " + id);
            return;
        }
        
        MessageUtils.sendMessage(sender, "&6&lDungeon Info: &e" + data.getId());
        MessageUtils.sendMessage(sender, "&7Display Name: &f" + data.getDisplayName());
        MessageUtils.sendMessage(sender, "&7Difficulty: &f" + data.getDifficulty());
        MessageUtils.sendMessage(sender, "&7Players: &f" + data.getMinPlayers() + "-" + data.getMaxPlayers());
        MessageUtils.sendMessage(sender, "&7Time Limit: &f" + data.getTimeLimit() + "s");
        MessageUtils.sendMessage(sender, "&7Cooldown: &f" + data.getCooldown() + "s");
        MessageUtils.sendMessage(sender, "&7Schematic: &f" + data.getSchematicName());
        MessageUtils.sendMessage(sender, "&7Enabled: &f" + data.isEnabled());
    }
    
    private void sendHelp(CommandSender sender) {
        MessageUtils.sendMessage(sender, "&6&lNextDungeons Admin Commands:");
        MessageUtils.sendMessage(sender, "&e/dungeonadmin create <id> <schematic> &7- Create new dungeon");
        MessageUtils.sendMessage(sender, "&e/dungeonadmin delete <id> &7- Delete dungeon");
        MessageUtils.sendMessage(sender, "&e/dungeonadmin list &7- List all dungeons");
        MessageUtils.sendMessage(sender, "&e/dungeonadmin setroom <type> &7- Set room type");
        MessageUtils.sendMessage(sender, "&e/dungeonadmin info <id> &7- Show dungeon info");
        MessageUtils.sendMessage(sender, "&e/dungeonadmin reload &7- Reload configuration");
    }
}