package it.polimi.metalnews;

import android.graphics.Bitmap;

public class News{
	
	Bitmap img;
	String title;
	String backtitle;
	String imgUrl;
	String targetUrl;
	
	public News(String completeTitle, String imgUrl, String targetUrl){
		
		StringBuilder str=new StringBuilder(completeTitle);
		int endTitle=str.indexOf(":");
		
		this.title=str.substring(0, endTitle+1);
		this.backtitle= str.substring(endTitle+2, str.length());
		
		this.imgUrl=imgUrl;
		this.targetUrl=targetUrl;
		
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
