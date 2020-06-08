package game;

import board.Board;
import board.HexVector;
import board.Move;
import board.MoveChecker;
import board.drawable.tile.Tile;
import game.controllers.PlayerController;

import java.util.List;
import java.util.ArrayList;

import static application.Program.MainApp.gameScene;

public class GameManager {
    private final MoveChecker moveChecker;
    private final Board board;
    private int turn;
    private boolean gameEnded;
    private final ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<PlayerController> controllers = new ArrayList<>();
    PawnSetUpper pawnSetUpper = new PawnSetUpper();

    public int getPlayersNumber() {
        return players.size();
    }

    public GameManager(MoveChecker moveChecker, Board board, List<String> nicknames, List<PlayerController> controllers) {
        this.moveChecker = moveChecker;
        this.board = board;

        if(nicknames.size() != controllers.size()) throw new RuntimeException();
        for(int i=0; i<nicknames.size(); i++) {
            addPlayer(nicknames.get(i), controllers.get(i));
        }

        pawnSetUpper.setUpPawns(board,players,2);
    }

    public void startGame() {
        controllers.get(0).nextTurn();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(String nickname, PlayerController playerController) {
        Player player = new Player(getPlayers().size(), nickname);
        players.add(player);
        playerController.set(player,board,moveChecker);
        playerController.setActionOnMove(move -> tryToMove(player,move));
        controllers.add(playerController);
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

    public void endGame() {
        gameEnded = true;
        gameScene.showGameOver();
    }

    private void updateState() {
        if(gameEnded) return;
        for(int i = 0; i < getPlayersNumber(); i++) {
            int playerID = turn % getPlayersNumber();
            if(!canPlayerMove(players.get(playerID)))
                turn++;
            else {
                controllers.get(playerID).nextTurn();
                return;
            }
        }
        endGame();
    }

    public PlayerController getCurrentController() {
        return controllers.get(turn % getPlayersNumber());
    }

    public boolean tryToMove(Player player, Move move) {
        if(players.get(turn % getPlayersNumber()).equals(player) && moveChecker.isValidMove(move)) {
            Tile tile = board.getTileAt(move.getFrom());
            player.addPoints(tile.getScore());
            board.removeTile(move.getFrom());
            board.movePawn(move);
            player.changePawnPosition(move);
            gameScene.updatePlayerPoints(player);
            turn++;
            updateState();
            return true;
        }
        return false;
    }

}
