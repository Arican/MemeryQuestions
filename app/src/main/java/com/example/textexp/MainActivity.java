package com.example.textexp;


import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
//    Myapplication myapplication = null;

    //获得对象
//    Myapplication myapplication = (Myapplication) getApplication();
//    getApplication();
    private TextView mTextMessage;
    //    Myapplication myapplication;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Myapplication myapplication = (Myapplication) getApplication();
            switch (item.getItemId()) {
                case R.id.selectOne:
//                    mTextMessage.setText("单选题");
                    if (!myapplication.get100) {
                        myapplication.type = "单选题";
                        nextQuestion();
                    }
                    return true;
                case R.id.selectMul:
//                    mTextMessage.setText("多选题");
                    if (!myapplication.get100) {
                        myapplication.type = "多选题";
                        nextQuestion();
                    }
                    return true;
                case R.id.judge:
                    if (!myapplication.get100) {
//                    mTextMessage.setText("判断题");
                        myapplication.type = "判断题";
                        nextQuestion();
                    }
                    return true;
            }
            return false;
        }
    };
    private BottomNavigationView.OnNavigationItemSelectedListener bottomOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Question question = new Question();
            Myapplication myapplication = (Myapplication) getApplication();
            DBHelper dbHelper = new DBHelper(getApplicationContext());
            SQLiteDatabase db = openOrCreateDatabase("questionDb.db", MODE_PRIVATE, null);
            switch (item.getItemId()) {
                case R.id.previous:
//                    mTextMessage.setText("上一题");
                    Log.i("", "上一题");
                    if (myapplication.get100) {
                        get100previous();
                    } else {
                        //随机测试上一题，标志位 myapplication.radom_flag 指示当前浏览位置，myapplication.radom_list[100] 存储最近的题目。
//                       0 =< radom_flag =< 99 ,题目浏览在历史区间内，“下一题”按钮不再检测题目是否正确。
//                      radom_flag == 0 ，“下一题”按钮检测题目是否正确，并使最近题目列表前移。

                        mTextMessage.setText(myapplication.lastQuestion.question + "\n" + myapplication.lastQuestion.opt);
                        Log.i("", "上一题结束");
                    }
                    return false;
                case R.id.answer:
//                    mTextMessage.setText("验证答案");
                    if (checkAnswer()) {
//                        Toast.makeText(getApplicationContext(), "答题正确", Toast.LENGTH_LONG).show();
                    }
                    return false;
                case R.id.next:
//                    mTextMessage.setText("下一题");
                    if (myapplication.get100) {             //生成100道题测试，测试模式，验证答案后直接下一题
                        checkAnswer();
                        nextQuestion();
                    } else if (checkAnswer() || "question".equals(myapplication.question.question)) {            //检查答案是否正确，正确后显示下一题。
                        //"下一题"按钮，检查答案正确，题目leveNum - 1，checkAnswer()中，答案不为空，检查答案错误，题目leveNum + 2
                        dbHelper.updateLeveNum(db, myapplication, true);
                        nextQuestion();
                    } else {

                    }
                    return false;
            }
            return false;
        }
    };

    private void nextQuestion() {
        //根据题型和工种（table）随机选择一道题显示
        //得到题目
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        Myapplication myapplication = (Myapplication) getApplication();
        SQLiteDatabase db = openOrCreateDatabase("questionDb.db", MODE_PRIVATE, null);
        Question question = new Question();
        if (myapplication.get100) {             //生成100道题测试
            get100next();
            return;
        }
//        Log.i("", "查询试题");
//        Log.i("", "myapplication.lasttype = " + myapplication.lasttype);
//        Log.i("", "myapplication.type = " + myapplication.type);
        try {           //随机产生题目
            question = dbHelper.getRadomQuestion(myapplication.occupation, myapplication.type, myapplication, db);
            Log.i("返回值", "myapplication.question = " + myapplication.question);
        } catch (Exception e) {

        }
//        Log.i("返回值", "myapplication.type = " + myapplication.type);
        if (question.question.equals("question")) {
//            Toast.makeText(getApplicationContext(), "当前题库选择为空，请在菜单栏选择题库", Toast.LENGTH_LONG).show();
            mTextMessage.setText("当前题库选择为空，请在菜单栏选择题库,然后点击上面的题目类型开始练习");
        } else
            mTextMessage.setText(question.question + "\n" + question.opt);
//        cleanOpts( );
//        Activity.getWindow()
//        (View)findViewById(R.id.top_view);
        View layout_list = View.inflate(this, R.layout.activity_main, null);
        cleanOpts(layout_list);
    }

    private boolean checkAnswer() {
        Log.i(TAG, "checkAnswer: ");
        Myapplication myapplication = (Myapplication) getApplication();
        CheckBox A = findViewById(R.id.A);
        CheckBox B = findViewById(R.id.B);
        CheckBox C = findViewById(R.id.C);
        CheckBox D = findViewById(R.id.D);
        CheckBox E = findViewById(R.id.E);
        CheckBox F = findViewById(R.id.F);
        CheckBox G = findViewById(R.id.G);
        CheckBox H = findViewById(R.id.H);
        CheckBox correct = findViewById(R.id.correct);
        CheckBox negative = findViewById(R.id.negative);
        String str = "";   //存放选中的选项的值
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = openOrCreateDatabase("questionDb.db", MODE_PRIVATE, null);
        Question question = new Question();
//        if (myapplication.get100) {
//            question = dbHelper.getQuestionById(myapplication.occupation, myapplication.list_ID[myapplication.flag], db);
//        }
//        if (myapplication.type.equals("单选题") | myapplication.type.equals("多选题") || (myapplication.get100 && (question.type.equals("单选题") | question.type.equals("多选题")))) {
        if (A.isChecked())
            str += A.getText().toString() + ",";
        if (B.isChecked())
            str += B.getText().toString() + ",";
        if (C.isChecked())
            str += C.getText().toString() + ",";
        if (D.isChecked())
            str += D.getText().toString() + ",";
        if (E.isChecked())
            str += E.getText().toString() + ",";
        if (F.isChecked())
            str += F.getText().toString() + ",";
        if (G.isChecked())
            str += G.getText().toString() + ",";
        if (H.isChecked())
            str += H.getText().toString() + ",";
        if (correct.isChecked())
            str += "正确,";
        if (negative.isChecked())
            str += "错误,";
        if (str.length() != 0)
            str = str.substring(0, str.length() - 1);
//        }


        if (myapplication.get100) {
            question = dbHelper.getQuestionById(myapplication.occupation, myapplication.list_ID[myapplication.flag], db);
            Log.i(TAG, "checkAnswer: myapplication.flag = " + myapplication.flag);
            Log.i(TAG, "checkAnswer: question.question = " + question.question);
            Log.i(TAG, "checkAnswer: question.answer = " + question.answer);
            if (question.answer.equals(str)) {
                Toast.makeText(getApplicationContext(), "答题正确", Toast.LENGTH_LONG).show();
                myapplication.list_check[myapplication.flag] = 1;       //生成100成功
                return true;
            } else {
                myapplication.list_check[myapplication.flag] = 2;       //生成100失败
                if (!"".equals(str)) {
                    int le = dbHelper.updateLeveNum(db, myapplication, false);
                    Toast.makeText(getApplicationContext(), "你的答案是：" + str +
                            "\n正确答案是：" + myapplication.question.answer +
                            "\n当前题目难度：" + le, Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "你的答案是：" + str +
                            "\n正确答案是：" + myapplication.question.answer, Toast.LENGTH_LONG).show();
                return false;
            }
        } else if (myapplication.question.answer.equals(str)) {
            Toast.makeText(getApplicationContext(), "答题正确", Toast.LENGTH_LONG).show();
            return true;
        } else {
            if (!"".equals(str)) {
                int le = dbHelper.updateLeveNum(db, myapplication, false);
                Toast.makeText(getApplicationContext(), "你的答案是：" + str +
                        "\n正确答案是：" + myapplication.question.answer +
                        "\n当前题目难度：" + le, Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(getApplicationContext(), "你的答案是：" + str +
                        "\n正确答案是：" + myapplication.question.answer, Toast.LENGTH_LONG).show();
            //        Log.i("", "你的答案是1：" + str);
//        Log.i("", "正确答案是1：" + myapplication.question.answer);
            //"下一题"按钮，检查答案正确，题目leveNum - 1，checkAnswer()中，答案不为空，检查答案错误，题目leveNum + 2

            return false;
        }
    }

    public void setLeve0(View view) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = openOrCreateDatabase("questionDb.db", MODE_PRIVATE, null);
        Myapplication myapplication = (Myapplication) getApplication();
        myapplication.setLeve0 = 10;
        int num = dbHelper.updateLeveNum(db, myapplication, false);
        myapplication.setLeve0 = 0;
        Toast.makeText(getApplicationContext(),
                "当前题目序号：" + myapplication.question.ID +
                        "\n当前题目难度：" + num, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Myapplication myapplication = (Myapplication) getApplication();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottom = findViewById(R.id.nav_view);
        bottom.setSelectedItemId(R.id.answer);
        bottom.setItemIconTintList(null);
//        bottom.setItemTextColor(null);
        BottomNavigationView navView = findViewById(R.id.top_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottom.setOnNavigationItemSelectedListener(bottomOnNavigationItemSelectedListener);

        getOverflowMenu();//添加方法，绘制菜单键（三点）
        nextQuestion();
//        Android图标的引用：
//        在源代码*.Java中可以进入如下方式引用：
//        myMenuItem.setIcon(android.R.drawable.ic_menu_save);
//        在*.XML文件中的<resource>可以进行如下引用：
//        android:icon="@android:drawable/ic_menu_save"
    }

    //    向菜单键列表中插入新内容，附在原视图后面显示
    public void setconctent() {
        setContentView(R.layout.activity_main);
        BottomNavigationView bottom = findViewById(R.id.nav_view);
        bottom.setSelectedItemId(R.id.answer);
        bottom.setItemIconTintList(null);
        BottomNavigationView navView = findViewById(R.id.top_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottom.setOnNavigationItemSelectedListener(bottomOnNavigationItemSelectedListener);
    }

    public void next(View view) throws IOException {
        String str = "按钮点击next";
        mTextMessage.setText(str);
        Question question = new Question();
//        AssetManager am = null;
//        am = getAssets();

        InputStream inputStream = null;
        inputStream = getAssets().open("点检题库.xls");
//
        //读取工作簿
        HSSFWorkbook workBook = new HSSFWorkbook(inputStream);
        //读取工作表
        HSSFSheet sheet = workBook.getSheetAt(0);
        String sheet_name = sheet.getSheetName();
        //读取行
        HSSFRow row = sheet.getRow(4);
        //读取单元格
        HSSFCell cell = row.getCell((short) 1);
//        String value = cell.getStringCellValue();
//        TextView tv = (TextView) findViewById(R.id.message);
//        tv.setText(value + "\n" + row.getCell((short) 2).getStringCellValue());
        question.question = row.getCell((short) 1).getStringCellValue();
        question.opt = row.getCell((short) 2).getStringCellValue();
        question.answer = row.getCell((short) 3).getStringCellValue();
        question.type = row.getCell((short) 0).getStringCellValue();

        inputStream.close();
    }

    public void inputExcel(String sheetname) {
        Question question = new Question();
        InputStream inputStream = null;

        SQLiteDatabase db = openOrCreateDatabase("questionDb.db", MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(getApplicationContext());
//        Myapplication myapplication = (Myapplication) getApplication();
        ArrayList<String> titleList = new ArrayList<>();
        titleList = dbHelper.selectOccupationList(db);      //从数据库中得到所有表的名字
        db.close();
        for (int i = 0; i < titleList.size(); i++) {
            if (sheetname.equals(titleList.get(i))) {
                Toast.makeText(getApplicationContext(), "当前题库已存在，导入操作未执行", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (titleList.size() == 0) {
            Toast.makeText(getApplicationContext(), "当前环境中没有题库，请导入题库", Toast.LENGTH_LONG).show();
            Log.i(TAG, "inputExcel: 当前环境中没有题库，请导入题库");
        }
        try {
            inputStream = getAssets().open("点检题库.xls");
            //读取工作簿
            HSSFWorkbook workBook = new HSSFWorkbook(inputStream);
            //读取工作表
//        HSSFSheet sheet = workBook.getSheetAt(0);
            HSSFSheet sheet = workBook.getSheet(sheetname);
//        String sheet_name = sheet.getSheetName();
            dbHelper = new DBHelper(getApplicationContext());
            db = openOrCreateDatabase("questionDb.db", MODE_PRIVATE, null);
            dbHelper.onCreateTable(db, sheetname);
//        SQLiteDatabase db = null;
//        Log.i("", "提取数据");
            int j = 0;
            for (int i = 0; ; i++) {
                j = i;
                //读取行
                HSSFRow row = sheet.getRow(i);
                //读取单元格
                //            HSSFCell cell = row.getCell((short) 1);
                if (i == 5000) {
                    Log.i("", "行数超过5000，break");
                    break;
                }
                try {
                    question.question = row.getCell((short) 1).getStringCellValue();
                } catch (Exception e) {
                    Log.i("", "查询单元格引用为空，break");
                    break;
                }
                if (question.question == "") break;
//               if (row.getCell((short) 2).equals(null))
                if (row.getCell((short) 2) != null)
                    question.opt = row.getCell((short) 2).getStringCellValue();
                else question.opt = "";
                question.answer = row.getCell((short) 3).getStringCellValue();
                question.type = row.getCell((short) 0).getStringCellValue();
                question.leveNum = 1;
                //            Log.i("", "inputExcel: question 提取完毕");

                //            Log.i("", "生成db");
//                Log.i("" + i, question.question);
                dbHelper.insertQuestion(db, question, sheetname);
            }
            Toast.makeText(getApplicationContext(), "题库" + sheetname + "共导入条目数量： " + j, Toast.LENGTH_LONG).show();
            Log.i(TAG, "inputExcel: " + "题库“" + sheetname + "”共导入条目数量： " + j);
            db.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
//         RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
//         operation succeeded. 默认值是-1
//        没有显式的调用，执行startActivityForResult（）后，此方法仍然执行。
        DBHelper dbHelper = new DBHelper(getApplicationContext());
//        导入题库
        if (resultCode == 1) {
            if (requestCode == 1) {
//                int size = data.getIntExtra("count",0);
//                        data.getStringArrayListExtra("count");
                ArrayList<String> titleList = intent.getStringArrayListExtra("name_list");
//                Log.i(TAG, "主程序获得checkbox列表返回值大小m：" + titleList.size());
                if (titleList.size() > 0) {
                    for (int i = 0; i < titleList.size(); i++) {
//                        Log.i(TAG, "主程序获得checkbox列表返回值m：" + i + " = " + titleList.get(i));
//                        dbHelper.
                        inputExcel(titleList.get(i));   //接收sheet表名，引入sheet数据库
                    }
                }
            }
        }
//        选择题库
        if (requestCode == 2 && resultCode == 2) {
//            Log.i(TAG, "onActivityResult: 选择题库425");
            Myapplication myapplication = (Myapplication) getApplication();
//            Log.i(TAG, "onActivityResult: intent.getStringArrayListExtra(\"name_list\").get(0) = " + intent.getStringArrayListExtra("name_list").get(0));
            myapplication.occupation = intent.getStringArrayListExtra("name_list").get(0);
        }
//      删除题库
        if (requestCode == 4 && resultCode == 2) {
//            Log.i(TAG, "onActivityResult: 选择要删除的题库425");
//            Myapplication myapplication = (Myapplication) getApplication();
//            Log.i(TAG, "onActivityResult: intent.getStringArrayListExtra(\"name_list\").get(0) = " + intent.getStringArrayListExtra("name_list").get(0));
//            myapplication.occupation = intent.getStringArrayListExtra("name_list").get(0);
            SQLiteDatabase db = openOrCreateDatabase("questionDb.db", MODE_PRIVATE, null);
            dbHelper.deletTable(db, intent.getStringArrayListExtra("name_list"));
            db.close();
        }

//        生成新表，传递一个数据库连接和新表名称。
//        SQLiteDatabase db = openOrCreateDatabase("questionDb.db", MODE_PRIVATE, null);
//        DBHelper dbHelper = new DBHelper(getApplicationContext());
//        dbHelper.onCreateTable(db, "测试表");

    }

    public void creatdb(View view) throws IOException {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.onCreateDb();
    }

    //添加方法，绘制菜单键（三点）
    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(getApplicationContext());//得到一个已经设置好设备的显示密度的对象
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");//反射获取其中的方法sHasPermanentMenuKey()，他的作用是报告设备的菜单是否对用户可用，如果不可用可强制可视化。
            if (menuKeyField != null) {
                //强制设置参数,让其重绘三个点
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 填充menu的main.xml文件; 给action bar添加条目
        getMenuInflater().inflate(R.menu.menu, menu);
        // 相当于在res/menu/main.xml文件中，给menu增加一个新的条目item，这个条目会显示title标签的文字（如备注1）
        //第二个参数代表唯一的item ID.

        menu.add(0, 0, 1, "->查看100道题成绩");
        menu.add(0, 1, 2, ">查看错误题目");
        menu.add(0, 2, 3, "->设置题目难度限制");
        menu.add(0, 3, 4, "查询数据");
        menu.add(0, 4, 5, "->删除题库表");
        menu.add(0, 5, 6, "题库格式说明");
        menu.add(0, 6, 7, "带有“->”标记为已完成功能");
        return true;
    }

    //响应“菜单键（三点）”中选择的选项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SQLiteDatabase db = openOrCreateDatabase("questionDb.db", MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        Question question = new Question();
        question.question = "问题3";
        question.opt = "选项";
        question.type = "单选题";
        question.leveNum = 1;
        question.answer = "答案3";
        Myapplication myapplication = (Myapplication) getApplication();
        Intent intent;
        ArrayList<String> titleList = new ArrayList<>();
        switch (item.getItemId()) {
            case 0://显示由menu.add()方法增加内容item的ID，查看100道题成绩
                Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
//                inputExcel(myapplication.occupation);
                int corect = 0;
                int mistake = 0;
                int mis = 0;
                for (int i = 0; i < 100; i++) {
                    if (myapplication.list_check[i] == 0) mis++;
                    if (myapplication.list_check[i] == 1) corect++;
                    if (myapplication.list_check[i] == 2) mistake++;
                }
                mTextMessage.setText("正确试题数量：" + corect +
                        "\n错误试题数量：" + mistake +
                        "\n未做试题数量：" + mis);
                return true;
//            Toast.makeText(getApplicationContext(), "响应“选择题库”按钮", Toast.LENGTH_LONG).show();

            case 1://显示由menu.add()方法增加内容item的ID，查看错误题目
//                dbHelper.onCreateTable(db, "测试表");
//                Log.i("asset", "生成新表：测试表");
//                Toast.makeText(getApplicationContext(), "生成表", Toast.LENGTH_LONG).show();
                String str = "";
                for (int i = 0; i < 100; i++) {
                    if (myapplication.list_check[i] == 2) {
                        question = dbHelper.getQuestionById(myapplication.occupation, myapplication.list_ID[i], db);
                        str = str + question.ID + question.question + "\n" + question.answer + "\n答案：" + question.answer + "\n";
                    }
                }
                mTextMessage.setText("所有错误试题：\n" + str);
                return true;
            case 2://设置leveNumLimit
//                dbHelper = new DBHelper(getApplicationContext());
//                dbHelper.addQuestion(db, question);
                intent = new Intent(MainActivity.this, LeveNumLimit.class);
                startActivityForResult(intent, 5);
                Log.i("asset", "设置leveNumLimit");
                Toast.makeText(getApplicationContext(), "设置leveNumLimit", Toast.LENGTH_LONG).show();
                return true;
            case 3://显示由menu.add()方法增加内容item的ID，查询数据
                try {
                    dbHelper.queryQuestion(db, myapplication.occupation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "查询数据", Toast.LENGTH_LONG).show();
                return true;
            case 4://显示由menu.add()方法增加内容item的ID，查询数据,,,根据table名字删除题库
                titleList = dbHelper.selectOccupationList(db);      //从数据库中得到所有表的名字
                db.close();
                if (titleList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "当前环境中没有题库，请导入题库", Toast.LENGTH_LONG).show();
                    return false;
                }
                intent = new Intent(MainActivity.this, OccupationList.class);
                intent.putStringArrayListExtra("table_names", titleList);
//                Log.i(TAG, "主程序，intent取出数据 = " + intent.getStringArrayListExtra("table_names").size());
//                for (int i = 0; i < intent.getStringArrayListExtra("table_names").size(); i++) {
//                    Log.i(TAG, "主程序，intent取出所有数据 = " + intent.getStringArrayListExtra("table_names").get(i));
//                }
                startActivityForResult(intent, 4);
                Toast.makeText(getApplicationContext(), "删除数据表", Toast.LENGTH_LONG).show();
                return true;
            case 5:
                intent = new Intent(MainActivity.this, OccupationList.class);
                startActivityForResult(intent, 5);
                return true;
            case R.id.chooseQuestion:   //选择题库      use table
                Toast.makeText(getApplicationContext(), "响应“选择题库”按钮", Toast.LENGTH_LONG).show();
                intent = new Intent(MainActivity.this, OccupationList.class);
                titleList = dbHelper.selectOccupationList(db);      //从数据库中得到所有表的名字
                db.close();
                if (titleList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "当前环境中没有题库，请导入题库", Toast.LENGTH_LONG).show();
                    return false;
                }
                Log.i("", "onClick: startActivityForResult(intent,2);选择题库");
                Log.i(TAG, "主程序，intent存入数据 = " + titleList.size());
                intent.putStringArrayListExtra("table_names", titleList);
                intent.putExtra("name", "电工");
//                Log.i(TAG, "主程序，intent取出数据 = " + intent.getStringArrayListExtra("table_names").size());
//                for (int i = 0; i < intent.getStringArrayListExtra("table_names").size(); i++) {
//                    Log.i(TAG, "主程序，intent取出所有数据 = " + intent.getStringArrayListExtra("table_names").get(i));
//                }
                startActivityForResult(intent, 2);
                return true;
            case R.id.inportQuestion://显示由xml添加的item的ID
                Toast.makeText(getApplicationContext(), "正确答案：A\n你的答案：C", Toast.LENGTH_LONG).show();
                intent = new Intent(MainActivity.this, list.class);
                myapplication.db = db;
                Log.i("", "onClick: startActivityForResult(intent,1);导入题库");
                startActivityForResult(intent, 1);
                if (db.isOpen()) {
                    Toast.makeText(getApplicationContext(), "db.isOpen()", Toast.LENGTH_LONG).show();
                }
                db.close();
                if (db.isOpen()) {
                    Toast.makeText(getApplicationContext(), "closed, db.isOpen()", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "db.isClose()", Toast.LENGTH_LONG).show();
//                 dbHelper = new DBHelper(this);
//                dbHelper.onCreateTable(db, "测试题");
                return true;
            case R.id.creat100Question:   //选择题库      use table
                myapplication.get100 = true;
                dbHelper.get100RadomQuestion(myapplication.occupation, "", myapplication, db);
                myapplication.flag = -1;
                get100next();
                return true;
            case R.id.radom:   //选择题库      use table
                myapplication.get100 = false;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void cleanOpts(View view) {
        CheckBox A = findViewById(R.id.A);
        CheckBox B = findViewById(R.id.B);
        CheckBox C = findViewById(R.id.C);
        CheckBox D = findViewById(R.id.D);
        CheckBox E = findViewById(R.id.E);
        CheckBox F = findViewById(R.id.F);
        CheckBox G = findViewById(R.id.G);
        CheckBox H = findViewById(R.id.H);
        CheckBox correct = findViewById(R.id.correct);
        CheckBox negative = findViewById(R.id.negative);

        A.setChecked(false);
        B.setChecked(false);
        C.setChecked(false);
        D.setChecked(false);
        E.setChecked(false);
        F.setChecked(false);
        G.setChecked(false);
        H.setChecked(false);
        correct.setChecked(false);
        negative.setChecked(false);

    }

    public void get100move(boolean next) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        Myapplication myapplication = (Myapplication) getApplication();
        SQLiteDatabase db = openOrCreateDatabase("questionDb.db", MODE_PRIVATE, null);
        Question question = new Question();
//        if (myapplication.get100) {             //生成100道题测试
        if (next) {
            if (myapplication.flag == 99) {
                Log.i(TAG, "已经是最后一道题了");
                Toast.makeText(getApplicationContext(), "已经是最后一道题了", Toast.LENGTH_LONG).show();
                return;
            } else myapplication.flag++;
        } else if (myapplication.flag == 0) {
            Log.i(TAG, "已经是第一道题了");
            Toast.makeText(getApplicationContext(), "已经是第一道题了", Toast.LENGTH_LONG).show();
            return;
        } else
            myapplication.flag--;
        Log.i(TAG, "get100next: myapplication.flag = " + myapplication.flag);
        Log.i(TAG, "get100next: myapplication.list_ID[myapplication.flag] = " + myapplication.list_ID[myapplication.flag]);

        if (myapplication.list_ID[myapplication.flag] != 0 || myapplication.flag > -1 || myapplication.flag < 100) {

            question = dbHelper.getQuestionById(myapplication.occupation, myapplication.list_ID[myapplication.flag], db);
            //上部导航栏根据question题型选择焦点
            BottomNavigationView bottom = findViewById(R.id.top_view);
            if ("单选题".equals(question.type))
                bottom.setSelectedItemId(R.id.selectOne);
            else if ("多选题".equals(question.type))
                bottom.setSelectedItemId(R.id.selectMul);
            else if ("判断题".equals(question.type))
                bottom.setSelectedItemId(R.id.judge);
            mTextMessage.setText(question.question + "\n" + question.opt);
            Toast.makeText(getApplicationContext(), "当前题目序号：" + (myapplication.flag + 1), Toast.LENGTH_LONG).show();
            Log.i(TAG, "get100next: myapplication.flag = " + myapplication.flag);
            Log.i(TAG, "get100next: question.question = " + question.question);
        } else {                              //随机生成题号检测
            Log.i("nextQuestion", "题号下标超界");
            Toast.makeText(getApplicationContext(), "题号下标超界", Toast.LENGTH_LONG).show();
        }
        View layout_list = View.inflate(this, R.layout.activity_main, null);
        cleanOpts(layout_list);
    }

    public void get100next() {
        get100move(true);
    }

    public void get100previous() {
        get100move(false);
    }

    public void get100next(View view) {
        get100next();
    }
}

