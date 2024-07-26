package newCar.event_page.service;

import newCar.event_page.dto.QuizDTO;
import newCar.event_page.entity.event.quiz.Quiz;
import newCar.event_page.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository)
    {
        this.quizRepository=quizRepository;
    }

    public List<QuizDTO> getQuizList()
    {
        List<Quiz> list = quizRepository.findAll();
        List<QuizDTO> quizDTOList = new ArrayList<>();
        for(Quiz temp : list)
        {
            quizDTOList.add(QuizDTO.toDTO(temp));
        }
        return quizDTOList;
    }
}
