package application.utils.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

public class ScoreRequest {
    @Nullable
    private Long id;
    @Nullable
    private String username;
    private int sScore;
    private int mScore;

    @JsonCreator
    public ScoreRequest(@JsonProperty(value = "id") @Nullable Long id,
                        @JsonProperty(value = "username") @Nullable String username,
                        @JsonProperty(value = "sScore", defaultValue = "0") int sScore,
                        @JsonProperty(value = "mScore", defaultValue = "0") int mScore) {
        this.id = id;
        this.username = username;
        this.sScore = sScore;
        this.mScore = mScore;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    public int getsScore() {
        return sScore;
    }

    public int getmScore() {
        return mScore;
    }
}
