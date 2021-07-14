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
    public RandomBot(Color color) {
        super(color);
    }

    @Override
    public Cell getAnswer(Board board) throws IOException {
        final Map<Cell, List<Cell>> scoreMap = board.getScoreMap(getPlayerColor());

        Random random = new Random();
        List<Cell> keys = new ArrayList<>(scoreMap.keySet());
        Cell randomCell = keys.get(random.nextInt(keys.size()));

        final StringWriter writer = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, randomCell);

        return randomCell;
    }
}
