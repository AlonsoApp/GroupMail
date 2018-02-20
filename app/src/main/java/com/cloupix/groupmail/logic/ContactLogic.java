package com.cloupix.groupmail.logic;

import android.content.Context;

import com.cloupix.groupmail.business.Contact;
import com.cloupix.groupmail.dao.Dao;

import java.util.ArrayList;

/**
 * Created by alonsoapp on 23/07/16.
 *
 */
public class ContactLogic {


    public ArrayList<Contact> getContactsByGroupId(long groupId, Context context) {
        ArrayList<Contact> emailEntities = null;
        Dao dao = new Dao(context);
        try{
            dao.open();
            emailEntities = dao.getContactsByGroupId(groupId);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
        return emailEntities;
    }

    public long createContact(String email, long groupId, Context context) {
        long emailEntityId = -1;
        Dao dao = new Dao(context);
        try{
            dao.open();
            emailEntityId = dao.insertContact(new Contact(email), groupId);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
        return emailEntityId;
    }

    public void deleteContactById(long emailEntityId, Context context) {
        Dao dao = new Dao(context);
        try{
            dao.open();
            dao.deleteContactById(emailEntityId);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
    }
}
