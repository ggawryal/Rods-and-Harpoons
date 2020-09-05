package game;

import board.Board;
import board.HexVector;
import board.Move;
import board.MoveChecker;
import board.drawable.tile.Tile;
import game.controllers.ControllerFactory;
import game.controllers.PlayerController;
import util.GameInfo;
import util.PlayerMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameManager {
    private final MoveChecker moveChecker;
    private final Board board;
    private int turn;
    private boolean gameEnded;
    private final ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<PlayerController> controllers = new ArrayList<>();
    private GameInfo gameInfo;
    PawnSetUpper pawnSetUpper = new PawnSetUpper();
    GameObserver gameObserver;

    public int getPlayersNumber() {
        return players.size();
    }

    public GameManager(MoveChecker moveChecker, Board board, List<String> nicknames, List<ControllerFactory> controllerFactories, int pawnsPerPlayer) {
        this.moveChecker = moveChecker;
        this.board = board;

        if(nicknames.size() != controllerFactories.size()) throw new RuntimeException();
        for(int i=0; i<nicknames.size(); i++) {
            Player player = new Player(getPlayers().size(), nicknames.get(i));
            bindPlayerWithController(player,controllerFactories.get(i));
            players.add(player);
        }

        pawnSetUpper.setUpPawns(board,players,pawnsPerPlayer);
        gameInfo = new GameInfo(board.getTilesCopy(),board.getPawnsCopy(), controllerFactories, players, new ArrayList<>(),turn, false);
    }

    public GameManager(MoveChecker moveChecker, Board board, GameInfo gameInfo) {
        this.moveChecker = moveChecker;
        this.board = board;
        board.clear();
        for(var entry : gameInfo.getTiles().entrySet())
            board.addTile(entry.getValue(),entry.getKey());

        for(int i=0; i<gameInfo.getPlayers().size(); i++) {
            bindPlayerWithController(gameInfo.getPlayers().get(i), gameInfo.getControllerFactories().get(i));
            players.add(gameInfo.getPlayers().get(i));

        }

        for(var entry : gameInfo.getPawns().entrySet()) {
            boolean foundOwner = false;
            for(Player player : players) {
                if(player.getId()+1 == entry.getValue().getId()) {
                    player.addPawn(entry.getKey());
                    board.addPawn(entry.getValue(),entry.getKey());
                    foundOwner = true;
                    break;
                }
            }
            if(!foundOwner)
                throw new RuntimeException("Wrong pawn's id in game info");
        }

        turn = gameInfo.getTurn();
        gameEnded = gameInfo.isGameFinished();
        this.gameInfo = gameInfo;
    }

    public void setObserver(GameObserver gameObserver) {
        this.gameObserver = gameObserver;
    }

    public void startGame() {
        gameObserver.onGameInfoUpdated(gameInfo);
        for(Player player : players)
            gameObserver.onPlayerPointsUpdated(player);
        controllers.get(turn % getPlayersNumber()).nextTurn();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    private void bindPlayerWithController(Player player, ControllerFactory playerControllerFactory) {
        PlayerController playerController = playerControllerFactory.newController();
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
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for(Thread thread : threadSet) {
            if(thread.getName().startsWith("bot")) {
                thread.interrupt();
            }
        }
        if(saveResult) {
            gameInfo = new GameInfo(board.getTilesCopy(), board.getPawnsCopy(), gameInfo.getControllerFactories(), gameInfo.getPlayers(), gameInfo.getPlayersMoves(), turn, true);
            gameObserver.onGameOver(gameInfo, true);
        }
    }

    private boolean updateTurn() {
        if(gameEnded)
            return true;
        turn++;
        for(int i = 0; i < getPlayersNumber(); i++) {
            int playerID = turn % getPlayersNumber();
            if(!canPlayerMove(players.get(playerID)))
                turn++;
            else {
                return false;
            }
        }
        return true;
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

            boolean hasGameAlreadyEnded = updateTurn();

            ArrayList<PlayerMove> updatedPlayerMoves = new ArrayList<>(gameInfo.getPlayersMoves());
            updatedPlayerMoves.add(new PlayerMove(player.getId(), tileScore, move));
            gameInfo = new GameInfo(board.getTilesCopy(), board.getPawnsCopy(), gameInfo.getControllerFactories(), gameInfo.getPlayers(), updatedPlayerMoves, turn, false);
            gameObserver.onGameInfoUpdated(gameInfo);
            if(!gameEnded)
                gameObserver.onPlayerPointsUpdated(player);

            if(hasGameAlreadyEnded)
                endGame(!gameEnded);
            else {
                controllers.get(turn % getPlayersNumber()).nextTurn();
            }
            return true;
        }
        return false;
    }

}
