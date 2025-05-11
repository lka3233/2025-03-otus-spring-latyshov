package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        List<QuestionDto> questionDtoList = getQuestionDtosFromFile();

        if (questionDtoList.isEmpty()) {
            return new ArrayList<>();
        }

        return questionDtoList.stream().map(QuestionDto::toDomainObject).toList();
    }

    private List<QuestionDto> getQuestionDtosFromFile() {
        String testFileName = fileNameProvider.getTestFileName();

        InputStream is = getClass().getClassLoader().getResourceAsStream(testFileName);
        if (is == null) {
            throw new QuestionReadException(String.format("Unable to find question file %s", testFileName));
        }

        try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);) {
            CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder<QuestionDto>(streamReader)
                    .withSkipLines(1)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .build();
            return csvToBean.parse();
        } catch (IOException exception) {
            throw new QuestionReadException("Questions file read error", exception);
        }
    }
}
