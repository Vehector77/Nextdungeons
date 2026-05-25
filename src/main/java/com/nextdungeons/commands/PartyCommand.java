package com.nextdungeons.commands;

import com.nextdungeons.party.Party;
import com.nextdungeons.plugin.NextDungeons;
import com.nextdungeons.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PartyCommand implements CommandExecutor {
    
    private final NextDungeons plugin;
    
    public PartyCommand(NextDungeons plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
        
        if (!player.hasPermission("nextdungeons.party")) {
            MessageUtils.sendMessage(player, plugin.getConfig().getString("messages.no-permission"));
            return true;
        }
        
        if (args.length == 0) {
            sendHelp(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "create" -> handleCreate(player);
            case "invite" -> handleInvite(player, args);
            case "accept" -> handleAccept(player, args);
            case "kick" -> handleKick(player, args);
            case "leave" -> handleLeave(player);
            case "list" -> handleList(player);
            case "disband" -> handleDisband(player);
            default -> sendHelp(player);
        }
        
        return true;
    }
    
    private void handleCreate(Player player) {
        if (plugin.getPartyManager().isInParty(player)) {
            MessageUtils.sendMessage(player, "&cYou are already in a party!");
            return;
        }
        
        Party party = plugin.getPartyManager().createParty(player);
        MessageUtils.sendMessage(player, "&aParty created! You are the leader.");
    }
    
    private void handleInvite(Player player, String[] args) {
        if (args.length < 2) {
            MessageUtils.sendMessage(player, "&cUsage: /party invite <player>");
            return;
        }
        
        if (!plugin.getPartyManager().isLeader(player)) {
            MessageUtils.sendMessage(player, "&cYou must be the party leader to invite players!");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            MessageUtils.sendMessage(player, "&cPlayer not found!");
            return;
        }
        
        if (plugin.getPartyManager().isInParty(target)) {
            MessageUtils.sendMessage(player, "&cThat player is already in a party!");
            return;
        }
        
        Party party = plugin.getPartyManager().getParty(player);
        if (party.isFull()) {
            MessageUtils.sendMessage(player, plugin.getConfig().getString("messages.party-full"));
            return;
        }
        
        plugin.getPartyManager().invitePlayer(player, target);
        MessageUtils.sendMessage(player, "&aInvite sent to " + target.getName());
        
        String inviteMsg = plugin.getConfig().getString("messages.party-invite")
            .replace("%player%", player.getName());
        MessageUtils.sendMessage(target, inviteMsg);
        MessageUtils.sendMessage(target, "&7Use /party accept " + player.getName() + " to join!");
    }
    
    private void handleAccept(Player player, String[] args) {
        if (args.length < 2) {
            MessageUtils.sendMessage(player, "&cUsage: /party accept <player>");
            return;
        }
        
        if (plugin.getPartyManager().isInParty(player)) {
            MessageUtils.sendMessage(player, "&cYou are already in a party!");
            return;
        }
        
        Player leader = Bukkit.getPlayer(args[1]);
        if (leader == null) {
            MessageUtils.sendMessage(player, "&cPlayer not found!");
            return;
        }
        
        Party party = plugin.getPartyManager().getParty(leader);
        if (party == null) {
            MessageUtils.sendMessage(player, "&cThat player is not in a party!");
            return;
        }
        
        if (!plugin.getPartyManager().hasInvite(player, party)) {
            MessageUtils.sendMessage(player, "&cYou don't have an invite from that party!");
            return;
        }
        
        plugin.getPartyManager().acceptInvite(player, party);
        
        String joinMsg = plugin.getConfig().getString("messages.party-joined")
            .replace("%player%", player.getName());
        
        for (UUID memberId : party.getMembers()) {
            Player member = Bukkit.getPlayer(memberId);
            if (member != null && member.isOnline()) {
                MessageUtils.sendMessage(member, joinMsg);
            }
        }
    }
    
    private void handleKick(Player player, String[] args) {
        if (args.length < 2) {
            MessageUtils.sendMessage(player, "&cUsage: /party kick <player>");
            return;
        }
        
        if (!plugin.getPartyManager().isLeader(player)) {
            MessageUtils.sendMessage(player, "&cYou must be the party leader to kick players!");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            MessageUtils.sendMessage(player, "&cPlayer not found!");
            return;
        }
        
        plugin.getPartyManager().kickMember(player, target);
        MessageUtils.sendMessage(player, "&aKicked " + target.getName() + " from the party.");
        MessageUtils.sendMessage(target, "&cYou have been kicked from the party!");
    }
    
    private void handleLeave(Player player) {
        if (!plugin.getPartyManager().isInParty(player)) {
            MessageUtils.sendMessage(player, "&cYou are not in a party!");
            return;
        }
        
        Party party = plugin.getPartyManager().getParty(player);
        String leaveMsg = plugin.getConfig().getString("messages.party-left")
            .replace("%player%", player.getName());
        
        plugin.getPartyManager().leaveParty(player);
        MessageUtils.sendMessage(player, "&aYou have left the party.");
        
        if (party != null) {
            for (UUID memberId : party.getMembers()) {
                Player member = Bukkit.getPlayer(memberId);
                if (member != null && member.isOnline()) {
                    MessageUtils.sendMessage(member, leaveMsg);
                }
            }
        }
    }
    
    private void handleList(Player player) {
        Party party = plugin.getPartyManager().getParty(player);
        if (party == null) {
            MessageUtils.sendMessage(player, "&cYou are not in a party!");
            return;
        }
        
        MessageUtils.sendMessage(player, "&6&lParty Members:");
        for (UUID memberId : party.getMembers()) {
            Player member = Bukkit.getPlayer(memberId);
            String name = member != null ? member.getName() : "Unknown";
            String role = memberId.equals(party.getLeader()) ? " &e(Leader)" : "";
            MessageUtils.sendMessage(player, "&e- &f" + name + role);
        }
    }
    
    private void handleDisband(Player player) {
        if (!plugin.getPartyManager().isLeader(player)) {
            MessageUtils.sendMessage(player, "&cYou must be the party leader to disband the party!");
            return;
        }
        
        Party party = plugin.getPartyManager().getParty(player);
        if (party == null) return;
        
        for (UUID memberId : party.getMembers()) {
            Player member = Bukkit.getPlayer(memberId);
            if (member != null && member.isOnline()) {
                MessageUtils.sendMessage(member, "&cThe party has been disbanded!");
            }
        }
        
        plugin.getPartyManager().disbandParty(party);
    }
    
    private void sendHelp(Player player) {
        MessageUtils.sendMessage(player, "&6&lParty Commands:");
        MessageUtils.sendMessage(player, "&e/party create &7- Create a new party");
        MessageUtils.sendMessage(player, "&e/party invite <player> &7- Invite a player");
        MessageUtils.sendMessage(player, "&e/party accept <player> &7- Accept an invite");
        MessageUtils.sendMessage(player, "&e/party kick <player> &7- Kick a member");
        MessageUtils.sendMessage(player, "&e/party leave &7- Leave the party");
        MessageUtils.sendMessage(player, "&e/party list &7- List party members");
        MessageUtils.sendMessage(player, "&e/party disband &7- Disband the party");
    }
}