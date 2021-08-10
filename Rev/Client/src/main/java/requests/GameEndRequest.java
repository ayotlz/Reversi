package requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Класс GameEndRequest - класс запроса для сереализации/десереализации результата игры
 */
public class GameEndRequest {

    /**
     * Поле запроса, которое хранит количество чёрных фишек
     */
    @JsonProperty
    private final int scoreBlack;

    /**
     * Поле запроса, которое хранит количество чёрных фишек
     */
    @JsonProperty
    private final int scoreWhite;

    /**
     * Конкструктор - создание запроса по scoreBlack и scoreWhite
     */
    @JsonCreator
    public GameEndRequest(@JsonProperty("scoreBlack") final int scoreBlack, @JsonProperty("scoreWhite") final int scoreWhite) {
        this.scoreBlack = scoreBlack;
        this.scoreWhite = scoreWhite;
    }

    public int getScoreBlack() {
        return scoreBlack;
    }

    public int getScoreWhite() {
        return scoreWhite;
    }
}
