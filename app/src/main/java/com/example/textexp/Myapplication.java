package com.example.textexp;

import android.app.Application;
//import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Myapplication extends Application {

    //声明一个变量
    public String type = "单选题";
    public String lasttype = "多选题";
    public Question question = new Question();
    public Question lastQuestion = new Question();
    public String occupation = "电工";


    int count;                                  //查询题库总数量
    int[] list_ID = new int[100];               //存储100个题目的id
    int[] list_check = new int[100];         //存储100个题目正确与否。初始0，正确1，错误2
    boolean get100 = false;                     //是否启用，答题100个题目。
    int flag = 0;                               //答题100个题目，进度数目。
    int leveNumLimit = 0;                           //题目难度限制，最低难度
    int setLeve0;                               //设置题目难度标志位。
    SQLiteDatabase db;
    @Override

    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Question getLastQuestion() {
        return lastQuestion;
    }

    public void setLastQuestion(Question lastQuestion) {
        this.lastQuestion = lastQuestion;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }





}