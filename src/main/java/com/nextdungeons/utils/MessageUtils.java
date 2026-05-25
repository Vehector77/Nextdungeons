package com.nextdungeons.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtils {
    
    public static void sendMessage(CommandSender sender, String message) {
        if (message == null || message.isEmpty()) return;
        sender.sendMessage(colorize(message));
    }
    
    public static void sendMessage(Player player, String message) {
        if (message == null || message.isEmpty()) return;
        player.sendMessage(colorize(message));
    }
    
    public static String colorize(String message) {
        if (message == null) return "";
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static void broadcast(String message) {
        org.bukkit.Bukkit.broadcastMessage(colorize(message));
    }
}