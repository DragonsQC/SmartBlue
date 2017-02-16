package com.dqc.smartblue.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BlueData extends RealmObject {

    @PrimaryKey
    private String address;
    private boolean connected     = false;
    private String  connectedMsg  = "已连接";
    private boolean disconnect    = false;
    private String  disconnectMsg = "断开连接";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisconnectMsg() {
        return disconnectMsg;
    }

    public void setDisconnectMsg(String disconnectMsg) {
        this.disconnectMsg = disconnectMsg;
    }

    public String getConnectedMsg() {
        return connectedMsg;
    }

    public void setConnectedMsg(String connectedMsg) {
        this.connectedMsg = connectedMsg;
    }

    public boolean getDisconnect() {
        return disconnect;
    }

    public void setDisconnect(boolean disconnect) {
        this.disconnect = disconnect;
    }

    public boolean getConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
