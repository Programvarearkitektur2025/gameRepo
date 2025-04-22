# ⚡ Think Fast – Multiplayer Mobile Game

**Think Fast** is a fast-paced real-time multiplayer trivia game built using **LibGDX** and structured around **MVC** and **Singleton** architecture patterns. Players join lobbies and compete in timed trivia rounds, with real-time score tracking and seamless Firebase integration.

---

## 🎞️ Images from the Game

<img width="150" alt="singleplayer-results" src="https://github.com/user-attachments/assets/f0226ed0-4a06-47bf-ba6d-fd38f92f80af" />
<img width="150" alt="settings" src="https://github.com/user-attachments/assets/146fe008-6558-48d6-a20a-0120084f316f" />
<img width="150" alt="roundview" src="https://github.com/user-attachments/assets/bff38041-3649-49b7-ba3c-c2ac737c3f32" />
<img width="150" alt="login" src="https://github.com/user-attachments/assets/f4ff51f8-701f-40c0-b53a-7dd1ba772f79" />
<img width="150" alt="leaderboard" src="https://github.com/user-attachments/assets/3e6ab29d-655a-4f2d-9492-a9371440c887" />
<img width="150" alt="join-game" src="https://github.com/user-attachments/assets/a820c1d4-faba-494e-aaf1-6758d5a0e349" />
<img width="150" alt="howtoplay" src="https://github.com/user-attachments/assets/b35ad639-8f03-4c18-a991-c0a69386515e" />
<img width="150" alt="homepage" src="https://github.com/user-attachments/assets/11310aef-f09d-4283-952c-8066293cfb94" />
<img width="150" alt="create-game" src="https://github.com/user-attachments/assets/14a135f5-b31c-43b8-867d-c73a759311da" />


## 🎮 Features

### 👤 Authentication
- Firebase-backed user registration and login
- Persistent sessions with automatic re-authentication
- Unique usernames

### 🧑‍🤝‍🧑 Multiplayer Lobbies
- Private lobby creation with shareable codes
- Host/participant roles
- Synchronized game start and status updates
- Lobby state synced through Firebase Realtime Database

### ⏱️ Real-Time Gameplay
- Synchronized question rounds with a 30-second timer
- Score based on rarity of correct answers
- Round winner determination
- Multiplayer and singleplayer support

### 🏆 Leaderboard
- Real-time score updates after each round
- Game winner declared at the end
- Leaderboard displayed using animated view

---

## 🧱 Architecture

### 📐 Design Patterns
- **MVC (Model-View-Controller)**: clean separation of concerns
- **Singleton Services**: shared instances of core logic classes

### 📦 Project Structure
```
project-root/
│
├── android/                         # Android launcher module
│   ├── manifests/                   # AndroidManifest.xml and app permissions
│   ├── java/                        # Android-specific bootstrapping code
│   ├── assets/                      # Game assets (JSON files, textures, etc.)
│   └── res/                         # Drawable and layout resources
│
├── core/                            # Shared core logic (LibGDX)
│   └── main/
│       └── java/
│           └── io.github.progark/
│               ├── Client/          # LibGDX frontend (MVC structure)
│               │   ├── Controllers/ # Controllers like GameController, LoginController
│               │   ├── Views/       # View classes using Scene2D UI
│               │   │   ├── Game/    # Game-related views (rounds, questions, etc.)
│               │   │   ├── Login/   # Login UI and flows
│               │   │   └── Menu/    # Home, settings, tutorial views
│               │   ├── ViewManager  # Handles switching between views
│               │   └── MusicManager # Background music management
│               │
│               └── Server/          # Client-side server logic and data sync
│                   ├── database/    # Firebase database abstraction
│                   ├── Model/       # Data models: GameModel, RoundModel, UserModel, etc.
│                   ├── Network/     # Network sync helpers (if applicable)
│                   └── Service/     # GameService, AuthService, HomeService
│
├── test/                            # Test module for unit testing
│
├── lwjgl3/                          # Desktop launcher for LibGDX
│
└── README.md                        # Project documentation (this file)
```
### 🔧 Clone & Compile

Follow these steps to clone and run the project locally:

#### 1. Clone the Repository
```bash
git clone https://github.com/Programvarearkitektur2025/gameRepo.git
cd gameRepo
```

#### 2. Open in Android Studio
- Open Android Studio.
- Select **"Open an Existing Project"**.
- Navigate to the cloned folder and open it.

#### 3. Sync Gradle
- Android Studio will automatically sync Gradle.
- If not, go to **File > Sync Project with Gradle Files**.

#### 4. Run the App
- Plug in your Android device (or start an emulator).
- Select the `android` module from the run configurations.
- Press **Run** (Shift + F10 or the green play button).

> Requires:
> - JDK 8 or higher  
> - Android SDK  
> - Gradle 7.0+  
> - Internet connection for Firebase access


### 👤 Authors

- **Mathilde Lykke**
- **Une Marie Stimo**
- **Marcus Larsen-Frivoll**
- **Molly Vangen**
- **Baste Olafsen**
- **Mauritz Skogøy**
- **Stian Wilhelmsen**  


---

**Project for TDT4240 - Software Architecture**  
Department of Computer Science, NTNU
