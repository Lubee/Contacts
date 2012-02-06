package com.leao.contacts.pojo;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


public class ContactInfo  implements Parcelable{
	private int id;
	private String avatar;
	private String company;
	private String position;
	private String name;

	private String phone;
	private String qq;
	private String email;
	private String persontity;
	private String date;
	private String sex;
	
	private Bitmap image;

	public ContactInfo() {
	}

	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public ContactInfo(int id, String avatar, String company, String position,
			String name, String phone, String qq, String email,
			String persontity, String date, String sex, Bitmap image) {
		super();
		this.id = id;
		this.avatar = avatar;
		this.company = company;
		this.position = position;
		this.name = name;
		this.phone = phone;
		this.qq = qq;
		this.email = email;
		this.persontity = persontity;
		this.date = date;
		this.sex = sex;
		this.image = image;
	}


	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPersontity() {
		return persontity;
	}

	public void setPersontity(String persontity) {
		this.persontity = persontity;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);  
		dest.writeString(company);  
		dest.writeString(position);  
		dest.writeString(phone);
		dest.writeString(qq);  
		dest.writeString(email);
		dest.writeString(persontity);  
		dest.writeString(date);
		dest.writeString(sex);  
		
	}
	
	public static final Parcelable.Creator<ContactInfo> CREATOR = new Creator<ContactInfo>() {  
        public ContactInfo createFromParcel(Parcel source) {  
        	ContactInfo info = new ContactInfo();  
        	info.company = source.readString();  
            return info;  
         }  
        public ContactInfo[] newArray(int size) {  
            return new ContactInfo[size];  
         }  
     };  
       

}
