package newCar.event_page.service;

import newCar.event_page.dto.QuizDTO;
import newCar.event_page.entity.event.Event;
import newCar.event_page.entity.event.quiz.Quiz;
import newCar.event_page.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
        Quiz quiz= quizRepository.findById(quizDTO.getId()).orElseThrow(
                ()->new NoSuchElementException("Event not found")
        );
        quiz.setWinnerCount(quizDTO.getWinnerCount());
        quiz.setQuestion(quizDTO.getQuestion());
        quiz.setChoice1(quizDTO.getChoice1());
        quiz.setChoice2(quizDTO.getChoice2());
        quiz.setChoice3(quizDTO.getChoice3());
        quiz.setChoice4(quizDTO.getChoice4());
        quiz.setCorrectAnswer(quizDTO.getCorrectAnswer());
        quizRepository.save(quiz);
        return QuizDTO.toDTO(quiz);
    }
}
