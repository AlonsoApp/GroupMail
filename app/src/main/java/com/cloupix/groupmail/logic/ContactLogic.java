package com.cloupix.groupmail.logic;

import android.content.Context;

import com.cloupix.groupmail.business.Contact;
import com.cloupix.groupmail.dao.Dao;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alonsoapp on 23/07/16.
 *
 */
public class ContactLogic {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

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

    private String[] parseEmails(String emails){
        /*
        emails = emails.replace(" ", "");
        emails = emails.replace("\n", "");
        return emails.split(",");
        */
        emails = emails.replace(" ", "\n");
        emails = emails.replace(",", "\n");
        String[] aEmails = emails.split("\n");
        ArrayList<String> cleanEmails = new ArrayList<>();
        for(String email : aEmails){
            Pattern p = Pattern.compile(EMAIL_PATTERN);
            Matcher m = p.matcher(email.toUpperCase());
            if (m.matches())
                cleanEmails.add(email);
        }
        return cleanEmails.toArray(new String[cleanEmails.size()]);
    }

    public ArrayList<Contact> createContacts(String emails, long groupId, Context context) {
        ArrayList<Contact> contacts = new ArrayList<>();
        String[] aEmails = parseEmails(emails);
        Dao dao = new Dao(context);
        try{
            dao.open();
            for(String email : aEmails){
                Contact contact = new Contact(email);
                long contactId = dao.insertContact(contact, groupId);
                contact.setContactId(contactId);
                contacts.add(contact);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
        return contacts;
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
