# DefaultKits

Sistema de kits para servidor Hytale. O objetivo é oferecer comandos de kits que configuram o estilo de jogo do jogador (itens, bônus e comportamentos específicos).

Atualmente, está em desenvolvimento o kit "archer" (arqueiro):
- Comando `/kit archer` entrega arco, flechas e equipamentos leves
- Dano de arco/flecha é aumentado somente para jogadores com o kit archer ativo
- Recompensa de munição: ao acertar um alvo válido, o jogador com kit archer recebe +2 flechas

## Instalação

1. Compile o projeto:
   ```bash
   ./gradlew build
   ```

2. Copie o arquivo gerado em `build/libs/DefaultKits-*.jar` para a pasta `mods` do servidor Hytale

3. Reinicie o servidor

## Requisitos

- Java 21
- Hytale Server

## Estrutura (parcial)

```
src/main/java/com/example/plugin/
├── DefaultKits.java                      # Classe principal do plugin
├── commands/KitCommand.java              # Comando /kit
├── kit/KitManager.java                   # Estado dos kits ativos por jogador
└── listeners/
    ├── ArrowDamageMultiplier.java        # Multiplica dano de arco (somente kit archer)
    └── ArrowAmmoRewardListener.java      # Dá +2 flechas em acerto (somente kit archer)
```

## Uso

- Jogador entra no servidor: dano normal de arco/flecha
- Jogador executa `/kit archer`: passa a causar dano aumentado com arco/flecha e ganhar flechas ao acertar

## Roadmap

- Novos kits (warrior, mage) com efeitos específicos
- Persistência de estado de kits entre reinícios
- Comando para limpar kit ativo (ex.: `/kit reset`)

## Licença

MIT
