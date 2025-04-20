package factory;

import java.sql.ResultSet;
import models.Question;
import java.util.List;
import java.sql.SQLException;

public interface QuestionFactoryInterface {
    List<Question> createQuestions(ResultSet resultSet) throws SQLException;
}
