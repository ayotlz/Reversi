package io.deeplay.reversi;

import io.deeplay.reversi.bot.Bot;
import io.deeplay.reversi.bot.HumanBot;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;
import io.deeplay.reversi.exceptions.ReversiException;

import java.io.IOException;

public class Selfplay {
    public static void main(String[] args) throws ReversiException, IOException {
        Bot[] bots = new Bot[]{new HumanBot(), new HumanBot()};
        Board board = new Board();
        Handler handler = new Handler();

        handler.initializationBoard(board);
        System.out.println(board.toString());

        Color color = Color.BLACK;
        while (!handler.isGameEnd(board)) {
            for (Bot bot : bots) {
//                Answer answer = bot.getAnswer()
                System.out.println("Ходят " + color.getString());
                Cell cell = bot.getAnswer();
                if (handler.makeStep(board, color, cell)) {
                    color = color.reverseColor();
                }
                System.out.println(board.toString());
            }
        }
    }
}
