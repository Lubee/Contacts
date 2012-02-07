package com.leao.contacts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.leao.contacts.db.DBAdapter;
import com.leao.contacts.util.DateUtil;

public class AddActivity extends Activity {

	public static final String AVATAR_DIR = Environment
			.getExternalStorageDirectory() + "/contacts/avatar/";

	private Uri u;
	private String mAvatarPath = "";

	private ImageView mBackImg;
	private ImageView mAvatarImg;
	private EditText mCompanyTv;
	private EditText mPositionTv;
	private EditText mNameTv;

	private EditText mPhoneTv;
	private EditText mQqTv;
	private EditText mEmailTv;
	private EditText mPersontityTv;
	private EditText mDateTv;

	private RadioButton mManRadioBtn;
	private RadioButton mFemaleRadioBtn;

	private ImageButton mSaveImage;

	private Uri mImageCaptureUri;
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;
	private static final int PICK_CROP_IMAGE = 3;

	private DBAdapter dbAdapter;
	private int contactID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_contact);

		dbAdapter = new DBAdapter(this);
		dbAdapter.open();

		initComponent();

		setComponentValues();
		initListener();
	}

	private void setComponentValues() {

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null && !bundle.isEmpty()) {
			contactID  = bundle.getInt("contact_id");
			Cursor myCursor = dbAdapter.quryItemById(contactID);
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

			if (myCursor.getInt(myCursor.getColumnIndexOrThrow(DBAdapter.SEX)) == 1) {
				mFemaleRadioBtn.setChecked(true);
			} else {
				mManRadioBtn.setChecked(true);
			}

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
						AddActivity.this.getResources(),
						R.drawable.default_icon);
			}
			mAvatarImg.setImageBitmap(bitmap);
			myCursor.close();

		}

	}

	private void initComponent() {
		mBackImg = (ImageView) findViewById(R.id.back_btn);
		mAvatarImg = (ImageView) findViewById(R.id.avatar_btn);
		mCompanyTv = (EditText) findViewById(R.id.company_et);
		mPositionTv = (EditText) findViewById(R.id.position_et);
		mNameTv = (EditText) findViewById(R.id.name_et);

		mPhoneTv = (EditText) findViewById(R.id.phonenum_et);
		mQqTv = (EditText) findViewById(R.id.qq_et);
		mEmailTv = (EditText) findViewById(R.id.email_et);
		mPersontityTv = (EditText) findViewById(R.id.personality_et);
		mDateTv = (EditText) findViewById(R.id.date_et);

		mManRadioBtn = (RadioButton) findViewById(R.id.myRadioButton1);
		mFemaleRadioBtn = (RadioButton) findViewById(R.id.myRadioButton2);

		mSaveImage = (ImageButton) findViewById(R.id.save_iv);

	}

	private Calendar cal = Calendar.getInstance();
	private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() { //

		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			cal.set(Calendar.YEAR, arg1);
			cal.set(Calendar.MONTH, arg2);
			cal.set(Calendar.DAY_OF_MONTH, arg3);
			updateDate();
		}

		private void updateDate() {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			mDateTv.setText(df.format(cal.getTime()));
		}
	};

	private void initListener() {
		// 出生日期
		mDateTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DatePickerDialog(AddActivity.this, listener, cal
						.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
						.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		// 相机
		final String[] items = new String[] { "从相机", "从相册" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				AddActivity.this, android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
		builder.setTitle("设置头像");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File imageFileDir = new File(AVATAR_DIR);
					if (!imageFileDir.exists()) {
						imageFileDir.mkdirs();
					}
					File file = new File(imageFileDir, "tmp_avatar" + ".jpg");
					mImageCaptureUri = Uri.fromFile(file);

					try {
						intent.putExtra(
								android.provider.MediaStore.EXTRA_OUTPUT,
								mImageCaptureUri);
						intent.putExtra("return-data", true);

						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (Exception e) {
						e.printStackTrace();
					}

					dialog.cancel();
				} else {
					Intent intent = new Intent();

					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					startActivityForResult(
							Intent.createChooser(intent, "手机图片选择"),
							PICK_FROM_FILE);
				}
			}
		});
		final AlertDialog dialog = builder.create();
		mAvatarImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});

		mSaveImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int sex = 0;
				if (mFemaleRadioBtn.isChecked()) {
					sex = 1;
				}
				if (null == mNameTv.getText()
						|| mNameTv.getText().toString().trim().length() == 0) {
					Toast.makeText(AddActivity.this, "请输入信息！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if(contactID > 0){
					dbAdapter.updateItem(contactID,mDateTv.getText().toString(), mNameTv
							.getText().toString(), mCompanyTv.getText().toString(),
							mPositionTv.getText().toString(), mPhoneTv.getText()
									.toString(), mQqTv.getText().toString(),
							mEmailTv.getText().toString(), mPersontityTv.getText()
									.toString(), mAvatarPath, sex, 0);
					
					Intent mIntent = new Intent();
					setResult(RESULT_OK, mIntent);
				}else{
					dbAdapter.insertItem(mDateTv.getText().toString(), mNameTv
						.getText().toString(), mCompanyTv.getText().toString(),
						mPositionTv.getText().toString(), mPhoneTv.getText()
								.toString(), mQqTv.getText().toString(),
						mEmailTv.getText().toString(), mPersontityTv.getText()
								.toString(), mAvatarPath, sex, 0);
				}
				finish();

			}
		});

		mBackImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK && PICK_CROP_IMAGE == requestCode) {
			if (null != u) {
				Bitmap photo = null;
				String path = "";

				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = 4;
				path = getRealPathFromURI(u);
				photo = BitmapFactory.decodeFile(path, opts);

				int bitmapWidth = photo.getWidth();
				int bitmapHeight = photo.getHeight();
				// 缩放图片的尺寸
				float scaleWidth = (float) 120 / bitmapWidth;
				float scaleHeight = (float) 120 / bitmapHeight;
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				// 产生缩放后的Bitmap对象
				Bitmap resizeBitmap = Bitmap.createBitmap(photo, 0, 0,
						bitmapWidth, bitmapHeight, matrix, false);
				photo.recycle();
				handleAvatarImage(resizeBitmap);

			}

			return;
		} else if (resultCode != RESULT_OK && PICK_CROP_IMAGE != requestCode) {
			return;
		}

		switch (requestCode) {
		case PICK_FROM_CAMERA:
		case PICK_FROM_FILE:
			try {
				if (data == null || data.getData() == null) {
					u = Uri.parse(android.provider.MediaStore.Images.Media
							.insertImage(getContentResolver(),
									mImageCaptureUri.getPath(), null, null));
				} else {
					u = data.getData();
				}
				Intent cj = new Intent("com.android.camera.action.CROP");
				cj.setData(u);
				cj.putExtra("crop", "true");
				cj.putExtra("aspectX", 1);
				cj.putExtra("aspectY", 1);
				cj.putExtra("outputX", 120);
				cj.putExtra("outputY", 120);
				cj.putExtra("noFaceDetection", true);
				cj.putExtra("return-data", true);
				startActivityForResult(Intent.createChooser(cj, "裁剪"),
						PICK_CROP_IMAGE);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			break;
		case PICK_CROP_IMAGE:
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				handleAvatarImage(photo);
			}
			break;
		default:
			break;
		}

	}

	private void handleAvatarImage(Bitmap photo) {
		try {
			File imageFileDir = new File(AVATAR_DIR);
			if (!imageFileDir.exists()) {
				imageFileDir.mkdirs();
			}

			File avatarFile = new File(imageFileDir, DateUtil.getImageDate()
					+ ".png");
			FileOutputStream outStreamz = new FileOutputStream(avatarFile);
			photo.compress(Bitmap.CompressFormat.PNG, 100, outStreamz);
			outStreamz.flush();
			outStreamz.close();
			photo.recycle();

			mAvatarPath = avatarFile.getAbsolutePath();
			Bitmap bitmap = BitmapFactory.decodeFile(mAvatarPath);
			mAvatarImg.setImageBitmap(bitmap);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		if (cursor == null)
			return null;
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
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
}
