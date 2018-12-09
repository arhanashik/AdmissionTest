package com.pstu.admissiontest.utils;

import android.content.Context;

import com.pstu.admissiontest.database.DBHandler;
import com.pstu.admissiontest.model.Item;
import com.pstu.admissiontest.variables.FinalVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blackSpider on 11/8/2016.
 */
public class Search {
    private String searchKey;
    private String selectedUnit;
    private String tableName;
    private ArrayList startRoll;
    private ArrayList endRoll;
    private ArrayList center;
    private ArrayList total;
    private ArrayList hallNumber;
    private ArrayList roomNumber;
    private ArrayList room_name;
    private DBHandler dbHandler;
    private Context context;
    private List<Item> allItem;

    public Search() {
    }

    public Search(String searchKey,  String selectedUnit, Context context) {
        this.searchKey = searchKey;
        this.selectedUnit = selectedUnit;
        this.context = context;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(String selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<String> searchInDatabase(){
        ArrayList<String> result = new ArrayList<>();
        dbHandler = new DBHandler(context);
        checkTableName();
        prepareData();
        int strtR = 0, endR = 0, key = 0;

        for (int i = 0; i<startRoll.size(); i++){
            strtR = Integer.parseInt(startRoll.get(i).toString());
            endR = Integer.parseInt(endRoll.get(i).toString());
            key = Integer.parseInt(searchKey);

            if(key >= strtR && key <=endR){
                result.add(searchKey);
                result.add(hallNumber.get(i).toString());
                result.add(roomNumber.get(i).toString());
                result.add(center.get(i).toString());
                result.add(room_name.get(i).toString());
                result.add(total.get(i).toString());
                result.add(checkCenterName(center.get(i).toString().trim().toLowerCase()));

                break;
            }

        }

        dbHandler.close();
        return result;
    }

    public void prepareData(){
        allItem = new ArrayList<>();
        allItem = dbHandler.getAllItem(tableName);

        hallNumber = new ArrayList<>();
        roomNumber = new ArrayList<>();
        center = new ArrayList<>();
        room_name = new ArrayList<>();
        startRoll = new ArrayList<>();
        endRoll = new ArrayList<>();
        total = new ArrayList<>();

        for(Item item: allItem){
            hallNumber.add(item.getHALL_NUMBER());
            roomNumber.add(item.getROOM_NUMBER());
            center.add(item.getCENTER());
            room_name.add(item.getROOM_NAME());
            startRoll.add(item.getSTART_ROLL());
            endRoll.add(item.getEND_ROLL());
            total.add(item.getTOTAL());
        }
    }

    public void checkTableName(){
        if(selectedUnit.equals(FinalVariables.A_UNIT)){
            tableName = dbHandler.TABLE_A_UNIT;

        }else if(selectedUnit.equals(FinalVariables.B_UNIT)){
            tableName = dbHandler.TABLE_B_UNIT;

        }else if(selectedUnit.equals(FinalVariables.C_UNIT)){
            tableName = dbHandler.TABLE_C_UNIT;
        }
    }

    public String checkCenterName(String given){
        String res = FinalVariables.MAP_CENTER1;

        if(given.contains(FinalVariables.CENTER1.trim().toLowerCase())) res = FinalVariables.MAP_CENTER1;
        else if(given.contains(FinalVariables.CENTER2.trim().toLowerCase())) res = FinalVariables.MAP_CENTER2;
        else if(given.contains(FinalVariables.CENTER3.trim().toLowerCase())) res = FinalVariables.MAP_CENTER3;
        else if(given.contains(FinalVariables.CENTER4.trim().toLowerCase())) res = FinalVariables.MAP_CENTER4;
        else if(given.contains(FinalVariables.CENTER5.trim().toLowerCase())) res = FinalVariables.MAP_CENTER5;
        else if(given.contains(FinalVariables.CENTER6.trim().toLowerCase())) res = FinalVariables.MAP_CENTER6;
        else if(given.contains(FinalVariables.CENTER7.trim().toLowerCase())) res = FinalVariables.MAP_CENTER7;
        else if(given.contains(FinalVariables.CENTER8.trim().toLowerCase())) res = FinalVariables.MAP_CENTER8;
        else if(given.contains(FinalVariables.CENTER9.trim().toLowerCase())) res = FinalVariables.MAP_CENTER9;
        else if(given.contains(FinalVariables.CENTER10.trim().toLowerCase())) res = FinalVariables.MAP_CENTER10;

        return res;
    }
}
