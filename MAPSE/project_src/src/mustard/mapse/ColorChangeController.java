package mustard.mapse;

/*
 * This class is used by main activity to set an appropriate background color
 * for the current speed of the vehicle when taking into account the current
 * speed limit and the user's preferred "speeding threshold."
 */

public class ColorChangeController {
	int startColor;
	int endColor;
	int threshold; //Amount above speed limit to display maximum color.
	
	ColorChangeController(int threshold){
		this.threshold = threshold;
	}
	
	public int getColor(int speedLimit, int currentSpeed){
		int dist = speedLimit + threshold - currentSpeed;
		if(dist <= 15 && dist > 12){
			return 0xFFF2F2F2;
		} else if (dist <= 12 && dist > 10){
			return 0xFFFFE5E5;
		} else if (dist <= 10 && dist > 5){
			return 0xFFFF8888;
		} else if (dist <= 5 && dist > 0){
			return 0xFFFF2B2B;
		} else if (dist <= 0){
			return 0xFFCD0000;
		} else
			return 0xFFFFFFFF;
		
	}

}
