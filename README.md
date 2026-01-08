<p align="center">
  <img src="_assets/cover.png" alt="Monster Game Cover" width="400"/>
</p>

# 🎮 Monster University Game – Android Application

## 📖 Overview
Monster University Game is an Android arcade-style game where the player controls a character moving between lanes to avoid obstacles, collect bonuses, and achieve the highest possible score.  
At the end of each game, the score is saved and displayed in a **Top 10 leaderboard**, with each high score also linked to the player’s geographic location and shown on a map.

The application is developed in **Kotlin**, with a strong focus on clean architecture, proper lifecycle handling, and integration of sensors, sound, and location services.

---

## ⭐ Key Features

- 🎮 Real-time gameplay with a game loop
- 🧭 Multiple control modes:
  - Button controls
  - Tilt (sensor-based) controls
- ⚡ Multiple game modes (slow, fast, sensors)
- 🏆 High score system (Top 10 leaderboard)
- 🧑 Player name saved with each high score
- 🗺️ High scores displayed on Google Maps
- 🔊 Sound effects and background music
- 📳 Vibration feedback on collisions
- 🔄 Full Android lifecycle support

---

## Demonstration
https://github.com/kerenkay/applications-assignment1/blob/master/_assets/demo.mp4

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Ladybug or newer  
- **Kotlin**: Version 1.9 or higher  
- **Minimum SDK**: 26  

### Installation

1. Clone the repository: https://github.com/kerenkay/applications-assignment1.git
1. Open the project in **Android Studio**
2. Add a valid **Google Maps API key**
3. Sync the Gradle files
4. Run the app on an emulator or a physical device
5. Choose a game mode from the home screen and start playing

> Note: Location permission is required to display score locations on the map.

---

## 🕹️ How to Play

### 🎯 Goal
Score as many points as possible before losing all lives.

### 👾 Gameplay
- The player character is positioned at the bottom of the screen
- Obstacles and bonuses fall from the top
- The score increases continuously over time
- Hitting an obstacle:
  - Reduces one life
  - Triggers sound and vibration feedback
- Collecting a bonus:
  - Adds extra points to the score

### 🎮 Controls
- **Buttons mode** – Tap left or right buttons to move
- **Tilt mode** – Tilt the device to move the character
- In sensor mode, game speed dynamically changes based on tilt intensity

---

## 🏁 Game Over
When all lives are lost:
1. The game stops
2. The final score is displayed
3. If the score is in the Top 10, the player is prompted to enter a name
4. The score is saved
5. The Top 10 leaderboard and map can be viewed

---

## 🗺️ Leaderboard & Map
- The Top 10 leaderboard displays:
  - Rank (1–10)
  - Player name
  - Score
- Selecting a score:
  - Centers the map on the location where the score was achieved
- Each high score is represented as a marker on the map

---

## 🛠️ Technologies Used
- Kotlin
- Android SDK
- Google Maps SDK
- Sensors API
- SharedPreferences
- MediaPlayer / SoundPool
- Material Design Components

---

🎉 **Enjoy the game and have fun!**
