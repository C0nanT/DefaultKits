# Guia Rápido - Hytale Server Lib

Referência rápida para encontrar o que você precisa na lib do Hytale.

---

## Comandos

**Onde:** `libs/src/com/hypixel/hytale/server/core/command/`

| Arquivo | Descrição |
|---------|-----------|
| `system/basecommands/AbstractPlayerCommand.java` | Classe base para comandos de jogador |
| `system/AbstractCommand.java` | Classe base genérica |
| `system/CommandManager.java` | Registro e execução de comandos |
| `system/CommandContext.java` | Contexto de execução (quem executou, args) |
| `system/arguments/` | Sistema de argumentos (Required, Optional, Flag) |

**Exemplo existente:** `commands/player/inventory/GiveCommand.java`

---

## Inventário

**Onde:** `libs/src/com/hypixel/hytale/server/core/inventory/`

| Arquivo | Descrição |
|---------|-----------|
| `Inventory.java` | Inventário completo do jogador |
| `ItemStack.java` | Representa um item (id, quantidade, durabilidade, metadata) |
| `container/ItemContainer.java` | Interface para manipular containers |

**Seções do inventário:**
- `hotbar` - 9 slots
- `storage` - 36 slots (4x9)
- `armor` - 4 slots
- `utility` - 4 slots
- `tools` - 23 slots

---

## Jogador

**Onde:** `libs/src/com/hypixel/hytale/server/core/entity/entities/`

| Arquivo | Descrição |
|---------|-----------|
| `Player.java` | Classe principal do jogador |
| `player/HotbarManager.java` | Gerencia hotbar |

**Acesso ao inventário:**
```java
Player player = ...;
Inventory inventory = player.getInventory();
```

---

## Itens

**Onde:** `libs/src/com/hypixel/hytale/server/core/asset/type/item/`

| Arquivo | Descrição |
|---------|-----------|
| `Item.java` | Definição de item |
| `config/ItemWeapon.java` | Config de armas |
| `config/ItemArmor.java` | Config de armaduras |

**Criar ItemStack:**
```java
ItemStack item = new ItemStack("item_id", quantidade);
```

---

## Eventos

**Onde:** `libs/src/com/hypixel/hytale/server/core/event/events/`

| Evento | Quando dispara |
|--------|----------------|
| `player/PlayerConnectEvent.java` | Jogador conecta |
| `player/PlayerReadyEvent.java` | Jogador pronto |
| `entity/LivingEntityInventoryChangeEvent.java` | Inventário muda |

---

## Estrutura Resumida

```
libs/src/com/hypixel/hytale/
├── server/core/
│   ├── command/          # Sistema de comandos
│   │   ├── system/       # Framework base
│   │   └── commands/     # Implementações
│   ├── inventory/        # Sistema de inventário
│   ├── entity/           # Entidades e jogadores
│   │   └── entities/     # Player.java aqui
│   ├── event/            # Sistema de eventos
│   └── asset/type/item/  # Definição de itens
└── protocol/
    └── packets/inventory/ # Packets de rede
```

---

## IDs de Itens

**Onde:** `Assets/Server/Item/Items/`

O ID do item = nome do arquivo JSON (sem `.json`).

| Categoria | Pasta | Exemplo de ID |
|-----------|-------|---------------|
| Espadas | `Weapon/Sword/` | `Weapon_Sword_Iron` |
| Arcos | `Weapon/Shortbow/` | `Weapon_Shortbow_Crude` |
| Flechas | `Weapon/Arrow/` | `Weapon_Arrow_Crude` |
| Escudos | `Weapon/Shield/` | `Weapon_Shield_Wood` |
| Staffs | `Weapon/Staff/` | `Weapon_Staff_Wood` |
| Armaduras | `Armor/{Material}/` | `Armor_Iron_Head`, `Armor_Iron_Chest` |
| Ferramentas | `Tool/Pickaxe/` | `Tool_Pickaxe_Iron` |

**Listar todos:**
```bash
find Assets/Server/Item/Items -name "*.json"
```

---

## Para Sistema de Kits - Onde Olhar

1. **Criar comando `/kit`:** Olhe `AbstractPlayerCommand.java` e `GiveCommand.java` como exemplo
2. **Dar itens:** Use `Inventory.java` e `ItemStack.java`
3. **Pegar jogador do comando:** Use `CommandContext.java`
4. **IDs de itens:** Veja pasta `Assets/Server/Item/Items/`

---

## Fluxo Básico - Kit

```
1. Jogador digita /kit archer
2. AbstractPlayerCommand recebe e parseia
3. Já tem acesso direto ao Player
4. Player.getInventory() retorna Inventory
5. Cria ItemStack para cada item do kit
6. Adiciona ao inventário
```

A API base do jogo fica em `libs/src/com/hypixel/hytale`, use ela para navegadr nos arquivos do jogo e entender o código existente.
