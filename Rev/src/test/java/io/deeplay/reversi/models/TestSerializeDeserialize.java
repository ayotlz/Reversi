package io.deeplay.reversi.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.reversi.models.board.Board;
import io.deeplay.reversi.models.board.Cell;
import io.deeplay.reversi.models.chip.Chip;
import io.deeplay.reversi.models.chip.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class TestSerializeDeserialize {

    @Test
    public void serializeAndDeserializeColor() throws IOException {
        final Chip chip = new Chip(Color.WHITE);
        final Color startColor = chip.getColor();
        final StringWriter writer = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        final StringReader reader = new StringReader("\"WHITE\"");
        mapper.writeValue(writer,startColor);
        final Color resultColor = mapper.readValue(reader, Color.class);

        assertEquals(startColor, resultColor);
    }

    @Test
    public void serializeAndDeserializeCell() throws IOException {
        final Cell startCell = new Cell(0,0);
        final StringWriter writer = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        final StringReader reader = new StringReader("{\"x\":0,\"y\":0}\n}");
        mapper.writeValue(writer,startCell);
        final Cell resultCell = mapper.readValue(reader, Cell.class);

        assertEquals(startCell, resultCell);
    }

    @Test
    public void serializeAndDeserializeChip() throws IOException {
        final Chip startChip = new Chip(Color.WHITE);
        final StringWriter writer = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        final StringReader reader = new StringReader("{\"color\":\"WHITE\"}");
        mapper.writeValue(writer,startChip);
        final Chip resultChip = mapper.readValue(reader, Chip.class);

        assertEquals(startChip, resultChip);
    }

    @Test
    public void serializeAndDeserializeBoard() throws IOException {
        Board startBoard = new Board();
        startBoard.setColor(0,0, Color.WHITE);
        startBoard.setColor(0,1, Color.WHITE);
        startBoard.setColor(0,2, Color.WHITE);
        final StringWriter writer = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        final StringReader reader = new StringReader("{\"board\":[[{\"color\":\"WHITE\"},{\"color\":\"WHITE\"},{\"color\":\"WHITE\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}]],\"boardSize\":8}\n");
        mapper.writeValue(writer,startBoard);

        final Board resultBoard = mapper.readValue(reader, Board.class);
        assertEquals(startBoard.toString(), resultBoard.toString());
    }
}