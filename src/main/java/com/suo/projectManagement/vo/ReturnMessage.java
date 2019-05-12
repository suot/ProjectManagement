package com.suo.projectManagement.vo;

/**
 * Created by TianSuo on 2018-07-30.
 */
public class ReturnMessage {
    private String status;
    private String rnid;
    private String groupName;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRnid() {
        return rnid;
    }

    public void setRnid(String rnid) {
        this.rnid = rnid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ReturnMessage(String status, String rnid, String groupName) {
        this.status = status;
        this.rnid = rnid;
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "ReturnMessage{" +
                "status='" + status + '\'' +
                ", rnid='" + rnid + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
