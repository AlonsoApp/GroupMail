package com.cloupix.groupmail.logic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.cloupix.groupmail.R;
import com.cloupix.groupmail.business.Contact;
import com.cloupix.groupmail.business.Group;
import com.cloupix.groupmail.dao.Dao;

import java.util.ArrayList;

/**
 * Created by alonsoapp on 23/07/16.
 *
 */
public class GroupLogic {


    public ArrayList<Group> getGroups(Context context) {
        ArrayList<Group> groups = null;
        Dao dao = new Dao(context);
        try{
            dao.open();
            groups = dao.getGroups();
            for (Group group : groups) {
                group.setContactList(dao.getContactsByGroupId(group.getGroupId()));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
        return groups;
    }

    public ArrayList<Group> getGroups(Context context, boolean archived) {
        ArrayList<Group> groups = null;
        Dao dao = new Dao(context);
        try{
            dao.open();
            groups = dao.getGroups(archived);
            for (Group group : groups) {
                group.setContactList(dao.getContactsByGroupId(group.getGroupId()));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
        return groups;
    }

    public long createGroup(String name, Context context) {
        long groupId = -1;
        Dao dao = new Dao(context);
        try{
            dao.open();
            groupId = dao.insertGroup(new Group(name));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
        return groupId;
    }

    public Group getGroupById(long groupId, Context context) {
        Group group = null;
        Dao dao = new Dao(context);
        try{
            dao.open();
            group = dao.getGroupById(groupId);
            group.setContactList(dao.getContactsByGroupId(group.getGroupId()));

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
        return group;
    }

    public void composeEmail(ArrayList<Contact> contacts, Context context) {
        if(contacts.size()<=0) {
            Toast.makeText(context, R.string.no_email_compose_error, Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> addressList = new ArrayList<>();
        for(Contact contact : contacts)
            if(contact.hasVerifiedEmail())
                addressList.add(contact.getEmail());
        String[] addresses = addressList.toArray(new String[0]);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        //intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public void removeGroupById(long groupId, Context context) {
        Dao dao = new Dao(context);
        try{
            dao.open();
            dao.deleteGroupById(groupId);
            // TODO Comprobar que el on delete cascade funciona
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
    }

    public void archiveGroupById(long groupId, boolean archived, Context context) {
        Dao dao = new Dao(context);
        try{
            dao.open();
            dao.updateArchiveGroupById(groupId, archived);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dao.isConnectionOpen())
                dao.close();
        }
    }
}
