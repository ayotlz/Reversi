package Ayotlz.utility;

import exceptions.ReversiException;
import handler.Handler;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;
import player.RandomBot;
import property.Property;

import java.io.IOException;

public final class MonteCarlo implements IFunction {
    @Override
    public final double getScore(final Board board, final Color playerColor, final Color turnOrder) {
        final int games = Property.getMCGames();
        double ratio = 0;
        for (int i = 0; i < games; i++) {
            final Handler handler = new Handler();
            final Board copyBoard = new Board(board);
            final Player[] players = new Player[]{new RandomBot(turnOrder), new RandomBot(turnOrder.reverseColor())};

            while (!handler.isGameEnd(copyBoard)) {
                for (final Player player : players) {
                    if (handler.isGameEnd(copyBoard)) {
                        break;
                    }
                    if (!handler.haveIStep(copyBoard, player.getPlayerColor())) {
                        continue;
                    }
                    while (true) {
                        try {
                            final Cell cell = player.getAnswer(copyBoard);
                            handler.makeStep(copyBoard, cell, player.getPlayerColor());
                            break;
                        } catch (final ReversiException | IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
            if (handler.getScoreBlack(copyBoard) > handler.getScoreWhite(copyBoard) && turnOrder == Color.BLACK) {
                ratio++;
            } else if (handler.getScoreBlack(copyBoard) < handler.getScoreWhite(copyBoard) && turnOrder == Color.WHITE) {
                ratio++;
            } else if (handler.getScoreBlack(copyBoard) == handler.getScoreWhite(copyBoard)) {
                ratio += 0.5;
            }
        }

        if (turnOrder == playerColor) {
            return ratio / games;
        } else return -ratio / games;
    }
}
