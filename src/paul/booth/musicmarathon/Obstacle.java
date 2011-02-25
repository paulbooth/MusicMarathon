package paul.booth.musicmarathon;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class Obstacle {
	public int distance=100, x=50, height=100, width=100;
	Drawable drawable;
	ObstacleType type;
	Paint paint;
	
	

	public Obstacle(int distance, int x) {
		this.distance = distance;
		type = ObstacleType.NOHIT;
		this.x =x;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setARGB(255, (int)(255*Math.random()), (int)(255*Math.random()), (int)(255*Math.random()));
	}
	public void setDrawable(Drawable drawable) {}
	public Obstacle(int distance, ObstacleType type) {
		this.distance = distance;
		this.type = type;
	}
	public Canvas draw(Canvas canvas, int curdist, double scale) {
		int y = curdist-distance;
		int width = (int)(this.width*scale);
		int height = (int)(this.height*scale);
		
		canvas.drawRect(x-width/2, y-height/2,x+width/2, y+width/2, paint);
		//drawable.setBounds(new Rect(x-width/2, y-height/2,x+width/2, y+width/2));
		//drawable.draw(canvas);
		return canvas;
	}
	
}
