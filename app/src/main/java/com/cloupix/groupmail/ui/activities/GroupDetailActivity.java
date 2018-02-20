package com.cloupix.groupmail.ui.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cloupix.groupmail.R;
import com.cloupix.groupmail.business.Contact;
import com.cloupix.groupmail.business.Group;
import com.cloupix.groupmail.business.adapters.ContactListAdapter;
import com.cloupix.groupmail.logic.ContactLogic;
import com.cloupix.groupmail.logic.GroupLogic;
import com.cloupix.groupmail.ui.dialogs.EditTextDialog;

/**
 * Created by alonsoapp on 23/07/16.
 *
 */
public class GroupDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {


    public static final String EXTRA_GROUP_ID = "group_id";

    private Group group;

    private ListView emailListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        emailListView = (ListView) findViewById(R.id.emailList);
        emailListView.setAdapter(new ContactListAdapter(this));
        emailListView.setOnItemClickListener(this);

        long groupId = getIntent().getLongExtra(EXTRA_GROUP_ID, -1);
        loadGroup(groupId);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_compose_email:
                GroupLogic groupLogic = new GroupLogic();
                groupLogic.composeEmail(group.getContactList(), this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                createEmail();
                break;
        }
    }

    private void loadGroup(final long groupId){
        new AsyncTask<Void, Void, Group>(){

            @Override
            protected Group doInBackground(Void... voids) {
                GroupLogic groupLogic = new GroupLogic();
                return groupLogic.getGroupById(groupId, getApplicationContext());
            }

            @Override
            protected void onPostExecute(Group obtainedGroup) {
                super.onPostExecute(obtainedGroup);
                group = obtainedGroup;
                setTitle(group.getName());
                if(emailListView.getAdapter() instanceof ContactListAdapter) {
                    ((ContactListAdapter) emailListView.getAdapter()).addEmailList(obtainedGroup.getContactList());
                    ((ContactListAdapter) emailListView.getAdapter()).notifyDataSetChanged();
                }
            }
        }.execute();
    }

    private void createEmail(){
        final EditTextDialog editTextDialog = new EditTextDialog();
        editTextDialog.setTitle(getString(R.string.add_email));
        editTextDialog.setHint(getString(R.string.email_hint));
        editTextDialog.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        editTextDialog.setEditTextDialogCallbacks(new EditTextDialog.EditTextDialogCallbacks() {
            @Override
            public void onDialogSet(final String email) {
                if(TextUtils.isEmpty(email))
                    return;
                new AsyncTask<Void, Void, Contact>() {

                    @Override
                    protected Contact doInBackground(Void... voids) {
                        ContactLogic contactLogic = new ContactLogic();
                        long emailEntityId = contactLogic.createContact(email, group.getGroupId(), getApplicationContext());
                        return new Contact(emailEntityId, email);
                    }

                    @Override
                    protected void onPostExecute(Contact contact) {
                        super.onPostExecute(contact);
                        if (contact.getContactId() == -1)
                            Toast.makeText(getApplicationContext(), R.string.add_email_error, Toast.LENGTH_SHORT).show();
                        else if(emailListView.getAdapter() instanceof ContactListAdapter){
                            ((ContactListAdapter) emailListView.getAdapter()).addEmail(contact);
                            ((ContactListAdapter) emailListView.getAdapter()).notifyDataSetChanged();
                        }
                    }
                }.execute();
                editTextDialog.dismiss();
            }

            @Override
            public void onDialogCancel() {

            }
        });
        editTextDialog.show(getSupportFragmentManager(), "NewGroupDialogListener");
    }
}
