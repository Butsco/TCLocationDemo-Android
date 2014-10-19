package com.butsco.tclocationdemo;

import com.evernote.client.android.EvernoteSession;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

	protected EvernoteSession mEvernoteSession;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        mEvernoteSession = MyEvernoteSession.getInstance(this);
        
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
        return super.onOptionsItemSelected(item);
    }
    
    public void login(View view){
        if (mEvernoteSession.isLoggedIn()==true){
          	Intent intent = new Intent(this, NotebookActivity.class);
        	startActivity(intent);
        } else {
        	mEvernoteSession.authenticate(this);
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      switch(requestCode) {
        // Update UI when oauth activity returns result
        case EvernoteSession.REQUEST_CODE_OAUTH:
          if (resultCode == Activity.RESULT_OK) {
          	Intent intent = new Intent(this, NotebookActivity.class);
        	startActivity(intent);
          }
          break;
      }
    }
}
