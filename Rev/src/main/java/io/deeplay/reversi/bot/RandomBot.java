package io.deeplay.reversi.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Color;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomBot extends Player {
    public RandomBot(final Color color) {
        super(color);
    }

    @Override
    public final Cell getAnswer(final Board board) throws IOException {
        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());

        final Random random = new Random();
        final List<Cell> keys = new ArrayList<>(scoreMap.keySet());
        final Cell randomCell = keys.get(random.nextInt(keys.size()));

        final StringWriter writer = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, randomCell);

        return randomCell;
    }
}
