package edu.oswego.mapse;

import org.wikispeedia.speedlimit.SpeedlimitListener;

import edu.oswego.mapse.R;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SpeedlimitListener {
	protected PowerManager.WakeLock mWakeLock;
    private TextView currentSpeedText;
    private View background;
    private Handler handler;

    final Context context = this;

    private int currentSpeed;
    private int currentSpeedLimit;
    private int speedingThreshold;

    private ColorChangeController colorScale;
    private RoadAnimator roadAnimator;
    private AlertDialog alertDialog;
    private SLMExtender slm;
    private Button signSubmitButton;

    private boolean speedSimRunning = false;
    private boolean alerted = false;

    private SoundPool soundPool;
    private int soundID;
    boolean soundLoaded = false;

@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "Off Lock");
        this.mWakeLock.acquire();

        // Instantiate sound stuff
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
                public void onLoadComplete(SoundPool soundPool, int sampleId,
                    int status) {
                soundLoaded = true;
                }
                });
        soundID = soundPool.load(this, R.raw.sound1, 1);

        // Instantiate other stuff
        currentSpeedText = (TextView) findViewById(R.id.textView1);
        currentSpeed = 0;
        currentSpeedLimit = 35;
        speedingThreshold = 10;
        background = findViewById(R.id.mainLayout);
        handler = new Handler();
        colorScale = new ColorChangeController(speedingThreshold);
        roadAnimator = new RoadAnimator(handler, this, findViewById(R.id.middleLine), currentSpeed);

        // Build speeding alert dialog
        alertDialog = new AlertDialog.Builder(this).setTitle("Caution").setMessage("You are speeding!").setCancelable(false).create();

        // Instantiate a new manager for the wikispeedia database
        try {
            slm = new SLMExtender(getBaseContext());

            // Create a listener to notify of speedlimit changes
            SpeedlimitListener sll = new SpeedlimitListener() {
                public void onSpeedLimitChanged(Integer speedlimit,
                        String copyright) {
                    // Everything below is called if there is a new sign
                    // returned
                }
            };
            slm.requestChanges(sll);
        } catch (Exception e) {
            Log.e("Thing", e.getMessage());
        }

        startSpeedChangeSimulation(this, 68, 1, 300);

        signSubmitButton = (Button) findViewById(R.id.signSubmitButton);
        // Listening to button event
        signSubmitButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                try {
                Intent i = new Intent(MainActivity.this,
                    SignSubmitter.class);
                i.putExtra("slm", slm);
                startActivity(i);
                } catch (Exception e) {
                }
                }// end onClick
                });// end new onclick listener


        startSpeedChangeSimulation(this, 30, -1, 500);

    }

@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

@Override
    public void onResume() {
		this.mWakeLock.acquire();
        super.onResume();

    }

@Override
	public void onPause() {
		this.mWakeLock.release();
		super.onPause();
	}

@Override
	public void onDestroy() {
		this.mWakeLock.release();
	    super.onDestroy();
	}

	public void onSpeedLimitChanged(Integer speedLimit, String copyright){
		currentSpeedLimit = speedLimit;
	}

    /*
     * Simulates driving speed change by calling updateSpeed() repeatedly. Needs
     * reference to MainActivity. The delay between each speed change is in
     * milliseconds.
     * Range is the amount the speed will change by.
     * Direction is positive or negative (if you want to give -1, to speed up use 1).
     * Rate is the time between each update in milliseconds and can be though of as speed.
     */
    private void startSpeedChangeSimulation(final MainActivity activity,
            final int range, final int direction, final int rate) {
        Runnable runnable = new Runnable() {
            public void run() {
                for (;;) {
                    if (activity.speedSimRunning == true) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        activity.speedSimRunning = true;
                        break;
                    }

                }

                int speed = currentSpeed;
                for (int i = 0; i < range; i++) {
                    speed += direction;
                    final int value = speed;
                    try {
                        Thread.sleep(rate);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                            public void run() {
                            activity.updateSpeed(value);
                            }
                            });
                }
                activity.speedSimRunning = false;
            }
        };
        new Thread(runnable).start();
    }

    public void updateSpeed(int speed) {
        currentSpeed = speed;
        roadAnimator.updateSpeed(speed);
        currentSpeedText.setText(Integer.toString(speed));
        int color = colorScale.getColor(currentSpeedLimit, currentSpeed);
        background.setBackgroundColor(color);

        if (currentSpeed - currentSpeedLimit > speedingThreshold) {
            if (!alerted) {
                alerted = true;
                alertDialog.show();
                playAlertSound();
            }
        } else {
            if (alerted) {
                alerted = false;
                alertDialog.cancel();
            }
        }
    }

    /*
     * Starts new thread to play alert sound in a loop. Thread terminates when
     * alerted is set to false by updateSpeed()
     */
    private void playAlertSound() {
        Runnable runnable = new Runnable() {
            public void run() {

                AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                float actualVolume = (float) audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC);
                float maxVolume = (float) audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float volume = actualVolume / maxVolume;
                // Is the sound loaded already?

                for (;;) {
                    if (alerted) {
                        if (soundLoaded) {
                            soundPool.play(soundID, volume, volume, 1, 0, 1f);
                        }

                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        break;
                    }
                }
            }
        };
        new Thread(runnable).start();
    }

}
