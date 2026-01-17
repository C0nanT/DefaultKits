package com.example.plugin.kit;

import com.hypixel.hytale.server.core.entity.entities.Player;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Gerencia o estado dos kits ativos dos jogadores.
 * Rastreia qual jogador tem o kit archer ativo.
 */
public class KitManager {

    private static final KitManager INSTANCE = new KitManager();
    private final Set<UUID> archerKitPlayers = new HashSet<>();

    private KitManager() {
    }

    public static KitManager getInstance() {
        return INSTANCE;
    }

    /**
     * Marca um jogador como tendo o kit archer ativo.
     */
    public void setArcherKit(@Nonnull Player player) {
        archerKitPlayers.add(player.getUuid());
    }

    /**
     * Verifica se um jogador tem o kit archer ativo.
     */
    public boolean hasArcherKit(@Nonnull Player player) {
        return archerKitPlayers.contains(player.getUuid());
    }

    /**
     * Remove um jogador completamente (desconexão).
     */
    public void removePlayer(@Nonnull Player player) {
        archerKitPlayers.remove(player.getUuid());
    }

    /**
     * Remove um jogador pelo UUID (desconexão / eventos via PlayerRef).
     */
    public void removePlayer(@Nonnull UUID uuid) {
        archerKitPlayers.remove(uuid);
    }
}
