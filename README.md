# arrows_java
**Henry Hu, Sahan Weeratunga, December 2018** 
## Turn-based "tanks" game in Java using libGDX.
* Features a basic UI, with two players positioned on opposite sides of the screen
* Randomly generated terrain between the players
* The user(s) take turns shooting an arrow at the other player a number of times to score points and reduce the other player's health
* Win the game by reducing a player's health to 0 *(health bar shows no green)* and restart!

Created for our grade 12 computer science class. \
Navigate to arrows_java/desktop/src/com/mygdx/game/desktop/ for the desktop launcher \
\
*note: although there are build errors, the program still runs*

## Controls
```
mouse: aim
space: shoot arrow
```

## Power-ups
`POWERUPS` may randomly appear after an arrow is shot. Hit the icons with an arrow for a special effect!

* ![health](core/assets/health.png) restores one health point for the player who shot it
* ![triple](core/assets/three.png) on contact, instantly creates two arrows that fly in the same direction as the originally shot arrow