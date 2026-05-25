package com.nextdungeons.party;

import com.nextdungeons.plugin.NextDungeons;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PartyManager {
    
    private final NextDungeons plugin;
    private final Map<UUID, Party> parties;
    private final Map<UUID, UUID> playerToParty;
    private final Map<UUID, Map<UUID, Long>> invites;
    
    public PartyManager(NextDungeons plugin) {
        this.plugin = plugin;
        this.parties = new ConcurrentHashMap<>();
        this.playerToParty = new ConcurrentHashMap<>();
        this.invites = new ConcurrentHashMap<>();
    }
    
    public Party createParty(Player leader) {
        if (isInParty(leader)) {
            return getParty(leader);
        }
        
        Party party = new Party(UUID.randomUUID(), leader.getUniqueId());
        parties.put(party.getId(), party);
        playerToParty.put(leader.getUniqueId(), party.getId());
        
        return party;
    }
    
    public void disbandParty(Party party) {
        for (UUID memberId : party.getMembers()) {
            playerToParty.remove(memberId);
        }
        parties.remove(party.getId());
    }
    
    public boolean isInParty(Player player) {
        return playerToParty.containsKey(player.getUniqueId());
    }
    
    public Party getParty(Player player) {
        UUID partyId = playerToParty.get(player.getUniqueId());
        return partyId != null ? parties.get(partyId) : null;
    }
    
    public boolean isLeader(Player player) {
        Party party = getParty(player);
        return party != null && party.getLeader().equals(player.getUniqueId());
    }
    
    public void invitePlayer(Player leader, Player target) {
        Party party = getParty(leader);
        if (party == null || !party.getLeader().equals(leader.getUniqueId())) {
            return;
        }
        
        invites.computeIfAbsent(target.getUniqueId(), k -> new HashMap<>())
            .put(party.getId(), System.currentTimeMillis() + 60000);
    }
    
    public boolean hasInvite(Player player, Party party) {
        Map<UUID, Long> playerInvites = invites.get(player.getUniqueId());
        if (playerInvites == null) return false;
        
        Long expiry = playerInvites.get(party.getId());
        if (expiry == null) return false;
        
        if (System.currentTimeMillis() > expiry) {
            playerInvites.remove(party.getId());
            return false;
        }
        
        return true;
    }
    
    public void acceptInvite(Player player, Party party) {
        if (!hasInvite(player, party)) return;
        
        party.addMember(player.getUniqueId());
        playerToParty.put(player.getUniqueId(), party.getId());
        
        Map<UUID, Long> playerInvites = invites.get(player.getUniqueId());
        if (playerInvites != null) {
            playerInvites.remove(party.getId());
        }
    }
    
    public void leaveParty(Player player) {
        Party party = getParty(player);
        if (party == null) return;
        
        playerToParty.remove(player.getUniqueId());
        party.removeMember(player.getUniqueId());
        
        if (party.getMembers().isEmpty()) {
            parties.remove(party.getId());
        } else if (party.getLeader().equals(player.getUniqueId())) {
            UUID newLeader = party.getMembers().iterator().next();
            party.setLeader(newLeader);
        }
    }
    
    public void kickMember(Player leader, Player target) {
        Party party = getParty(leader);
        if (party == null || !party.getLeader().equals(leader.getUniqueId())) {
            return;
        }
        
        playerToParty.remove(target.getUniqueId());
        party.removeMember(target.getUniqueId());
    }
    
    public Collection<Party> getAllParties() {
        return new ArrayList<>(parties.values());
    }
}