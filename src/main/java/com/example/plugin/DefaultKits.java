package com.example.plugin;

import com.example.plugin.commands.KitCommand;
import com.example.plugin.listeners.ArrowAmmoRewardListener;
import com.example.plugin.listeners.ArrowDamageMultiplier;
import com.example.plugin.kit.KitManager;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import javax.annotation.Nonnull;

public class DefaultKits extends JavaPlugin {
    public DefaultKits(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    public void setup() {
        super.setup();
        getCommandRegistry().registerCommand(new KitCommand());
        getEntityStoreRegistry().registerSystem(new ArrowDamageMultiplier());
        getEntityStoreRegistry().registerSystem(new ArrowAmmoRewardListener());
    }
}
