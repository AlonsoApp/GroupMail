package com.cloupix.groupmail.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import com.cloupix.groupmail.R;
import com.cloupix.groupmail.business.Group;
import com.cloupix.groupmail.business.adapters.GroupRecyclerViewAdapter;
import com.cloupix.groupmail.logic.GroupLogic;
import com.cloupix.groupmail.logic.Logic;
import com.cloupix.groupmail.ui.dialogs.EditTextDialog;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private ArrayList<Group> groups;
    private boolean archivingState;

    private RecyclerView groupRecyclerView;
    private RecyclerView.Adapter groupAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //private ListView groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        groupRecyclerView = (RecyclerView) findViewById(R.id.groupRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        groupRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        groupRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        groupAdapter = new GroupRecyclerViewAdapter(new ArrayList<Group>(), this);
        groupRecyclerView.setAdapter(groupAdapter);

        // Slide to remove
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                //Toast.makeText(getApplicationContext(), "Hey!", Toast.LENGTH_SHORT).show();
                if(groups!=null){
                    GroupLogic groupLogic = new GroupLogic();
                    if(archivingState)
                        groupLogic.removeGroupById(groups.get(viewHolder.getAdapterPosition()).getGroupId(), getApplicationContext());
                    else
                        groupLogic.archiveGroupById(groups.get(viewHolder.getAdapterPosition()).getGroupId(), true, getApplicationContext());
                    groups.remove(viewHolder.getAdapterPosition());
                    groupAdapter.notifyDataSetChanged();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(groupRecyclerView);

        /*
        groupList = (ListView) findViewById(R.id.groupList);
        groupList.setAdapter(new GroupListAdapter(this));
        groupList.setOnItemClickListener(this);
        */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_save_backup) {
            saveBackup();
        } else if (id == R.id.action_restore_backup) {

        }

        return super.onOptionsItemSelected(item);
    }

    private void saveBackup(){
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids) {
                Logic logic = new Logic();
                return logic.saveBackup(getApplicationContext());
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                Toast.makeText(getApplicationContext(), success?R.string.backup_success:R.string.backup_fail, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.nav_groups:
                if(!archivingState)
                    break;
                setTitle(R.string.groups_activity_title);
                this.archivingState = false;
                loadGroups();
                break;
            case R.id.nav_archive:
                if(archivingState)
                    break;
                setTitle(R.string.archive_activity_title);
                this.archivingState = true;
                loadGroups();
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_help_feedback:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGroups();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, GroupDetailActivity.class);
        intent.putExtra(GroupDetailActivity.EXTRA_GROUP_ID, id);
        startActivity(intent);
    }

    private void loadGroups(){
        new AsyncTask<Void, Void, ArrayList<Group>>(){

            @Override
            protected ArrayList<Group> doInBackground(Void... voids) {
                GroupLogic groupLogic = new GroupLogic();
                return groupLogic.getGroups(getApplicationContext(), archivingState);
            }

            @Override
            protected void onPostExecute(ArrayList<Group> resultGroups) {
                super.onPostExecute(resultGroups);
                groups = resultGroups;
                if(groupAdapter instanceof GroupRecyclerViewAdapter) {
                    ((GroupRecyclerViewAdapter) groupAdapter).addGroupList(resultGroups);
                    groupAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                createGroup();
                break;
        }
    }

    private void createGroup(){
        final EditTextDialog editTextDialog = new EditTextDialog();
        editTextDialog.setTitle(getString(R.string.new_group));
        editTextDialog.setHint(getString(R.string.group_name));
        editTextDialog.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editTextDialog.setEditTextDialogCallbacks(new EditTextDialog.EditTextDialogCallbacks() {
            @Override
            public void onDialogSet(String name) {

                name = TextUtils.isEmpty(name)?getString(R.string.untitled_group):name;
                final String finalName = name;
                new AsyncTask<Void, Void, Group>() {

                    @Override
                    protected Group doInBackground(Void... voids) {
                        GroupLogic groupLogic = new GroupLogic();
                        long groupId = groupLogic.createGroup(finalName, getApplicationContext());
                        return new Group(groupId, finalName);
                    }

                    @Override
                    protected void onPostExecute(Group group) {
                        super.onPostExecute(group);
                        if (group.getGroupId() == -1)
                            Toast.makeText(getApplicationContext(), R.string.create_group_error, Toast.LENGTH_SHORT).show();
                        else if(groupAdapter instanceof GroupRecyclerViewAdapter){
                            ((GroupRecyclerViewAdapter) groupAdapter).addGroup(group);
                            groupAdapter.notifyDataSetChanged();
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
