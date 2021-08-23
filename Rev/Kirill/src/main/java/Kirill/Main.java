package Kirill;

import Kirill.bots.MiniMaxBot;
import Kirill.bots.MiniMaxBotFJP;
import Kirill.utilityFunctions.ExpertTableScoreFunction;
import Kirill.utilityFunctions.MonteCarloFunction;
import exceptions.ReversiException;
import handler.Handler;
import models.board.Board;
import models.chip.Color;
import player.Player;
import player.RandomBot;

import java.io.IOException;
import java.util.Random;

public final class Main {
    public static void main(final String[] args) throws ReversiException, IOException {
        int scoreBot1 = 0;
        int scoreBot2 = 0;
        int scoreDraw = 0;
        int countBlackWin = 0;
        int countWhiteWin = 0;
        int iterations = 1000;

        for (int i = 0; i < iterations; i++) {
            Handler handler = new Handler();
            Board board = new Board();
            handler.initializationBoard(board);
            final Random random = new Random();

            Color playerColor;
            int color = random.nextInt(2);
            if (color == 1) {
                playerColor = Color.BLACK;
            } else {
                playerColor = Color.WHITE;
            }

//            System.out.println(playerColor);
//            Player bot1 = new OneSimulationBot(playerColor);
//            Player bot1 = new MiniMaxBot(playerColor, new ExpertMobilityTableScoreFunction(), 4);
//            872:114:14, deep 4
//            840:136:24, deep 2
            Player bot1 = new MiniMaxBotFJP(playerColor, new MonteCarloFunction(), 1);
//            855:121:24, deep 4
//            Player bot2 = new ReflectionBot(playerColor.reverseColor());
            Player bot2 = new RandomBot(playerColor.reverseColor());

            boolean bliat = false;

            if (bot1.getPlayerColor() == Color.BLACK) {
                while (!handler.isGameEnd(board)) {
                    if (handler.haveIStep(board, bot1.getPlayerColor())) {
                        long start = System.currentTimeMillis();
                        handler.makeStep(board, bot1.getAnswer(board), bot1.getPlayerColor());
                        long finish = System.currentTimeMillis();
                        System.out.println(finish - start);
                        if ((finish - start) >= 5000) {
                            bliat = true;
                        }
//                        System.out.println("ХОД");
//                        System.out.println(board);
                    }
                    if (handler.haveIStep(board, bot2.getPlayerColor())) {
                        long start = System.currentTimeMillis();
                        handler.makeStep(board, bot2.getAnswer(board), bot2.getPlayerColor());
                        long finish = System.currentTimeMillis();
//                        System.out.println(finish - start);
                        if ((finish - start) >= 5000){
                            bliat = true;
                        }
//                        System.out.println("ХОД");
//                        System.out.println(board);
                    }
                }
            } else {
                while (!handler.isGameEnd(board)) {
                    if (handler.haveIStep(board, bot2.getPlayerColor())) {
                        long start = System.currentTimeMillis();
                        handler.makeStep(board, bot2.getAnswer(board), bot2.getPlayerColor());
                        long finish = System.currentTimeMillis();
//                        System.out.println(finish - start);
                        if ((finish - start) >= 5000){
                            bliat = true;
                        }
//                        System.out.println("ХОД");
//                        System.out.println(board);
                    }
                    if (handler.haveIStep(board, bot1.getPlayerColor())) {
                        long start = System.currentTimeMillis();
                        handler.makeStep(board, bot1.getAnswer(board), bot1.getPlayerColor());
                        long finish = System.currentTimeMillis();
                        System.out.println(finish - start);
                        if ((finish - start) >= 5000){
                            bliat = true;
                        }
//                        System.out.println("ХОД");
//                        System.out.println(board);
                    }
                }
            }

            if (handler.getScoreBlack(board) == handler.getScoreWhite(board)) {
                scoreDraw++;
            } else if (handler.getScoreBlack(board) > handler.getScoreWhite(board)) {
                countBlackWin++;
                if (bot1.getPlayerColor() == Color.BLACK) {
                    scoreBot1++;
                } else {
                    scoreBot2++;
                }
            } else {
                countWhiteWin++;
                if (bot1.getPlayerColor() == Color.WHITE) {
                    scoreBot1++;
                } else {
                    scoreBot2++;
                }
            }

            System.out.println(countBlackWin);
            System.out.println(countWhiteWin);
            System.out.println("Чёрные");
            System.out.println(handler.getScoreBlack(board));
            System.out.println("Белые");
            System.out.println(handler.getScoreWhite(board));
            System.out.println(scoreBot1 + ":" + scoreBot2 + ":" + scoreDraw);
            System.out.println(bot1.getName() + ":" + bot2.getName());
            if (bliat){
                System.out.println("Бляяяяяяя");
            } else {
                System.out.println("Хорошо");

            }
        }
    }
}
