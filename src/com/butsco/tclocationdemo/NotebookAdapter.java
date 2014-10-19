package com.butsco.tclocationdemo;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;

public class NotebookAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Notebook> notebooks;
	
	public NotebookAdapter(Context context, ArrayList<Notebook> notebooks){
		this.context = context;
		this.notebooks = notebooks;
	}
	
	@Override
	public int getCount(){
		return notebooks.size();
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
			convertView = inflater.inflate(R.layout.notebook_layout, null);
		}
		TextView title = (TextView) convertView.findViewById(R.id.title);
		title.setText(notebooks.get(position).getName());
		return convertView;
	}

	@Override
	public Object getItem(int position) {
		return this.notebooks.get(position);
	}
}
