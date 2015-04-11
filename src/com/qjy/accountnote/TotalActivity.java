package com.qjy.accountnote;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        ViewUtils.inject(this);

        db = DbUtils.create(this);
        List<DbModel> list = new ArrayList<DbModel>();
        try {
            list = db.findDbModelAll(Selector.from(
                            Account.class).select("sum(money)","type")
                            .where("contentId","!=","null")
                    .groupBy("type")
            );

            List<Map<String,Object>> sumMoneyList = new ArrayList<Map<String, Object>>();

            if (list != null) {
                for (DbModel model : list) {
                    HashMap<String, String> dataMap = model.getDataMap();
                    Set<String> keySet = dataMap.keySet();
                    double sumMoney = model.getDouble("sum(money)");
                    String type = model.getString("type");
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("type",type);
                    map.put("key",sumMoney);
                    sumMoneyList.add(map);
                    for (String key : keySet) {
                        Log.d("DbUtils","SUM: "+key+" "+dataMap.get(key));
                    }
                }
            }



//            for (int i = 0; i < list.size(); i++) {
//                list.get(i).getDouble("sum");
//                arr[i] = list.get(i).getMoney();
//            }
//            for (int i = 0; i < arr.length; i++) {
//                Log.e("HAHA",arr[i]+"");
//            }

//            arcChartView.loadData(arr,"haha");


//            ArcChartView view = new ArcChartView(this);
//            view.loadData(arr,"money");
//            linearLayout.addView(view);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}