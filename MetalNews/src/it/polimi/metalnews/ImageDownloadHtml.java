package it.polimi.metalnews;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloadHtml extends ImageDownloader {

	protected final WeakReference imageViewReference;
	
	public ImageDownloadHtml(ImageView imageView) {
		super();
		imageViewReference = new WeakReference(imageView);	
	}

	
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}

		if (imageViewReference != null) {
			ImageView imageView = (ImageView) imageViewReference.get();
			if (imageView != null) {

				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
				}
			}

		}
		
	}

	

}

