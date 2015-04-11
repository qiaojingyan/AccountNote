package com.qjy.accountnote.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by qjy on 15-4-10.
 */
@Table(name = "type")
public class Type {
    @Column(column = "type")
    @Id
    private String type;

    @Column(column = "payment")
    private String payment;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return type;
    }
}
