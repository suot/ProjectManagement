package com.suo.projectManagement.vo;

/**
 * Created by wuxu on 2018/5/31.
 */
public class ReturnEntity {
    private String status,message;
    Object o;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }
}
