package com.example.textexp;

public class Question {
    public String question = "question";
    public String opt = "opt";
    public String answer = "answer";
    public int leveNum = 1;
    public int ID;
    public String type = "type";

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getLeveNum() {
        return leveNum;
    }

    public void setLeveNum(int leveNum) {
        this.leveNum = leveNum;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
