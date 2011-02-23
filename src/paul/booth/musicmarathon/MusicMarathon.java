package paul.booth.musicmarathon;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Params;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.SongParams;
import com.echonest.api.v4.Track;

import paul.booth.musicmarathon.R;
import paul.booth.musicmarathon.MusicMarathonView.MusicMarathonGestureListener.MusicMarathonThread;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MusicMarathon extends Activity {	
	
	/** A handle to the thread that's actually running the animation. */
    private MusicMarathonThread musicMarathonThread;

    /** A handle to the View in which the game is running. */
    private MusicMarathonView musicMarathonView;
    
    final static String API_KEY = "SNPTOEM7JQD9EGMF2";
    EchoNestAPI echoNest;
    String firstSongUrl;
    MediaPlayer mp;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        musicMarathonView = (MusicMarathonView) findViewById(R.id.musicMarathon);
        musicMarathonThread = musicMarathonView.getThread();
        
        musicMarathonView.setTextView((TextView) findViewById(R.id.text));
        
        
        echoNest = new EchoNestAPI(API_KEY);
        mp = new MediaPlayer();
        
        firstSongUrl = getRandomSong();
        setNewSong(firstSongUrl);
    }
    
    private void setNewSong(String url) {
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
    
    private String getRandomSong() {
        float songHottness;
        String url = null;
        Song song = null;
        Track t;
        String id = null;
        
        while (id == null) {
        	songHottness  = (float) Math.random();
            SongParams p = new SongParams();
            p.setMinSongHotttnesss(songHottness);
            p.addIDSpace("7digital");
            try {
    			List<Song> songs = echoNest.searchSongs(p);
    			if (songs.isEmpty()) {
    				continue;
    			}
    			song = songs.get(0);
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
		
		return url;
    }
}