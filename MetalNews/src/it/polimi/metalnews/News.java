package it.polimi.metalnews;

import java.io.Serializable;

import android.graphics.Bitmap;

public class News implements Serializable {
	
	Bitmap img;
	String title;
	String backtitle;
	
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
	
	
	

}
