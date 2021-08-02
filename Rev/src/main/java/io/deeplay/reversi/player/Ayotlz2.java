package io.deeplay.reversi.player;

import io.deeplay.reversi.exceptions.ReversiException;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.player.minimax.AnswerAndWin;
import io.deeplay.reversi.player.minimax.WinnerType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;

public class Ayotlz2 extends Player {
    public Ayotlz2(final Color color) {
        super(color);
    }

    @Override
    public Cell getAnswer(final Board board) throws IOException {
        final Handler handler = new Handler();
        if (handler.getScoreBlack(board) + handler.getScoreWhite(board) < 24) {
            final Player player = new MiniBot(getPlayerColor());
            return player.getAnswer(board);
        }

        if (handler.getScoreBlack(board) + handler.getScoreWhite(board) < 58) {
            final Player player = new MaxiBot(getPlayerColor());
            return player.getAnswer(board);
        }

        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());
        final List<AnswerAndWin> awList = new ArrayList<>();
        for (final Map.Entry<Cell, List<Cell>> entry : scoreMap.entrySet()) {
            final Board copy = new Board(board);
            try {
                handler.makeStep(copy, entry.getKey(), getPlayerColor());
            } catch (ReversiException ignored) {
            }
            final double win = getWinByGameTree(copy, getPlayerColor().reverseColor());
            awList.add(new AnswerAndWin(entry.getKey(), win));
        }
        return getGreedyDecision(awList, aw -> aw.win).answer;
    }

    private WinnerType detectWinner(Board board) {
        Handler handler = new Handler();
        if (!handler.isGameEnd(board)) {
            return WinnerType.NONE;
        } else if (handler.getScoreWhite(board) > handler.getScoreBlack(board)) {
            return WinnerType.WHITE;
        } else if (handler.getScoreWhite(board) < handler.getScoreBlack(board)) {
            return WinnerType.BLACK;
        } else {
            return WinnerType.DRAW;
        }
    }

    public double getWinByGameTree(Board board, Color turnOrder) {
        final ToDoubleFunction<AnswerAndWin> winCalculator;
        if (turnOrder == getPlayerColor()) {
            winCalculator = aw -> aw.win;
        } else {
            winCalculator = aw -> -aw.win;
        }
        final WinnerType winnerType = detectWinner(board);
        if (winnerType != WinnerType.NONE) {
            return computeWin(winnerType);
        }
        final Handler handler = new Handler();
        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(turnOrder);
        final List<AnswerAndWin> awList = new ArrayList<>();

        if (!handler.haveIStep(board, turnOrder)) {
            final Board copy = new Board(board);
            final double win = getWinByGameTree(copy, turnOrder.reverseColor());
            awList.add(new AnswerAndWin(null, win));
        }
        for (final Map.Entry<Cell, List<Cell>> entry : scoreMap.entrySet()) {
            final Board copy = new Board(board);
            try {
                handler.makeStep(copy, entry.getKey(), turnOrder);
            } catch (ReversiException ignored) {
            }
            final double win = getWinByGameTree(copy, turnOrder.reverseColor());
            awList.add(new AnswerAndWin(entry.getKey(), win));
        }
        return getGreedyDecision(awList, winCalculator).win;
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


    private double computeWin(final WinnerType winnerType) {
        if (winnerType == WinnerType.DRAW) {
            return 0;
        } else if (winnerType == WinnerType.BLACK && getPlayerColor() == Color.BLACK
                || winnerType == WinnerType.WHITE && getPlayerColor() == Color.WHITE) {
            return 1;
        } else {
            return -1;
        }
    }
}
