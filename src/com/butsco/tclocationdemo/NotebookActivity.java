package com.butsco.tclocationdemo;

import java.util.ArrayList;
import java.util.List;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.InvalidAuthenticationException;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.transport.TTransportException;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class NotebookActivity extends ListActivity {
	private static final String TAG = "NotebookActivity";
	
	protected EvernoteSession mEvernoteSession;
    private ArrayList<Notebook> notebooksList = new ArrayList<Notebook>();
    private NotebookAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notebook);
		
		mEvernoteSession = MyEvernoteSession.getInstance(this);
		
		getNotebooks();
		
        mAdapter = new NotebookAdapter(this, notebooksList);
        setListAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notebook, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.logout){
		    try {
		        mEvernoteSession.logOut(this);
		    } catch (InvalidAuthenticationException e) {
		        Log.e(TAG, "Tried to call logout with not logged in", e);
		    }
	      	Intent intent = new Intent(this, MainActivity.class);
	    	startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int pos, long id){
		super.onListItemClick(l, v, pos, id);
		Log.d(TAG, "clicked on notebook: " + notebooksList.get(pos).getName());
		Intent intent = new Intent(this, MyListActivity.class);
		startActivity(intent);
	}
	
	public void getNotebooks(){
		final OnClientCallback<List<Notebook>> callback = new OnClientCallback<List<Notebook>>(){
			@Override
			public void onSuccess(List<Notebook> notebooks){
                Log.d(TAG, "Got notebooks");
                for (Notebook notebook : notebooks) {
                	notebooksList.add(notebook);
                	mAdapter.notifyDataSetChanged();
                }
			}

			@Override
			public void onException(Exception exception) {
			    Log.e(TAG, "Error retrieving notebooks", exception);
            }
		};
		try {
			mEvernoteSession.getClientFactory().createNoteStoreClient().listNotebooks(callback);
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
