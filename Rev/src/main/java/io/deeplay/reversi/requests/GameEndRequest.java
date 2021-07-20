package io.deeplay.reversi.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameEndRequest {
    @JsonProperty
    private final int scoreBlack;
    @JsonProperty
    private final int scoreWhite;

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
