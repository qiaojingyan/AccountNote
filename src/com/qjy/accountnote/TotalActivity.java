package com.qjy.accountnote;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qjy.accountnote.bean.Account;
import com.qjy.accountnote.view.ArcChartView;

import java.util.*;

/**
 * Created by qjy on 15-4-10.
 */
public class TotalActivity extends Activity {

    private DbUtils db;
    @ViewInject(R.id.linearLayout_total)
    private LinearLayout linearLayout;
    @ViewInject(R.id.arcChartView_total)
    private ArcChartView arcChartView;
    @ViewInject(R.id.button_total_back)
    private Button btn_back;
    @ViewInject(R.id.radioGroup_total)
    private RadioGroup radioGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        ViewUtils.inject(this);

        db = DbUtils.create(this);

        //算出支出比例
        initPay();

        //返回键
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //收入支出切换
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_total_get:
                        initGet();
                        break;
                    case R.id.radioButton_total_pay:
                        initPay();
                        break;
                }
            }
        });
    }

    /**
     * 算支出比例
     */
    private void initPay(){
        List<DbModel> list = new ArrayList<DbModel>();
        try {
            //按type分类算sumMoney
            list = db.findDbModelAll(Selector.from(
                            Account.class).select("sum(money)","type")
                            .where("contentId","!=","null")
                            .groupBy("type")
            );

            //用来放钱数传给ArtChartView去绘画
            double[] arr = new double[5];

            if (list != null) {
                for (DbModel model : list) {
                    HashMap<String, String> dataMap = model.getDataMap();
                    Set<String> keySet = dataMap.keySet();
                    double sumMoney = model.getDouble("sum(money)");
                    String type = model.getString("type");
                    if("吃".equals(type)){
                        arr[0] = sumMoney;
                    }else if("穿".equals(type)){
                        arr[1] = sumMoney;
                    }else if("住".equals(type)){
                        arr[2] = sumMoney;
                    }else if("行".equals(type)){
                        arr[3] = sumMoney;
                    }else if("用".equals(type)){
                        arr[4] = sumMoney;
                    }
                }

                arcChartView.loadData(arr,"");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 画出收入比例饼状图
     */
    private void initGet(){
        List<DbModel> list = new ArrayList<DbModel>();
        try {
            //按type分类算sumMoney
            list = db.findDbModelAll(Selector.from(
                            Account.class).select("sum(money)","type")
                            .where("contentId","==","null")
                            .groupBy("type")
            );

            //用来放钱数传给ArtChartView去绘画
            double[] arr = new double[5];

            if (list != null) {
                for (DbModel model : list) {
                    HashMap<String, String> dataMap = model.getDataMap();
                    Set<String> keySet = dataMap.keySet();
                    double sumMoney = model.getDouble("sum(money)");
                    String type = model.getString("type");
                    if("工资".equals(type)){
                        arr[0] = sumMoney;
                    }else if("外快".equals(type)){
                        arr[1] = sumMoney;
                    }else if("其他".equals(type)){
                        arr[2] = sumMoney;
                    }
                }

                arcChartView.loadData(arr,"");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}