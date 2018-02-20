package com.cloupix.groupmail.business.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cloupix.groupmail.R;
import com.cloupix.groupmail.business.Contact;
import com.cloupix.groupmail.logic.ContactLogic;

import java.util.ArrayList;

/**
 * Created by alonsoapp on 23/07/16.
 *
 */
public class ContactListAdapter extends BaseAdapter {



    private ArrayList<Contact> contactList;
    private LayoutInflater mInflater;
    private Context context;

    public ContactListAdapter(Context context) {
        this.context = context;
        this.contactList = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contactList.get(position).getContactId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.row_contact, null);
            holder = new ViewHolder();
            holder.textViewEmail = (TextView) convertView.findViewById(R.id.textViewEmail);
            holder.btnClear = (Button) convertView.findViewById(R.id.btnClear);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textViewEmail.setText(contactList.get(position).getEmail());
        holder.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactLogic contactLogic = new ContactLogic();
                contactLogic.deleteContactById(contactList.get(position).getContactId(), context);
                contactList.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void addEmailList(ArrayList<Contact> contactList) {
        this.contactList = contactList;
    }

    public void addEmail(Contact contact) {
        this.contactList.add(contact);
    }

    public void addContacts(ArrayList<Contact> contacts){
        this.contactList.addAll(contacts);
    }

    static class ViewHolder {
        TextView textViewEmail;
        Button btnClear;
    }

}
