package game;

import board.Board;
import board.HexVector;
import board.Move;
import board.MoveChecker;
import board.drawable.pawn.Pawn;
import board.drawable.tile.Tile;
import game.controllers.PlayerController;
import util.GameInfo;
import util.PlayerMove;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;



public class GameManager {
    private final MoveChecker moveChecker;
    private final Board board;
    private int turn;
    private boolean gameEnded;
    private final ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<PlayerController> controllers = new ArrayList<>();
    private final ArrayList<PlayerMove> playersMoves = new ArrayList<PlayerMove>();
    PawnSetUpper pawnSetUpper = new PawnSetUpper();
    GameObserver gameObserver;

    public int getPlayersNumber() {
        return players.size();
    }

    public GameManager(MoveChecker moveChecker, Board board, List<String> nicknames, List<? extends PlayerController> controllers, int pawnsPerPlayer) {
        this.moveChecker = moveChecker;
        this.board = board;

        if(nicknames.size() != controllers.size()) throw new RuntimeException();
        for(int i=0; i<nicknames.size(); i++) {
            addPlayer(nicknames.get(i), controllers.get(i));
        }

        pawnSetUpper.setUpPawns(board,players,pawnsPerPlayer);
    }

    public GameInfo getGameInfo() {
        return new GameInfo(board.getTilesCopy(), board.getPawnsCopy(),
                players, playersMoves);
    }

    public void setObserver(GameObserver gameObserver) {
        this.gameObserver = gameObserver;
    }

    public void startGame() {
        controllers.get(0).nextTurn();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    private void addPlayer(String nickname, PlayerController playerController) {
        Player player = new Player(getPlayers().size(), nickname);
        players.add(player);
        playerController.set(player,board,moveChecker);
        playerController.setActionOnMove(move -> tryToMove(player,move));
        controllers.add(playerController);
    }

    public ArrayList<PlayerController> getControllers() {
        return controllers;
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

    public void endGame(boolean saveResult) {
        gameEnded = true;
        gameObserver.onGameOver(saveResult);
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
        endGame(true);
    }

    public PlayerController getCurrentController() {
        return controllers.get(turn % getPlayersNumber());
    }

    public boolean tryToMove(Player player, Move move) {
        if(players.get(turn % getPlayersNumber()).equals(player) && moveChecker.isValidMove(move)) {
            Tile tile = board.getTileAt(move.getFrom());
            int tileScore = tile.getScore();
            player.addPoints(tileScore);
            board.removeTile(move.getFrom());
            board.movePawn(move);
            player.changePawnPosition(move);
            gameObserver.updatePlayerPoints(player);
            turn++;
            playersMoves.add(new PlayerMove(player.getId(), tileScore, move));
            updateState();
            return true;
        }
        return false;
    }

}
