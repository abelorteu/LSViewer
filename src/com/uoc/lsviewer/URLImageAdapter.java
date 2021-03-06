package com.uoc.lsviewer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class URLImageAdapter extends BaseAdapter {

	// an object we'll use to keep our cache data together
	private class Image {
		String url;
		Bitmap thumb;
	}

	// an array of resources we want to display
	private Image[] images;

	// a context so we can later create a view within it
	private Context myContext;

	// the background task object
	private LoadThumbsTask thumbnailGen;
	
	// URL images array
	private String[] imageURLs;
	

	// Constructor
	public URLImageAdapter(Context c, Object previousList, String[] iURLs) {

		myContext = c;		
		imageURLs = iURLs;
		
		// get our thumbnail generation task ready to execute
		thumbnailGen = new LoadThumbsTask();
		
		// we'll want to use pre-existing data, if it exists
		if(previousList != null) {
			images = (Image[]) previousList;

			// continue processing remaining thumbs in the background
			thumbnailGen.execute(images);

			// no more setup required in this constructor
			return;
		}
		
		// if no pre-existing data, we need to generate it from scratch.

		// initialize array
		images = new Image[imageURLs.length];
		for(int i = 0, j = imageURLs.length; i < j; i++) {
			images[i] = new Image();
			images[i].url = imageURLs[i];
		}
		
		// start the background task to generate thumbs
		thumbnailGen.execute(images);
	}


	@Override
	/**
	 * Getter: number of items in the adapter's data set
	 */
	public int getCount() {
		return images.length;
	}


	@Override
	/**
	 * Getter: return URL at specified position
	 */
	public Object getItem(int position) {
		return images[position].url;
	}

	@Override
	/**
	 * Getter: return resource ID of the item at the current position
	 */
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * Getter: return generated data
	 * @return array of Image
	 */
	public Object getData() {
		// stop the task if it isn't finished
		if(thumbnailGen != null && thumbnailGen.getStatus() != AsyncTask.Status.FINISHED) {
			// cancel the task
			thumbnailGen.cancel(true);

		}
		// return generated thumbs
		return images;
	}

	/**
	 * Create a new ImageView when requested, filling it with a 
	 * thumbnail or a blank image if no thumb is ready yet.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView imgView;		
		// pull the cached data for the image assigned to this position
		Image cached = images[position];

		// can we recycle an old view?
		if(convertView == null) {
			// no view to recycle; create a new view
			imgView = new ImageView(myContext);
			imgView.setLayoutParams(new GridView.LayoutParams(100,100));
		} else {	
			// recycle an old view (it might have old thumbs in it!)
			imgView = (ImageView) convertView;	
		}

		// do we have a thumb stored in cache?
		if(cached.thumb == null) {			
			// no cached thumb, so let's set the view as blank
			imgView.setImageResource(R.drawable.ic_menu_image);		
			imgView.setScaleType(ScaleType.CENTER);
		} else {
			// yes, cached thumb! use that image
			imgView.setScaleType(ScaleType.FIT_CENTER);
			imgView.setImageBitmap(cached.thumb);
		}
		return imgView;
	}
	
	/**
	 * Notify the adapter that our data has changed so it can
	 * refresh the views & display any newly-generated thumbs
	 */
	private void cacheUpdated() {
		this.notifyDataSetChanged();
	}

	/**
	 * Download and return a thumb specified by url, subsampling 
	 * it to a smaller size.
	 */
	private Bitmap loadThumb(String url) {

		// the downloaded thumb (none for now!)
		Bitmap thumb = null;

		// sub-sampling options
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 4;

		try {
			// open a connection to the URL
			// Note: pay attention to permissions in the Manifest file!
			URL u = new URL(url);
			URLConnection c = u.openConnection();
			c.connect();			
			// read data
			BufferedInputStream stream = new BufferedInputStream(c.getInputStream());
			// decode the data, subsampling along the way
			thumb = BitmapFactory.decodeStream(stream, null, opts);
			// close the stream
			stream.close();
		} catch (MalformedURLException e) {
			Log.e("Threads03", "malformed url: " + url);
		} catch (IOException e) {
			Log.e("Threads03", "An error has occurred downloading the image: " + url);
		}

		// return the fetched thumb (or null, if error)
		return thumb;
	}
	
	// the class that will create a background thread and generate thumbs
	private class LoadThumbsTask extends AsyncTask<Image, Void, Void> {

		/**
		 * Generate thumbs for each of the Image objects in the array
		 * passed to this method. This method is run in a background task.
		 */
		@Override
		protected Void doInBackground(Image... cache) {			
			// define the options for our bitmap subsampling 
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 4;

			// iterate over all images ...
			for (Image i : cache) {

				// if our task has been cancelled then let's stop processing
				if(isCancelled()) return null;

				// skip a thumb if it's already been generated
				if(i.thumb != null) continue;

				// artificially cause latency!
				SystemClock.sleep(500);
				
				// download and generate a thumb for this image
				i.thumb = loadThumb(i.url);

				// some unit of work has been completed, update the UI
				publishProgress();
			}			
			return null;
		}

		/**
		 * Update the UI thread when requested by publishProgress()
		 */
		@Override
		protected void onProgressUpdate(Void... param) {
			cacheUpdated();
		}
	}
}