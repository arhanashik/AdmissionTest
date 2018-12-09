package com.pstu.admissiontest.model;

/**
 * Created by blackSpider on 11/8/2016.
 */
public class Item {
    private Integer _SL;
    private String HALL_NUMBER;
    private String ROOM_NUMBER;
    private String CENTER;
    private String ROOM_NAME;
    private String START_ROLL;
    private String END_ROLL;
    private String TOTAL;
    private String ROLL;
    private String UNIT;

    public Item() {

    }

    public Item(String HALL_NUMBER, String ROOM_NUMBER, String CENTER, String ROOM_NAME, String START_ROLL, String END_ROLL, String TOTAL) {
        this.HALL_NUMBER = HALL_NUMBER;
        this.ROOM_NUMBER = ROOM_NUMBER;
        this.CENTER = CENTER;
        this.ROOM_NAME = ROOM_NAME;
        this.START_ROLL = START_ROLL;
        this.END_ROLL = END_ROLL;
        this.TOTAL = TOTAL;
    }

    public Item(String HALL_NUMBER, String ROOM_NUMBER, String CENTER, String ROOM_NAME, String ROLL, String UNIT) {
        this.HALL_NUMBER = HALL_NUMBER;
        this.ROOM_NUMBER = ROOM_NUMBER;
        this.CENTER = CENTER;
        this.ROOM_NAME = ROOM_NAME;
        this.ROLL = ROLL;
        this.UNIT = UNIT;
    }

    public Integer get_SL() {
        return _SL;
    }

    public void set_SL(Integer _SL) {
        this._SL = _SL;
    }

    public String getHALL_NUMBER() {
        return HALL_NUMBER;
    }

    public void setHALL_NUMBER(String HALL_NUMBER) {
        this.HALL_NUMBER = HALL_NUMBER;
    }

    public String getROOM_NUMBER() {
        return ROOM_NUMBER;
    }

    public void setROOM_NUMBER(String ROOM_NUMBER) {
        this.ROOM_NUMBER = ROOM_NUMBER;
    }

    public String getCENTER() {
        return CENTER;
    }

    public void setCENTER(String CENTER) {
        this.CENTER = CENTER;
    }

    public String getROOM_NAME() {
        return ROOM_NAME;
    }

    public void setROOM_NAME(String ROOM_NAME) {
        this.ROOM_NAME = ROOM_NAME;
    }

    public String getSTART_ROLL() {
        return START_ROLL;
    }

    public void setSTART_ROLL(String START_ROLL) {
        this.START_ROLL = START_ROLL;
    }

    public String getEND_ROLL() {
        return END_ROLL;
    }

    public void setEND_ROLL(String END_ROLL) {
        this.END_ROLL = END_ROLL;
    }

    public String getROLL() {
        return ROLL;
    }

    public void setROLL(String ROLL) {
        this.ROLL = ROLL;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(String TOTAL) {
        this.TOTAL = TOTAL;
    }
}
