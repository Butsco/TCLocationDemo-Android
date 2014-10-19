package com.butsco.tclocationdemo;

import java.util.ArrayList;

import com.evernote.edam.type.Note;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoteAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Note> notes;
	
	public NoteAdapter(Context context, ArrayList<Note> notes){
		this.context = context;
		this.notes = notes;
	}
	
	@Override
	public int getCount(){
		return notes.size();
	}
	
	@Override
	public long getItemId(int position){
		return position;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.note_layout, null);
		}
		TextView title = (TextView) convertView.findViewById(R.id.title);
		title.setText(notes.get(position).getTitle());
		return convertView;
	}

	@Override
	public Object getItem(int position) {
		return this.notes.get(position);
	}
}