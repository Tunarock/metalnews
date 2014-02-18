package it.polimi.metalnews;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ImageDownloaderNews extends ImageDownloader {

	protected final WeakReference imageViewReference;
	
	public ImageDownloaderNews(ImageView imageView, News[] news, int position) {
		super();
		imageViewReference = new WeakReference(imageView);
		this.position=position;
		this.news=news;
	
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
					news[position].setImg(bitmap);
				}
			}

		}
		
	}
	

}
