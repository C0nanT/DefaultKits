package com.example.plugin.listeners;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.SystemGroup;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.AllLegacyLivingEntityTypesQuery;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Sistema que escuta eventos de dano de projéteis.
 * Quando um jogador acerta um animal/monstro com flecha, ganha +2 flechas.
 */
public class ArrowHitListener extends DamageEventSystem {

    private static final boolean DEBUG_ENABLED = false; // Trocar para false para desativar

    private static final String ARROW_ITEM_ID = "Weapon_Arrow_Crude";
    private static final int ARROWS_TO_GIVE = 2;

    private void log(String message) {
        if (DEBUG_ENABLED) {
            System.out.println("[ArrowHitListener] " + message);
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
        // Verifica se o dano foi causado por um projétil (arco/flecha)
        DamageCause cause = damage.getCause();
        if (cause == null || !cause.getId().equals(DamageCause.PROJECTILE.getId())) {
            log("Dano não é de projétil, causa: " + (cause != null ? cause.getId() : "null"));
            return;
        }

        if (!(damage.getSource() instanceof Damage.EntitySource)) {
            log("Dano não é de entidade, tipo recebido: " + (damage.getSource() != null ? damage.getSource().getClass().getSimpleName() : "null"));
            return;
        }

        // EntitySource funciona para ambos os casos (arco e flecha jogada)
        // porque ProjectileSource estende EntitySource
        Damage.EntitySource entitySource = (Damage.EntitySource) damage.getSource();
        Ref<EntityStore> shooterRef = entitySource.getRef();


        // Verifica se o atirador é um jogador
        Entity shooterEntity = EntityUtils.getEntity(shooterRef, commandBuffer);
        if (!(shooterEntity instanceof Player)) {
            log("Atirador não é jogador, ignorando");
            return;
        }

        // Verifica se o atirador é válido
        if (!shooterRef.isValid()) {
            log("Atirador não é válido, tipo recebido: " + shooterRef.getClass().getSimpleName());
            return;
        }

        // Verifica se o alvo é um NPC (animal/monstro/player)
        Entity targetEntity = EntityUtils.getEntity(index, archetypeChunk);

        // Verifica se é uma entidade viva (animal/monstro/player)
        if (!(targetEntity instanceof LivingEntity)) {
            log("Alvo não é LivingEntity, ignorando");
            return;
        }

        // Dá +2 flechas ao jogador
        Player player = (Player) shooterEntity;
        Inventory inventory = player.getInventory();

        if (inventory == null) {
            log("Inventário é null, não foi possível dar flechas");
            return;
        }

        ItemStack arrows = new ItemStack(ARROW_ITEM_ID, ARROWS_TO_GIVE, null);
        inventory.getCombinedHotbarFirst().addItemStack(arrows);
        log("Sucesso! +" + ARROWS_TO_GIVE + " flechas dadas ao jogador");
    }
}
