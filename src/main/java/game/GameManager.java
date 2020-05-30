package game;

import board.Board;
import board.HexVector;
import board.Move;
import board.MoveChecker;
import board.drawable.tile.Tile;

import java.util.ArrayList;

import static application.Program.MainApp.gameScene;

public class GameManager {
    private final MoveChecker moveChecker;
    private final Board board;
    private int numOfPlayers;
    private HexVector activePawnPosition;
    private int turn;
    private final ArrayList<Player> players = new ArrayList<>();


    public GameManager(MoveChecker moveChecker, Board board, int numOfPlayers) {
        this.moveChecker = moveChecker;
        this.board = board;
        this.numOfPlayers = numOfPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    private void addPlayers(int numOfPlayers) {
        for(int i = 1; i <= numOfPlayers; i++) {
            Player player = new Player(i - 1);
            for(HexVector position : board.getPawnsPositions(i)) {
                player.addPawn(position);
            }
            players.add(player);
        }
    }

    public void init() {
        players.clear();
        turn = 0;
        addPlayers(numOfPlayers);
    }

    private boolean canPawnMove(HexVector position) {
        return !moveChecker.getPossibleMoves(position).isEmpty();
    }

    private boolean canPlayerMove(Player player) {
        for(int i = 0; i < player.getPawnsCount(); i++) {
            if(canPawnMove(player.getPawnPosition(i)))
                return true;
        }
        return false;
    }

    private void endGame() {
        gameScene.showGameOver();
    }

    private void updateState() {
        for(int i = 0; i < numOfPlayers; i++) {
            int playerID = turn % numOfPlayers;
            if(!canPlayerMove(players.get(playerID)))
                turn++;
            else
                return;
        }
        endGame();
    }

    private void tryToMove(Player player, Move move) {
        if(moveChecker.isValidMove(move)) {
            Tile tile = board.getTileAt(move.getFrom());
            player.addPoints(tile.getScore());
            board.removeTile(move.getFrom());
            board.movePawn(move);
            player.changePawnPosition(move);
            gameScene.updatePlayerPoints(player);
            activePawnPosition = null;
            turn++;
            updateState();
        }
    }

    private void deactivatePawn() {
        if(activePawnPosition != null) {
            board.switchPawnSelection(activePawnPosition);
        }
        activePawnPosition = null;
    }

    private void activatePawnAt(HexVector position) {
        activePawnPosition = position;
        board.switchPawnSelection(position);
    }

    public void onClickResponse(HexVector position) {
        int playerID = turn % numOfPlayers;
        Player player = players.get(playerID);
        if(player.hasPawnAt(position)) {
            deactivatePawn();
            activatePawnAt(position);
        } else {
            if(activePawnPosition != null)
                tryToMove(player, new Move(activePawnPosition, position));
        }
    }
}
