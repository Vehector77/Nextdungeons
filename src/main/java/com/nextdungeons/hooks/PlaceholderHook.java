package com.nextdungeons.hooks;

import com.nextdungeons.plugin.NextDungeons;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderHook extends PlaceholderExpansion {
    
    private final NextDungeons plugin;
    
    public PlaceholderHook(NextDungeons plugin) {
        this.plugin = plugin;
    }
    
    @Override
    @NotNull
    public String getIdentifier() {
        return "nextdungeons";
    }
    
    @Override
    @NotNull
    public String getAuthor() {
        return "NextDungeons";
    }
    
    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }
        
        if (identifier.equals("in_dungeon")) {
            return String.valueOf(plugin.getDungeonManager().isInDungeon(player));
        }
        
        if (identifier.equals("dungeon_name")) {
            var instance = plugin.getDungeonManager().getDungeonInstance(player);
            return instance != null ? instance.getTemplate().getDisplayName() : "None";
        }
        
        if (identifier.equals("in_party")) {
            return String.valueOf(plugin.getPartyManager().isInParty(player));
        }
        
        if (identifier.equals("party_size")) {
            var party = plugin.getPartyManager().getParty(player);
            return party != null ? String.valueOf(party.getMembers().size()) : "0";
        }
        
        if (identifier.equals("is_party_leader")) {
            return String.valueOf(plugin.getPartyManager().isLeader(player));
        }
        
        return null;
    }
}