package com.nextdungeons.hooks;

import com.nextdungeons.plugin.NextDungeons;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    
    private final NextDungeons plugin;
    private Economy economy;
    
    public VaultHook(NextDungeons plugin) {
        this.plugin = plugin;
    }
    
    public boolean setup() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        
        RegisteredServiceProvider<Economy> rsp = plugin.getServer()
            .getServicesManager().getRegistration(Economy.class);
        
        if (rsp == null) {
            return false;
        }
        
        economy = rsp.getProvider();
        return economy != null;
    }
    
    public Economy getEconomy() {
        return economy;
    }
    
    public boolean hasEconomy() {
        return economy != null;
    }
}