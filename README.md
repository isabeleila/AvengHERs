# AvengHERs

A Marvel-themed 2D fighting platformer created for Quinnipiac University's SER225 Agile Development class. Two players battle head-to-head across multiple stages, each choosing from four Marvel heroes. Supports local 2-player and vs CPU modes.

## Team

- Isabela Ayers
- Tripp Menhall
- Tyler Rinko
- Reeya Patel
- Megan Mohr

---

## How to Run

**Requirements:** Java 11 or later


1. Clone or download the repository
2. Open the project in your IDE
3. Run `Game.java` located in the `src/Game/` package

The map editor (not required to play) is in the `MapEditor` package → `MapEditor.java`.

---

## Game Flow

```
Main Menu
  ├── Play Game → Tutorial → Character Select → Level Select → Level
  ├── Tutorial  → Tutorial (standalone) → Main Menu
  └── Credits
```

---

## Characters

| Character | Description |
|---|---|
| **Hulk** | High health, heavy hitter |
| **Iron Man** | Balanced ranged fighter |
| **Captain America** | Agile and durable |
| **Spider-Man** | Fast, quick attacks |

---

## Levels

| Level | Description |
|---|---|
| **Level 1** | Classic rooftop platform stage |
| **Level 2** | Multi-tier platform arena |
| **Level 3** | Outdoor stage with water hazards — falling into water is instant death |

---

## Controls

### Menu Navigation
| Key | Action |
|---|---|
| W / S or Up / Down | Move selection |
| Space | Confirm selection |

### Character Select Screen
| Action | Player 1 | Player 2 / CPU |
|---|---|---|
| Navigate characters | W / A / S / D | Arrow keys |
| Confirm selection | Q | Shift |

**Mode setup (shown before character select):**
- Press **C** to toggle between Player 2 and vs CPU mode
- In vs CPU mode, press **1**, **2**, or **3** to choose difficulty (Regular / Hard / Impossible)
- Press **Enter** to confirm and proceed to character selection

### Level Select Screen
| Key | Action |
|---|---|
| Left / Right arrows | Browse levels |
| Space | Select level |

### In-Game
| Action | Player 1 | Player 2 |
|---|---|---|
| Move left | A | Left arrow |
| Move right | D | Right arrow |
| Jump | W | Up arrow |
| Crouch | S | Down arrow |
| Shoot | Q | Shift |

### Misc
| Key | Action |
|---|---|
| F | Toggle fullscreen |
| Escape | Exit fullscreen |

---

## vs CPU Mode

Select **vs CPU** on the mode setup screen before character select. After Player 1 picks their character, use the arrow keys to pick the CPU's character and press **Shift** to confirm.

Three difficulty levels are available:

| Difficulty | Behavior |
|---|---|
| **Regular** | Slower reactions, pauses often, easier to outmaneuver |
| **Hard** | Faster decisions, smarter platform navigation |
| **Impossible** | Near-instant reactions, aggressive pursuit, frequent shooting |
