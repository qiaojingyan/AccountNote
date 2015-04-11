package com.qjy.accountnote.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;

/**
 * Created by qjy on 15-4-10.
 */
@Table(name = "account")
public class Account {
    @Id
    private int id;
    @Column(column = "date")
    private String date;
    @Foreign(column = "contentId", foreign = "id")
    private Content content;
    @Column(column = "money")
    private double money;
    @Column(column = "remarks")
    private String remarks;
    @Foreign(column = "type", foreign = "type")
    private Type type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", date=" + date +
                ", content=" + content +
                ", money=" + money +
                ", remarks='" + remarks + '\'' +
                ", type=" + type +
                '}';
    }
}
