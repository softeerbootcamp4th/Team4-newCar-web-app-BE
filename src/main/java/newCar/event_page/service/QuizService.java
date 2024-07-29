package newCar.event_page.service;

import newCar.event_page.dto.QuizDTO;
import newCar.event_page.entity.event.quiz.Quiz;
import newCar.event_page.repository.quiz.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
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

    public QuizDTO updateQuiz(QuizDTO quizDTO)
    {
        Quiz quiz = quizRepository.findById(quizDTO.getId()).get();
        quiz.update(quizDTO);
        quizRepository.save(quiz);
        return QuizDTO.toDTO(quiz);
    }
}
