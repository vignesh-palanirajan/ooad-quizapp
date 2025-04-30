package strategy;

import models.Question;

public class NegativeScoringStrategy implements ScoringStrategy {
    @Override
    public int calculateScore(Question question, String selectedOption, long timeTaken) {
        if (selectedOption.equals("")) return 0; // skipped
        if (selectedOption.equals(question.getCorrectOption())) return 1;
        return -1;
    }
}
