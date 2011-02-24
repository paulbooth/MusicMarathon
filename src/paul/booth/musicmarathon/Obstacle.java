package paul.booth.musicmarathon;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class Obstacle {
	public int distance, x=0;
	Drawable drawable;
	ObstacleType type;
	
	public Obstacle(int distance) {
		this.distance = distance;
		type = ObstacleType.NOHIT;
	}
	
	public Obstacle(int distance, ObstacleType type) {
		this.distance = distance;
		this.type = type;
	}
	
	public void draw(Canvas c, int curdist) {
		int y = this.distance-curdist;
		
		
	}
	
}
