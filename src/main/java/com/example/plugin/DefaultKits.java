package com.example.plugin;

import com.example.plugin.commands.KitCommand;
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
    }
}
