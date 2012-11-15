package edu.oswego.mapse;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class RoadAnimator {
	
	private ViewGroup middleLine;
	private Context context;
	private Handler handler;
	private int delay;
	
	private final int DASH_HEIGHT = 40;
	private final int DASH_WIDTH = 20;
	
	public RoadAnimator(Handler handler, Context context, View middleLine, int speed){
		this.handler = handler;
		this.middleLine = (LinearLayout)middleLine;
		this.context = context;
		
		//Populate initial dashes
		addDash();
		addBlank();
		addDash();
		addBlank();
		addDash();
		addBlank();
		addDash();
		
		//Start animation loop
		start();
	}
	
	//I arbitrarily chose a delay of 30ms at 30mph.
	public void updateSpeed(int speed){
		if (speed >= 57){
			delay = 3;
		} else if (speed <= 0){
			delay = -1;
		} else {
			delay = 60 - speed;
		}
		
	}
	
	//This class is only used in the below Runnable since non-locals must be final but we want to
	//to use a non-local int variable.
	public class MyColor{
		public int val = 0;
	}
		
	private void start(){
		final MyColor color = new MyColor();
		Runnable runnable = new Runnable() {
			public void run() {
				for (;;) {
						try {
							
							if(delay == -1){
								Thread.sleep(10);
								continue;
							}
							
							Thread.sleep(delay);
								handler.post(new Runnable() {
									public void run() {
										View head = middleLine.getChildAt(0);
										ViewGroup.LayoutParams params = head.getLayoutParams();
										
										//If we have a maximum sized dash, create a new dash with height 1
										//and of opposite color. Also remove the last dash in the layout.
										if(params.height >= DASH_HEIGHT){
											View newHead = new View(context);
											ViewGroup.LayoutParams newParams = new ViewGroup.LayoutParams(DASH_WIDTH,1);
											newHead.setLayoutParams(newParams);
											
											if(color.val == 0){
												newHead.setBackgroundColor(Color.TRANSPARENT);
												color.val = 1;
											} else {
												newHead.setBackgroundColor(Color.BLACK);
												color.val = 0;
											}
											
											middleLine.addView(newHead, 0);
											middleLine.removeViewAt(middleLine.getChildCount() - 1);
											
										} else {
											params.height++;
												
													head.setLayoutParams(params);
										}
									}
								});	
							
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
				}

			}
		};
		
		new Thread(runnable).start();	
	}
	
	// Use for the initial population of dashes (adds a solid dash).
	private void addDash(){
		View v = new View(context);
		v.setBackgroundColor(Color.BLACK);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DASH_WIDTH,DASH_HEIGHT);
		v.setLayoutParams(params);
		middleLine.addView(v);
	}
	
	// Use for the initial population of dashes (adds a "blank" dash).
	private void addBlank(){
		View v = new View(context);
		v.setBackgroundColor(Color.TRANSPARENT);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DASH_WIDTH,DASH_HEIGHT);
		v.setLayoutParams(params);
		middleLine.addView(v);
	}

}
