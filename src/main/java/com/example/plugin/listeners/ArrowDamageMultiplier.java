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
import com.hypixel.hytale.server.core.modules.entity.AllLegacyLivingEntityTypesQuery;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Sistema que aumenta o dano de arco e flecha para jogadores.
 * Modifica o dano durante a fase FILTER (antes da aplicação final).
 */
public class ArrowDamageMultiplier extends DamageEventSystem {

    private static final boolean DEBUG_ENABLED = false; // Trocar para true para debug
    
    // Multiplicador de dano: 2.0f = dano dobrado, 1.5f = 50% mais dano
    private static final float DAMAGE_MULTIPLIER = 1.5f;

    private void log(String message) {
        if (DEBUG_ENABLED) {
            System.out.println("[ArrowDamageMultiplier] " + message);
        }
    }

    @Nonnull
    private static final Query<EntityStore> QUERY = AllLegacyLivingEntityTypesQuery.INSTANCE;

    @Override
    @Nullable
    public SystemGroup<EntityStore> getGroup() {
        // IMPORTANTE: Usar filterDamageGroup para modificar dano
        // Isso executa ANTES de aplicar o dano final
        return DamageModule.get().getFilterDamageGroup();
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
            log("Evento de arco inválido ou não é jogador");
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

        // Aplica apenas se o jogador tiver kit archer ativo
        if (!KitManager.getInstance().hasArcherKit(playerRef)) {
            // Sem kit archer, dano normal
            return;
        }

        // Aplica o multiplicador de dano
        float currentDamage = damage.getAmount();
        float newDamage = currentDamage * DAMAGE_MULTIPLIER;
        damage.setAmount(newDamage);
        
        log("Dano de arco aumentado: " + currentDamage + " -> " + newDamage);
    }
}
