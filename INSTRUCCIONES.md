# 🎮 NextDungeons - Plugin Compilado y Listo

## ✅ Estado: COMPLETADO Y FUNCIONAL

El plugin NextDungeons ha sido desarrollado y compilado exitosamente.

## 📦 Archivo del Plugin

**Ubicación**: `/app/NextDungeons.jar` (58 KB)

También disponible en: `/app/NextDungeons/target/NextDungeons.jar`

## 📊 Estadísticas del Proyecto

- **Clases Java creadas**: 26 archivos
- **Tamaño del JAR**: 58 KB
- **Versión de Java**: 17
- **Versión de Minecraft**: 1.21.1
- **Sistema de build**: Maven

## 🚀 Instalación Rápida

1. **Descarga el plugin**:
   ```bash
   # El archivo ya está en /app/NextDungeons.jar
   ```

2. **Copia a tu servidor**:
   ```bash
   cp /app/NextDungeons.jar /ruta/a/tu/servidor/plugins/
   ```

3. **Reinicia el servidor** y el plugin se cargará automáticamente

4. **Configuración inicial**:
   - Los archivos de configuración se generarán en `plugins/NextDungeons/`
   - Edita `config.yml`, `dungeons.yml` y `loot.yml` según tus necesidades

## 🎯 Características Implementadas

### ✅ Sistema Core
- [x] Plugin principal con sistema de gestión
- [x] ConfigManager para múltiples archivos de configuración
- [x] Sistema de mensajes personalizado
- [x] Creación automática de mundo de dungeons

### ✅ Sistema de Dungeons
- [x] DungeonManager para gestión de instancias
- [x] DungeonInstance con generación y limpieza automática
- [x] DungeonData con configuración completa
- [x] Sistema de cooldowns por dungeon
- [x] Límites de tiempo configurables
- [x] Teleportación automática
- [x] Sistema de recompensas

### ✅ Sistema de Party
- [x] PartyManager completo
- [x] Creación y gestión de grupos
- [x] Sistema de invitaciones con expiración
- [x] Kick, leave, transfer leadership
- [x] Lista de miembros
- [x] Límite configurable de jugadores

### ✅ Sistema de Cofres
- [x] ChestManager con loot tables
- [x] LootTable con sistema de probabilidades
- [x] DungeonChest con gestión individual
- [x] Soporte para items custom
- [x] Reinicio automático de cofres

### ✅ Sistema de Comandos
- [x] /dungeons - Comando principal
- [x] /dungeonadmin - Comandos de administración
- [x] /party - Gestión completa de party
- [x] Autocompletado y ayuda

### ✅ Integraciones (Hooks)
- [x] VaultHook - Economía
- [x] PlaceholderHook - Placeholders custom
- [x] MythicMobsHook - Mobs custom
- [x] FAWEHook - Generación de estructuras
- [x] CMIHook - Integración con CMI

### ✅ Sistema de Listeners
- [x] PlayerJoinListener
- [x] PlayerQuitListener (auto-exit de dungeons)
- [x] PlayerDeathListener (respawn en dungeon)
- [x] ChestListener (loot automático)
- [x] DungeonListener

### ✅ Sistema de GUI
- [x] GUIManager con menú interactivo
- [x] Menú de selección de dungeons
- [x] Información visual de dungeons
- [x] Click to enter

### ✅ Utilidades
- [x] MessageUtils para mensajes coloreados
- [x] RoomData para tipos de salas
- [x] Sistema de formateo de tiempo

## 📁 Estructura del Proyecto

```
NextDungeons/
├── pom.xml                          # Configuración de Maven
├── README.md                        # Documentación completa
├── INSTRUCCIONES.md                 # Este archivo
└── src/main/
    ├── java/com/nextdungeons/
    │   ├── plugin/
    │   │   └── NextDungeons.java    # Clase principal
    │   ├── commands/
    │   │   ├── DungeonCommand.java
    │   │   ├── DungeonAdminCommand.java
    │   │   └── PartyCommand.java
    │   ├── config/
    │   │   └── ConfigManager.java
    │   ├── dungeon/
    │   │   ├── DungeonManager.java
    │   │   ├── DungeonInstance.java
    │   │   ├── DungeonData.java
    │   │   └── RoomData.java
    │   ├── party/
    │   │   ├── PartyManager.java
    │   │   └── Party.java
    │   ├── chest/
    │   │   ├── ChestManager.java
    │   │   ├── DungeonChest.java
    │   │   └── LootTable.java
    │   ├── hooks/
    │   │   ├── VaultHook.java
    │   │   ├── PlaceholderHook.java
    │   │   ├── MythicMobsHook.java
    │   │   ├── FAWEHook.java
    │   │   └── CMIHook.java
    │   ├── listeners/
    │   │   ├── PlayerJoinListener.java
    │   │   ├── PlayerQuitListener.java
    │   │   ├── PlayerDeathListener.java
    │   │   ├── ChestListener.java
    │   │   └── DungeonListener.java
    │   ├── gui/
    │   │   └── GUIManager.java
    │   └── utils/
    │       └── MessageUtils.java
    └── resources/
        ├── plugin.yml              # Definición del plugin
        ├── config.yml              # Configuración principal
        ├── dungeons.yml            # Configuración de dungeons
        ├── loot.yml                # Tablas de loot
        └── messages.yml            # Mensajes del plugin
```

