package strategy;

import models.Question;

public interface ScoringStrategy {
    int calculateScore(Question question, String selectedOption, long timeTaken);
}
