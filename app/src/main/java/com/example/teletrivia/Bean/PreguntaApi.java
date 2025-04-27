package com.example.teletrivia.Bean;

import java.util.List;

public class PreguntaApi {
    private String question;
    private String correct_answer;
    private List<String> incorrect_answers;

    public String getQuestion() {
        return question;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public List<String> getIncorrect_answers() {
        return incorrect_answers;
    }

}
