package it.polimi.metalnews;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.util.Log;

public class ImageDownloadHtml extends ImageDownloader implements ImageGetter {

	public Bitmap b;
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		this.b=bitmap;
	}

	@Override
	public Drawable getDrawable(String source) {

		ImageDownloadHtml idh = new ImageDownloadHtml();
		
		idh.execute(source);

		//		b = downloadBitmap(source);
		do{
			Log.i("STATUS", this.getStatus().toString());
		}while(idh.getStatus()!=AsyncTask.Status.FINISHED);
		
		BitmapDrawable bitmapDrawable = new BitmapDrawable(idh.b);
		return bitmapDrawable;
	}

}

