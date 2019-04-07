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


import java.io.InputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class OccupationList extends Activity {
    TableLayout occupation;
    private Button bt;

    ArrayList<String> table_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_occup);
        occupation = this.findViewById(R.id.occupation);
        Intent intent = getIntent();
        Log.i(TAG, "onCreate: 获取字符串，intent.getStringExtra(\"name\") = " + intent.getStringExtra("name"));
//        Log.i(TAG, "onCreate: 获取字符串，intent.toString() = " + intent.);

        table_names = intent.getStringArrayListExtra("table_names");

//        Log.i(TAG, "onCreate: table_names.size() = " + table_names.size());
        Log.i(TAG, "onCreate: table_names.size() = " + table_names.get(0));
        //下面是生成表单的操作
        for (int i = 0; i < table_names.size(); i++) {
            TableRow row = new TableRow(this);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(table_names.get(i));
            checkBox.setTag(table_names.get(i));
            checkBox.setPadding(3, 3, 3, 3);
            Log.i(TAG, "checkBox.setTag[" + i + "] = " + checkBox.getTag());
            row.addView(checkBox);      //把表单名称加入行里

            ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            occupation.addView(row, layoutParams2);
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
                sheet_numbers = occupation.getChildCount();
                String[] strlist = new String[sheet_numbers];
                ArrayList<String> titleList;
                titleList = selectsOfCheckBox();
//                for (int i = 0; i < titleList.size(); i++) {
//                    Log.i(TAG, "onClick: test()返回值：" + i + " = " + titleList.get(i));
//                }

                data.putStringArrayListExtra("name_list", titleList);
                data.putExtra("count", titleList.size());
                Log.i(TAG, "设置参数: titleList.size() = " + titleList.size());
                ArrayList<String> titleList1 = data.getStringArrayListExtra("name_list");
                Log.i(TAG, "提取参数: titleList.size() = " + titleList1.size());
                if (titleList.size() > 0) {
                    for (int i = 0; i < titleList.size(); i++) {
                        Log.i(TAG, "主程序获得checkbox列表返回值：" + i + " = " + titleList.get(i));
                    }
                }
                //resultCode是返回码,用来确定是哪个页面传来的数据，这里设置返回码是2
                //这个页面传来数据,要用到下面这个方法setResult(int resultCode,Intent data)
                setResult(2, data);
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
        id_logTableRow = this.findViewById(R.id.occupation);            //布局中的 TableLayout，里面存放 checkbox
        TableRow[] childs = new TableRow[id_logTableRow.getChildCount()];   //得到TableLayout中的所有子元素数目
        for (int i = 0; i < childs.length; i++) {                           ////循环TableLayout中的所有子元素
            childs[i] = (TableRow) id_logTableRow.getChildAt(i);
            View[] childss = new View[childs[i].getChildCount()];
            childss[0] = childs[i].getChildAt(0);
            if (childss[0] instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) childss[0];
                Log.i(TAG, "selectsOfCheckBox: 遍历的CheckBox标签：" + checkBox.getTag());
                if (checkBox.isChecked()) {
                    Log.i(TAG, "selectsOfCheckBox: 被选中的CheckBox标签：" + checkBox.getTag());
                    titleList.add((String) childss[0].getTag());
                }
            }
        }
        return titleList;
    }
}
//////////////////////////////////////////////////