## 🎮 Uso Rápido

### Para Jugadores

1. **Ver dungeons disponibles**:
   ```
   /dungeons
   ```

2. **Entrar a una dungeon**:
   ```
   /dungeons <nombre>
   ```

3. **Crear una party**:
   ```
   /party create
   /party invite <jugador>
   ```

### Para Administradores

1. **Crear una dungeon**:
   ```
   /dungeonadmin create mi_dungeon schematic_name
   ```

2. **Listar dungeons**:
   ```
   /dungeonadmin list
   ```

3. **Recargar configuración**:
   ```
   /dungeonadmin reload
   ```

## 📝 Configurar tu Primera Dungeon

1. **Construye la estructura** en el mundo

2. **Guarda con WorldEdit**:
   ```
   //copy
   //schem save mi_primera_dungeon
   ```

3. **Mueve el archivo** `.schem` a `plugins/NextDungeons/schematics/`

4. **Edita** `dungeons.yml`:
   ```yaml
   dungeons:
     primera:
       name: '&6Mi Primera Dungeon'
       description:
         - 'Una dungeon de prueba'
       difficulty: 'EASY'
       min-players: 1
       max-players: 4
       time-limit: 600
       cooldown: 180
       schematic: 'mi_primera_dungeon'
       enabled: true
   ```

5. **Recarga** el plugin:
   ```
   /dungeonadmin reload
   ```

6. **¡Pruébalo!**:
   ```
   /dungeons primera
   ```

## 🔌 Plugins Recomendados

### Esenciales
- **Spigot/Paper 1.21.1**: Servidor base

### Opcionales (mejoran funcionalidad)
- **FastAsyncWorldEdit**: Para generar estructuras (RECOMENDADO)
- **Vault**: Para economía y recompensas
- **MythicMobs**: Para mobs custom en dungeons
- **PlaceholderAPI**: Para placeholders en otros plugins
- **CMI**: Integración adicional

## ⚠️ Notas Importantes

1. **Compatibilidad**: 
   - Funciona en Spigot y Paper 1.21.1
   - Requiere Java 17 o superior

2. **Performance**:
   - Las dungeons se generan de forma asíncrona
   - Se eliminan automáticamente para ahorrar recursos
   - Cooldowns previenen spam

3. **Schematics**:
   - Los archivos deben estar en formato `.schem` o `.schematic`
   - Deben ubicarse en `plugins/NextDungeons/schematics/`
   - Requiere FastAsyncWorldEdit para funcionar

4. **Mundo de Dungeons**:
   - Se crea automáticamente al iniciar
   - Se llama `dungeons_world` por defecto
   - No se guarda automáticamente (mejor performance)

## 🐛 Resolución de Problemas

**El plugin no carga:**
- Verifica que tienes Java 17+
- Comprueba que la versión de Spigot/Paper es 1.21.1

**Las dungeons no se generan:**
- Instala FastAsyncWorldEdit
- Verifica que los archivos .schem existan
- Comprueba los logs del servidor

**Los cofres están vacíos:**
- Edita `loot.yml` para agregar items
- Asegúrate de que los nombres de materiales sean correctos

## 📞 Información Adicional

- **Documentación completa**: Ver `README.md`
- **Código fuente**: Carpeta `src/`
- **Configuración**: Ver archivos `.yml` en resources

## ✨ Próximas Mejoras Posibles

- [ ] Sistema de niveles de dificultad dinámicos
- [ ] Rankings y leaderboards
- [ ] Recompensas más complejas
- [ ] Más tipos de salas especiales
- [ ] Boss fights con fases
- [ ] Integración con más plugins

---

**¡El plugin está listo para usar!** 🎉

Simplemente copia el archivo `/app/NextDungeons.jar` a tu servidor y comienza a crear dungeons épicas.
