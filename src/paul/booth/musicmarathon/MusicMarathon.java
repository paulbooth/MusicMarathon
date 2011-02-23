package paul.booth.musicmarathon;

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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        musicMarathonView = (MusicMarathonView) findViewById(R.id.fingerRunner);
        musicMarathonThread = musicMarathonView.getThread();
        
        musicMarathonView.setTextView((TextView) findViewById(R.id.text));
        
    }
    
    
}