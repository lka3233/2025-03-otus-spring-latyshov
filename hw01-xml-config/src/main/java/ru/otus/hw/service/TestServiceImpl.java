package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = new ArrayList<>();
        try {
            questions = questionDao.findAll();
        } catch (QuestionReadException qre) {
            ioService.printFormattedLine("There is an error while getting question. Error message:%s", qre.getMessage());
            return;
        }
        questions.forEach(question -> {
            ioService.printFormattedLine("%n%s", question.text());
            question.answers().forEach(answer -> ioService.printFormattedLine("[ ] %s",answer.text()));
        });
    }
}
