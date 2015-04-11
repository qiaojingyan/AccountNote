package com.qjy.accountnote.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.qjy.accountnote.R;
import com.qjy.accountnote.bean.Account;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by qjy on 15-4-10.
 */
public class MyItemAdapter extends BaseAdapter{

    private List<Account> list;
    private Context context;

    public MyItemAdapter(Context context,List<Account> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_main,null);
            viewHolder = new ViewHolder();
            viewHolder.textView_content = (TextView) convertView.findViewById(R.id.textView_item_content);
            viewHolder.textView_date = (TextView) convertView.findViewById(R.id.textView_item_date);
            viewHolder.textView_money = (TextView) convertView.findViewById(R.id.textView_item_money);
            viewHolder.textView_remarks = (TextView) convertView.findViewById(R.id.textView_item_remarks);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Account account = list.get(position);
        viewHolder.textView_remarks.setText(account.getRemarks());
        viewHolder.textView_date.setText(account.getDate());
        viewHolder.textView_money.setText(account.getMoney()+"");
        if(account.getContent()!=null){
            viewHolder.textView_content.setText(account.getContent().toString());
            viewHolder.textView_money.setTextColor(Color.BLACK);
        }else{
            viewHolder.textView_content.setText(account.getType().toString());
            viewHolder.textView_money.setTextColor(Color.RED);
        }

        return convertView;
    }

    class ViewHolder{
        TextView textView_date;
        TextView textView_content;
        TextView textView_money;
        TextView textView_remarks;
    }
}
