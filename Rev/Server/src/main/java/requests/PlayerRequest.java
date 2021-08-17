package requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.board.Board;
import models.chip.Color;

/**
 * Класс PlayerRequest - класс запроса для сереализации/десереализации хода игрока
 */
public final class PlayerRequest {

    /**
     * Поле запроса, которое хранит количество доску
     */
    @JsonProperty
    private final Board board;

    /**
     * Поле запроса, которое хранит количество цвет игрока
     */
    @JsonProperty
    private final Color color;

    /**
     * Конкструктор - создание запроса по board и color
     */
    @JsonCreator
    public PlayerRequest(@JsonProperty("board") final Board board, @JsonProperty("color") final Color color) {
        this.board = board;
        this.color = color;
    }

    public final Board getBoard() {
        return board;
    }

    public final Color getColor() {
        return color;
    }
}
