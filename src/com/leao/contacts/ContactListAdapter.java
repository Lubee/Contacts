package com.leao.contacts;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leao.contacts.db.DBAdapter;
import com.leao.contacts.pojo.ContactInfo;

public class ContactListAdapter extends BaseAdapter {

	static String tag = "ContactListAdapter";

	// private Context context;
	protected Resources res;
	protected static Context mContext;
	protected LayoutInflater inflater;
	private ArrayList<ContactInfo> mContactList;



	public ContactListAdapter(Context context, final ArrayList<ContactInfo> contactList) {
		mContext = context;
		inflater = LayoutInflater.from(context);
		mContactList = contactList;

	}

	@Override
	public int getCount() {
		return mContactList.size();
	}

	@Override
	public Object getItem(int position) {
		return mContactList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Viewholder holder;
	    if (convertView == null) {
	        convertView = inflater.inflate(R.layout.contact_item, null);
	        holder = new Viewholder();
	        holder.name = (TextView)convertView.findViewById(R.id.name_tv);
	        holder.sex = (TextView)convertView.findViewById(R.id.sex_tv);
	        holder.persontity = (TextView)convertView.findViewById(R.id.personality_tv);
	        holder.position = (TextView)convertView.findViewById(R.id.position_tv);
	        holder.image = (ImageView)convertView.findViewById(R.id.avatar_btn);
	        convertView.setTag(holder);
	      } else {
	        holder=(Viewholder)convertView.getTag();
	      }
	    
	    ContactInfo info = mContactList.get(position);
	    holder.name.setText(info.getName());
	    holder.sex.setText(info.getSex());
	    holder.persontity.setText(info.getPersontity());
	    holder.position.setText(info.getPosition());
	    holder.image.setImageBitmap(info.getImage());
	    
		return convertView;
	}

	static class Viewholder {
		TextView name;
		TextView sex;
		TextView persontity;
		TextView position;
		ImageView image;
	}
}
