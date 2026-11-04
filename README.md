<div align="center">

<img src="docs/readme/banner.svg" width="100%" alt="CyberTura, proyecto de grado de Laura Viviana León. Director: profesor Fabián Enrique Suárez Carvajal. Codirector: profesor Yamid Gabriel Gamba.">

### 🐾 Un portal se abre. Cinco mundos digitales están en peligro. ¿Te atreves a entrar?

<img alt="Android 9+" src="https://img.shields.io/badge/Android-9%2B-3DDC84?style=for-the-badge&logo=android&logoColor=white">
<img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-2.2-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white">
<img alt="Jetpack Compose" src="https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white">
<img alt="Firebase" src="https://img.shields.io/badge/Firestore-Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black">

<br>

**[La aventura](#-la-aventura)** ·
**[Gamificación](#-así-se-juega-la-gamificación)** ·
**[Los mundos](#-los-cinco-mundos)** ·
**[Pantallazos](#-la-aventura-en-pantallazos)** ·
**[Evaluación](#-qué-mide-la-aplicación)** ·
**[Tecnología](#%EF%B8%8F-debajo-del-portal-la-tecnología)** ·
**[Créditos](#-créditos)**

<br>

<img src="docs/readme/showcase.webp" width="100%" alt="Cinco pantallas de CyberTura: el mapa de mundos, una sopa de letras, un enfrentamiento con el Capitán Phish, un ahorcado y el Salón de las Insignias.">

</div>

<br>

> [!NOTE]
> **CyberTura** es el proyecto de grado de **Laura Viviana León**, dirigido por el **profesor Fabián Enrique Suárez Carvajal** y codirigido por el **profesor Yamid Gabriel Gamba**.
> Es una aplicación Android que convierte la ciberseguridad en una aventura gamificada para niñas y niños.

<div align="center"><img src="docs/readme/divider.svg" width="70%" alt=""></div>

## 🌌 La aventura

Internet también es un lugar donde los más pequeños juegan, conversan y aprenden. Allí encuentran anzuelos, contraseñas débiles, burlas, preguntas indiscretas y perfiles falsos. **Explicarles esos riesgos con una charla es difícil; vivirlos como una misión es otra historia.**

En CyberTura, cada niña o niño crea su propio héroe felino, cruza un portal y viaja para liberar cinco mundos digitales. Un gato guía lo acompaña y le da la bienvenida con esta misión:

> *«¡Ya estamos listos! Cinco enemigos han tomado el control de diferentes mundos digitales. Para detenerlos tendremos que viajar de mundo en mundo. Pero existe una regla: un portal solo se abrirá cuando hayas completado la misión del mundo anterior.»*
>
> — 🐱 Narración de inicio de CyberTura

Cada enemigo encarna un riesgo real. Para vencerlo hay que aprender a reconocerlo, y **eso es justamente lo que queda después de jugar**: saber qué hacer ante un enlace sospechoso, cómo crear una buena contraseña y cuándo pedir ayuda a un adulto de confianza.

<div align="center"><img src="docs/readme/divider.svg" width="70%" alt=""></div>

## 🎮 Así se juega: la gamificación

La gamificación no es un adorno en CyberTura: cada mecánica cumple una función en el aprendizaje.

| Mecánica | Cómo funciona en el juego | Qué aporta |
|:--:|---|---|
| 🦸 **Avatar propio** | Se elige alias, edad, género y traje: Spidergato, Batigato o Supergato, en versión gato o gata | La niña o el niño se siente protagonista de la historia |
| 🗺️ **Mapa con portales** | Cada mundo se desbloquea al completar el anterior | Da una meta clara y un avance por etapas |
| ⭐ **Puntos** | **+10** por acierto y **−5** por error, sin bajar nunca de cero | Retroalimentación inmediata que permite equivocarse sin castigo |
| 🧩 **Minijuegos** | Sopa de letras, ahorcado de 6 corazones y rompecabezas de 9 piezas | Refuerzan el vocabulario y mantienen la motivación |
| 🏴‍☠️ **Enfrentamiento final** | Tres situaciones de la vida real contra el enemigo, ilustradas con el héroe elegido | Lleva lo aprendido a la toma de decisiones |
| 🏅 **Insignias** | Cada mundo otorga una insignia para el Salón de las Insignias | Reconoce el logro y motiva a coleccionar |
| 🎬 **Narrativa multimedia** | Videos, narración, música por mundo, efectos de sonido y confeti | Crea ambientación y refuerza los temas sin depender solo de la lectura |
| 💾 **Progreso guardado** | Se retoma en la misma pantalla al volver a abrir la app | Nadie pierde su aventura si tiene que dejar de jugar |

### 🧭 El recorrido de cada mundo

Los cinco mundos siguen la misma estructura de **aprender → practicar → aplicar → celebrar**:

```mermaid
flowchart LR
    A["🎬 Video"] --> B["🔍 Sopa de letras"]
    B --> C["🎬 Video"]
    C --> D["🪓 Ahorcado"]
    D --> E["🎬 Video"]
    E --> F["🧩 Rompecabezas"]
    F --> G["🎬 Video"]
    G --> H["⚔️ 3 retos<br/>contra el enemigo"]
    H --> I["🏆 Victoria e insignia"]
    I --> J["🌀 Se abre<br/>el siguiente portal"]
```

### 🐾 Elige tu héroe

Cada traje tiene versión gato y gata. El personaje elegido reaparece en las ilustraciones de los enfrentamientos: **90 imágenes** personalizadas entre todos los mundos.

<div align="center">
<table>
  <tr>
    <td align="center"><img src="docs/readme/characters/spidergato.webp" width="120" alt="Spidergato"><br><sub><b>Spidergato</b></sub></td>
    <td align="center"><img src="docs/readme/characters/spidergata.webp" width="120" alt="Spidergata"><br><sub><b>Spidergata</b></sub></td>
    <td align="center"><img src="docs/readme/characters/batigato.webp" width="120" alt="Batigato"><br><sub><b>Batigato</b></sub></td>
    <td align="center"><img src="docs/readme/characters/batigata.webp" width="120" alt="Batigata"><br><sub><b>Batigata</b></sub></td>
    <td align="center"><img src="docs/readme/characters/supergato.webp" width="120" alt="Supergato"><br><sub><b>Supergato</b></sub></td>
    <td align="center"><img src="docs/readme/characters/supergata.webp" width="120" alt="Supergata"><br><sub><b>Supergata</b></sub></td>
  </tr>
</table>
</div>

<div align="center"><img src="docs/readme/divider.svg" width="70%" alt=""></div>

## 🪐 Los cinco mundos

<div align="center">
<table>
  <tr>
    <th>Mundo</th><th>Riesgo que enseña</th><th>Enemigo</th><th>Insignia</th>
  </tr>
  <tr>
    <td align="center"><img src="docs/readme/worlds/mundo1.webp" width="110" alt=""><br><b>1. El Mar de las Cadenas</b></td>
    <td align="center">🌊 Phishing</td>
    <td align="center">🏴‍☠️ Capitán Phish</td>
    <td align="center"><img src="docs/readme/badges/insignia1.webp" width="90" alt=""><br><sub>Detective Anti-Phishing</sub></td>
  </tr>
  <tr>
    <td align="center"><img src="docs/readme/worlds/mundo2.webp" width="110" alt=""><br><b>2. La Fortaleza de las Cerraduras</b></td>
    <td align="center">🔐 Cuentas y contraseñas</td>
    <td align="center">🔨 El Rompeclaves</td>
    <td align="center"><img src="docs/readme/badges/insignia2.webp" width="90" alt=""><br><sub>Guardián de las Claves</sub></td>
  </tr>
  <tr>
    <td align="center"><img src="docs/readme/worlds/mundo3.webp" width="110" alt=""><br><b>3. El Valle de las Sombras</b></td>
    <td align="center">🌑 Ciberacoso</td>
    <td align="center">👹 El Troll</td>
    <td align="center"><img src="docs/readme/badges/insignia3.webp" width="90" alt=""><br><sub>Defensor Digital</sub></td>
  </tr>
  <tr>
    <td align="center"><img src="docs/readme/worlds/mundo4.webp" width="110" alt=""><br><b>4. El Archivo de los Secretos</b></td>
    <td align="center">📁 Privacidad y datos personales</td>
    <td align="center">🕵️ El Espía</td>
    <td align="center"><img src="docs/readme/badges/insignia4.webp" width="90" alt=""><br><sub>Guardián de los Datos</sub></td>
  </tr>
  <tr>
    <td align="center"><img src="docs/readme/worlds/mundo5.webp" width="110" alt=""><br><b>5. El Reino de las Máscaras</b></td>
    <td align="center">🎭 Identidad y huella digital</td>
    <td align="center">👤 El Suplantador</td>
    <td align="center"><img src="docs/readme/badges/insignia5.webp" width="90" alt=""><br><sub>Guardián de la Identidad</sub></td>
  </tr>
</table>
</div>

<div align="center"><img src="docs/readme/divider.svg" width="70%" alt=""></div>

## 📱 La aventura en pantallazos

### 🚀 Comienza el viaje

<div align="center">
<table>
  <tr>
    <td align="center"><img src="docs/readme/screens/S1.webp" width="210" alt="Pantalla de inicio con el gato frente al portal"><br><sub><b>El portal se abre</b></sub></td>
    <td align="center"><img src="docs/readme/screens/S2.webp" width="210" alt="Creación del personaje: alias, edad, género y traje"><br><sub><b>Crea tu héroe</b></sub></td>
    <td align="center"><img src="docs/readme/screens/S3N.webp" width="210" alt="Mapa de mundos con la narración de la misión"><br><sub><b>El gato guía explica la misión</b></sub></td>
  </tr>
</table>
</div>

### 🌊 Una misión completa: el Mar de las Cadenas

<div align="center">
<table>
  <tr>
    <td align="center"><img src="docs/readme/screens/W1V.webp" width="210" alt="Video que explica qué es el phishing"><br><sub><b>🎬 Aprende con videos</b></sub></td>
    <td align="center"><img src="docs/readme/screens/W1A.webp" width="210" alt="Sopa de letras del mundo 1"><br><sub><b>🔍 Sopa de letras</b></sub></td>
    <td align="center"><img src="docs/readme/screens/W1B.webp" width="210" alt="Ahorcado del mundo 1"><br><sub><b>🪓 Ahorcado</b></sub></td>
  </tr>
  <tr>
    <td align="center"><img src="docs/readme/screens/W1C.webp" width="210" alt="Rompecabezas del mundo 1"><br><sub><b>🧩 Rompecabezas</b></sub></td>
    <td align="center"><img src="docs/readme/screens/W1E.webp" width="210" alt="Reto contra el Capitán Phish"><br><sub><b>⚔️ Reto contra el Capitán Phish</b></sub></td>
    <td align="center"><img src="docs/readme/screens/W1H.webp" width="210" alt="El gato guía muestra la insignia Detective Anti-Phishing"><br><sub><b>🏆 ¡Insignia ganada!</b></sub></td>
  </tr>
</table>
</div>

### 🗺️ Los demás mundos

<details>
<summary><b>🔐 Mundo 2: La Fortaleza de las Cerraduras</b> (contraseñas contra El Rompeclaves)</summary>
<br>
<div align="center">
<table><tr>
  <td align="center"><img src="docs/readme/screens/W2A.webp" width="200" alt="Sopa de letras del mundo 2"><br><sub>Los secretos de una buena contraseña</sub></td>
  <td align="center"><img src="docs/readme/screens/W2B.webp" width="200" alt="Ahorcado del mundo 2"><br><sub>La contraseña misteriosa</sub></td>
  <td align="center"><img src="docs/readme/screens/W2E.webp" width="200" alt="Reto del mundo 2"><br><sub>¿Cuál es la más segura?</sub></td>
</tr></table>
</div>
</details>

<details>
<summary><b>🌑 Mundo 3: El Valle de las Sombras</b> (ciberacoso contra El Troll)</summary>
<br>
<div align="center">
<table><tr>
  <td align="center"><img src="docs/readme/screens/W3A.webp" width="200" alt="Sopa de letras del mundo 3"><br><sub>Las palabras de luz</sub></td>
  <td align="center"><img src="docs/readme/screens/W3C.webp" width="200" alt="Rompecabezas del mundo 3"><br><sub>Reconstruye la imagen</sub></td>
  <td align="center"><img src="docs/readme/screens/W3E.webp" width="200" alt="Reto del mundo 3"><br><sub>¿Qué debería hacer?</sub></td>
</tr></table>
</div>
</details>

<details>
<summary><b>📁 Mundo 4: El Archivo de los Secretos</b> (privacidad contra El Espía)</summary>
<br>
<div align="center">
<table><tr>
  <td align="center"><img src="docs/readme/screens/W4A.webp" width="200" alt="Sopa de letras del mundo 4"><br><sub>Datos personales</sub></td>
  <td align="center"><img src="docs/readme/screens/W4C.webp" width="200" alt="Rompecabezas del mundo 4"><br><sub>Reconstruye la imagen</sub></td>
  <td align="center"><img src="docs/readme/screens/W4E.webp" width="200" alt="Reto del mundo 4"><br><sub>El Espía pide tu contraseña</sub></td>
</tr></table>
</div>
</details>

<details>
<summary><b>🎭 Mundo 5: El Reino de las Máscaras</b> (identidad contra El Suplantador)</summary>
<br>
<div align="center">
<table><tr>
  <td align="center"><img src="docs/readme/screens/W5A.webp" width="200" alt="Sopa de letras del mundo 5"><br><sub>Rastreando la identidad</sub></td>
  <td align="center"><img src="docs/readme/screens/W5B.webp" width="200" alt="Ahorcado del mundo 5"><br><sub>La palabra secreta</sub></td>
  <td align="center"><img src="docs/readme/screens/W5E.webp" width="200" alt="Reto del mundo 5"><br><sub>¿Este perfil es falso?</sub></td>
</tr></table>
</div>
</details>

### 🏅 Final de la aventura

<div align="center">
<table>
  <tr>
    <td align="center"><img src="docs/readme/screens/S3.webp" width="210" alt="Mapa con mundos completados y bloqueados"><br><sub><b>Los portales se van abriendo</b></sub></td>
    <td align="center"><img src="docs/readme/screens/BADGES.webp" width="210" alt="Salón de las Insignias"><br><sub><b>El Salón de las Insignias</b></sub></td>
    <td align="center"><img src="docs/readme/screens/RATING.webp" width="210" alt="Encuesta de valoración con estrellas"><br><sub><b>¡Cuéntanos tu aventura!</b></sub></td>
  </tr>
</table>
</div>

<div align="center"><img src="docs/readme/divider.svg" width="70%" alt=""></div>

## 📊 Qué mide la aplicación

Además de enseñar, CyberTura recoge información para analizar cómo se desempeñan quienes juegan:

- **Minijuegos:** tiempo, errores e intentos en la sopa de letras y el ahorcado; tiempo y movimientos en el rompecabezas.
- **Retos contra el enemigo:** intentos en cada situación.
- **Encuesta final:** 10 preguntas con escala de 1 a 5 estrellas sobre gusto, facilidad de uso, personajes, videos y comprensión de los temas, más un comentario abierto.

El perfil de juego se guarda en el dispositivo con Room, y tanto ese perfil como la encuesta se sincronizan con Cloud Firestore.

<div align="center"><img src="docs/readme/divider.svg" width="70%" alt=""></div>

## ⚙️ Debajo del portal: la tecnología

```mermaid
flowchart TB
    UI["🎨 Interfaz<br/>Jetpack Compose + Navigation"] --> VM["🧠 GameViewModel<br/>StateFlow + Coroutines"]
    VM --> DOM["📐 Dominio<br/>modelos y contratos de repositorio"]
    DOM --> REPO["🗄️ Repositorios"]
    REPO --> ROOM[("📱 Room<br/>progreso local")]
    REPO --> FS[("☁️ Cloud Firestore<br/>perfil y encuestas")]
    HILT["💉 Hilt"] -. inyecta dependencias .-> VM
    HILT -. inyecta dependencias .-> REPO
```

| Capa | Tecnología |
|---|---|
| **Lenguaje** | Kotlin 2.2 |
| **Interfaz** | Jetpack Compose, Material 3, Navigation Compose con rutas tipadas, Coil |
| **Arquitectura** | MVVM en capas (`ui`, `domain`, `data`) con Hilt, Coroutines y Flow |
| **Datos** | Room para el progreso local y Cloud Firestore para perfil y encuestas |
| **Multimedia** | `VideoView` para los videos y `MediaPlayer` para música, narración y efectos |
| **Plataforma** | Android 9 (API 28) en adelante, compilado para API 37, en orientación vertical |

<details>
<summary><b>📂 Estructura del proyecto</b></summary>

```text
app/src/main/
├── java/me/lauraleon/cybertura/
│   ├── data/          # Room, repositorios y contenido de los mundos
│   ├── di/            # Módulos de Hilt
│   ├── domain/        # Modelos y contratos de repositorio
│   └── ui/
│       ├── navigation/  # Grafo y rutas de navegación
│       ├── screens/     # Inicio, personaje, mapa, insignias, encuesta y world1 a world5
│       ├── theme/       # Colores, tipografía, confeti y layout común
│       └── viewmodel/   # GameViewModel
└── res/
    ├── drawable/      # Fondos, mundos, insignias, personajes e ilustraciones
    └── raw/           # Videos, música, narración y efectos de sonido
```

</details>

### 🛠️ Ejecutar el proyecto

**Necesitas:** Android Studio actualizado (con JDK 17 o superior), el SDK de Android 37 y un dispositivo o emulador con Android 9 o superior.

1. Clona el repositorio:

   ```bash
   git clone https://github.com/Lauleon11/CyberTuraApp.git
   ```

2. Copia tu archivo `google-services.json` de Firebase en la carpeta `app/`. No está en el repositorio y la compilación lo necesita.

3. Abre el proyecto en Android Studio, sincroniza Gradle y ejecuta la configuración **app**. También puedes compilar desde la terminal:

   ```bash
   ./gradlew installDebug
   ```

> [!TIP]
> La carpeta de videos pesa cerca de 280 MB, así que la primera descarga y la primera instalación pueden tardar unos minutos.

<div align="center"><img src="docs/readme/divider.svg" width="70%" alt=""></div>

## 🎓 Créditos

<div align="center">

**CyberTura** es un proyecto de grado.

<table>
  <tr>
    <td align="center" width="250">
      <br>🎓<br><sub><b>AUTORA</b></sub><br>
      <h3>Laura Viviana León</h3>
      <a href="https://github.com/Lauleon11">@Lauleon11</a><br><br>
    </td>
    <td align="center" width="250">
      <br>🧭<br><sub><b>DIRECTOR</b></sub><br>
      <h3>Prof. Fabián Enrique Suárez Carvajal</h3>
      <sub>Dirección del proyecto</sub><br><br>
    </td>
    <td align="center" width="250">
      <br>🤝<br><sub><b>CODIRECTOR</b></sub><br>
      <h3>Prof. Yamid Gabriel Gamba</h3>
      <sub>Codirección del proyecto</sub><br><br>
    </td>
  </tr>
</table>

<br>

<img src="docs/readme/banner_cat.webp" width="110" alt="El gato guía de CyberTura">

*«No hagas clic. No respondas. Verifica quién lo envió. Pide ayuda a un adulto de confianza.»*

**Hecho con 💜 para que las niñas y los niños naveguen seguros.**

</div>
