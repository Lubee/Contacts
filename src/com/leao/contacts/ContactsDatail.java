package com.leao.contacts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.leao.contacts.db.DBAdapter;

public class ContactsDatail extends Activity {

	private DBAdapter dbAdapter;
	
	private ImageView mBackImg;
	private ImageView mAvatarImg;
	private TextView mCompanyTv;
	private TextView mPositionTv;
	private TextView mNameTv;

	private TextView mPhoneTv;
	private TextView mQqTv;
	private TextView mEmailTv;
	private TextView mPersontityTv;
	private TextView mDateTv;
	
	private TextView mSxeTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		initComp();
		initView();
	}

	private void initComp() {
		mBackImg = (ImageView) findViewById(R.id.back_btn);
		mAvatarImg = (ImageView) findViewById(R.id.avatar_btn);
		mCompanyTv = (TextView) findViewById(R.id.company_tv);
		mPositionTv = (TextView) findViewById(R.id.position_tv);
		mNameTv = (TextView) findViewById(R.id.name_tv);

		mPhoneTv = (TextView) findViewById(R.id.phonenum_tv);
		mQqTv = (TextView) findViewById(R.id.qq_tv);
		mEmailTv = (TextView) findViewById(R.id.email_tv);
		mPersontityTv = (TextView) findViewById(R.id.personality_tv);
		mDateTv = (TextView) findViewById(R.id.date_tv);

		mSxeTv = (TextView) findViewById(R.id.sex_tv);

		
		mBackImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}

	private void initView() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null && !bundle.isEmpty()) {
			int id = bundle.getInt("contact_id");
			Cursor myCursor = dbAdapter.quryItemById(id);
			mNameTv.setText(myCursor.getString(myCursor
					.getColumnIndexOrThrow(DBAdapter.NAME)));
			mCompanyTv.setText(myCursor.getString(myCursor
					.getColumnIndexOrThrow(DBAdapter.COMPANY)));
			mPersontityTv.setText(myCursor.getString(myCursor
					.getColumnIndexOrThrow(DBAdapter.PERSONALITY)));
			
			mPositionTv.setText(myCursor.getString(myCursor
					.getColumnIndexOrThrow(DBAdapter.POSITION)));
			mPhoneTv.setText(myCursor.getString(myCursor
					.getColumnIndexOrThrow(DBAdapter.PHONE_NUM)));
			mQqTv.setText(myCursor.getString(myCursor
					.getColumnIndexOrThrow(DBAdapter.QQ)));
			mEmailTv.setText(myCursor.getString(myCursor
					.getColumnIndexOrThrow(DBAdapter.EMAIL)));
			mDateTv.setText(myCursor.getString(myCursor
					.getColumnIndexOrThrow(DBAdapter.DATA)));
			
			String sex = ContactsDatail.this.getResources().getString(
					R.string.man);
			if (myCursor.getInt(myCursor.getColumnIndexOrThrow(DBAdapter.SEX)) == 1) {
				sex = ContactsDatail.this.getResources().getString(
						R.string.female);
			}
			mSxeTv.setText(sex);

			String imagePath = "";
			if (null != myCursor.getString(myCursor
					.getColumnIndexOrThrow(DBAdapter.IMAGEPATH))
					&& myCursor
							.getString(
									myCursor.getColumnIndexOrThrow(DBAdapter.IMAGEPATH))
							.length() > 0) {
				imagePath = myCursor.getString(myCursor
						.getColumnIndexOrThrow(DBAdapter.IMAGEPATH));
			}

			Bitmap bitmap = null;
			if (null != imagePath && !"".equals(imagePath)) {
				bitmap = BitmapFactory.decodeFile(imagePath);
			} else {
				bitmap = BitmapFactory.decodeResource(
						ContactsDatail.this.getResources(),
						R.drawable.default_icon);
			}
			mAvatarImg.setImageBitmap(bitmap);
			myCursor.close();
			
			
		}
		
	}

}
