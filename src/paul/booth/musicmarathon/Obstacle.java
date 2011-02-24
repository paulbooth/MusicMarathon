package paul.booth.musicmarathon;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class Obstacle {
	public int distance, x=0, height=100, width=100;
	Drawable drawable;
	ObstacleType type;
	
	
	public Obstacle(int distance) {
		this.distance = distance;
		type = ObstacleType.NOHIT;
	}
	public Obstacle(int distance, int x) {
		this.distance = distance;
		type = ObstacleType.NOHIT;
		this.x =x;
	}
	public void setDrawable(Drawable drawable) {}
	public Obstacle(int distance, ObstacleType type) {
		this.distance = distance;
		this.type = type;
	}
	public void draw(Canvas canvas, int curdist) {
		int y = this.distance-curdist;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setARGB(0, 125, 0, 0);
		canvas.drawRect(x-width/2, y-height/2,x+width/2, y+width/2, paint);
		//drawable.setBounds(new Rect(x-width/2, y-height/2,x+width/2, y+width/2));
		//drawable.draw(canvas);
		
	}
	
}
