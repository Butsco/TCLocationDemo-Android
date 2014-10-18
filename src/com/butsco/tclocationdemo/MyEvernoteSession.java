package com.butsco.tclocationdemo;

import android.content.Context;

import com.evernote.client.android.EvernoteSession;

public class MyEvernoteSession {
	private static EvernoteSession instance;
	
	private static final String CONSUMER_KEY = "houbenkristof-7667";
	private static final String CONSUMER_SECRET = "9f939b7333a42712";
	private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
	private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;
	
	public static EvernoteSession getInstance(Context context){
		if (instance == null){
			instance = EvernoteSession.getInstance(context, CONSUMER_KEY, CONSUMER_SECRET, EVERNOTE_SERVICE, SUPPORT_APP_LINKED_NOTEBOOKS);
		}
		return instance;
	}
}
