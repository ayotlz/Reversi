import com.fasterxml.jackson.databind.ObjectMapper;
import models.board.Board;
import models.board.Cell;
import models.chip.Chip;
import models.chip.Color;
import org.junit.jupiter.api.Test;
import requests.GameEndRequest;
import requests.PlayerRequest;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializeDeserializeTest {

    @Test
    public void serializeAndDeserializeColor() throws IOException {
        final Chip chip = new Chip(Color.WHITE);
        final Color startColor = chip.getColor();
        final StringWriter writer = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        final StringReader reader = new StringReader("\"WHITE\"");
        mapper.writeValue(writer, startColor);
        final Color resultColor = mapper.readValue(reader, Color.class);

        assertEquals(startColor, resultColor);
    }

    @Test
    public void serializeAndDeserializeCell() throws IOException {
        final Cell startCell = new Cell(0, 0);
        final StringWriter writer = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        final StringReader reader = new StringReader("{\"x\":0,\"y\":0}\n}");
        mapper.writeValue(writer, startCell);
        final Cell resultCell = mapper.readValue(reader, Cell.class);

        assertEquals(startCell, resultCell);
    }

    @Test
    public void serializeAndDeserializeChip() throws IOException {
        final Chip startChip = new Chip(Color.WHITE);
        final StringWriter writer = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        final StringReader reader = new StringReader("{\"color\":\"WHITE\"}");
        mapper.writeValue(writer, startChip);
        final Chip resultChip = mapper.readValue(reader, Chip.class);

        assertEquals(startChip, resultChip);
    }

    @Test
    public void serializeAndDeserializeBoard() throws IOException {
        Board startBoard = new Board();
        startBoard.setChip(0, 0, Color.WHITE);
        startBoard.setChip(0, 1, Color.WHITE);
        startBoard.setChip(0, 2, Color.WHITE);
        final StringWriter writer = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        final StringReader reader = new StringReader("{\"board\":[[{\"color\":\"WHITE\"},{\"color\":\"WHITE\"},{\"color\":\"WHITE\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}]],\"boardSize\":8}\n");
        mapper.writeValue(writer, startBoard);

        final Board resultBoard = mapper.readValue(reader, Board.class);
        assertEquals(startBoard.toString(), resultBoard.toString());
    }

    @Test
    public void serializeAndDeserializePlayerRequest() throws IOException {
        final PlayerRequest startRequest = new PlayerRequest(new Board(), Color.WHITE);
        final StringWriter writer = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        final StringReader reader = new StringReader("{\"board\":{\"board\":[[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}],[{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"},{\"color\":\"NEUTRAL\"}]],\"boardSize\":8},\"color\":\"WHITE\"}\n");
        System.out.println(writer);
        final PlayerRequest resultRequest = mapper.readValue(reader, PlayerRequest.class);

        assertEquals(startRequest.getColor(), resultRequest.getColor());
        assertEquals(startRequest.getBoard().toString(), resultRequest.getBoard().toString());
    }

    @Test
    public void serializeAndDeserializeGameEndRequest() throws IOException {
        final GameEndRequest startRequest = new GameEndRequest(32, 32);
        final StringWriter writer = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        final StringReader reader = new StringReader("{\"scoreBlack\":32,\"scoreWhite\":32}");
        mapper.writeValue(writer, startRequest);
        final GameEndRequest resultRequest = mapper.readValue(reader, GameEndRequest.class);
        assertEquals(startRequest.getScoreBlack(), resultRequest.getScoreBlack());
        assertEquals(startRequest.getScoreWhite(), resultRequest.getScoreWhite());
    }
}
