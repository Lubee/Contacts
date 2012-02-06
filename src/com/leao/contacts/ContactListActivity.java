package com.leao.contacts;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.leao.contacts.db.DBAdapter;
import com.leao.contacts.pojo.ContactInfo;

public class ContactListActivity extends Activity implements OnItemClickListener {

	protected static final int INITFINISH = 1001;
	private DBAdapter dbAdapter;
	private ArrayList<ContactInfo> mContactList = new ArrayList<ContactInfo>();

	private ListView mItemlist = null;
	private ProgressDialog mProgressDialog;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);

		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		mItemlist = (ListView) findViewById(R.id.list);

		handleMessage();
		initContactInfos();
	}

	private void handleMessage() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case INITFINISH:
					initList();
					break;

				default:
					break;
				}
			}

		};

	}

	private void initList() {
		ContactListAdapter adapter = new ContactListAdapter(this, mContactList);
		mItemlist.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		 dismissProgressDialog();
	}

	private void initContactInfos() {
		 showProgressDialog(true);
		new Thread() {
			@Override
			public void run() {
				synchronized (mContactList) {
					mContactList.clear();
					Cursor myCursor = dbAdapter.quryItems(0);
					myCursor.moveToFirst();
					int count = myCursor.getCount();
					for (int i = 0; i < count; i++) {

						ContactInfo info = new ContactInfo();
						info.setId(myCursor.getInt(0));
						info.setName(myCursor.getString(myCursor
								.getColumnIndexOrThrow(DBAdapter.NAME)));
						info.setPosition(myCursor.getString(myCursor
								.getColumnIndexOrThrow(DBAdapter.COMPANY)));
						info.setPersontity(myCursor.getString(myCursor
								.getColumnIndexOrThrow(DBAdapter.PERSONALITY)));

						String sex = ContactListActivity.this.getResources()
								.getString(R.string.man);
						if (myCursor.getInt(myCursor
								.getColumnIndexOrThrow(DBAdapter.SEX)) == 1) {
							sex = ContactListActivity.this.getResources()
									.getString(R.string.female);
						}
						info.setSex(sex);

						String imagePath = "";
						if (null != myCursor.getString(myCursor
								.getColumnIndexOrThrow(DBAdapter.IMAGEPATH))
								&& myCursor
										.getString(
												myCursor.getColumnIndexOrThrow(DBAdapter.IMAGEPATH))
										.length() > 0) {
							imagePath = myCursor
									.getString(myCursor
											.getColumnIndexOrThrow(DBAdapter.IMAGEPATH));
						}

						Bitmap bitmap = null;
						if(null != imagePath && !"".equals(imagePath)){
							bitmap = BitmapFactory.decodeFile(imagePath);
						}else{
							bitmap = BitmapFactory.decodeResource(ContactListActivity.this.getResources(), R.drawable.default_icon);
						}
						info.setImage(bitmap);
						mContactList.add(info);
						myCursor.moveToNext();
					}
					myCursor.close();

					Message message = mHandler.obtainMessage();
					message.what = INITFINISH;
					mHandler.sendMessage(message);
				}
			}
		}.start();

	}

	private void showProgressDialog(boolean isInit) {
		if (null == mProgressDialog) {
			if (isInit) {
				mProgressDialog = ProgressDialog.show(this, null,
						getString(R.string.initialization), true);
			}
		}
	}

	private void dismissProgressDialog() {
		if (mProgressDialog != null) {
			try {
				mProgressDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mProgressDialog = null;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// land
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// port
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ContactInfo info = mContactList.get(position);
		Intent intent = new Intent(ContactListActivity.this,ContactsDatail.class);
		intent.putExtra("contact_id", info.getId());
		startActivity(intent);
		
		
	}
}
