package com.example.textexp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class list extends Activity {

    //     TableLayout _pnlContent = (TableLayout) this.findViewById(R.id.pnlContent);
//    _pnlContent = (TableLayout) this.findViewById(R.id.pnlContent);

//    String content = "你好";//想返回的内容
    //    final TableLayout _pnlContent;
//    _pnlContent = (TableLayout) this.findViewById(R.id.pnlContent);
//    Button _btnSave= (Button) this.findViewById(R.id.creatdb);;
//    Question question = new Question();
    InputStream inputStream = null;
    private Button bt;
    TableLayout _pnlContent;
    //            Log.i("", "drawTable: 第一次点击");
//    _btnSave = (Button) this.findViewById(R.id.creatdb);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        _pnlContent = this.findViewById(R.id.pnlContent);
        int sheet_number = 0;
//        String[] strlist = new String[sheet_number];
        //获取excel文档中的所有工作表名字，根据名字生成checkbox列表
        try {
            inputStream = getAssets().open("点检题库.xls");
            //读取工作簿
            HSSFWorkbook workBook = new HSSFWorkbook(inputStream);
            sheet_number = workBook.getNumberOfSheets();         //工作表总数
            Log.i(TAG, "onCreate: getNumberOfSheets = " + sheet_number);

            //下面是生成表单的操作
            for (int i = 0; i < sheet_number; i++) {
//            Toast.makeText(getApplicationContext(), "正在读取题库 " + i, Toast.LENGTH_LONG).show();

                //读取工作表
                HSSFSheet sheet = workBook.getSheetAt(i);
                String sheet_name = workBook.getSheetName(i);
//                String sheet_name = sheet.getSheetName();               //工作表名字
//                strlist[i] = sheet_name;
//                sheet.toString();
                TableRow row = new TableRow(this);
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(sheet_name);
                checkBox.setTag(sheet_name);
                checkBox.setPadding(3, 3, 3, 3);
                Log.i(TAG, "checkBox.setTag[" + i + "] = " + checkBox.getTag());
                row.addView(checkBox);      //把表单名称加入行里

                ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                _pnlContent.addView(row, layoutParams2);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        第二个页面什么时候给第一个页面回传数据
        回传到第一个页面的实际上是一个Intent对象
         */
        bt = (Button) findViewById(R.id.input);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
//                Intent intent = getIntent();
                //name相当于一个key,content是返回的内容

                int sheet_numbers = 0;
                sheet_numbers = _pnlContent.getChildCount();
                String[] strlist = new String[sheet_numbers];
                ArrayList<String> titleList;
                titleList = selectsOfCheckBox();
//                for (int i = 0; i < titleList.size(); i++) {
//                    Log.i(TAG, "onClick: test()返回值：" + i + " = " + titleList.get(i));
//                }

                data.putStringArrayListExtra("name_list", titleList);
                data.putExtra("count", titleList.size());
//                Log.i(TAG, "设置参数: titleList.size() = " + titleList.size());
                ArrayList<String> titleList1 = data.getStringArrayListExtra("name_list");
//                Log.i(TAG, "提取参数: titleList.size() = " + titleList1.size());
//                if (titleList.size() > 0) {
//                    for (int i = 0; i < titleList.size(); i++) {
//                        Log.i(TAG, "主程序获得checkbox列表返回值：" + i + " = " + titleList.get(i));
//                    }
//                }
                //resultCode是返回码,用来确定是哪个页面传来的数据，这里设置返回码是2
                //这个页面传来数据,要用到下面这个方法setResult(int resultCode,Intent data)
                setResult(1, data);
                //结束当前页面
                Log.i(TAG, "onClick: list.finish");
                finish();
            }
        });


    }

    //////////////////////////////////////////////////////
    public ArrayList<String> selectsOfCheckBox() {  //返回被选中的checkbox的Tag值
//       TableLayout ----> TableRow[]---->CheckBox
        TableLayout id_logTableRow;
        ArrayList<String> titleList = new ArrayList<String>();          //存放被选中的checkbox的值
        id_logTableRow = this.findViewById(R.id.pnlContent);            //布局中的 TableLayout，里面存放 checkbox
        TableRow[] childs = new TableRow[id_logTableRow.getChildCount()];   //得到TableLayout中的所有子元素数目
        for (int i = 0; i < childs.length; i++) {                           ////循环TableLayout中的所有子元素
            childs[i] = (TableRow) id_logTableRow.getChildAt(i);
            View[] childss = new View[childs[i].getChildCount()];
            childss[0] = childs[i].getChildAt(0);
            if (childss[0] instanceof CheckBox) {
                CheckBox checkBox = (CheckBox)childss[0];
                Log.i(TAG, "selectsOfCheckBox: 遍历的CheckBox标签：" + checkBox.getTag());
                if (checkBox.isChecked()){
                    Log.i(TAG, "selectsOfCheckBox: 被选中的CheckBox标签：" + checkBox.getTag());
                    titleList.add((String) childss[0].getTag());
                }
            }
        }
        return titleList;
    }
}
//////////////////////////////////////////////////





