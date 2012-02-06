package com.leao.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ContactsActivity extends Activity  implements OnClickListener{
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ImageView addContactImg;
        ImageView contactListImg;
        addContactImg = (ImageView) findViewById(R.id.add_contact);
        contactListImg = (ImageView) findViewById(R.id.contact_list);
        
        addContactImg.setOnClickListener(this);
        contactListImg.setOnClickListener(this);
    }
    
    
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.add_contact:
			Intent addIntent = new Intent(ContactsActivity.this,AddActivity.class);
			startActivity(addIntent);
			break;
		case R.id.contact_list:
			Intent listIntent = new Intent(ContactsActivity.this,ContactListActivity.class);
			startActivity(listIntent);
			break;
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
    

}