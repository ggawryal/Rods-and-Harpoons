package game;

import board.Board;
import board.HexVector;
import board.Move;
import board.MoveChecker;
import board.drawable.tile.Tile;

import java.util.List;
import java.util.ArrayList;

import static application.Program.MainApp.gameScene;

public class GameManager {
    private final MoveChecker moveChecker;
    private final Board board;
    private int turn;
    private final ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<PlayerController> controllers = new ArrayList<>();
    PawnSetUpper pawnSetUpper = new PawnSetUpper();

    public int getPlayersNumber() {
        return players.size();
    }

    public GameManager(MoveChecker moveChecker, Board board, List<PlayerController> controllers) {
        this.moveChecker = moveChecker;
        this.board = board;

        for(PlayerController c : controllers)
            addPlayer(c);

        pawnSetUpper.setUpPawns(board,players,2);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(PlayerController playerController) {
        Player player = new Player(getPlayers().size());
        players.add(player);
        playerController.setPlayerAndBoard(player,board);
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

    private void endGame() {
        gameScene.showGameOver();
    }

    private void updateState() {
        for(int i = 0; i < getPlayersNumber(); i++) {
            int playerID = turn % getPlayersNumber();
            if(!canPlayerMove(players.get(playerID)))
                turn++;
            else
                return;
        }
        endGame();
    }

    public PlayerController getCurrentController() {
        return controllers.get(turn % getPlayersNumber());
    }

    public boolean tryToMove(Player player, Move move) {
        if(moveChecker.isValidMove(move)) {
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
