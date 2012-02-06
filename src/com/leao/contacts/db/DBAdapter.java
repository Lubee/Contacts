package com.leao.contacts.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

	public static final String DATABASE_NAME = "contactleao.db";

	public static final String CONTACTS_TABLE = "contacts";
	public static final String CONTACTS_ID = "contacts_id";
	public static final String IMAGEPATH = "imagepath";
	public static final String DATA = "date";
	public static final String NAME = "name";
	public static final String COMPANY = "company";
	public static final String POSITION = "position";
	public static final String PHONE_NUM = "phone_num";

	public static final String QQ = "qq";
	public static final String EMAIL = "email";
	public static final String PERSONALITY = "personality";
	public static final String SEX = "sex";
	public static final String GROUP_CONTACT = "group_contact";

	private static final int DATABASE_VERSION = 1;

	protected static final String CONTACTS_TABLE_DDL = "CREATE TABLE IF NOT EXISTS "
			+ CONTACTS_TABLE
			+ " ("
			+ CONTACTS_ID
			+ " INTEGER primary key autoincrement, "
			+ " "
			+ IMAGEPATH
			+ " VARCHAR, "
			+ " "
			+ DATA
			+ "  CHAR(10),"
			+ " "
			+ NAME
			+ " VARCHAR, "
			+ " "
			+ COMPANY
			+ " VARCHAR, "
			+ " "
			+ POSITION
			+ " VARCHAR, "
			+ " "
			+ PHONE_NUM
			+ " VARCHAR, "
			+ " "
			+ QQ
			+ " VARCHAR, "
			+ " "
			+ EMAIL
			+ " VARCHAR, "
			+ " "
			+ PERSONALITY
			+ " VARCHAR, "
			+ " "
			+ SEX
			+ " INTEGER DEFAULT '0', "
			+ " "
			+ GROUP_CONTACT + " INTEGER DEFAULT '0');";

	private static final String DROP_ACCOUNT_DLL = "DROP TABLE IF EXISTS "
			+ CONTACTS_TABLE;
	private final Context mCtx;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CONTACTS_TABLE_DDL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DROP_ACCOUNT_DLL);
			onCreate(db);
		}
	}

	public DBAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public DBAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public Cursor quryItems(int group) {
		return mDb.query(CONTACTS_TABLE, new String[] { CONTACTS_ID, IMAGEPATH,
				DATA, NAME, COMPANY, POSITION, PHONE_NUM, QQ, EMAIL,
				PERSONALITY, SEX, GROUP_CONTACT }, GROUP_CONTACT
				+ "=" + group, null, null, null, CONTACTS_ID
				+ " DESC");
	}

	/**
	 * 
	 * @param rowId
	 * @return
	 * @throws SQLException
	 */
	public Cursor quryItemById(long rowId) throws SQLException {
		Cursor mCursor = mDb.query(true, CONTACTS_TABLE, new String[] {
				CONTACTS_ID, IMAGEPATH, DATA, NAME, COMPANY, POSITION,
				PHONE_NUM, QQ, EMAIL, PERSONALITY, SEX, GROUP_CONTACT }, CONTACTS_ID
				+ "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	/**
	 * 
	 * @param rowId
	 * @param itemName
	 * @param itemType
	 * @return
	 */
	public boolean updateItem(long rowId,  String date,
			String name, String company, String position,
			String phoneNum, String qq, String email,
			String personality, String imagePath, int sex ,int group ) {
		ContentValues args = new ContentValues();
		args.put(IMAGEPATH, imagePath);
		args.put(DATA, date);
		args.put(NAME, name);
		args.put(COMPANY, company);
		args.put(POSITION, position);
		args.put(PHONE_NUM, phoneNum);
		args.put(QQ, qq);
		args.put(EMAIL, email);
		args.put(PERSONALITY, personality);
		args.put(SEX, sex);
		args.put(GROUP_CONTACT, group);
		return mDb.update(CONTACTS_TABLE, args, CONTACTS_ID + "=" + rowId, null) > 0;
	}

	/**
	 * 
	 * @param itemName
	 * @param itemType
	 * @return
	 */
	public long insertItem(String date,
			String name, String company, String position,
			String phoneNum, String qq, String email,
			String personality, String imagePath, int sex ,int group) {
		ContentValues args = new ContentValues();
		args.put(IMAGEPATH, imagePath);
		args.put(DATA, date);
		args.put(NAME, name);
		args.put(COMPANY, company);
		args.put(POSITION, position);
		args.put(PHONE_NUM, phoneNum);
		args.put(QQ, qq);
		args.put(EMAIL, email);
		args.put(PERSONALITY, personality);
		args.put(SEX, sex);
		args.put(GROUP_CONTACT, group);
		return mDb.insert(CONTACTS_TABLE, null, args);

	}

	/**
	 * 
	 * @param rowId
	 * @return
	 */
	public boolean deleteItem(long rowId) {
		return mDb.delete(CONTACTS_TABLE, CONTACTS_ID + "=" + rowId, null) > 0;

	}

}
