package Ayotlz;

import Ayotlz.algorithms.MiniMax;
import Ayotlz.utility.MonteCarlo;
import handler.Handler;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;

import java.io.IOException;

public final class AyotlzBot extends Player {
    private int deep = 1;

    public AyotlzBot(final Color color) {
        super(color);
        setName("AyotlzBot");
    }

    @Override
    public final Cell getAnswer(final Board board) throws IOException {
        final Handler handler = new Handler();
        // Минимакс бот бесполезен на первых ходах (скорее даже вреден), поэтому на первых 24 ходах используется
        // рукописный бот, который играет по стратегии поддавков
//        if (handler.getScoreBlack(board) + handler.getScoreWhite(board) < 24) {
//            final Player player = new MiniBot(getPlayerColor());
//            return player.getAnswer(board);
//        }

        // С 45 хода уже можно поставить глубину побольше
        if (handler.getScoreBlack(board) + handler.getScoreWhite(board) > 45) {
            deep = 2;
        }

        // После 55 хода обычно можно легко построить полное дерево игры, поэтому устанавливаю глубину 10
        if (handler.getScoreBlack(board) + handler.getScoreWhite(board) > 55) {
            deep = 10;
        }

        final MiniMax mm = new MiniMax(new MonteCarlo(), deep);
        return mm.getCell(board, getPlayerColor());
    }
}
