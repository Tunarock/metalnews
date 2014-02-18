package it.polimi.metalnews;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;

public class ImageDownloadHtml extends ImageDownloader implements ImageGetter {

	Bitmap b;
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		this.b=bitmap;
	}
	
	@Override
	public Drawable getDrawable(String source) {
	
		this.execute(source);
		return new BitmapDrawable(b);
	}

}

