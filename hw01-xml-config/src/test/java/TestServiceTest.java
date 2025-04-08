import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestService;
import ru.otus.hw.service.TestServiceImpl;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class TestServiceTest {
    private IOService ioService;

    private QuestionDao questionDao;

    private TestService testService;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
        questionDao = mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    public void givenQuestionsWhenPrintThenPrintCorrect() {
        Question question = new Question(
                "Question?",
                List.of(
                        new Answer("Yes", true),
                        new Answer("No", false)
                )
        );
        when(questionDao.findAll()).thenReturn(List.of(question));

        testService.executeTest();

        verify(ioService).printLine("");
        verify(ioService).printFormattedLine("Please answer the questions below%n");
        verify(ioService).printFormattedLine("%n%s", "Question?");
        verify(ioService).printFormattedLine("[ ] %s", "Yes");
        verify(ioService).printFormattedLine("[ ] %s", "No");
        verifyNoMoreInteractions(ioService);
    }

    @Test
    public void givenEmptyQuestionListWhenPrintThenPrintHeaderOnly() {
        when(questionDao.findAll()).thenReturn(List.of());

        testService.executeTest();

        verify(ioService).printLine("");
        verify(ioService).printFormattedLine("Please answer the questions below%n");
        verifyNoMoreInteractions(ioService);
    }
}
