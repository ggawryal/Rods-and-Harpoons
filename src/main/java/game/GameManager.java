package game;

import board.Board;
import board.HexVector;
import board.Move;
import board.MoveChecker;
import board.drawable.tile.PlayerTile;
import board.drawable.tile.Tile;

import java.util.ArrayList;

public class GameManager {
    private final MoveChecker moveChecker;
    private final Board board;
    private final int numOfPlayers;
    private int turn;
    private final ArrayList<Player> players = new ArrayList<>();


    public GameManager(MoveChecker moveChecker, Board board, int numOfPlayers) {
        this.moveChecker = moveChecker;
        this.board = board;
        this.numOfPlayers = numOfPlayers;
    }


    private void addPlayer(int id) {
        if(players.size() == numOfPlayers) return;
        HexVector position = board.getRandomPosition();
        while(invalidNewPlayerPosition(position)) position = board.getRandomPosition();
        board.removeTile(position);

        Tile tile = new PlayerTile("hex_P" + id + ".png");
        board.addTile(tile, position);

        players.add(new Player(position));
    }


    public void init() {
        players.clear();
        turn = 0;
        for(int i = 0; i < numOfPlayers; i++) {
            addPlayer(i + 1);
        }
    }

    private boolean invalidNewPlayerPosition(HexVector position) {
        for(Player player : players) {
            if(player.getPosition().equals(position)) return true;
        }
        return false;
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
        if(moveChecker.isValidMove(new Move(player.getPosition(), position))) {
            Tile tile = board.getTileAt(position);
            player.addPoints(tile.getScore());
            board.moveTile(player.getPosition(), position);
            player.setPosition(position);
            System.out.println("Player_ID: " + (playerID + 1) + "\nPoints: " + player.getPoints() + "\n");
            turn++;
            updateState();
        }
    }
}
