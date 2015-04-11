package com.qjy.accountnote;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qjy.accountnote.adapter.MyItemAdapter;
import com.qjy.accountnote.bean.Account;
import com.qjy.accountnote.bean.Content;
import com.qjy.accountnote.bean.Type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @ViewInject(R.id.button_main_add)
    private Button button_add;
    @ViewInject(R.id.button_main_menu)
    private Button button_menu;
    @ViewInject(R.id.listView_main_info)
    private ListView listView_main;
    @ViewInject(R.id.textView_main_year)
    private TextView textView_year;
    @ViewInject(R.id.textView_main_month)
    private TextView textView_month;
    private SharedPreferences pfs;
    private DbUtils db;
    private PopupWindow popupWindow;
    private View convertView;
    private List<Account> totalList;
    private String year;
    private String month;
    private MyItemAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ViewUtils.inject(this);

        pfs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean first = pfs.getBoolean("first", true);
        db = DbUtils.create(this);
        if (first) {
            initDataBase();
            initSharedPreferences();

        }
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddDetailActivity.class);
                startActivity(intent);

            }
        });

        convertView = getLayoutInflater().inflate(R.layout.popupwindow_main, null);
        Button btn_week = (Button) convertView.findViewById(R.id.btn_week);
        Button brn_month = (Button) convertView.findViewById(R.id.btn_month);
        Button btn_year = (Button) convertView.findViewById(R.id.btn_year);
        Button btn_total = (Button) convertView.findViewById(R.id.btn_total);
        Button btn_cancel = (Button) convertView.findViewById(R.id.btn_cancel);
        popupWindow = new PopupWindow(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        button_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else{
                    popupWindow.showAtLocation(convertView, Gravity.BOTTOM, 0, 0);
                }
            }
        });
        btn_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TotalActivity.class);
                startActivity(intent);
            }
        });

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR)+"";
        month = (calendar.get(Calendar.MONTH)+1)+"月";
        textView_month.setText(month);
        textView_year.setText(year);

        totalList = new ArrayList<Account>();
        adapter = new MyItemAdapter(this,totalList);
        listView_main.setAdapter(adapter);
        initListView();


    }

    private void initListView(){
        String dateStr = year+"-"+"%"+month.substring(0,month.length()-1)+"%";
        Log.e("DATE",dateStr);
        try {
            totalList.clear();
            List<Account> list = db.findAll(Selector.from(Account.class).where("date","like",dateStr).orderBy("date", true));
            if (list!=null){
                totalList.addAll(list);
                adapter.notifyDataSetChanged();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(popupWindow.isShowing()){
            popupWindow.dismiss();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void initDataBase() {
        db.configAllowTransaction(true);
        db.configDebug(true);
        saveType("工资", "收入");
        saveType("外快", "收入");
        saveType("其他", "收入");
        saveType("吃", "支出");
        saveType("穿", "支出");
        saveType("住", "支出");
        saveType("行", "支出");
        saveType("用", "支出");
        saveContent("日常花销", "吃");
        saveContent("请客", "吃");
        saveContent("烟酒", "吃");
        saveContent("自用", "穿");
        saveContent("礼物", "穿");
        saveContent("房租", "住");
        saveContent("水电费", "住");
        saveContent("公共交通", "行");
        saveContent("出租", "行");
        saveContent("远途", "行");
        saveContent("学习用品", "用");
        saveContent("生活用品", "用");
    }

    public void saveType(String typeStr, String payment) {
        Type type = new Type();
        type.setType(typeStr);
        type.setPayment(payment);
        try {
            db.save(type);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void saveContent(String contentStr, String typeStr) {
        Content content = new Content();
        content.setContent(contentStr);
        try {
            content.setType((Type) db.findFirst(Selector.from(Type.class).where("type", "=", typeStr)));
        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
            db.save(content);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void initSharedPreferences() {
        SharedPreferences.Editor editor = pfs.edit();
        editor.putBoolean("first", false);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListView();
    }
}
