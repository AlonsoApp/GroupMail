package com.cloupix.groupmail.business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alonsoapp on 23/07/16.
 *
 */
public class Group {

    private static final String key_groupId = "id";
    private static final String key_name = "name";
    private static final String key_archived = "archived";
    private static final String key_contactList = "contactList";

    private long groupId;
    private String name;
    private boolean archived;
    private ArrayList<Contact> contactList;

    public Group() {
        this.contactList = new ArrayList<>();
    }

    public Group(long groupId, String name, boolean archived, ArrayList<Contact> emailEntities) {
        this.groupId = groupId;
        this.name = name;
        this.archived = archived;
        this.contactList = emailEntities;
    }

    public Group(String name) {
        this.name = name;
        this.contactList = new ArrayList<>();
    }

    public Group(long groupId, String name) {
        this.groupId = groupId;
        this.name = name;
        this.contactList = new ArrayList<>();
    }

    public Group(JSONObject jsonObject) {
        try {
            this.groupId = jsonObject.getLong(key_groupId);
            this.name = jsonObject.getString(key_name);
            this.archived = jsonObject.getBoolean(key_archived);
            this.contactList = new ArrayList<>();
            //TODO Refactor this
            for (int i = 0; i < jsonObject.getJSONArray(key_contactList).length(); i++){
                contactList.add(new Contact(jsonObject.getJSONArray(key_contactList).getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public ArrayList<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(ArrayList<Contact> contactList) {
        this.contactList = contactList;
    }

    // Helpers

    public String getEmailPreviewString(){
        String prev = "";
        if (contactList.size()>0) {
            prev += contactList.get(0).getEmail();
            if(contactList.size()>1) {
                prev += ",\n" + contactList.get(1).getEmail();
                if(contactList.size()>2){
                    prev += "...";
                }
            }
        }
        return prev;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(key_groupId, groupId);
        jsonObject.put(key_name, name);
        jsonObject.put(key_archived, archived);
        JSONArray jsonArray = new JSONArray();
        for (Contact contact : this.contactList)
            jsonArray.put(contact.toJson());
        jsonObject.put(key_contactList, jsonArray);
        return jsonObject;
    }


}
