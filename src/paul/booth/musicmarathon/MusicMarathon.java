package paul.booth.musicmarathon;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import paul.booth.musicmarathon.MusicMarathonView.MusicMarathonGestureListener.MusicMarathonThread;
import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.SongParams;
import com.echonest.api.v4.Track;


public class MusicMarathon extends Activity {	
	
	/** A handle to the thread that's actually running the animation. */
    private MusicMarathonThread musicMarathonThread;

    /** A handle to the View in which the game is running. */
    private MusicMarathonView musicMarathonView;
    
    final static String API_KEY = "SNPTOEM7JQD9EGMF2";
    final static int NEW_SONG_URL = 0;

    EchoNestAPI echoNest;
    String currentSongURL;
    Boolean isNewSong = false;
    JSONObject currentSong;
    
    Thread musicPlayingThread;
    Thread musicGettingThread;

    
	
	Handler handler = new Handler() {
		public void handleMessage(Message m) {
			if (m.what == NEW_SONG_URL) {
				try {
					currentSong = new JSONObject(m.getData().getString("song"));
					currentSongURL = m.getData().getString("url");
					isNewSong = true;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				super.handleMessage(m);
			}
		}
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        setContentView(R.layout.main);
        
        musicMarathonView = (MusicMarathonView) findViewById(R.id.musicMarathon);
        musicMarathonThread = musicMarathonView.getThread();
        
        musicMarathonView.setTextView((TextView) findViewById(R.id.text));
        
        
        echoNest = new EchoNestAPI(API_KEY);        
        
        musicPlayingThread = new Thread(new Runnable() {
			MediaPlayer mp = new MediaPlayer();
 			public void run() {
 				while (true) {
 					try {
 	 					if (isNewSong) {
 	 						isNewSong = false;
 	 						update(currentSongURL);
 	 	 					Thread.sleep(10*1000); 						
 	 					}
 					}
 					catch (Throwable t) {
 						//do nothing
 					}
 				}
	        }
 			
 			public void update(String url) {
		    	mp.reset();
		    	try {
					mp.setDataSource(url);
			    	mp.prepare();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	mp.start();
 			}
		});
		musicPlayingThread.start();
        
    	musicGettingThread = new Thread(new Runnable() {
    		public void run() {
    	        Timer timer = new Timer();
    	        timer.scheduleAtFixedRate( new TimerTask() {

    	        	public void run() {
    	    			try {
	    			       float songHottness;
	    			        String url = null;
	    			        Song song = null;
	    			        Track t;
	    			        String id = null;
	    			        
	    			        while (id == null) {
	    			        	Random random= new Random();
	    			        	songHottness  = random.nextFloat();
	    			            SongParams p = new SongParams();
	    			            p.setMinSongHotttnesss(songHottness);
	    			            p.setMinDanceability(random.nextFloat()/2+.25f);
	    			            p.sortBy(SongParams.SORT_SONG_HOTTTNESSS, true);
	    			            p.addIDSpace("7digital");
	    			            try {
	    			    			List<Song> songs = echoNest.searchSongs(p);
	    			    			if (songs.isEmpty()) {
	    			    				continue;
	    			    			}
	    			    			song = songs.get(random.nextInt(songs.size()));
	    			    			t = song.getTrack("7digital");
	    			    			if (t == null) {
	    			    				continue;
	    			    			}
	    			    			id = t.getForeignID();
	    			    			if (id == null) {
	    			    				continue;
	    			    			}
	    			    			url = SevenDigitalAPI.getPreviewTrackURL(id);
	    			            } catch (EchoNestException e) {
	    			    			// TODO Auto-generated catch block
	    			    			e.printStackTrace();
	    			    		}        	
	    			        }
    	    				Bundle bundle = new Bundle();
    	    				bundle.putString("url", url);
    	    				bundle.putString("song", song.toString());
    	    			    Message message = new Message();
    	    			    message.what = NEW_SONG_URL;
    	    			    message.setData(bundle);
    	    				handler.sendMessage(message);
    	    			}
    	    			catch (Throwable t) {
    	    			}
    	        	}

    	        	}, 0, 25*1000);
    		}
    	});
    	musicGettingThread.start();


    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      /*setContentView(R.layout.myLayout);*/
    }
}