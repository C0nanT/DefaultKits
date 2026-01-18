package com.example.plugin.commands;

import com.example.plugin.kit.KitManager;
import com.example.plugin.kit.KitItem;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;


import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitCommand extends AbstractPlayerCommand {

    private static final Message MESSAGE_KIT_RECEIVED = Message.raw("Kit %kit% recebido!");
    private static final Message MESSAGE_KIT_NOT_FOUND = Message.raw("Kit '%kit%' nao encontrado. Kits disponiveis: %available%");

    private final RequiredArg<String> kitArg = this.withRequiredArg("kit", "Nome do kit", ArgTypes.STRING);

    private final Map<String, List<KitItem>> kits = new HashMap<>();

    public KitCommand() {
        super("kit", "Receba um kit de itens");
        registerKits();
    }

    private void registerKits() {
        // Kit Archer
        kits.put("archer", List.of(
            new KitItem("Weapon_Shortbow_Crude", 1),
            new KitItem("Weapon_Arrow_Crude", 20),
            new KitItem("Armor_Cloth_Linen_Head", 1)
            // new KitItem("Armor_Cloth_Linen_Chest", 1),
            // new KitItem("Armor_Cloth_Linen_Legs", 1),
            // new KitItem("Armor_Cloth_Linen_Hands", 1)
        ));

        // Kit Warrior
        kits.put("warrior", List.of(
            new KitItem("Weapon_Sword_Iron", 1),
            new KitItem("Weapon_Shield_Wood", 1),
            new KitItem("Armor_Iron_Head", 1)
            // new KitItem("Armor_Iron_Chest", 1),
            // new KitItem("Armor_Iron_Legs", 1),
            // new KitItem("Armor_Iron_Hands", 1)
        ));

        // Kit Mage
        kits.put("mage", List.of(
            new KitItem("Weapon_Staff_Wood", 1),
            new KitItem("Armor_Cloth_Cindercloth_Head", 1)
            // new KitItem("Armor_Cloth_Cindercloth_Chest", 1),
            // new KitItem("Armor_Cloth_Cindercloth_Legs", 1),
            // new KitItem("Armor_Cloth_Cindercloth_Hands", 1)
        ));

        kits.put("assassin", List.of(
            new KitItem("Weapon_Dagger_Steel", 1),
            new KitItem("Armor_Leather_Head", 1)
            // new KitItem("Armor_Leather_Chest", 1),
            // new KitItem("Armor_Leather_Legs", 1),
            // new KitItem("Armor_Leather_Hands", 1)
        ));
    }

    @Override
    protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {

        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;

        String kitName = kitArg.get(context).toLowerCase();

        if(kitName.isEmpty()) {
            context.sendMessage(MESSAGE_KIT_NOT_FOUND.param("kit", kitName));
            return;
        }

        List<KitItem> kitItems = kits.get(kitName);
        if (kitItems == null) {
            String available = String.join(", ", kits.keySet());

            context.sendMessage(MESSAGE_KIT_NOT_FOUND.param("kit", kitName).param("available", available != null ? available : "nenhum"));
            return;
        }

        for (KitItem item : kitItems) {
            String itemId = item.getItemId();
            if (itemId != null) {
                player.getInventory().getCombinedHotbarFirst().addItemStack(
                    new ItemStack(itemId, item.getQuantity(), null)
                );
            }
        }

        if ("archer".equals(kitName)) {
            KitManager.getInstance().setArcherKit(playerRef, true);
        }

        context.sendMessage(MESSAGE_KIT_RECEIVED.param("kit", kitName));
    }
}
