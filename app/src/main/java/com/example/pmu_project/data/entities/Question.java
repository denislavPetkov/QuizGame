package com.example.pmu_project.data.entities;

import java.io.Serializable;

public class Question implements Serializable {
    private String question;
    private String answer;

    public Question() {}
    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return this.question + "\nотговор: " + this.answer;
    }

}
