package com.butsco.tclocationdemo;

import java.util.ArrayList;
import java.util.List;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.InvalidAuthenticationException;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.transport.TTransportException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MyListActivity extends ListActivity {

	private static final String TAG = "ListActivity";
	
	protected EvernoteSession mEvernoteSession;
	private String guid;
    private ArrayList<Note> notesList = new ArrayList<Note>();
    private NoteAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_list);
		
        mEvernoteSession = MyEvernoteSession.getInstance(this);
        
        getNotebookGuid("London Trip");
        
        mAdapter = new NoteAdapter(this, notesList);
        setListAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
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
		if (id == R.id.map){
          	Intent intent = new Intent(this, MapActivity.class);
        	startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	public String getNotebookGuid(String name){
		final String notebookName = name;
		final OnClientCallback<List<Notebook>> callback = new OnClientCallback<List<Notebook>>(){
			@Override
			public void onSuccess(List<Notebook> notebooks){
                Log.d(TAG, "Got notebooks");
                for (Notebook notebook : notebooks) {
                  if(notebook.getName().equals(notebookName)){
                	  Log.d(TAG, "Found correct notebook");
                	  guid = notebook.getGuid();
                	  listNotes();
                  }
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
		return guid;
	}
	
	public void listNotes(){
		NoteFilter filter = new NoteFilter();
	    filter.setNotebookGuid(guid);
	    int offset = 0;
	    int limit = 100;
	    final OnClientCallback<NoteList> callback = new OnClientCallback<NoteList>(){
            @Override
            public void onSuccess(NoteList data) {
            	Log.d(TAG, "Got notes");
                for (Note note : data.getNotes()) {
                    String title = note.getTitle();
                    NoteAttributes attr = note.getAttributes();
                    Log.d(TAG, title);
                    Log.d(TAG, "Lattitude: " + attr.getLatitude());
                    Log.d(TAG, "Lattitude: " + attr.getLongitude());
                    notesList.add(note);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onException(Exception exception) {
			    Log.e(TAG, "Error retrieving notebooks", exception);
            }
	    };
	    
		try {
			mEvernoteSession.getClientFactory().createNoteStoreClient().findNotes(filter, offset, limit, callback);			
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
