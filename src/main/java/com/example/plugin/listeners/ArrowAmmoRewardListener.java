package com.example.plugin.listeners;

import com.example.plugin.kit.KitManager;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.SystemGroup;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.AllLegacyLivingEntityTypesQuery;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Sistema que recompensa municao (flechas) quando jogador acerta com arco.
 * Quando um jogador acerta um animal/monstro com flecha, ganha +2 flechas.
 */
public class ArrowAmmoRewardListener extends DamageEventSystem {

    private static final boolean DEBUG_ENABLED = false;

    private static final String ARROW_ITEM_ID = "Weapon_Arrow_Crude";
    private static final int ARROWS_TO_GIVE = 2;

    private void log(String message) {
        if (DEBUG_ENABLED) {
            System.out.println("[ArrowAmmoRewardListener] " + message);
        }
    }

    @Nonnull
    private static final Query<EntityStore> QUERY = AllLegacyLivingEntityTypesQuery.INSTANCE;

    @Override
    @Nullable
    public SystemGroup<EntityStore> getGroup() {
        return DamageModule.get().getInspectDamageGroup();
    }

    @Override
    @Nonnull
    public Query<EntityStore> getQuery() {
        return QUERY;
    }

    @Override
    public void handle(
        int index,
        @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
        @Nonnull Store<EntityStore> store,
        @Nonnull CommandBuffer<EntityStore> commandBuffer,
        @Nonnull Damage damage
    ) {
        // Valida se eh um evento de arco/flecha de um jogador
        Player shooter = ArrowEventValidator.validateArrowHitEvent(damage, commandBuffer);
        if (shooter == null) {
            log("Evento de arco invalido ou nao eh jogador");
            return;
        }

        // Obtém o Ref do shooter para pegar o PlayerRef
        Ref<EntityStore> shooterRef = null;
        if (damage.getSource() instanceof Damage.EntitySource) {
            shooterRef = ((Damage.EntitySource) damage.getSource()).getRef();
        }
        
        if (shooterRef == null || !shooterRef.isValid()) {
            return;
        }

        // Obtém o PlayerRef do atirador
        PlayerRef playerRef = commandBuffer.getComponent(shooterRef, PlayerRef.getComponentType());
        if (playerRef == null) {
            return;
        }

        // Recompensa apenas se o jogador tiver kit archer ativo
        if (!KitManager.getInstance().hasArcherKit(playerRef)) {
            return;
        }

        // Verifica se o alvo eh uma entidade viva (animal/monstro/player)
        Entity targetEntity = EntityUtils.getEntity(index, archetypeChunk);
        if (!ArrowEventValidator.isLivingEntity(targetEntity)) {
            log("Alvo nao eh LivingEntity, ignorando");
            return;
        }

        // Da +2 flechas ao jogador
        Inventory inventory = shooter.getInventory();
        if (inventory == null) {
            log("Inventario eh null, nao foi possivel dar flechas");
            return;
        }

        ItemStack arrows = new ItemStack(ARROW_ITEM_ID, ARROWS_TO_GIVE, null);
        inventory.getCombinedHotbarFirst().addItemStack(arrows);
        log("Sucesso! +" + ARROWS_TO_GIVE + " flechas dadas ao jogador (kit archer)");
    }
}
