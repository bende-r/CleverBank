package entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Score {
    public final int scoreId;
    public final String scoreNumber;
    public final int bankId;
    public final String currency;
    public final long balance;
    public final int userId;

    public Score(int scoreId, String scoreNumber, int bankId, String currency, long balance, int userId) {
        this.scoreId = scoreId;
        this.scoreNumber = scoreNumber;
        this.bankId = bankId;
        this.currency = currency;
        this.balance = balance;
        this.userId = userId;
    }
}
