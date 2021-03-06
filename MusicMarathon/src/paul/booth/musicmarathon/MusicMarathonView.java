package paul.booth.musicmarathon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.widget.TextView;

public class MusicMarathonView extends SurfaceView implements Callback {
	static final double DISTTOACC = .01;

	TextView statusText;
	MusicMarathonGestureListener gestureListener;
	GestureDetector gestureDetector;
	public float middleX=50;
	private double leftFingerX, leftFingerY, rightFingerX, rightFingerY, runnerDist=900, runnerSpeed=0, maxRunnerDist=1000;
	public boolean leftSide=true;
	private double lastTouchX = Double.NaN, lastTouchY;
	
	
	
	public MusicMarathonView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// register our interest in hearing about changes to our surface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		leftFingerX = 0; rightFingerX = display.getWidth();
		leftFingerY = display.getHeight();
		rightFingerY = display.getHeight();
		
		gestureListener = new MusicMarathonGestureListener(holder, context);
		gestureDetector = new GestureDetector(context, gestureListener);
		
		//sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		setFocusable(true); // make sure we get key events
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		//Toast.makeText(getContext(),"touch me toasy", Toast.LENGTH_SHORT).show();
		
		if (me.getX() < middleX) {
			leftFingerX = me.getX();
			leftFingerY = me.getY();
		} else {
			rightFingerX = me.getX();
			rightFingerY = me.getY();
		}
		Log.i("musicmarathon","I have been touched at:"+me.getX()+":"+me.getY());
		
		if ( (lastTouchX != Double.NaN) && ((me.getX() < middleX) == (lastTouchX < middleX)) ){
			runnerSpeed += (me.getY() - lastTouchY) * DISTTOACC;
			if (me.getY() - lastTouchY != 0)
				statusText.setText(""+(me.getY() - lastTouchY)+"\n"+(me.getX() < middleX));
		}
//		return gestureDetector.onTouchEvent(me);
		lastTouchX = me.getX();
		lastTouchY = me.getY();
		return true;
	}
	
	public void setTextView(TextView textView) {
		statusText = textView;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	public void surfaceCreated(SurfaceHolder holder) {
		try{
		gestureListener.thread.start();
		} catch (Exception e) {
			Log.e("musicmarathon",e.toString());
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode
		boolean retry = true;
		gestureListener.thread.isRunning = false;
		while(retry) {
			try {
				gestureListener.thread.updateRunnerThread.join();
				gestureListener.thread.join();
				
				retry = false;
			} catch (InterruptedException e) {
			}

		}
	}

	public MusicMarathonGestureListener.MusicMarathonThread getThread() {
		return gestureListener.thread;
	}

	
	
	
	public class MusicMarathonGestureListener extends GestureDetector.SimpleOnGestureListener {
		public MusicMarathonThread thread;
		
		
		public MusicMarathonGestureListener(SurfaceHolder holder, Context context) {
			
			// create thread only; it's started in surfaceCreated()
			thread = new MusicMarathonThread(holder, context, new Handler() {
				@Override
				public void handleMessage(Message m) {
					statusText.setVisibility(m.getData().getInt("viz"));
					statusText.setText(m.getData().getString("text"));
				}
			});
		}
		@Override

		public boolean onSingleTapUp(MotionEvent ev) {

			Log.d("onSingleTapUp",ev.toString());

			return true;

		}

		@Override

		public void onShowPress(MotionEvent ev) {

			Log.d("onShowPress",ev.toString());

		}

		@Override

		public void onLongPress(MotionEvent ev) {

			Log.d("onLongPress",ev.toString());

		}

		@Override

		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			Log.d("onScroll",e1.toString());
			if (e2.getX() < middleX) {
				leftFingerX = e2.getX();
				leftFingerY = e2.getY();
			} else {
				rightFingerX = e2.getX();
				rightFingerY = e2.getY();
			}
			Log.i("musicmarathon","left:"+leftSide+" middleX:"+middleX+" e1:"+e1.getX()+" e2:"+e2.getX()+" runnerDist:"+runnerDist+" runnerSpeed:"+runnerSpeed);
			if (leftSide) {
				if (e1.getX() < middleX && e2.getX() < middleX) {
					runnerSpeed += distanceY/100;
					leftSide = false;
				}
				
				
			} else {
				if (e1.getX() > middleX && e2.getX() > middleX) {
					runnerSpeed += distanceY/100;
					leftSide = true;
				}
				
			}
			return true;

		}

		@Override

		public boolean onDown(MotionEvent ev) {
			
			Log.d("onDownd",ev.toString());

			return true;

		}

		@Override

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			Log.d("d",e1.toString());

			Log.d("e2",e2.toString());

			return true;
		}


		
		class MusicMarathonThread extends Thread {
			SurfaceHolder surfaceHolder;
			Context context;
			Handler handler;
			Paint paint;
			boolean isRunning=true;
			public Thread updateRunnerThread;
			
			public MusicMarathonThread(SurfaceHolder surfaceHolder, Context context,
					Handler handler) {
				this.surfaceHolder = surfaceHolder;
				this.context = context;
				this.handler = handler;
				paint = new Paint();
				paint.setAntiAlias(true);
				updateRunnerThread = new Thread(){
					
					@Override
					public void run(){
						while(isRunning){
						updateRunner();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
					}
				};
				updateRunnerThread.start();
//				Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
//				middleX = display.getWidth()/2; 
			}
			@Override
			public void run(){
				while(isRunning) {
					Canvas c = null;
					try {
						c = surfaceHolder.lockCanvas(null);
						synchronized (surfaceHolder) {
							doDraw(c);
						}
					} finally {
						if ( c!= null){
							surfaceHolder.unlockCanvasAndPost(c);
						}
					}
				}
			
			}
			private void updateRunner() {
				runnerDist += runnerSpeed;
				runnerSpeed *= .95;
				runnerDist = (runnerDist) % maxRunnerDist;
				Log.i("musicmarathon", "runnerSpeeed:"+ runnerSpeed*100 + " dist:" + runnerDist);
			}
			private void doDraw(Canvas canvas) {
//				paint.setARGB(0, r, g, b)
//				canvas.drawPaint(new Paint())
//				
				Log.i("Debug","runnerDist:"+runnerDist+" runnerSpeed:"+runnerSpeed);
				paint.setARGB(255, 0,0, 0);
				canvas.drawPaint(paint);
				paint.setARGB(255, 120, 180, 0);
				middleX = canvas.getWidth()/2f;
				canvas.drawLine(middleX, 
						0, 
						middleX, 
						canvas.getHeight(),
						paint);
				canvas.drawCircle((float)leftFingerX, (float)leftFingerY, 20, paint);
				canvas.drawCircle((float)rightFingerX, (float)rightFingerY, 20, paint);
				paint.setARGB(255, 100,50,200);
				canvas.drawCircle(middleX, (float)(runnerDist/maxRunnerDist*canvas.getHeight()), 20, paint);
			}

		}
	}

}
