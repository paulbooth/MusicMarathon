package paul.booth.musicmarathon;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

public class SevenDigitalAPI {	
	// 7digital API key
	final static public String _7DIGITAL_API_KEY = "7dravp2zz8";
	final static public String _7DIGITAL_API_SECRET = "z2ur43m9p9s2n8pe";

	public static String getPreviewTrackURL(String trackID) {
		trackID = trackID.substring("7digital:track:".length());
		Log.d("Slice", "[7Digital] Getting preview track for echonest #" + trackID);
		
		URL url;
		HttpURLConnection httpConnection;
		
		try {
			url = new URL("http://api.7digital.com/1.2/track/preview?trackid=" +
					trackID + "&oauth_consumer_key=" + _7DIGITAL_API_KEY);
            URLConnection connection = url.openConnection();
            httpConnection = (HttpURLConnection) connection;
		} catch (Exception e) {
            throw new RuntimeException(e);
		}
		
		return httpConnection.getURL().toExternalForm();
	}
	
	/*
	 * private URL opener
	 */
	
	static InputStream openUrl(String path) {
		URL url;
		HttpURLConnection httpConnection;
		int responseCode;
		
		try {
			url = new URL(path);
            URLConnection connection = url.openConnection();
            httpConnection = (HttpURLConnection) connection;
            responseCode = httpConnection.getResponseCode();
		} catch (Exception e) {
            throw new RuntimeException(e);
		}
		
        if (responseCode == HttpURLConnection.HTTP_OK)
	        try {
				return httpConnection.getInputStream();
			} catch (IOException e) {
			}
       	throw new RuntimeException("Invalid response from server.");
	}
}
