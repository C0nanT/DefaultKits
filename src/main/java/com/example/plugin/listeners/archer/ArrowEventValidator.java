package com.example.plugin.listeners.archer;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utilitário para validar eventos de arco e flecha.
 * Centraliza verificações comuns para reutilização entre sistemas.
 */
public class ArrowEventValidator {

    private static final boolean DEBUG_ENABLED = false;

    private static void log(String message) {
        if (DEBUG_ENABLED) {
            System.out.println("[ArrowEventValidator] " + message);
        }
    }

    /**
     * Valida se o dano foi causado por projétil (arco/flecha).
     */
    public static boolean isProjectileDamage(@Nonnull Damage damage) {
        DamageCause cause = damage.getCause();
        if (cause == null || !cause.getId().equals(DamageCause.PROJECTILE.getId())) {
            log("Dano não é de projétil");
            return false;
        }
        return true;
    }

    /**
     * Obtém o atirador (shooter) como Player.
     */
    @Nullable
    public static Player getShooterAsPlayer(@Nonnull Damage damage,
                                            @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        if (!(damage.getSource() instanceof Damage.EntitySource)) {
            log("Dano não é de entidade");
            return null;
        }

        Damage.EntitySource entitySource = (Damage.EntitySource) damage.getSource();
        Ref<EntityStore> shooterRef = entitySource.getRef();

        if (!shooterRef.isValid()) {
            log("Referência de atirador inválida");
            return null;
        }

        Entity shooterEntity = EntityUtils.getEntity(shooterRef, commandBuffer);
        if (!(shooterEntity instanceof Player)) {
            log("Atirador não é jogador");
            return null;
        }

        return (Player) shooterEntity;
    }

    /**
     * Valida se o alvo é uma entidade viva.
     */
    public static boolean isLivingEntity(@Nullable Entity targetEntity) {
        if (!(targetEntity instanceof LivingEntity)) {
            log("Alvo não é LivingEntity");
            return false;
        }
        return true;
    }

    /**
     * Valida completamente um evento de arco (projétil + jogador atirador).
     * Retorna o jogador atirador se válido, null caso contrário.
     */
    @Nullable
    public static Player validateArrowHitEvent(@Nonnull Damage damage,
                                               @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        if (!isProjectileDamage(damage)) {
            return null;
        }

        return getShooterAsPlayer(damage, commandBuffer);
    }
}
