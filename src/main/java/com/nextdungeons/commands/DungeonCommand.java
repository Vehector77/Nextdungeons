package com.nextdungeons.commands;

import com.nextdungeons.dungeon.DungeonData;
import com.nextdungeons.plugin.NextDungeons;
import com.nextdungeons.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class DungeonCommand implements CommandExecutor {
    
    private final NextDungeons plugin;
    
    public DungeonCommand(NextDungeons plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
        
        if (!player.hasPermission("nextdungeons.use")) {
            MessageUtils.sendMessage(player, plugin.getConfig().getString("messages.no-permission"));
            return true;
        }
        
        if (args.length == 0) {
            if (plugin.getGuiManager() != null) {
                plugin.getGuiManager().openDungeonMenu(player);
            } else {
                showDungeonList(player);
            }
            return true;
        }
        
        String dungeonId = args[0];
        
        if (plugin.getPartyManager().isInParty(player)) {
            if (!plugin.getPartyManager().isLeader(player)) {
                MessageUtils.sendMessage(player, plugin.getConfig().getString("messages.must-be-party-leader"));
                return true;
            }
        }
        
        plugin.getDungeonManager().createDungeonInstance(player, dungeonId);
        return true;
    }
    
    private void showDungeonList(Player player) {
        MessageUtils.sendMessage(player, "&6&lAvailable Dungeons:");
        
        Map<String, DungeonData> dungeons = plugin.getDungeonManager().getDungeonTemplates();
        if (dungeons.isEmpty()) {
            MessageUtils.sendMessage(player, "&cNo dungeons available!");
            return;
        }
        
        for (DungeonData data : dungeons.values()) {
            if (data.isEnabled()) {
                MessageUtils.sendMessage(player, 
                    "&e- &6" + data.getDisplayName() + " &7(" + data.getDifficulty() + ")");
                MessageUtils.sendMessage(player, 
                    "  &7/dungeons " + data.getId());
            }
        }
    }
}