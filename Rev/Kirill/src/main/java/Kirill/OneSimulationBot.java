package Kirill;

import Kirill.UtilityFunctions.IFunction;
import Kirill.UtilityFunctions.SimpleScoreFunction;
import models.board.Board;
import models.board.Cell;
import models.chip.Color;
import player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс OneSimulationBot - класс игрока расширяюшего Player
 */
public class OneSimulationBot extends Player {

    private IFunction utilityFunction = new SimpleScoreFunction();

    public OneSimulationBot(final Color color) {
        super(color);
        setName("KirillOneSimulationBot");
    }

    public OneSimulationBot(final Color color, final IFunction function) {
        super(color);
        setName("KirillOneSimulationBot "+function.toString());
        this.utilityFunction = function;
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
            for (final Cell cell : scoreMap.get(key)) {
                copyBoard.reverseChip(cell.getX(), cell.getY());
            }
            utility.put(key, getWinScore(copyBoard));
        }
        return utility;
    }

    private double getWinScore(final Board board) {
        return utilityFunction.getScore(board, getPlayerColor()) - utilityFunction.getScore(board, getPlayerColor().reverseColor());
    }

//    public static void main(final String[] args) throws ReversiException, IOException {
//        int scoreBot1 = 0;
//        int scoreBot2 = 0;
//        int scoreDraw = 0;
//        int countBlackWin = 0;
//        int countWhiteWin = 0;
//        int iterations = 100;
//
//        for (int i = 0; i < iterations; i++) {
//            Handler handler = new Handler();
//            Board board = new Board();
//            handler.initializationBoard(board);
//            final Random random = new Random();
//
//            Color playerColor;
//            int color = random.nextInt(2);
//            if (color == 1) {
//                playerColor = Color.BLACK;
//            } else {
//                playerColor = Color.WHITE;
//            }
//
////            System.out.println(playerColor);
////            Player bot1 = new OneSimulationBot(playerColor);
//            Player bot1 = new MiniMaxBot(playerColor);
////            Player bot2 = new ReflectionBot(playerColor.reverseColor());
//            Player bot2 = new RandomBot(playerColor.reverseColor());
//
//            if (bot1.getPlayerColor() == Color.BLACK) {
//                while (!handler.isGameEnd(board)) {
//                    if (handler.haveIStep(board, bot1.getPlayerColor())) {
//                        handler.makeStep(board, bot1.getAnswer(board), bot1.getPlayerColor());
////                        System.out.println("ХОД");
////                        System.out.println(board);
//                    }
//                    if (handler.haveIStep(board, bot2.getPlayerColor())) {
//                        handler.makeStep(board, bot2.getAnswer(board), bot2.getPlayerColor());
////                        System.out.println("ХОД");
////                        System.out.println(board);
//                    }
//                }
//            } else {
//                while (!handler.isGameEnd(board)) {
//                    if (handler.haveIStep(board, bot2.getPlayerColor())) {
//                        handler.makeStep(board, bot2.getAnswer(board), bot2.getPlayerColor());
////                        System.out.println("ХОД");
////                        System.out.println(board);
//                    }
//                    if (handler.haveIStep(board, bot1.getPlayerColor())) {
//                        handler.makeStep(board, bot1.getAnswer(board), bot1.getPlayerColor());
////                        System.out.println("ХОД");
////                        System.out.println(board);
//                    }
//                }
//            }
//
//            if (handler.getScoreBlack(board) == handler.getScoreWhite(board)) {
//
//                scoreDraw++;
//            } else if (handler.getScoreBlack(board) > handler.getScoreWhite(board)) {
//                countBlackWin++;
//                if (bot1.getPlayerColor() == Color.BLACK) {
//                    scoreBot1++;
//                } else {
//                    scoreBot2++;
//                }
//            } else {
//                countWhiteWin++;
//                if (bot1.getPlayerColor() == Color.WHITE) {
//                    scoreBot1++;
//                } else {
//                    scoreBot2++;
//                }
//            }
//
//            System.out.println(countBlackWin);
//            System.out.println(countWhiteWin);
//            System.out.println("Чёрные");
//            System.out.println(handler.getScoreBlack(board));
//            System.out.println("Белые");
//            System.out.println(handler.getScoreWhite(board));
//            System.out.println(scoreBot1 + ":" + scoreBot2 + ":" + scoreDraw);
//        }
//    }
}
