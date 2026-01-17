# Guia: Criando um Novo Mod Hytale

Este guia resume o que precisa ser alterado para criar um novo mod baseado nesta estrutura.

## Arquivos a Modificar

### 1. `manifest.json` (src/main/resources/)
```json
{
    "Group": "com.example",
    "Name": "NomeDoSeuMod",
    "Version": "1.0.0",
    "Main": "com.example.plugin.NomeDoSeuMod"
}
```
- **Name**: Nome do mod
- **Main**: Caminho completo da classe principal

### 2. `build.gradle`
Alterar nas seguintes linhas:
```gradle
group = 'com.example.plugin'        // Linha 5 - grupo do pacote
version = '1.0.0'                   // Linha 6 - versão

jar {
    archiveBaseName = 'NomeDoSeuMod'           // Linha 23
    archiveFileName = 'NomeDoSeuMod.jar'       // Linha 25

    manifest {
        attributes(
            'Implementation-Title': 'NomeDoSeuMod'  // Linha 30
        )
    }
}

// Task de clean (linha 38)
delete file("$rootDir/../NomeDoSeuMod.jar")
```

### 3. `settings.gradle`
```gradle
rootProject.name = 'NomeDoSeuMod'
```

### 4. `gradle.properties`
```properties
mod.name=NomeDoSeuMod
mod.version=1.0.0
```

## Estrutura de Pastas

```
MeuMod/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/plugin/
│       │       └── NomeDoSeuMod.java    <- Classe principal
│       └── resources/
│           └── manifest.json
├── libs/
│   └── HytaleServer.jar                 <- API do servidor (copiar)
├── build.gradle
├── settings.gradle
├── gradle.properties
└── gradle/                              <- Wrapper (copiar pasta inteira)
```

## Checklist Rápido

- [ ] Renomear pasta do projeto
- [ ] Editar `manifest.json` (Name, Main)
- [ ] Editar `build.gradle` (archiveBaseName, archiveFileName, Implementation-Title, cleanExternalJar)
- [ ] Editar `settings.gradle` (rootProject.name)
- [ ] Editar `gradle.properties` (mod.name)
- [ ] Renomear/criar classe principal em `src/main/java/com/example/plugin/`
- [ ] Copiar `libs/HytaleServer.jar`

## Build

```bash
./gradlew clean build
```

O JAR será gerado na pasta pai do projeto (configurado em `destinationDirectory`).
