package com.capa.capa.capa;



public class History {
    private String startTime;
    private String endTime;
    private String fee;

    public History(String startTime,String endTime,String fee)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.fee = fee;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public History(){

    }
}
