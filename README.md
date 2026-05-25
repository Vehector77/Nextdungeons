# NextDungeons - Plugin de Dungeons para Minecraft 1.21.1

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Minecraft](https://img.shields.io/badge/minecraft-1.21.1-green.svg)
![Java](https://img.shields.io/badge/java-17-orange.svg)

## 📋 Descripción

NextDungeons es un plugin completo y altamente personalizable para Minecraft 1.21.1 que permite crear y gestionar dungeons instanciadas con soporte para MythicMobs, FastAsyncWorldEdit, Vault, PlaceholderAPI y CMI.

## ✨ Características Principales

### 🏰 Sistema de Dungeons
- **Dungeons Instanciadas**: Cada jugador obtiene su propia instancia de dungeon
- **Auto-eliminación**: Las dungeons se eliminan automáticamente al salir
- **Generación dinámica**: Las estructuras se generan usando FastAsyncWorldEdit
- **Límite de tiempo configurable**: Control completo sobre la duración de las dungeons
- **Sistema de cooldowns**: Evita el spam con cooldowns personalizables

### 👥 Sistema de Party
- **Creación de grupos**: Hasta 5 jugadores por party (configurable)
- **Invitaciones**: Sistema de invitaciones con expiración automática
- **Gestión completa**: Kick, leave, transfer leadership
- **Friendly fire opcional**: Configura si los miembros pueden dañarse entre sí
- **Recompensas compartidas**: Opción para compartir recompensas entre miembros

### 📦 Sistema de Cofres
- **Loot tables configurables**: Define drops personalizados en archivos YAML
- **Soporte para items custom**: Compatible con items de otros plugins
- **Reinicio automático**: Los cofres se reinician al reiniciar la dungeon
- **Efectos de partículas**: Efectos visuales al abrir cofres

### 🎭 Sistema de Salas
- **Salas normales**: Para combates estándar
- **Salas de mini-boss**: Desafíos intermedios
- **Salas de boss**: Jefes finales épicos
- **Configuración por comandos**: Establece el tipo de sala fácilmente

### 🔌 Integraciones (Hooks)
- **MythicMobs**: Spawn de mobs custom en dungeons
- **FastAsyncWorldEdit**: Generación de estructuras
- **Vault**: Sistema de economía y recompensas
- **PlaceholderAPI**: Placeholders personalizados
- **CMI**: Integración con CMI

### 🎨 Interfaz Gráfica
- **Menú principal**: GUI intuitiva para seleccionar dungeons
- **Información detallada**: Muestra dificultad, tiempo, jugadores requeridos
- **Click to enter**: Entra a la dungeon con un solo click

## 📥 Instalación

1. **Descarga el plugin**:
   - El archivo compilado se encuentra en `/app/NextDungeons/target/NextDungeons.jar`

2. **Copia el archivo JAR**:
   ```bash
   cp /app/NextDungeons/target/NextDungeons.jar /tu/servidor/plugins/
   ```

3. **Reinicia el servidor**

4. **Configura el plugin**:
   - Edita `plugins/NextDungeons/config.yml`
   - Edita `plugins/NextDungeons/dungeons.yml`
   - Edita `plugins/NextDungeons/loot.yml`

## ⚙️ Configuración

### config.yml

```yaml
# Configuración general
general:
  prefix: '&8[&6NextDungeons&8] '
  dungeon-world: 'dungeons_world'
  auto-create-world: true

# Configuración de dungeons
dungeons:
  max-active-per-player: 1
  auto-delete-on-exit: true
  save-player-inventory: true

# Configuración de party
party:
  enabled: true
  max-members: 5
  friendly-fire: false

# Cooldowns (en segundos)
cooldowns:
  enabled: true
  global: 300

# Límites de tiempo (en segundos)
time-limits:
  enabled: true
  default: 1800
```

### dungeons.yml

```yaml
dungeons:
  mi_dungeon:
    name: '&6&lMi Dungeon'
    description:
      - 'Una dungeon épica'
      - 'Para jugadores valientes'
    difficulty: 'NORMAL'
    min-players: 1
    max-players: 4
    time-limit: 1200
    cooldown: 300
    schematic: 'mi_dungeon'  # Archivo de schematic
    enabled: true
```

### loot.yml

```yaml
loot-tables:
  default:
    items:
      - material: DIAMOND
        amount: 3
        chance: 50.0
      - material: GOLD_INGOT
        amount: 5
        chance: 75.0
```

## 🎮 Comandos

### Comandos de Usuario

| Comando | Descripción | Permiso |
|---------|-------------|---------|
| `/dungeons` | Abre el menú de dungeons | `nextdungeons.use` |
| `/dungeons <id>` | Entra a una dungeon específica | `nextdungeons.use` |
| `/party create` | Crea una party | `nextdungeons.party` |
| `/party invite <jugador>` | Invita a un jugador | `nextdungeons.party` |
| `/party accept <jugador>` | Acepta una invitación | `nextdungeons.party` |
| `/party kick <jugador>` | Expulsa a un miembro | `nextdungeons.party` |
| `/party leave` | Sale de la party | `nextdungeons.party` |
| `/party list` | Lista los miembros | `nextdungeons.party` |
| `/party disband` | Disuelve la party | `nextdungeons.party` |

### Comandos de Admin

| Comando | Descripción | Permiso |
|---------|-------------|---------|
| `/dungeonadmin create <id> <schematic>` | Crea una dungeon | `nextdungeons.admin` |
| `/dungeonadmin delete <id>` | Elimina una dungeon | `nextdungeons.admin` |
| `/dungeonadmin list` | Lista todas las dungeons | `nextdungeons.admin` |
| `/dungeonadmin info <id>` | Info de una dungeon | `nextdungeons.admin` |
| `/dungeonadmin setroom <type>` | Establece tipo de sala | `nextdungeons.admin` |
| `/dungeonadmin reload` | Recarga la configuración | `nextdungeons.admin` |

## 🔐 Permisos

| Permiso | Descripción | Por Defecto |
|---------|-------------|-------------|
| `nextdungeons.use` | Usar dungeons | `true` |
| `nextdungeons.party` | Usar el sistema de party | `true` |
| `nextdungeons.admin` | Comandos de administración | `op` |
| `nextdungeons.create` | Crear estructuras de dungeon | `op` |

## 📊 Placeholders (PlaceholderAPI)

| Placeholder | Descripción | Ejemplo |
|-------------|-------------|---------|
| `%nextdungeons_in_dungeon%` | Si está en dungeon | `true/false` |
| `%nextdungeons_dungeon_name%` | Nombre de la dungeon | `Mi Dungeon` |
| `%nextdungeons_in_party%` | Si está en party | `true/false` |
| `%nextdungeons_party_size%` | Tamaño de la party | `3` |
| `%nextdungeons_is_party_leader%` | Si es líder | `true/false` |

## 🗂️ Estructura de Archivos

```
NextDungeons/
├── src/
│   └── main/
│       ├── java/com/nextdungeons/
│       │   ├── plugin/         # Clase principal
│       │   ├── commands/       # Comandos
│       │   ├── managers/       # Gestores
│       │   ├── dungeon/        # Sistema de dungeons
│       │   ├── party/          # Sistema de party
│       │   ├── chest/          # Sistema de cofres
│       │   ├── hooks/          # Integraciones
│       │   ├── listeners/      # Event listeners
│       │   ├── gui/            # Interfaz gráfica
│       │   ├── config/         # Configuración
│       │   └── utils/          # Utilidades
│       └── resources/
│           ├── plugin.yml
│           ├── config.yml
│           ├── dungeons.yml
│           ├── loot.yml
│           └── messages.yml
├── pom.xml
└── README.md
```

## 🔧 Dependencias

### Requeridas
- **Spigot/Paper**: 1.21.1+
- **Java**: 17+

### Opcionales
- **Vault**: Para economía
- **PlaceholderAPI**: Para placeholders
- **MythicMobs**: Para mobs custom
- **FastAsyncWorldEdit**: Para estructuras
- **CMI**: Integración adicional

## 📝 Crear una Dungeon

### 1. Construye tu estructura
Construye la estructura de tu dungeon en el mundo

### 2. Guarda la estructura
```
//copy  (con WorldEdit)
//schem save mi_dungeon
```

### 3. Mueve el schematic
Mueve el archivo `.schem` a `plugins/NextDungeons/schematics/`

### 4. Registra la dungeon
```
/dungeonadmin create mi_dungeon mi_dungeon
```

### 5. Configura la dungeon
Edita `plugins/NextDungeons/dungeons.yml` para personalizar

## 🎯 Características Avanzadas

### Loot Tables Personalizadas
Puedes crear loot tables con items de otros plugins:
```yaml
boss_loot:
  items:
    - material: DIAMOND_SWORD
      amount: 1
      chance: 40.0
```

### Integración con MythicMobs
Los mobs de MythicMobs pueden spawnearse en las salas configuradas

### Sistema de Cooldowns
Previene el farmeo excesivo con cooldowns por dungeon o globales

## 🐛 Solución de Problemas

**El mundo de dungeons no se crea:**
- Verifica que `auto-create-world` esté en `true`
- Comprueba los permisos del servidor

**Las estructuras no se pegan:**
- Asegúrate de tener FastAsyncWorldEdit instalado
- Verifica que los archivos .schem estén en la carpeta correcta

**Los cofres no tienen items:**
- Revisa las loot tables en `loot.yml`
- Asegúrate de que los materiales sean válidos

## 📜 Licencia

Este plugin fue creado para uso privado. Todos los derechos reservados.

## 👨‍💻 Desarrollo

**Compilar el plugin:**
```bash
cd /app/NextDungeons
mvn clean package
```

**Archivo compilado:**
```
/app/NextDungeons/target/NextDungeons.jar
```

## 📞 Soporte

Para soporte, preguntas o sugerencias, contacta al desarrollador.

---

**Versión**: 1.0.0  
**Minecraft**: 1.21.1  
**Java**: 17  
**Fecha**: 2026

¡Disfruta de NextDungeons! 🎮✨
