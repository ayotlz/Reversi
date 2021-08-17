package requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.chip.Color;

/**
 * Класс PlayerRequest - класс запроса для сереализации/десереализации хода игрока
 */
public final class BotRequest {

    /**
     * Поле запроса, которое хранит количество доску
     */
    @JsonProperty
    private final String nameBot;

    /**
     * Поле запроса, которое хранит количество цвет игрока
     */
    @JsonProperty
    private final Color color;

    /**
     * Конкструктор - создание запроса по board и color
     */
    @JsonCreator
    public BotRequest(@JsonProperty("nameBot") final String nameBot, @JsonProperty("color") final Color color) {
        this.nameBot = nameBot;
        this.color = color;
    }

    public final String getNameBot() {
        return nameBot;
    }

    public final Color getColor() {
        return color;
    }
}
