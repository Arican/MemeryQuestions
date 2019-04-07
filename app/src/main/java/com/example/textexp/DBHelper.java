package com.example.textexp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

//import static android.content.Context.MODE_PRIVATE;
//import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class DBHelper extends SQLiteOpenHelper {

    //数据库版本号
    private static final int DATABASE_VERSION = 1;

    //数据库名称
    private static final String DATABASE_NAME = "questionDb.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //生成数据库
    public void onCreateDb() {
//
//        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("questionDb.db", null);
//        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase("questionDb.db", null);
//        String CREATE_DB = "$sqlite3 questionDb.db";
//        SQLiteDatabase db.execSQL(CREATE_DB);
//                SQLiteDatabase db = openOrCreateDatabase("/data/data/com.example.textEmp/databases/questionDb.db", MODE_PRIVATE, null);
//        SQLiteDatabase db = openOrCreateDatabase("questionDb.db", MODE_PRIVATE, null);
//        Toast.makeText(this, "openOrCreateDatabase(\"questionDb.db\", MODE_PRIVATE, null);", Toast.LENGTH_LONG).show();
//        db.close();
//        Toast.makeText(getApplicationContext(), "db.close();", Toast.LENGTH_LONG).show();
    }

    //生成数据表
    public void onCreateTable(SQLiteDatabase db, String TABLE_NAME) {
//        onCreateDb();questionDb.
//        如果SQLite中已存在这个名字的数据表，不再生成新表
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS  " + TABLE_NAME +
                "(ID INTEGER   PRIMARY KEY AUTOINCREMENT , " +
                "question text," +
                "opt text, " +
                "answer text, " +
                "type INTEGER, " +
                "leveNum INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    public void addQuestion(SQLiteDatabase db, Question question) {
        String INSERT_QUESTION1 = "Insert Into 测试表(question,opt, answer, type,leveNum) Values('题目1','选项1','答案1',1,1 )";
        String INSERT_QUESTION2 = "Insert Into 测试表(question,opt, answer, type,leveNum) Values('题目2','选项2',答'案2',1,1 )";
//        SQLiteDatabase db = helper.getWritableDatabase();
//        Log.i("asset", "addQuestion: ");
        db.execSQL(INSERT_QUESTION1);
//        db.execSQL(INSERT_QUESTION2);
        db.close();
    }

    public void insertQuestion(SQLiteDatabase db, Question question) {
        insertQuestion(db, question, "测试表");
    }

    public void insertQuestion(SQLiteDatabase db, Question question, String tableName) {
        String INSERT_QUESTION1 = "Insert Into 测试表(question,opt, answer, type,leveNum) Values('题目1','选项1','答案1',1,1 )";
        String INSERT_QUESTION2 = "Insert Into " + tableName + " (question,opt, answer, type,leveNum) Values ( '" +
                question.question +
                "','" + question.opt +
                "','" + question.answer +
                "','" + question.type +
                "'," + question.leveNum + ")";
//        SQLiteDatabase db = helper.getWritableDatabase();
//        Log.i("asset", "执行insertQuestion: " + INSERT_QUESTION2);
//        db.execSQL(INSERT_QUESTION1);
        db.execSQL(INSERT_QUESTION2);
//        db.close();
    }

    public void queryQuestion(SQLiteDatabase db) {
        this.queryQuestion(db, "测试表");

    }

    public void queryQuestion(SQLiteDatabase db, String tablename) {
        // Cursor为查询结果对象，类似于JDBC中的ResultSet

//        Log.i("asset", "查询数据开始");
        Cursor queryResult = db.rawQuery("select * from " + tablename, null);
//        Log.i("asset", "记录数据开始");

        if (queryResult != null) {
            Log.i("asset", "记录数据开始循环");
            while (queryResult.moveToNext()) {
                Log.i("asset",
                        "ID: "
                                + queryResult.getInt(queryResult
                                .getColumnIndex("ID"))
                                + "  question: "
                                + queryResult.getString(queryResult
                                .getColumnIndex("question"))
                                + " opt: "
                                + queryResult.getString(queryResult
                                .getColumnIndex("opt"))
                                + " answer: "
                                + queryResult.getString(queryResult
                                .getColumnIndex("answer"))
                                + " type: "
                                + queryResult.getString(queryResult
                                .getColumnIndex("type"))
                                + " leveNum: "
                                + queryResult.getInt(queryResult
                                .getColumnIndex("leveNum")));
            }
            // 关闭游标对象
            queryResult.close();
        }
        db.close();
    }

    public ArrayList<String> selectOccupationList(SQLiteDatabase db) {
//      搜索所有的table，获得名字。
        Cursor queryResult = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name; ", null);
        ArrayList<String> titleList = new ArrayList<>();
        if (queryResult != null) {
//            Log.i("asset", "记录数据开始循环");
            while (queryResult.moveToNext()) {
//                Log.i("asset",
//                        "ID: "
//                                + " table: "
//                                + queryResult.getString(queryResult
//                                .getColumnIndex("name"))
//                );
                String string = queryResult.getString(queryResult.getColumnIndex("name"));
//                Log.i("table，获得名字", "selectOccupationList: "+string);
                if (!string.equals("android_metadata") && !string.equals("sqlite_sequence"))
                    titleList.add(string);
            }
            // 关闭游标对象
            queryResult.close();
        }
//        db.close();
        return titleList;
    }

    public void deletTable(SQLiteDatabase db) {
        //如果旧表存在，删除，所以数据将会消失
        db.execSQL("DROP TABLE IF EXISTS 测试表 ");
//        db.execSQL("DROP TABLE 测试表 " );
        //再次创建表
//        onCreate(db);
    }

    public void deletTable(SQLiteDatabase db, ArrayList<String> list) {
        //如果旧表存在，删除，所以数据将会消失
        for (int i = 0; i < list.size(); i++) {
            db.execSQL("DROP TABLE IF EXISTS  " + list.get(i));
            Log.i("", "deletTable: 删除工作表：" + list.get(i));
        }
//        db.execSQL("DROP TABLE 测试表 " );
    }

    public void deletTable(SQLiteDatabase db, String table_name) {
        //如果旧表存在，删除，所以数据将会消失

        db.execSQL("DROP TABLE IF EXISTS  " + table_name);

//        db.execSQL("DROP TABLE 测试表 " );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
//        String CREATE_TABLE_Question="CREATE TABLE IF NOT EXISTS "+ Question.TABLE+"("
//                +Question.KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
//                +Question.quest +" TEXT, "
//                +Question.getOpt()+" INTEGER, "
//                +Question.KEY_email+" TEXT)";
//        String CREATE_TABLE_Question = "CREATE TABLE IF NOT EXISTS  question." + db. + occupation;
//        db.execSQL(CREATE_TABLE_Question);
//        onCreateTable(db, "电工题");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果旧表存在，删除，所以数据将会消失
//        db.execSQL("DROP TABLE IF EXISTS " + Question.TABLE);

        //再次创建表
//        onCreate(db);
    }

    public int updateLeveNum(SQLiteDatabase db, Myapplication myapplication, boolean up) {
//修改SQL语句
        Cursor queryResult;

        Log.i(TAG, "updateLeveNum: " + "select * from '" + myapplication.occupation + "' where ID = " + myapplication.question.getID());
        queryResult = db.rawQuery("select * from '" + myapplication.occupation + "' where ID = " + myapplication.question.getID(), null);
        queryResult.moveToPosition(0);
        int leveNum = queryResult.getInt(queryResult.getColumnIndex("leveNum"));
        int leveNum2 = leveNum + 2;
        int leveNum1 = leveNum;
        if (leveNum > 1)
            leveNum1 = leveNum - 1;
        if (up == true) {
            leveNum = leveNum1;
        } else leveNum = leveNum2;

        String sql = "update " + myapplication.occupation + " set leveNum = " + leveNum + " where ID = " + myapplication.question.getID();
        if (myapplication.setLeve0 == 10 && up == false)
            sql = "update " + myapplication.occupation + " set leveNum = 0 where ID = " + myapplication.question.getID();
//执行SQL
        queryResult.close();
        db.execSQL(sql);
        //执行更新后，重新查询题目难度，并返回
        queryResult = db.rawQuery("select * from '" + myapplication.occupation + "' where ID = " + myapplication.question.getID(), null);
        queryResult.moveToPosition(0);
        leveNum = queryResult.getInt(queryResult.getColumnIndex("leveNum"));
        Log.i(TAG, "updateLeveNum: leveNum = " + leveNum);
        queryResult.close();
        return leveNum;
    }

    public Question getRadomQuestion(String occupation, String type, Myapplication myapplication, SQLiteDatabase db) {
        return getRadomQuestion(occupation, type, myapplication, db, 0);
    }

    public Question getRadomQuestion(String occupation, String type, Myapplication myapplication, SQLiteDatabase db, int limit) {

        Question question = myapplication.question;
//            SQLiteDatabase db = openOrCreateDatabase("questionDb.db", Context.MODE_PRIVATE,  null);
        String str;
        Cursor queryResult;
        if (type.equals("radom")) {     //get100题调用，随机选题，不受题型限制。
            queryResult = db.rawQuery("select * from " + occupation, null);
        } else                          //根据题型，随机选题，受题型限制。主程序调用。
            queryResult = db.rawQuery("select * from " + occupation + " where type='" + type + "' and leveNum >= " + myapplication.leveNumLimit, null);
        int num = queryResult.getCount();
//        myapplication.queryResult = queryResult;
        myapplication.count = num;
        Log.i("getRadomQuestion", "查询结果数量：" + num);
//        if (queryResult != null) {
//
//        }
//        Math.Random()函数能够返回带正号的double值，该值大于等于0.0且小于1.0，即取值范围是[0.0,1.0)的左闭右开区间，返回值是一个伪随机选择的数，在该范围内（近似）均匀分布
        int tag = (int) Math.floor(Math.random() * myapplication.count);
        Log.i(".", "查询题目总数 =  " + num + " 目标 question 序号：" + tag);

//        myapplication.queryResult.moveToPosition(tag);
//        Log.i("myapplication.queryResult", "移动前上次题目类型 myapplication.lasttype " + myapplication.lasttype);
//        Log.i("myapplication.queryResult", "移动前 myapplication.lastQuestion" + myapplication.lastQuestion);
        myapplication.lasttype = myapplication.type;
        myapplication.lastQuestion = myapplication.question;
        queryResult.moveToPosition(tag);
//        Log.i("myapplication.queryResult", " 移动结束，传送数据到questionID " + queryResult.getString(queryResult.getColumnIndex("ID")));
        question.ID = queryResult.getInt(queryResult.getColumnIndex("ID"));
//        question.setID(queryResult.getInt(queryResult.getColumnIndex("ID")));
//        int id = queryResult.getInt(queryResult.getColumnIndex("ID"));
//        Log.i("myapplication.queryResult", " 移动结束，传送数据到question         " + queryResult.getString(queryResult.getColumnIndex("question")));
        question.question = queryResult.getString(queryResult.getColumnIndex("question"));
//        Log.i("myapplication.queryResult", " 移动结束，传送数据到questionopt      " + queryResult.getString(queryResult.getColumnIndex("opt")));
        question.opt = queryResult.getString(queryResult.getColumnIndex("opt"));
//        Log.i("myapplication.queryResult", " 移动结束，传送数据到question answer  " + queryResult.getString(queryResult.getColumnIndex("answer")));
        question.answer = queryResult.getString(queryResult.getColumnIndex("answer"));
//        Log.i("myapplication.queryResult", " 移动结束，传送数据到question type    " + queryResult.getString(queryResult.getColumnIndex("type")));
        question.type = queryResult.getString(queryResult.getColumnIndex("type"));
//        Log.i("myapplication.queryResult", " 移动结束，传送数据到question leveNum " + queryResult.getString(queryResult.getColumnIndex("leveNum")));
        question.leveNum = queryResult.getInt(queryResult.getColumnIndex("leveNum"));
//        Log.i("myapplication.queryResult", " 移动结束，传送数据到question "+queryResult.getString(queryResult.getColumnIndex("question")));
        queryResult.close();
        myapplication.question = new Question();
        myapplication.question = question;
//        Log.i("", " 移动结束，传送数据，全局变量 myapplication.question.question =    " + myapplication.question.question);
//        Log.i("", " 移动结束，传送数据，全局变量 myapplication.question.answer =    " + myapplication.question.answer);
        return question;
    }

    public int getCount(String occupation, SQLiteDatabase db) {
        int count = 0;
        Cursor c = db.rawQuery("SELECT count(*) FROM " + occupation, null);
        c.moveToPosition(0);
        count = c.getInt(0);
        Log.i("", "getCount: table名字：" + occupation + "， 查询整个table的行数：" + count);
        return count;
    }

    public void get100RadomQuestion(String occupation, String type, Myapplication myapplication, SQLiteDatabase db) {
        Question question;      //随机生成100道题目，保存ID到全局变量myapplication.list_ID[100]
        int flag = 0;
        int count = getCount(occupation, db);
//        if (count > 100) count = 100;
        Log.i(TAG, "get100RadomQuestion: 全局变量初始赋值 myapplication.list_ID[i] = 0");
        for (int i = 0; i < 100; i++) {
            myapplication.list_ID[i] = 0;
            myapplication.list_check[i] = 0;
        }
        boolean skip;
        for (int i = 0; ; ) {
            question = getRadomQuestion(myapplication.occupation, "radom", myapplication, db);
            skip = false;
            for (int j = 0; j < i; j++) {
                if (question.getID() == myapplication.list_ID[j]) {
                    skip = true;
                    break;
                }
            }
            if (i > 10000) {
                Log.i(TAG, "get100RadomQuestion: 取值次数超过10000次，退出循环。");
                break;
            }

            if (skip == false) {
                myapplication.list_ID[flag] = question.getID();
                Log.i("", "get100RadomQuestion:  myapplication.list_ID[" + flag + "]  =  ID = " + question.getID());
                if (i == count - 1 || flag == 99) {
                    Log.i("", "get100RadomQuestion: i = " + i + " flag = " + flag + " count = " + count + "  break;");
                    myapplication.get100 = true;

                    //结束之前，对myapplication.list_ID[j]进行排序，待添加
                    begin:
                    for (int x = 1; x < 100; x++) {
                        int temp = myapplication.list_ID[x];
                        int temp1;
//                        Log.i(TAG, "get100RadomQuestion: 当前myapplication.list_ID[x] = " + myapplication.list_ID[x]);
                        for (int y = 0; y < x; y++) {
//                            Log.i(TAG, "get100RadomQuestion: 当前myapplication.list_ID[x]" + x + " = " + myapplication.list_ID[x] + ",myapplication.list_ID[y]" + y + " = " + myapplication.list_ID[y]);
                            if (myapplication.list_ID[y] > temp) {
                                Log.i(TAG, "get100RadomQuestion: myapplication.list_ID[y] = " + myapplication.list_ID[y] + " > temp  = " + temp);
//                                temp1 = myapplication.list_ID[y];
//                                myapplication.list_ID[y] = temp;
//                                temp = temp1;


//                                int temp2 = myapplication.list_ID[x];
                                for (int z = x; z > y; z--) {
                                    myapplication.list_ID[z] = myapplication.list_ID[z - 1];
                                    Log.i(TAG, "get100RadomQuestion: myapplication.list_ID[z]" + z + " = " + myapplication.list_ID[z]);
                                }
                                myapplication.list_ID[y] = temp;
                                continue begin;
                            }
                        }
                    }
                    for (int x = 1; x < 100; x++) {
                        Log.i(TAG, "get100RadomQuestion: 当前myapplication.list_ID[x]" + x + " = " + myapplication.list_ID[x]);
                    }
                    return;
                }
                i++;
                flag++;
            }
        }

//        return question;
    }

    public Question getQuestionById(String occupation, int id, SQLiteDatabase db) {
        Question question = new Question();
        Cursor queryResult = db.rawQuery("select * from " + occupation + " where ID =" + id, null);
        queryResult.moveToPosition(0);
        question.ID = queryResult.getInt(queryResult.getColumnIndex("ID"));
        question.question = queryResult.getString(queryResult.getColumnIndex("question"));
        question.opt = queryResult.getString(queryResult.getColumnIndex("opt"));
        question.answer = queryResult.getString(queryResult.getColumnIndex("answer"));
        question.type = queryResult.getString(queryResult.getColumnIndex("type"));
        question.leveNum = queryResult.getInt(queryResult.getColumnIndex("leveNum"));
        queryResult.close();
        return question;
    }
}
