package arena;

import board.*;
import board.arranger.RatioTileChooser;
import board.arranger.RectangleTileArranger;
import board.arranger.TileArranger;
import board.arranger.TileScoreChooser;
import board.views.BoardView;
import board.views.NoneBoardView;
import game.GameManager;
import game.GameObserver;
import game.Player;
import game.controllers.BotControllerFactory;
import game.controllers.botcontrollerfactories.HardBotControllerFactory;
import game.controllers.botcontrollerfactories.MediumBotControllerFactory;
import game.threads.OnlyMainThreadRunner;
import util.sleeper.FakeSleeper;
import util.GameInfo;

import java.util.ArrayList;

/**
This class is for AI developers to test and compare their strategies, optimize hyperparameters etc.
It also demonstrates that game model is independent from view (JavaFX)
*/
public class BotArena {

    public static void main(String[] args) {
        final int totalRounds = 1000;

        BotArena botArena = new BotArena();

        //set strategies
        botArena.addPlayer(new MediumBotControllerFactory(new FakeSleeper(), new OnlyMainThreadRunner()));
        botArena.addPlayer(new HardBotControllerFactory(new FakeSleeper(), new OnlyMainThreadRunner()));

        ArrayList<String> nicks = botArena.getNicknames();

        //set statistics which will be collected after each game
        StatisticsGroup statistics = new StatisticsGroup();
        statistics.setPlayersNumber(nicks.size());
        statistics.addStatistics(new WinStatistic(nicks)).addStatistics(new AveragePointsStatistic(nicks));
        botArena.setStatistics(statistics);

        for(int i=0;i<totalRounds;i++) {
            botArena.newRound();
        }

        System.out.println(statistics.getStatsGroupedByPlayer());
    }

    private GameManager gameManager;

    private ArrayList<BotControllerFactory> botFactories = new ArrayList<>();
    private ArrayList<String> nicknames = new ArrayList<>();
    private BoardView boardView = new NoneBoardView();
    private Board board = new Board(boardView);
    private MoveChecker moveChecker = new StandardMoveChecker(board);
    private Statistics statistics;

    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public void resetBoard() {
        TileScoreChooser tileScoreChooser = new RatioTileChooser(3,2,1);
        TileArranger tileArranger = new RectangleTileArranger(8,8);
        tileArranger.arrange(board,tileScoreChooser);
    }

    public void addPlayer(BotControllerFactory botControllerFactory) {
        botFactories.add(botControllerFactory);
        nicknames.add(botControllerFactory.toString());
    }

    public void newRound() {
        resetBoard();
        gameManager = new GameManager(moveChecker, board, nicknames, new ArrayList<>(botFactories), 2);
        gameManager.setObserver(new GameObserver() {
            @Override public void onGameOver(GameInfo gameInfo, boolean saveGame) {}
            @Override public void onPlayerPointsUpdated(Player player) {}
            @Override public void onGameInfoUpdated(GameInfo gameInfo) {}
        });
        gameManager.startGame();
        statistics.collectGameResult(gameManager.getPlayers());
    }
}
