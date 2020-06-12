package arena;

import board.Board;
import board.BoardView;
import board.MoveChecker;
import board.StandardMoveChecker;
import board.arranger.RatioTileChooser;
import board.arranger.RectangleTileArranger;
import board.arranger.TileArranger;
import board.arranger.TileScoreChooser;
import game.GameManager;
import game.GameObserver;
import game.Player;
import game.controllers.BotController;
import game.controllers.strategies.GreedyStrategy;
import game.controllers.strategies.MixedStrategy;
import game.controllers.strategies.RandomMoveStrategy;
import game.threads.OnlyMainThreadRunner;
import util.FakeSleeper;

import java.util.ArrayList;

public class BotArena {
    private GameManager gameManager;

    private ArrayList<BotController> players = new ArrayList<>();
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


    public void addPlayer(BotController botController) {
        botController.setThreadRunner(new OnlyMainThreadRunner());
        botController.setSleeper(new FakeSleeper());
        players.add(botController);
        nicknames.add(botController.toString());
    }

    public void newRound() {
        resetBoard();
        gameManager = new GameManager(moveChecker, board, nicknames, players);
        gameManager.setObserver(new GameObserver() {
            @Override public void onGameOver(boolean saveGame) {}
            @Override public void updatePlayerPoints(Player player) {}
        });
        gameManager.startGame();
        statistics.collectGameResult(gameManager.getPlayers());
    }
    public static void main(String[] args) {
        int totalRounds = 1000;
        BotArena botArena = new BotArena();

        botArena.addPlayer(new BotController(new GreedyStrategy()));
        botArena.addPlayer(new BotController(new MixedStrategy()));

        StatisticsGroup statistics = new StatisticsGroup();

        ArrayList<String> nicks = botArena.getNicknames();

        statistics.setPlayersNumber(nicks.size());
        statistics.addStatistics(new WinStatistic(nicks)).addStatistics(new AveragePointsStatistic(nicks));
        botArena.setStatistics(statistics);

        for(int i=0;i<totalRounds;i++) {
            botArena.newRound();
        }
        System.out.println(statistics.getStatsGroupedByPlayer());

    }
}
