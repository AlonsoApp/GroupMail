package com.cloupix.groupmail.logic;

import android.content.Context;

import com.cloupix.groupmail.business.Group;
import com.cloupix.groupmail.dao.SQLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alonsoapp on 22/02/18.
 *
 */

public class Logic {

    private static final String JSON_VERSION = "db_version";
    private static final String JSON_TIMESTAMP = "timestamp";
    private static final String JSON_DATA = "data";

    private JSONObject exportDbToJson(Context context) throws JSONException {
        JSONObject jsonBackup = new JSONObject();
        GroupLogic gLogic = new GroupLogic();

        ArrayList<Group> dbGroups =  gLogic.getGroups(context);
        JSONArray jsonGroups = new JSONArray();
        for(Group group : dbGroups){
            jsonGroups.put(group.toJson());
        }

        jsonBackup.put(JSON_VERSION, SQLHelper.DATABASE_VERSION);
        jsonBackup.put(JSON_TIMESTAMP, System.currentTimeMillis());
        jsonBackup.put(JSON_DATA, jsonGroups);

        return jsonBackup;
    }

    private void importJsonToDb(Context context, JSONObject jsonBackup) throws JSONException {
        switch (jsonBackup.getInt(JSON_VERSION)){
            case 1:
                importJsonToDbV1(context, jsonBackup);
                break;
        }
    }

    private void importJsonToDbV1(Context context, JSONObject jsonBackup) throws JSONException {
        JSONArray jsonGroups = jsonBackup.getJSONArray(JSON_DATA);
        // Parse JSON to tree of objects
        ArrayList<Group> groups = new ArrayList<>();
        for(int i = 0; i<jsonGroups.length(); i++)
            groups.add(new Group(jsonGroups.getJSONObject(i)));
        // Save list of groups on bd
        GroupLogic gLogic = new GroupLogic();
        gLogic.saveGroups(groups, context);
    }

    public boolean saveBackup(Context context){
        try {
            JSONObject jsonObject = exportDbToJson(context);
            String str = jsonObject.toString();


        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
}
