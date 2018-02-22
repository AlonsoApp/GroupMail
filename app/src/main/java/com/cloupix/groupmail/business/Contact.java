package com.cloupix.groupmail.business;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alonsoapp on 23/07/16.
 *
 */
public class Contact {

    private static final String key_contactId = "id";
    private static final String key_email = "email";
    private static final String key_groupId = "groupId";


    private long contactId;
    private String email;
    private long groupId;

    public Contact() {
        this.contactId = -1;
    }

    public Contact(long contactId, String email) {
        this.contactId = -1;
        this.contactId = contactId;
        this.email = email;
    }

    public Contact(String email, long groupId) {
        this.contactId = -1;
        this.email = email;
        this.groupId = groupId;
    }

    public Contact(JSONObject jsonObject) {
        try {
            this.contactId = jsonObject.getLong(key_contactId);
            this.email = jsonObject.getString(key_email);
            this.groupId = jsonObject.getLong(key_groupId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean hasVerifiedEmail() {
        // TODO Implementar un verificador de emails true si est'a bien y false si est'a mal
        return true;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getGroupId() {
        return groupId;
    }



    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(key_contactId, contactId);
        jsonObject.put(key_email, email);
        jsonObject.put(key_groupId, groupId);
        return jsonObject;
    }
}
