package com.butsco.tclocationdemo;

import java.util.ArrayList;
import java.util.List;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.transport.TTransportException;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class MapActivity extends Activity {
	MapView mMapView;
	private static final String TAG = "MapActivity";
	
	protected EvernoteSession mEvernoteSession;
	private String guid;
    private ArrayList<Note> notesList = new ArrayList<Note>();

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		mEvernoteSession = MyEvernoteSession.getInstance(this);
        
        getNotebookGuid("London Trip");
		
		// Retrieve the map and initial extent from XML layout
		mMapView = (MapView)findViewById(R.id.map);
		// Add dynamic layer to MapView
		ArcGISTiledMapServiceLayer layer = new ArcGISTiledMapServiceLayer("" +
				"http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer");
		mMapView.addLayer(layer);
	}

	protected void onPause() {
		super.onPause();
		mMapView.pause();
	}

	protected void onResume() {
		super.onResume();
		mMapView.unpause();
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
                    plotNotes();
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
	
	public void plotNotes(){
		GraphicsLayer gLayer = new GraphicsLayer();
		SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 10, STYLE.CIRCLE);
		SpatialReference webSR = SpatialReference.create(3857);
		for (Note note : notesList){
            NoteAttributes attr = note.getAttributes();
			Point webPoint = GeometryEngine.project(attr.getLongitude(), attr.getLatitude(), webSR);
			Graphic graphic = new Graphic(webPoint, sms);
			gLayer.addGraphic(graphic);
		}
		mMapView.addLayer(gLayer);
	}
}
