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
 * Sistema que recompensa durabilidade do arco quando jogador acerta com arco.
 * Quando um jogador acerta um animal/monstro/player com flecha, ganha +2 de durabilidade no arco.
 */
public class ArrowRecoveryArchListener extends DamageEventSystem {

    private static final boolean DEBUG_ENABLED = true;

    private static final int RECOVERY = 1;
    private static final String ARCH_ITEM_START = "Weapon_Shortbow_";


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
        // Valida se é um evento de arco/flecha de um jogador
        Player shooter = ArrowEventValidator.validateArrowHitEvent(damage, commandBuffer);
        if (shooter == null) {
            log("Evento de arco invalido ou nao é jogador");
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

        // Verifica se o alvo é uma entidade viva (animal/monstro/player)
        Entity targetEntity = EntityUtils.getEntity(index, archetypeChunk);
        if (!ArrowEventValidator.isLivingEntity(targetEntity)) {
            log("Alvo nao é LivingEntity, ignorando");
            return;
        }

        // Da +2 de durabilidade se o item na mão do usuário for um arco
        Inventory inventory = shooter.getInventory();
        if (inventory == null) {
            log("Inventario é null");
            return;
        }

        ItemStack itemInHand = inventory.getItemInHand();

        if (itemInHand == null) {
            log("Item na mao é null");
            return;
        } else if (!itemInHand.getItemId().startsWith(ARCH_ITEM_START)) {
            log("Item na mao nao é arco");
            return;
        }   

        //gera um número aleatórico de 0 até 10
        int randomRecovery = (int) (Math.random() * 11);
        log("Numero aleatorio gerado: " + randomRecovery);
        if(randomRecovery < 7) {
            log("Recuperacao do arco");
            return;
        }

        // Adiciona durabilidade ao arco
        ItemStack repairedArco = itemInHand.withIncreasedDurability(RECOVERY);
        
        // Atualiza o item no inventário
        inventory.getHotbar().setItemStackForSlot(inventory.getActiveHotbarSlot(), repairedArco);
        log("Sucesso! +" + RECOVERY + " durabilidade adicionada ao arco");

    }
}
