# DefaultKits

Sistema de kits para servidor Hytale. O objetivo é oferecer comandos de kits que configuram o estilo de jogo do jogador (itens, bônus e comportamentos específicos).

Atualmente, está em desenvolvimento o kit "archer" (arqueiro):
- Comando `/kit archer` entrega arco, flechas e equipamentos leves
- Dano de arco/flecha é aumentado somente para jogadores com o kit archer ativo
- Recompensa de munição: ao acertar um alvo válido, o jogador com kit archer recebe +2 flechas

## Instalação

### Opção 1: Download da Release (Recomendado)

1. Vá para a aba [Releases](../../releases) do repositório
2. Baixe o arquivo `DefaultKits.jar` da versão mais recente
3. Copie para a pasta `mods` do servidor Hytale
4. Reinicie o servidor

### Opção 2: Compilar localmente

1. Clone o repositório e entre na pasta:
   ```bash
   git clone <seu-repositorio>
   cd DefaultKits
   ```

2. Compile o projeto:
   ```bash
   ./gradlew build
   ```

3. Copie o arquivo `build/libs/DefaultKits.jar` para a pasta `mods` do servidor Hytale

4. Reinicie o servidor

## CI/CD

Este projeto possui **integração contínua automática** com GitHub Actions:
- A cada push na branch `main`, o projeto é compilado automaticamente
- Um arquivo `.jar` é gerado e disponibilizado nas **Releases** do GitHub
- Você nunca precisa compilar manualmente para colocar em produção

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

## Contribuindo

1. Faça um fork e crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
2. Commit suas mudanças (`git commit -m 'Add MinhaFeature'`)
3. Push para a branch (`git push origin feature/MinhaFeature`)
4. Abra um Pull Request

## Licença

MIT
