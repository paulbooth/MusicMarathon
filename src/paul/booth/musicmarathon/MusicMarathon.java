package paul.booth.musicmarathon;

import java.util.List;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Params;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.SongParams;

import paul.booth.musicmarathon.R;
import paul.booth.musicmarathon.MusicMarathonView.MusicMarathonGestureListener.MusicMarathonThread;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

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