package Ayotlz;

import exceptions.ReversiException;
import handler.Handler;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import Ayotlz.minimax.AnswerAndWin;
import player.Player;
import player.RandomBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.ToDoubleFunction;

public class AyotlzBot extends Player {
    private int deep = 1;

    public AyotlzBot(final Color color) {
        super(color);
        setName("AyotlzBot");
    }

    @Override
    public Cell getAnswer(final Board board) throws IOException {
        final Handler handler = new Handler();
        if (handler.getScoreBlack(board) + handler.getScoreWhite(board) < 24) {
            final Player player = new MiniBot(getPlayerColor());
            return player.getAnswer(board);
        }

        if (handler.getScoreBlack(board) + handler.getScoreWhite(board) < 40) {
            final Player player = new MaxiBot(getPlayerColor());
            return player.getAnswer(board);
        }

        if (handler.getScoreBlack(board) + handler.getScoreWhite(board) > 54) {
            deep = 10;
        }

        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());
        final List<AnswerAndWin> awList = new ArrayList<>();
        for (final Map.Entry<Cell, List<Cell>> entry : scoreMap.entrySet()) {
            final Board copy = new Board(board);
            try {
                handler.makeStep(copy, entry.getKey(), getPlayerColor());
            } catch (final ReversiException ignored) {
            }
            final ForkJoinPool pool = new ForkJoinPool(2);
            final double win = pool.invoke(new GameTree(copy, getPlayerColor().reverseColor(), deep - 1));
            awList.add(new AnswerAndWin(entry.getKey(), win));
        }
        return getGreedyDecision(awList, aw -> aw.win).answer;
    }

    private final class GameTree extends RecursiveTask<Double> {
        private final Board board;
        private final Color turnOrder;
        private final int deepOfTree;

        private GameTree(final Board board, final Color turnOrder, final int deepOfTree) {
            this.board = board;
            this.turnOrder = turnOrder;
            this.deepOfTree = deepOfTree;
        }

        @Override
        protected Double compute() {
            final ToDoubleFunction<AnswerAndWin> winCalculator;
            if (turnOrder == getPlayerColor()) {
                winCalculator = aw -> aw.win;
            } else {
                winCalculator = aw -> -aw.win;
            }

            final Handler handler = new Handler();
            if (!handler.isGameEnd(board) && deepOfTree < 1) {
                return monteCarlo(board, turnOrder);
            }
            if (handler.isGameEnd(board)) {
                return computeWin(board);
            }
            final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(turnOrder);
            final List<AnswerAndWin> awList = new ArrayList<>();

            if (!handler.haveIStep(board, turnOrder)) {
                final Board copy = new Board(board);
                final GameTree subTask = new GameTree(copy, turnOrder.reverseColor(), deepOfTree - 1);
                final double win = subTask.fork().join();
                awList.add(new AnswerAndWin(null, win));
            }
            for (final Map.Entry<Cell, List<Cell>> entry : scoreMap.entrySet()) {
                final Board copy = new Board(board);
                try {
                    handler.makeStep(copy, entry.getKey(), turnOrder);
                } catch (final ReversiException ignored) {
                }
                final GameTree subTask = new GameTree(copy, turnOrder.reverseColor(), deepOfTree - 1);
                final double win = subTask.fork().join();
                awList.add(new AnswerAndWin(entry.getKey(), win));
                if (win >= 0.9 || win <= -0.9) {
                    break;
                }
                if (win >= -0.1 && win <= 0.1) {
                    break;
                }
            }
            return getGreedyDecision(awList, winCalculator).win;
        }
    }

    private double computeWin(final Board board) {
        final Handler handler = new Handler();
        if (handler.getScoreBlack(board) == handler.getScoreWhite(board)) {
            return 0;
        } else if (handler.getScoreBlack(board) > handler.getScoreWhite(board) && getPlayerColor() == Color.BLACK
                || handler.getScoreBlack(board) < handler.getScoreWhite(board) && getPlayerColor() == Color.WHITE) {
            return 1;
        } else {
            return -1;
        }
    }

    private AnswerAndWin getGreedyDecision(final List<AnswerAndWin> awList, final ToDoubleFunction<AnswerAndWin> winCalculator) {
        AnswerAndWin bestAW = awList.get(0);
        double bestWin = winCalculator.applyAsDouble(bestAW);
        for (int i = 1; i < awList.size(); i++) {
            final AnswerAndWin currentAW = awList.get(i);
            final double currentWin = winCalculator.applyAsDouble(currentAW);
            if (currentWin > bestWin) {
                bestAW = currentAW;
                bestWin = currentWin;
            }
        }
        return bestAW;
    }

    //todo(Вынести функцию оценивания в интерфейс)
    private double monteCarlo(final Board b, final Color turnOrder) {
        final int games = 25;
        double ratio = 0;
        for (int i = 0; i < games; i++) {
            final Handler handler = new Handler();
            final Board board = new Board(b);
            final Player[] players = new Player[]{new RandomBot(turnOrder), new RandomBot(turnOrder.reverseColor())};

            while (!handler.isGameEnd(board)) {
                for (Player player : players) {
                    if (handler.isGameEnd(board)) {
                        break;
                    }
                    if (!handler.haveIStep(board, player.getPlayerColor())) {
                        continue;
                    }
                    while (true) {
                        try {
                            final Cell cell = player.getAnswer(board);
                            handler.makeStep(board, cell, player.getPlayerColor());
                            break;
                        } catch (final ReversiException | IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
            if (handler.getScoreBlack(board) > handler.getScoreWhite(board) && turnOrder == Color.BLACK) {
                ratio++;
            } else if (handler.getScoreBlack(board) < handler.getScoreWhite(board) && turnOrder == Color.WHITE) {
                ratio++;
            } else if (handler.getScoreBlack(board) == handler.getScoreWhite(board)) {
                ratio += 0.5;
            }
        }

        if (turnOrder == getPlayerColor()) {
            return ratio / games;
        } else return -ratio / games;
    }
}
