package io.deeplay.reversi;

import io.deeplay.reversi.bot.Player;
import io.deeplay.reversi.bot.RandomBot;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.exceptions.ReversiException;

public class Selfplay {
    public static void main(String[] args) throws ReversiException {
        Board board = new Board();
        Handler handler = new Handler();

        Player[] players = new Player[]{new RandomBot(Color.BLACK), new RandomBot(Color.WHITE)};

        handler.initializationBoard(board);
        System.out.println(board.toString());

        Color color = Color.BLACK;
        while (!handler.isGameEnd(board)) {
            for (Player player : players) {
                System.out.println("Ходят " + color.getString());
                Cell cell = player.getAnswer(board);
                if (handler.makeStep(board, color, cell)) {
                    color = color.reverseColor();
                }
                System.out.println(board.toString());
            }
        }
    }
}
