package com.qjy.accountnote;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qjy.accountnote.bean.Account;
import com.qjy.accountnote.bean.Content;
import com.qjy.accountnote.bean.Type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by qjy on 15-4-10.
 */
public class AddDetailActivity extends Activity {

    @ViewInject(R.id.radioGroup_adddetail_payment)
    private RadioGroup radioGroup_payment;
    @ViewInject(R.id.spinner_adddetail_type)
    private Spinner spinner_type;
    @ViewInject(R.id.spinner_adddetail_content)
    private Spinner spinner_content;
    @ViewInject(R.id.editText_adddetail_money)
    private EditText editText_money;
    @ViewInject(R.id.editText_adddetail_remarks)
    private EditText editText_remark;
    @ViewInject(R.id.linearLayout_adddetail_date)
    private LinearLayout linearLayout_date;
    @ViewInject(R.id.linearLayout_adddetail_content)
    private LinearLayout linearLayout_content;
    @ViewInject(R.id.textView_adddetail_date)
    private TextView textView_date;
    @ViewInject(R.id.button_adddetail_back)
    private Button button_back;
    @ViewInject(R.id.button_adddetail_ok)
    private Button button_ok;
    private DbUtils db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddetail);
        ViewUtils.inject(this);
        db = DbUtils.create(this);

        initSpinner("支出");

        radioGroup_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_adddetail_get:
                        linearLayout_content.setVisibility(LinearLayout.GONE);
                        initSpinner("收入");
                        break;
                    case R.id.radioButton_adddetail_pay:
                        linearLayout_content.setVisibility(LinearLayout.VISIBLE);
                        initSpinner("支出");
                        break;
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateStr = year+"-"+(month+1)+"-"+day;
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateStr1 = new SimpleDateFormat("yyyy-MM-dd").format(date);
        textView_date.setText("选择日期："+dateStr1);

        linearLayout_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(AddDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String dateStr = year+"-"+(month+1)+"-"+day;
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String dateStr1 = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        textView_date.setText("选择日期："+dateStr1);
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String typeStr = spinner_type.getSelectedItem().toString();
                String date = getDate();
                String remarks = editText_remark.getText().toString();
                double money = 0;
                try{
                    money = Double.parseDouble(editText_money.getText().toString());
                }catch (Exception e){
                    Toast.makeText(AddDetailActivity.this,"金额格式不正确",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Account account = new Account();
                    account.setDate(date);
                    account.setMoney(money);
                    account.setRemarks(remarks);
                    Type type = db.findFirst(Selector.from(Type.class).where("type","=",typeStr));
                    account.setType(type);
                    if(spinner_content.getSelectedItem() != null){
                        String contentStr = spinner_content.getSelectedItem().toString();
                        Content content = db.findFirst(Selector.from(Content.class).where("content","=",contentStr));
                        account.setContent(content);
                    }
                    db.save(account);

                } catch (DbException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

    }

    public String getDate(){
        String date1 = textView_date.getText().toString();
        String[] dates = date1.split("：");
        String dateStr = dates[1];
        return dateStr;
    }

    public void initSpinner(String payment){

        try {
            final List<Type> list = db.findAll(Selector.from(Type.class).where("payment","=",payment));
            ArrayAdapter<Type> adapter = new ArrayAdapter<Type>(this,android.R.layout.simple_spinner_item, android.R.id.text1,list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_type.setAdapter(adapter);
            spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String typeStr = list.get(i).getType();
                    try {
                        List<Content> contents = db.findAll(Selector.from(Content.class).where("type","=",typeStr));
                        ArrayAdapter<Content> contentAdapter = new ArrayAdapter<Content>(AddDetailActivity.this,android.R.layout.simple_spinner_item,android.R.id.text1,contents);
                        contentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_content.setAdapter(contentAdapter);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}