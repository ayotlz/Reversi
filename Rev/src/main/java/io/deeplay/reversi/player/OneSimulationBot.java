package io.deeplay.reversi.player;

import io.deeplay.reversi.exceptions.ReversiException;
import io.deeplay.reversi.handler.Handler;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

import java.io.IOException;
import java.util.*;

/**
 * Класс OneSimulationBot - класс игрока расширяюшего Player
 */
public class OneSimulationBot extends Player {

    public OneSimulationBot(final Color color) {
        super(color);
    }

    /**
     * Функция, которая запращивает у бота ответ
     *
     * @param board - доска
     * @return возвращается ответ от игрока
     */
    @Override
    public final Cell getAnswer(final Board board) {

        final Map<Cell, Double> utilityMap = new HashMap<>(getUtilityMap(board));

        Map.Entry<Cell, Double> entry = utilityMap.entrySet().iterator().next();
        Cell tempCell = entry.getKey();
        double tempDouble = entry.getValue();

        final Cell answerCell;

        for (final Cell cell : utilityMap.keySet()) {
            if (utilityMap.get(cell) > tempDouble) {
                tempDouble = utilityMap.get(cell);
                tempCell = cell;
            }
        }
        answerCell = tempCell;

        return answerCell;
    }

    // Мапа содержащая ячейки и полезность
    private Map<Cell, Double> getUtilityMap(final Board board) {

        //Клетки в которые можно походить и список которые перевернутся
        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());

        //Клетки в которые можно походить
        final List<Cell> keys = new ArrayList<>(scoreMap.keySet());
        final Map<Cell, Double> utility = new HashMap<>();

        for (final Cell key : keys) {
            final Board copyBoard = new Board(board);
            copyBoard.setChip(key.getX(), key.getY(), getPlayerColor());
            for (Cell cell : scoreMap.get(key)) {
                copyBoard.reverseChip(cell.getX(), cell.getY());
            }
            utility.put(key, getWinScore(copyBoard));
        }
        return utility;
    }

    private double getColorScore(final Board board, final Color color) {
        double score = 0;
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.getChipColor(i, j) == color) {
                    score++;
                }
            }
        }
        return score;
    }

    private double getWinScore(final Board board) {
        return getColorScore(board, getPlayerColor()) - getColorScore(board, getPlayerColor().reverseColor());
    }

    public static void main(final String[] args) throws ReversiException, IOException {
        int scoreBot1 = 0;
        int scoreBot2 = 0;
        int scoreDraw = 0;
        int iterations = 100;

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
            Player bot1 = new KirillBot(Color.BLACK);
//            Player bot2 = new ReflectionBot(playerColor.reverseColor());
            Player bot2 = new RandomBot(Color.WHITE);

            if (bot1.getPlayerColor() == Color.BLACK) {
                while (!handler.isGameEnd(board)) {
                    if (handler.haveIStep(board, bot1.getPlayerColor())) {
                        handler.makeStep(board, bot1.getAnswer(board), bot1.getPlayerColor());
                        System.out.println("ХОД");
                        System.out.println(board);
                    }
                    if (handler.haveIStep(board, bot2.getPlayerColor())) {
                        handler.makeStep(board, bot2.getAnswer(board), bot2.getPlayerColor());
                        System.out.println("ХОД");
                        System.out.println(board);
                    }
                }
            } else {
                while (!handler.isGameEnd(board)) {
                    if (handler.haveIStep(board, bot2.getPlayerColor())) {
                        handler.makeStep(board, bot2.getAnswer(board), bot2.getPlayerColor());
                        System.out.println("ХОД");
                        System.out.println(board);
                    }
                    if (handler.haveIStep(board, bot1.getPlayerColor())) {
                        handler.makeStep(board, bot1.getAnswer(board), bot1.getPlayerColor());
                        System.out.println("ХОД");
                        System.out.println(board);
                    }
                }
            }
            if (handler.getScoreBlack(board) > handler.getScoreWhite(board)) {
                if (bot1.getPlayerColor() == Color.BLACK) {
                    scoreBot1++;
                } else {
                    scoreBot2++;
                }
//                System.out.println("Чёрные победили");
            } else if (handler.getScoreBlack(board) < handler.getScoreWhite(board)) {
                if (bot1.getPlayerColor() == Color.WHITE) {
                    scoreBot1++;
                } else {
                    scoreBot2++;
                }
//                System.out.println("Белые победили");
            } else {
                scoreDraw++;
//                System.out.println("Ничья");
            }
            System.out.println("Чёрные");
            System.out.println(handler.getScoreBlack(board));
            System.out.println("Белые");
            System.out.println(handler.getScoreWhite(board));
            System.out.println(scoreBot1 + ":" + scoreBot2 + ":" + scoreDraw);
        }
    }
}

//OSB с функцией разности очков на 1 уровне или функция больше в целом фишек vs Reflection 5142:4858:0 зависит от цвета
//RandomBot vs Reflection 3447:6217:336
//OSB функция больше в целом фишек vs RandomBot 5973:3674:353
//OSB с функцией разности очков на 1 уровне vs RandomBot 5821:3812:367