package it.polimi.metalnews;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Info implements Parcelable{

	Bitmap img;
	String title;
	String backtitle;
	String imgUrl;
	String targetUrl;

	public Info (Parcel in){
		title= in.readString();
		backtitle = in.readString();
		imgUrl = in.readString();
		targetUrl = in.readString();

	}

	public Info(String completeTitle, String imgUrl, String targetUrl){

		StringBuilder str=new StringBuilder(completeTitle);
		int endTitle=str.indexOf(":");

		if(endTitle == -1){
			endTitle=str.indexOf("–");

			this.title=str.substring(0, endTitle-2);
			this.backtitle= str.substring(endTitle+2, str.length());
		}else{
			this.title=str.substring(0, endTitle+1);
			this.backtitle= str.substring(endTitle+2, str.length());
		}
		this.imgUrl=imgUrl;
		this.targetUrl=targetUrl;

	}

	public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>() {

		public Info createFromParcel(Parcel in) {
			return new Info(in);
		}

		public Info[] newArray(int size) {
			return new Info[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(title);
		out.writeString(backtitle);
		out.writeString(imgUrl);
		out.writeString(targetUrl);

	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public Bitmap getImg() {
		return img;
	}
	public void setImg(Bitmap img) {
		this.img = img;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBacktitle() {
		return backtitle;
	}
	public void setBacktitle(String backtitle) {
		this.backtitle = backtitle;
	}
	public String getImgUrl(){
		return imgUrl;
	}
	public void setImgUrl(String imgUrl){
		this.imgUrl=imgUrl;
	}


}
