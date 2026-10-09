# Walkthrough - Sistema de Guardado Automático y Persistencia en CyberTura

Se ha implementado con éxito un sistema robusto de persistencia y guardado automático utilizando **Room** y **Architecture Components (ViewModel + Hilt + Coroutines)** para garantizar que el progreso del jugador nunca se pierda ante cierres de la app, eliminación de aplicaciones recientes, reinicios o cierre por falta de memoria.

---

## Cambios Realizados

### 1. Modelos de Dominio y Entidades de Base de Datos (`PlayerProfile`, `PlayerProfileEntity`)
- Actualizado [`PlayerProfile.kt`](file:///D:/Users/fsuarez/AndroidStudioProjects/CyberTuraApp/app/src/main/java/me/lauraleon/cybertura/domain/model/PlayerProfile.kt) y [`PlayerProfileEntity.kt`](file:///D:/Users/fsuarez/AndroidStudioProjects/CyberTuraApp/app/src/main/java/me/lauraleon/cybertura/data/local/entity/PlayerProfileEntity.kt) para almacenar:
  - Apodo (`nickname`) y edad (`age`).
  - Género (`gender`) y traje/personaje seleccionado (`costumeId`).
  - Estado de la narrativa de introducción (`hasPlayedIntroNarrative`).

### 2. Base de Datos Room y Configuración de Migración (`CyberturaDatabase`, `DataModule`)
- Configurado [`CyberturaDatabase.kt`](file:///D:/Users/fsuarez/AndroidStudioProjects/CyberTuraApp/app/src/main/java/me/lauraleon/cybertura/data/local/CyberturaDatabase.kt) y [`DataModule.kt`](file:///D:/Users/fsuarez/AndroidStudioProjects/CyberTuraApp/app/src/main/java/me/lauraleon/cybertura/di/DataModule.kt`) con `.fallbackToDestructiveMigration(true)` para asegurar una transición limpia ante cambios de esquema en base de datos.

### 3. ViewModel de Estado del Juego (`GameViewModel`)
- Creado [`GameViewModel.kt`](file:///D:/Users/fsuarez/AndroidStudioProjects/CyberTuraApp/app/src/main/java/me/lauraleon/cybertura/ui/viewmodel/GameViewModel.kt`) inyectado con Hilt, encargado de:
  - Observar el perfil del jugador y el progreso de los mundos en tiempo real mediante `Flow` / `StateFlow`.
  - Guardar automáticamente el perfil del jugador al completar la configuración inicial (`Screen2`).
  - Actualizar el estado de la narrativa introductoria y completar mundos (`completeWorld`) de forma inmediata al finalizar cada nivel.

### 4. Navegación Inteligente y Restauración Automática (`CyberturaNavGraph`)
- Actualizado [`CyberturaNavGraph.kt`](file:///D:/Users/fsuarez/AndroidStudioProjects/CyberTuraApp/app/src/main/java/me/lauraleon/cybertura/ui/navigation/CyberturaNavGraph.kt`) para:
  - Cargar los datos guardados en Room antes de decidir la pantalla inicial.
  - **Continuación automática**: Si existe un perfil guardado con apodo, la app inicia directamente en [`Screen3`](file:///D:/Users/fsuarez/AndroidStudioProjects/CyberTuraApp/app/src/main/java/me/lauraleon/cybertura/ui/screens/screen3/Screen3.kt`) (Mapa de mundos). Si no hay partida, inicia en [`Screen1`](file:///D:/Users/fsuarez/AndroidStudioProjects/CyberTuraApp/app/src/main/java/me/lauraleon/cybertura/ui/screens/screen1/Screen1.kt`).
  - Evitar parpadeos al iniciar mostrando un indicador de carga sincronizado con la base de datos.

---

## Dónde se Guarda el Progreso
- **Base de datos local**: SQLite / Room ubicada en el almacenamiento interno de la app bajo el nombre `cybertura.db`.
- **Tablas**:
  - `player_profile`: Almacena apodo, edad, género, traje y estado de la narrativa.
  - `world_progress`: Almacena el estado de cada mundo (Bloqueado, Desbloqueado, Completado) y su fecha de finalización.

---

## Cómo se Restaura
Al iniciar la aplicación, `GameViewModel` lee los datos de Room en segundo plano. `CyberturaNavGraph` evalúa el perfil recuperado:
- Si el usuario ya había creado su personaje y avanzado, la app restaura sus datos (`gender`, `costumeId`, `unlockedWorldMax`) y lo lleva directo a la selección de mundos (`Screen3`).

---

## Pruebas Recomendadas
1. **Flujo nuevo**: Abre la aplicación en una instalación limpia, verifica que inicie en [`Screen1`](file:///D:/Users/fsuarez/AndroidStudioProjects/CyberTuraApp/app/src/main/java/me/lauraleon/cybertura/ui/screens/screen1/Screen1.kt`), pasa por [`Screen2`](file:///D:/Users/fsuarez/AndroidStudioProjects/CyberTuraApp/app/src/main/java/me/lauraleon/cybertura/ui/screens/screen2/Screen2.kt`) eligiendo alias y personaje, y llega a [`Screen3`](file:///D:/Users/fsuarez/AndroidStudioProjects/CyberTuraApp/app/src/main/java/me/lauraleon/cybertura/ui/screens/screen3/Screen3.kt`).
2. **Persistencia por cierre de app**: Cierra la aplicación por completo (quítala de aplicaciones recientes o fuerza su detención en Ajustes). Vuelve a abrirla: **debe abrir directamente en el mapa de mundos ([`Screen3`](file:///D:/Users/fsuarez/AndroidStudioProjects/CyberTuraApp/app/src/main/java/me/lauraleon/cybertura/ui/screens/screen3/Screen3.kt`))** con tu personaje y progreso guardados.
3. **Progreso de Mundos**: Completa el Mundo 1 hasta desbloquear el Mundo 2, sal de la aplicación y entra de nuevo; verifica que el Mundo 2 permanezca desbloqueado.
