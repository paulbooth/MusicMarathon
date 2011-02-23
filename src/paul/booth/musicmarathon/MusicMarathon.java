package paul.booth.musicmarathon;

import java.util.List;

import paul.booth.musicmarathon.MusicMarathonView.MusicMarathonGestureListener.MusicMarathonThread;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.SongParams;

public class MusicMarathon extends Activity {	
	
	/** A handle to the thread that's actually running the animation. */
    private MusicMarathonThread musicMarathonThread;

    /** A handle to the View in which the game is running. */
    private MusicMarathonView musicMarathonView;
    
    final static String API_KEY = "SNPTOEM7JQD9EGMF2";
    EchoNestAPI echoNest;
    Song firstSong;
	
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
        firstSong = getRandomSong(echoNest);
        String s = firstSong.getAudio();
        int x = 4;
        x +=3;
    }
    
    private Song getRandomSong(EchoNestAPI echoNest) {
        float songHottness = (float) Math.random();
        Song song = null;
        
        SongParams p = new SongParams();
        p.setMinSongHotttnesss(songHottness);
        p.setMaxSongHotttnesss(songHottness);
        try {
			List<Song> songs = echoNest.searchSongs(p);
			song = songs.get(0);
		} catch (EchoNestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return song;
    }
    
    
}