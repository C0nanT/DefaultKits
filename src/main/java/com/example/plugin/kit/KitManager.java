package com.example.plugin.kit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gerencia o estado dos kits ativos dos jogadores.
 * Armazena informações de kits por UUID do jogador em um único arquivo JSON.
 */
public class KitManager {

    private static final KitManager INSTANCE = new KitManager();
    private final Map<UUID, PlayerKitData> playerKits = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path dataFile = Paths.get("plugins", "kits", "kits.json");

    private KitManager() {
        loadFromJson();
    }

    public static KitManager getInstance() {
        return INSTANCE;
    }

    /**
     * Obtém ou cria os dados de kit de um jogador.
     */
    private PlayerKitData getOrCreatePlayerData(@Nonnull UUID uuid) {
        return playerKits.computeIfAbsent(uuid, k -> new PlayerKitData());
    }

    /**
     * Marca um jogador como tendo o kit archer ativo.
     */
    public void setArcherKit(@Nonnull PlayerRef playerRef, boolean active) {
        PlayerKitData data = getOrCreatePlayerData(playerRef.getUuid());
        data.setArcherKit(active);
        saveToJson();
    }

    /**
     * Verifica se um jogador tem o kit archer ativo.
     */
    public boolean hasArcherKit(@Nonnull PlayerRef playerRef) {
        PlayerKitData data = playerKits.get(playerRef.getUuid());
        return data != null && data.isArcherKit();
    }

    /**
     * Remove um jogador completamente (desconexão).
     */
    public void removePlayer(@Nonnull PlayerRef playerRef) {
        playerKits.remove(playerRef.getUuid());
        saveToJson();
    }

    /**
     * Remove um jogador pelo UUID (desconexão / eventos via PlayerRef).
     */
    public void removePlayer(@Nonnull UUID uuid) {
        playerKits.remove(uuid);
        saveToJson();
    }

    /**
     * Salva os dados de kits dos jogadores no JSON.
     */
    private void saveToJson() {
        try {
            // Cria os diretórios se não existirem
            Files.createDirectories(dataFile.getParent());
            
            // Converte o Map para JSON
            String json = gson.toJson(playerKits);
            
            // Escreve no arquivo
            try (FileWriter writer = new FileWriter(dataFile.toFile())) {
                writer.write(json);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar kits no JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carrega os dados de kits dos jogadores do JSON.
     */
    private void loadFromJson() {
        if (!Files.exists(dataFile)) {
            return;
        }

        try (FileReader reader = new FileReader(dataFile.toFile())) {
            Type mapType = new TypeToken<Map<UUID, PlayerKitData>>(){}.getType();
            Map<UUID, PlayerKitData> loaded = gson.fromJson(reader, mapType);
            if (loaded != null) {
                playerKits.putAll(loaded);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar kits do JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
