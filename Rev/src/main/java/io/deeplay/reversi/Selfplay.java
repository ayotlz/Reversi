package io.deeplay.reversi;

import io.deeplay.reversi.bot.*;
import io.deeplay.reversi.models.board.*;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.exceptions.ReversiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Selfplay {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    public static void main(String[] args) throws ReversiException {
        Board board = new Board();
        Handler handler = new Handler();

//        Player[] players = new Player[]{new RandomBot(Color.BLACK), new RandomBot(Color.WHITE)};
        Player[] players = new Player[]{new HumanPlayer(Color.BLACK), new HumanPlayer(Color.WHITE)};

        handler.initializationBoard(board);
        System.out.println(board.toString());

        logger.debug("Началась новая игра");
        while (!handler.isGameEnd(board)) {
            for (Player player : players) {
                while (true) {
                    logger.debug("Ходят {}", player.getPlayerColor());
                    System.out.println("Ходят " + player.getPlayerColor());
                    Cell cell = player.getAnswer(board);

                    if (handler.makeStep(board, player.getPlayerColor(), cell)) {
                        System.out.println(board.toString());
                        logger.debug("Ход поменялся");
                        break;
                    }
                    else {
                        System.out.println("Ход не может быть сделан");
                    }
                    System.out.println(board.toString());
                }
            }
        }
    }
}
