package io.deeplay.reversi;

import io.deeplay.reversi.bot.*;
import io.deeplay.reversi.models.board.*;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.exceptions.ReversiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Selfplay {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);
    //    getPlayers
//    getAnswer

    public static void game() throws ReversiException {
        Board board = new Board();
        Handler handler = new Handler();
        Player[] players = new Player[]{new RandomBot(Color.BLACK), new RandomBot(Color.WHITE)};
        ;

        handler.initializationBoard(board);
        logger.debug("Началась новая игра");

        while (!handler.isGameEnd(board)) {
            for (Player player : players) {
                if (handler.isGameEnd(board)) {
                    break;
                }

                if (!handler.haveIStep(board, player.getPlayerColor())) {
                    System.out.println("Ход переходит");
                    continue;
                }

                while (true) {
                    logger.debug("Ходят {}", player.getPlayerColor());
                    System.out.println("Ходят " + player.getPlayerColor());

                    try {
                        final Cell cell = player.getAnswer(board);
                        handler.makeStep(board, cell, player.getPlayerColor());
                        System.out.println(board.toString());
                        break;
                    } catch (ReversiException | IOException e) {
                        System.out.println("Ход не может быть сделан\n");
                    }
                }
            }
        }
        System.out.println("Игра закончилась");
        System.out.println("Черные: " + handler.getScoreBlack(board));
        System.out.println("Белые: " + handler.getScoreWhite(board));
        logger.debug("Игра закончилась");
    }

    public static void main(String[] args) throws ReversiException {
        for (int i = 0; i < 100; i++) {
            game();
        }
    }
}
