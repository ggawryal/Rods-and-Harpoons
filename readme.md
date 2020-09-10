# Rods and Harpoons #
Rods and Harpoons is a clone of the board game *Hey, that's my fish*, written in JavaFX.

## Rules ##

The board consists of hexagonal tiles with fishes.
Players move their pawns (colored frames) sequentially on the ice floe tiles, collecting fish. The player who has collected the most fish by the end of the game wins. The game is played over several turns. Each player’s turn consists of two steps:

1. Move One Pawn
2. Collect Ice Floe Tile from which that pawn moved

During the turn, the player moves any one of his pawns as far as he wants in a straight line. The pawn may move in any one of the six directions of the hexagon, but it cannot change direction during the move. The pawn can only move onto unoccupied ice floes. It cannot move onto or through floes occupied by another pawn (even one of its own color) or spaces without ice floes. Once the current player takes his ice floe tile, the next player’s turn begins.
A player must move one of his pawns each turn. If he cannot, he does not take any more turns. The last tile, at which pawn ends game does not add into player's score. Play continues in this manner until no pawns have any more legal moves.

## Controls ##
To move, simply select one of yours pawns by clicking on it and click on the tile, at which you want to move. You can also move the view by clicking middle mouse button and moving the mouse.

## Game features ##
Game can be played by *2-4* players, either controlled by humans or AI, at three different levels. Board size and number of pawns per player is also customizable.
The game keeps highscores and also records matches in an online database, which allows them to be replayed later.
The game is serialized after each move to `game.sav` json file, which allows players to continue game in the future.

## Running ##
There is already a built .jar file, which requires Java 11.
For building and running manually, you can load project as gradle project.
The main app class is [Program.java](src/main/java/application/Program.java)
You can also check [BotArena.java](src/main/java/arena/BotArena.java), where you can test your AI strategies implementations and compare them. 
