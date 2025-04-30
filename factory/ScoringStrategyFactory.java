package factory;

import strategy.*;

public class ScoringStrategyFactory {
    public static ScoringStrategy getScoringStrategy(String type) {
        switch (type.toLowerCase()) {
            case "simple":
                return new SimpleScoringStrategy();
            case "negative":
                return new NegativeScoringStrategy();
            case "time":
                return new TimeBasedScoringStrategy();
            default:
                throw new IllegalArgumentException("Unknown scoring type: " + type);
        }
    }
}
