package factory;

import models.Question;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionFactory implements QuestionFactoryInterface {
    @Override
    public List<Question> createQuestions(ResultSet resultSet) throws SQLException {
        List<Question> questions = new ArrayList<>();

        while (resultSet.next()) {
            Question question = new Question(
                    resultSet.getInt("id"),
                    resultSet.getString("question"),
                    resultSet.getString("option_a"),
                    resultSet.getString("option_b"),
                    resultSet.getString("option_c"),
                    resultSet.getString("option_d"),
                    resultSet.getString("correct_option")
            );
            questions.add(question);
        }

        return questions;
    }
}
