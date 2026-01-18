package com.example.plugin.kit;

/**
 * Armazena informações sobre os kits ativos de um jogador.
 */
public class PlayerKitData {
    private boolean archerKit;

    public PlayerKitData() {
        this.archerKit = false;
    }

    public boolean isArcherKit() {
        return archerKit;
    }

    public void setArcherKit(boolean archerKit) {
        this.archerKit = archerKit;
    }
}
