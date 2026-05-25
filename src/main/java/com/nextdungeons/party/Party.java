package com.nextdungeons.party;

import java.util.*;

public class Party {
    
    private final UUID id;
    private UUID leader;
    private final Set<UUID> members;
    private int maxMembers;
    
    public Party(UUID id, UUID leader) {
        this.id = id;
        this.leader = leader;
        this.members = new HashSet<>();
        this.maxMembers = 5;
        
        members.add(leader);
    }
    
    public UUID getId() {
        return id;
    }
    
    public UUID getLeader() {
        return leader;
    }
    
    public void setLeader(UUID leader) {
        this.leader = leader;
    }
    
    public Set<UUID> getMembers() {
        return new HashSet<>(members);
    }
    
    public boolean addMember(UUID playerId) {
        if (members.size() >= maxMembers) {
            return false;
        }
        return members.add(playerId);
    }
    
    public void removeMember(UUID playerId) {
        members.remove(playerId);
    }
    
    public boolean isMember(UUID playerId) {
        return members.contains(playerId);
    }
    
    public int getMaxMembers() {
        return maxMembers;
    }
    
    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }
    
    public boolean isFull() {
        return members.size() >= maxMembers;
    }
}