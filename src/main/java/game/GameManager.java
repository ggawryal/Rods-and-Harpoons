package game;

import board.Board;
import board.HexVector;
import board.Move;
import board.MoveChecker;
import board.drawable.tile.Tile;

import java.util.ArrayList;

public class GameManager {
    private final MoveChecker moveChecker;
    private final Board board;
    private int numOfPlayers;
    private int turn;
    private final ArrayList<Player> players = new ArrayList<>();


    public GameManager(MoveChecker moveChecker, Board board) {
        this.moveChecker = moveChecker;
        this.board = board;
    }

    private void addPlayers() {
        this.numOfPlayers = board.getNumOfPawns();
        for(int i = 1; i <= numOfPlayers; i++) {
            players.add(new Player(board.getPawnPosition(i)));
        }
    }

    public void init() {
        players.clear();
        turn = 0;
        addPlayers();
    }

    private boolean canPlayerMove(int id) {
        Player player = players.get(id);
        HexVector position = player.getPosition();
        return moveChecker.isValidMove(new Move(position, position.copy().add(new HexVector(-1, 0)))) ||
                moveChecker.isValidMove(new Move(position, position.copy().add(new HexVector(1, 0)))) ||
                moveChecker.isValidMove(new Move(position, position.copy().add(new HexVector(0, -1)))) ||
                moveChecker.isValidMove(new Move(position, position.copy().add(new HexVector(0, 1)))) ||
                moveChecker.isValidMove(new Move(position, position.copy().add(new HexVector(1, -1)))) ||
                moveChecker.isValidMove(new Move(position, position.copy().add(new HexVector(-1, 1))));
    }

    private void endGame() {
        System.out.println("GAME OVER!\nFINAL SCORE:\n");
        for(int i = 1; i <= numOfPlayers; i++) {
            Player player = players.get(i - 1);
            System.out.println("PLAYER " + i + " - " + player.getPoints() + "p\n");
        }
    }

    private void updateState() {
        for(int i = 0; i < numOfPlayers; i++) {
            int playerID = turn % numOfPlayers;
            if(!canPlayerMove(playerID))
                turn++;
            else
                return;
        }
        endGame();
    }


    public void onClickResponse(HexVector position) {
        int playerID = turn % numOfPlayers;
        Player player = players.get(playerID);
        HexVector playerPosition = player.getPosition();
        if(moveChecker.isValidMove(new Move(playerPosition, position))) {
            Tile tile = board.getTileAt(playerPosition);
            player.addPoints(tile.getScore());
            board.removeTile(playerPosition);
            board.movePawn(playerPosition, position);
            player.setPosition(position);
            System.out.println("Player_ID: " + (playerID + 1) + "\nPoints: " + player.getPoints() + "\n");
            turn++;
            updateState();
        }
    }
}
