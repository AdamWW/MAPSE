package org.wikispeedia.speedlimit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import com.digitalagora.utilities.Converter;
import com.digitalagora.utilities.InfoObject;
import com.digitalagora.utilities.XMLToInfoObjectLoader;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;


public class SpeedlimitManager {

	static LocationManager lm;
    static String myname, myemail;
	private static SpeedlimitListener myspeedlimitListener;	
    static int mymph, mykph;
    static int pegMph, pegKph;
    static Float myCog;
    static Float pegCog;
    static double myLatitude;
    static double pegLatitude;
   	static double myLongitude;
   	static double pegLongitude;
   	static double  myAltitude_meters;
   	static double pegAltitude_meters;
   	static String rval1;
   	static Boolean locFound=false;
    static double theSpeedLimitIs_latitude=0.0;
    static double theSpeedLimitIs_longitude=0.0;
    static double theSpeedLimitIs_cog=0.0;
    static int theSpeedLimitIs_i= -1;
    static double theSpeedLimitIsDouble=0;
    static Integer theSpeedLimitIs_Integer=0;
    static String  theCopyrightIs;
    static double min_distance=2000.0;
    static double Global_min_distanceFt=2000.0;
	private int positionChanged;
	private int timeChanged;	
	private static Timer mmTimer_submit;  
	private static Timer mmTimer_instantSL;
    final Handler mHandler_time = new Handler();
    final static Handler mHandler_distance = new Handler();
    final static Handler mHandler_submit = new Handler();
	private static double mySpeed;
    static Boolean mph_kph=true;	//not sure how to toggle this yet...
    static Boolean test=false;

    private CountDownTimer mCountDownTimer;
    static String pegName="";
	static String pegEmail="";


	public static Integer imph;
	public static Integer ikph;
	static CharSequence stuff;
	
    
	public SpeedlimitManager(Context baseContext) {

		   lm= (LocationManager) baseContext.getSystemService(Context.LOCATION_SERVICE);
					   
	  	   getMyLocation_oneTimeSetup(lm);
	}

	public void requestChanges(SpeedlimitListener speedlimitListener) {
			
		//save for later
		myspeedlimitListener= speedlimitListener;	
		
	}	

    public void deleteNearestSpeedLimitSign() {
    	deletePoint();
    }
    
    public String getVal1() {
    	return theString;
    }
    
    static Boolean lockout=false;
    
	public void submit(String name, String email, int mph, int kph) {

		lockout=true;
		
		//store new mph
		pegMph= mph;	
		pegKph= kph;
		pegName=name;
		pegEmail=email;
 		if(mph>0 && kph==0) {
 			mph_kph= true;
 		} else {
 			mph_kph= false;		//if mph and kph are both 0 thats an error. Not coded...
 		}

 		
		//restarts the submit-to-wikispeedia timer
		if(mCountDownTimer != null) {
			mCountDownTimer.cancel();
			mCountDownTimer.start();
			Log.d("TAGGGG","Library: submit subsequent click");
		} else {
			Log.d("TAGGGG","Library: submit first click");	
			//only do this on first submit-click
			getMyLocation();
			pegLatitude= myLatitude;
			pegLongitude=myLongitude;
			pegAltitude_meters= myAltitude_meters;
			pegCog= myCog;			
 			   	 		
 			mCountDownTimer = new CountDownTimer(10000, 1000) {

 				public void onTick(long millisUntilFinished) {
 					Log.d("TAGG","seconds remaining: " + millisUntilFinished / 1000);
 					mCountDownTimer.cancel();
 				}

 				public void onFinish() {
		        
 					startLongRunningOperation_submit();
 					Log.d("TAGGGG","done, After  submitting to submitToWikiSPEEDia!");
 					
 					mCountDownTimer=null;
 				}
 			};
	 		
	 		
	 		
	 		
		}
		

 		
      	//Log.d("TAGG","Try to prevent old/new fighting: clearSigns()");
      	//clearSigns();
      	//theString=  "4";

			
		  
		
	}
	

	private void getMyLocation_oneTimeSetup(LocationManager lm) {
        //float meters;
        
	 	LocationListener locationListener_distance, locationListener_time;

        locationListener_distance = new LocationListener() {
            public void onLocationChanged(Location newloc) {
               positionChanged++;
               Log.d("TAGG","positionChanged= " + Integer.toString(positionChanged));
      		   startLongRunningOperation_distance();
            }
            public void onProviderDisabled(String provider) {} 
            public void onProviderEnabled(String provider) {}
            public void onStatusChanged(String provider, int status,
            		Bundle extras) {}
        };  
        
        float meters= (float)800.0;	// .002 lat,lng is 1 miles square. which is 1609meters.
	 	//fancy way of creating one constant from other...   	 
        //double d=haversign(       35.0,        35.0,       -89.0, -89.0 + .02 );

	 	//if(mph_kph){
	 	//	meters= (float) (d * 5280/3);   //miles to meters (roughly)
	 	//} else {
	 	//	meters= (float) (d * 1000);	  //km to m		   
	 	//}	 	   
	 	
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, meters, locationListener_distance);

        
        locationListener_time = new LocationListener() {
            public void onLocationChanged(Location newloc) {
            	timeChanged++;
                Log.d("TAGG","timeChanged= " + Integer.toString(timeChanged));      
                 	
            	startLongRunningOperation_time();
            }
            public void onProviderDisabled(String provider) {} 
            public void onProviderEnabled(String provider) {}
            public void onStatusChanged(String provider, int status,
            		Bundle extras) {}
        };          
        long ms= 5000; //every 5seconds. So if you are at a red light, speed heads to zero
        //this doesnt work. It always hits at 1 second no matter what value I give for ms
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, ms, 0, locationListener_time);        
    }


    private void updateResultsInUi_time() {
    }

    private static void updateResultsInUi_distance() {
    }

    private static void updateResultsInUi_submit() {
    }
    
    // Create runnable for posting
    final Runnable mUpdateResults_time = new Runnable() {
        public void run() {
            updateResultsInUi_time();
        }
    };

    final static Runnable mUpdateResults_distance = new Runnable() {
        public void run() {
            updateResultsInUi_distance();
        }
    };

    final static Runnable mUpdateResults_submit = new Runnable() {
        public void run() {
            updateResultsInUi_submit();
        }
    };
	private static double Global_min_distance = 0;
	

    
    
    
    void startLongRunningOperation_time() {

    	
    	
        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread("startLongRunningOperation_time") {
            public void run() {
                doSomethingExpensive_time();
                mHandler_time.post(mUpdateResults_time);
            }

			private void doSomethingExpensive_time() {

				getMyLocation();
				
				doSearchThruSigns(
                  		myLatitude, myLongitude, 
                  		myCog, rval1);      
				
				instant();
            	
			}
        };
        t.start();
    }
    
    static int ngrabs;
    

    public static void startLongRunningOperation_distance() {

        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread("startLongRunningOperation_distance") {
            public void run() {
                doSomethingExpensive_distance();
                mHandler_distance.post(mUpdateResults_distance);
            }

			private void doSomethingExpensive_distance() {
			      doTranslate(rval1); 
			}
	    };
	    t.start();
    }

        		    
	
    
    private static int instant_counter=0;
	
	   public static void instant() {   	   
	   
		Log.d("TAGX","in instant= " + Integer.toString(instant_counter));
		
		//this calls Google and wikispeedia.org is charged money, so avoid calling this instant() routine!!!
		if(theSpeedLimitIs_Integer>0) {
			return;
		}
		
		//call at 1/2 minute, 2 minute, 5 minute only  (1sec each)
		instant_counter++;
		
		if( (instant_counter==(30)) || (instant_counter==(60*2)) || (instant_counter==(60*5))) {		
			Log.d("TAGX","not 30 120 300");
		} else {
			return;
		}		
		
		theString= "reverse-geocode";
		Log.d("TAGX",theString);
		
		
		StringBuffer response = new StringBuffer("");
		/*
		 *    START SIGN QUERY
		 * 
		 */
		URL url = null;
		
		String latlngString= Double.toString(myLatitude) + "," + Double.toString(myLongitude);
			
       try {        		
       	url = new URL("http://www.wikispeedia.org/speedlimit/geo.php" +		
       			"?latlng=" + latlngString);			 
       	//System.out.println(url.toString());
       } catch (MalformedURLException e) {
       	//Log.e(e.getMessage(), null);
       }

       if (url != null) {
       	try {
       		HttpURLConnection urlConn = (HttpURLConnection) url
               	.openConnection();
       		
       		//TODO I GET ANR KEY TIMEOUTS FROM THIS NEXT LINE. NOT SURE HOW TO FIX!!!!
               BufferedReader in = new BufferedReader(new InputStreamReader(
               		urlConn.getInputStream()));
               String inputLine;
               int amount=0;
               while (   ((amount++)<500) &&       ((inputLine = in.readLine()) != null)         ) {	
               	//System.out.println(inputLine);
               	response.append( inputLine );
               }       
               //Log.d("TAGG","amount= " + Integer.toString(amount));
               in.close();
               urlConn.disconnect();

       	} catch (IOException e) {
       		System.out.println(e.toString());
       	}
       }
       /*
        *     END SIGN QUERY 
        * 
        */
       
		int nlen= response.length();
		if(nlen>0) {			
			int n,m,max;
			n= response.indexOf("mph");
			m= response.indexOf("kph");
			
			max= m>n ? m : n;
			
			if ( max > 0) {
								
				stuff= response.subSequence(0, max-1);
								
				
				
				String str= stuff.toString();
				
				try {
					theSpeedLimitIs_Integer= Integer.parseInt(str.trim());
				} catch ( NumberFormatException e ) {
				}
				
				
				
		       
				
				
				myspeedlimitListener.onSpeedLimitChanged(theSpeedLimitIs_Integer, theCopyrightIs);
	        	//theString= "reverse-geo SL = " + Integer.toString(theSpeedLimitIs_Integer);	   

	        	//TODO add copyright to theString. Dont violate copyright jim
	        	  
	        	  
			}

		}				      
				      
  }//private



		
	   public static void doTranslate(
		   		String rval1) {   	   
	   
		   
		   
		StringBuffer response = new StringBuffer("");
		/*
		 *    START SIGN QUERY
		 * 
		 */
		URL url = null;
		
		//By my rough calculation, the most you can travel in 10 seconds (130kph) is 
		//      0.023  (very rough guess). So lets quadruple that and say 0.1 so we don't get too much data!
       try {        		
       	url = new URL("http://www.wikispeedia.org/a/marks_bb2.php" +
       			"?name=all" +
       			"&nelat=" + ( myLatitude + .002) +			//was 0.02
       			"&swlat=" + ( myLatitude - .002) +			//was 0.02
       			"&nelng=" + ( myLongitude + .002) +			//was 0.02
       			"&swlng=" + ( myLongitude - .002));			//was 0.02
       	System.out.println(url.toString());
       } catch (MalformedURLException e) {
       	//Log.e(e.getMessage(), null);
       }

       int amount=0;
       
       if (url != null) {
       	try {
       		HttpURLConnection urlConn = (HttpURLConnection) url
               	.openConnection();
       		
       		//TODO I GET ANR KEY TIMEOUTS FROM THIS NEXT LINE. NOT SURE HOW TO FIX!!!!
               BufferedReader in = new BufferedReader(new InputStreamReader(
               		urlConn.getInputStream()));
               String inputLine;
               //was 500
               while (   ((amount++)<1000) &&       ((inputLine = in.readLine()) != null)         ) {	
               	System.out.println(inputLine);
               	response.append( inputLine );
               }       
               //Log.d("TAGG","amount= " + Integer.toString(amount));
               in.close();
               urlConn.disconnect();

       	} catch (IOException e) {
       		System.out.println(e.toString());
       	}
       }
       
       if(amount>5) {
    	   	theString= "network down";
    		return;
       }
       
       /*
        *     END SIGN QUERY 
        * 
        */
		  
       
		String label="";
		String lat="", lng="", mph="", kph="", cog="";


		int nlen= response.length();
		if(nlen>0) {
		  // Following is the code that loads up the XML and allows us to grab each value for each station
		  XMLToInfoObjectLoader loader = new XMLToInfoObjectLoader(); // instantiate tool
		  loader.setXmlContent( response ); // set the content to be what's grabbed from the web site
		  InfoObject infoObject = loader.xmlToInfoObject();  // get it all in an object
		
		  // infoObject.writeHtmlToFile("/home/sean/infoobject.html"); // <--- By the way, this works.		
		  
		  Vector<?> allMarkersVector = infoObject.getChildrenById("marker");
		  ngrabs= allMarkersVector.size();
		  if(ngrabs>0) {
					  for( int i = 0; i < ngrabs; i++ )
					  {
						  try {  	  
							  //I am getting errors here, so put in try catch for grins...
						      InfoObject markerObject = (InfoObject)allMarkersVector.elementAt(i);
						      label = markerObject.getAttributeById("label").getFirstChild().getId();
						      lat = markerObject.getAttributeById("lat").getFirstChild().getId();
						      lng = markerObject.getAttributeById("lng").getFirstChild().getId();
						      mph = markerObject.getAttributeById("mph").getFirstChild().getId();
						      kph = markerObject.getAttributeById("kph").getFirstChild().getId();
						      cog = markerObject.getAttributeById("cog").getFirstChild().getId();
				
						  } catch (IllegalArgumentException name) {
							  	clearSigns();		
							  	theString=  "56";
						  }
						  
						  
						  
					     
					      //If no winner below, default is to show copyright here...
						  rval1= label; //new
			
					      //System.out.println("label: " + label );
					      //System.out.println("  lat: " + lat );
					      //System.out.println("  lng: " + lng );
					      //System.out.println("  mph: " + mph );
					      //System.out.println("  kph: " + kph );
					      //System.out.println("  cog: " + cog );
					      //System.out.println("================================");
			
					      double latDouble        = Double.valueOf(lat.trim()).doubleValue();
					      double lonDouble        = Double.valueOf(lng.trim()).doubleValue();			      
					      int    cogInt           = Integer.parseInt(cog);
					      int    mphInt           = Integer.parseInt(mph);
					      int    kphInt           = Integer.parseInt(kph);
			
					      Signs mySign = new Signs();
					      mySign.cog= Integer.parseInt(cog.trim()); 
					      mySign.lat= latDouble;
					      mySign.lng= lonDouble;
					      mySign.cog= cogInt;
					      mySign.mph= mphInt;
					      mySign.kph= kphInt;
					      mySign.label= label;
						  try {  	  
							  signs.add(mySign);
						  } catch (IllegalArgumentException nsme) {
							  	/* failure */
							  	//Log.d("TAGG","ERR: cant add to signs List, clearing it. That may help. Not sure if this is best...");
							  	clearSigns();		
							  	theString=  "5";
						  }
					  }
		  	}
		}	  
		

		//To make it cleaner, only doing this in _time, not _distance
		//doSearchThruSigns(
		//	   		myLatitude, myLongitude, myCog, rval1);   

		
		
  }//private


	   

    public static void getMyLocation() {

 	   if(lm != null){
 		   Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
 		   if (loc != null) {
 	       		
				if(mph_kph) {
 	       		   mySpeed= loc.getSpeed() * 2.23693629;
 			   } else {
 				   mySpeed= loc.getSpeed() * 3.6;	//1 meter per second = 3.6 kilometers per hour
 			   }
 			   			   

 			   myLatitude= loc.getLatitude();
 	       	   myLongitude= loc.getLongitude();	       	   
 	       	   myAltitude_meters= loc.getAltitude();	       	   
 	       	   myCog= loc.getBearing();	       	   

 	       	   locFound= true;
 	       	   
 			   if(test) {
 				   Log.d("TAGG","TEST!!!!!!!!!!!!!");
 			       mySpeed= 100;
 			       myAltitude_meters= 8527.12;
 			   }

 			   
 			   
 		   } else {
 		   		locFound= false;  //if false, dont trust the mySpeed, etc from above... Used as a lockout since I am a terrible hack!
 		   }
 		   
 	   }
 		   
    }	


			
public static void deletePoint() {

	   
	deletePointAt(theSpeedLimitIs_latitude, theSpeedLimitIs_longitude);    
	
    if(theSpeedLimitIs_i>=0) {    	
    	signs.remove(theSpeedLimitIs_i);
    	theSpeedLimitIs_i= -9;
    }

    
    
	//now delete 3 more around it.
    
    for( int j = 0; j < 3; j++ )	{
		int cog;	
		double distance;
		double min_distance=2000.0;
		double min_latDouble = 0;
		
		double min_lonDouble=0;
		double min_cogDouble = 0;
	
		double lonDouble;
		double latDouble;
		double cogDouble;
	
		int min_i = 0;
		
		int size= signs.size();
		
		if(size==0) {
			return;
		}
	
		//Log.d("TAGG","signs.size= " + Integer.toString(signs.size()));
		
	    for( int i = 0; i < size; i++ )	{
			  Signs mySign= signs.get(i);
		      latDouble= mySign.lat;
		      lonDouble = mySign.lng;
		      cog= mySign.cog;
	    	  distance = Converter.computeLatLongDistance(latDouble, lonDouble, theSpeedLimitIs_latitude , theSpeedLimitIs_longitude);
	    	  cogDouble= (double)cog;
	          if(distance < min_distance) {
	        	  min_distance= distance;
	        	  min_latDouble= latDouble;
	        	  min_lonDouble= lonDouble;
	        	  min_cogDouble= cogDouble;
	        	  min_i=i;
	          }
		  }
	    
	      min_distanceFt= (int)(min_distance * 364320);	 //69*5280= 364,320
	    
		  if((min_distanceFt<1000.0) & (cogDiff(theSpeedLimitIs_cog, min_cogDouble) < (double)45.0)) {
			  //declare a winner     	
	    	  deletePointAt(min_latDouble, min_lonDouble);
	
	    	  Log.d("TAGGGGG","Before "+Integer.toString(signs.size()));
	    	  signs.remove(min_i);
	    	  Log.d("TAGGGGG","                After  "+Integer.toString(signs.size()));
	    	  
		  }
    }	
	
	
}
	


	   public static void deletePointAt(double pointLat, double pointLon) {
    	  
  	   
		   //ck for garbage, cases to ignore...
		   if(pointLat>=0.0 && pointLat<0.000001) {
			   theString= "pointLat=0  delete-IGNORED";
			   return;
		   }
  	   
  	   
		   StringBuffer response = new StringBuffer("");

			/*
			 *    START SIGN QUERY
			 * 
			 */
			URL url = null;

			//crude way to identify point, but I hate to resort to using the "index"....
	        try {
	        	url = new URL("http://www.wikispeedia.org/a/delete_bb2.php" +
	        			"?name=all" +
	        			"&nelat=" + ( pointLat + .00001) +			
	        			"&swlat=" + ( pointLat - .00001) +			
	        			"&nelng=" + ( pointLon + .00001) +			
	        			"&swlng=" + ( pointLon - .00001));		
	        	//System.out.println(url.toString());
	        } catch (MalformedURLException e) {
	        	//Log.e(e.getMessage(), null);
	        }

	        if (url != null) {
	        	try {
	        		HttpURLConnection urlConn = (HttpURLConnection) url
	                	.openConnection();
	                BufferedReader in = new BufferedReader(new InputStreamReader(
	                		urlConn.getInputStream()));
	                String inputLine;
	                while ((inputLine = in.readLine()) != null) {	
	                	//System.out.println(inputLine);
	                	response.append( inputLine );
	                }       
	                in.close();
	                urlConn.disconnect();
	        	} catch (IOException e) {
	        		System.out.println(e.toString());
	        	}
	        }
	        
	        /*
	         *     END SIGN QUERY 
	         * 
	         */			
	        
			String label="";
			String lat="", lng="", mph="", kph="", cog="";
						        
			int nlen= response.length();
			if(nlen>0) {
			  // Following is the code that loads up the XML and allows us to grab each value for each station
			  XMLToInfoObjectLoader loader = new XMLToInfoObjectLoader(); // instantiate tool
			  loader.setXmlContent( response ); // set the content to be what's grabbed from the web site
			  InfoObject infoObject = loader.xmlToInfoObject();  // get it all in an object
			
			  Vector allMarkersVector = infoObject.getChildrenById("marker");
			  for( int i = 0; i < allMarkersVector.size(); i++ )
			  {
			      InfoObject markerObject = (InfoObject)allMarkersVector.elementAt(i);
			      label = markerObject.getAttributeById("label").getFirstChild().getId();
			      lat = markerObject.getAttributeById("lat").getFirstChild().getId();
			      lng = markerObject.getAttributeById("lng").getFirstChild().getId();
			      mph = markerObject.getAttributeById("mph").getFirstChild().getId();
			      kph = markerObject.getAttributeById("kph").getFirstChild().getId();
			      cog = markerObject.getAttributeById("cog").getFirstChild().getId();
			  }//vectors				  
			}//ifresponse>0		   			

			zeroOutEverything();
  	  	
	   }

	   
	   public static void zeroOutEverything() {
			//fake out everything in sight to ignore the DELETED sign...
			Global_min_distanceFt= 1000.0;		//was 2000.0;
		   
	   }

	   public static void clearSigns(int tagval) {

		   int n= signs.size();
		   
		   
		   signs.clear();
		   theString= "7";
		   
	   } 
		  
	   
	   public static void clearSigns() {

		   int n= signs.size();
		   int nn;
		   
		   signs.clear();
		   
			   
		  
		   //Log.d("TAGG","All " + Integer.toString(n) + " signs deleted. ");

		   nn= signs.size();
		   
		   //Log.d("TAGG", Integer.toString(nn) + " signs remaining after clearing them all....");
	   }
	   

	   static int min_distanceFt=0;
	   
	   public static int min_distanceFt() {
		   //gives distance to closest

		   return min_distanceFt;
	   }
	   

	   /** Queue of Speed Limit Signs */
	   static ArrayList<Signs> signs = new ArrayList<Signs>();

	   static double old_min_latDouble= 99;
	   static String theString="";
	   
	   
	   public static void doSearchThruSigns(
			   		double myLatitude, double myLongitude, double myCog, String rval1) {   
		   
			int mph=0, kph=0, cog=0;	
			double distance=0.0;
			double min_distance=1000.0;	//was 2000.0;
			double min_latDouble= 0.0;
			int    min_i = 0;
			double min_lonDouble= 0.0;
			double min_cogDouble= 0.0;
			double min_theSpeedLimitIsDouble=0;
			String min_copyright = null;
			double lonDouble=0.0;
			double latDouble=0.0;
			double cogDouble=0.0;
			String copyright;
			        	
			int size= signs.size();
			
			if(size==0) {
				return;
			}

			//Log.d("TAGG","signs.size= " + Integer.toString(signs.size()));
			
		    for( int i = 0; i < size; i++ )	{
				  Signs mySign= signs.get(i);
			      latDouble= mySign.lat;
			      lonDouble = mySign.lng;
			      mph= mySign.mph;
			      kph= mySign.kph;
			      cog= mySign.cog;
				  copyright= mySign.label;
			      double speedlimitDouble= 0.0;
			      if(mph_kph == true) {
				      speedlimitDouble = (double)mph;
			      } else {
		    		  speedlimitDouble = (double)kph;
			      }
			      if(speedlimitDouble<1.0) continue;
	        	  distance = Converter.computeLatLongDistance(latDouble, lonDouble, myLatitude , myLongitude);
	        	  cogDouble= (double)cog;
		          //was if(distance < min_distance) {
		          if((distance < min_distance)   &&  (cogDiff(myCog, cogDouble) < (double)45.0)) {		          
		        	  min_theSpeedLimitIsDouble= speedlimitDouble;
		        	  min_distance= distance;
		        	  min_latDouble= latDouble;
		        	  min_lonDouble= lonDouble;
		        	  min_copyright= copyright;
		        	  min_cogDouble= cogDouble;
		        	  min_i= i;
		          }
			  }//for size()
    
		    
		    
		      min_distanceFt= (int)(min_distance * 364320);	 //69*5280= 364,320
		      
		      //if( Math.abs(old_min_latDouble - min_latDouble) > 0.001 ) {
		    	//  theString=   
	    		//	  Integer.toString((int)(min_latDouble*1000.0)) 	+ "L " + 
	    		//	  Integer.toString((int)min_theSpeedLimitIsDouble)  + "S " + 
	    		//	  Integer.toString((int)min_cogDouble)          	+ "C " + 
	    	  	//	  Integer.toString(min_distanceFt)                  + "Ft "+
	    	  	//	  Integer.toString(size)                            + "ns "+
		    	 // 	  Integer.toString(ngrabs)                          + "ng ";
		    	 // 
		    	  //Log.d("TAGGGGG",  theString);
		      //}
 	          old_min_latDouble= min_latDouble;
		    
		    
		    //At 60 MPH you would travel 88 feet per second. 5 x 88 = 440 feet in five seconds.
 	          
		    
			  //watch out. This is my magic constant... Should be DEFed but I am lazy...
			  //was if((min_distanceFt<880.0) & (cogDiff(myCog, min_cogDouble) < (double)45.0)) {
			  if(min_distance<1000.0) {	
					  if(min_distance<= Global_min_distance) {
						  Global_min_distance= min_distance;
			        	  //int d= (int)((min_distance*69*5280)/100)*100;
			              //Log.d("TAGG","Global.rval1= " + rval1);
					  } else {
						  //declare a winner
						  Global_min_distance= 1000.0;
		    	    	  theSpeedLimitIs_Integer=  (int) min_theSpeedLimitIsDouble;
		        		  theCopyrightIs= min_copyright; 
		        		  theSpeedLimitIs_latitude= min_latDouble;
		    	    	  theSpeedLimitIs_longitude= min_lonDouble;
		        		  theSpeedLimitIs_cog= min_cogDouble;
						  theSpeedLimitIs_i= min_i; 
						  //Log.d("TAGG","Declared a winner. SL= " + Integer.toString((int)theSpeedLimitIsDouble) + " " + 
						  //	  Double.toString(theSpeedLimitIs_latitude) + " " + 
						  //      Double.toString(theSpeedLimitIs_longitude));		     	
			        	  if(lockout) {
			        		  //theString="Lockout: not changing speed limit right now";
	    		    	  } else {
	        				  myspeedlimitListener.onSpeedLimitChanged(theSpeedLimitIs_Integer, theCopyrightIs);
		        			  //theString= "Library: dip SL = " + Integer.toString(theSpeedLimitIs_Integer);	   
	        	  		  }
      			  		  Log.d("TAGGGGG",theString);
      			  	  }
			  }
	   }
	 		  	 		  	 	  

	   //computes angle difference using two course-over-ground's in degrees and also returning degree difference 0...360-1
	   public static double cogDiff(double cog1, double cog2) {

		   //code borrowed from my half/angle stuff, 
		   //   so compute half-angles here
		   double cogByTwo_x= cog1 / 2.0;
		   double cogByTwo  = cog2 / 2.0;
		   double i16;
		   double diff;
		   
			//simple algorithm to compute angle error.
		   
		   if(cogByTwo_x >= cogByTwo) {
			   i16= (cogByTwo_x - cogByTwo);
		   } else {
			   i16= (cogByTwo   - cogByTwo_x);
	   	   }
	   	
			if(i16 > 90.0) {
				if(i16 >= 180.0) {
					i16= i16 - 180.0;
				} else {
					i16= 180.0 - i16;
				}
			}

			//convert half-difference into full-difference [0-359]
			diff= i16 * 2.0;

			//Log.d("TAG", "angle: cog1 cog1 diff= " + Double.toString(cog1) + " " + Double.toString(cog2) + " " + Double.toString(diff) );
			
			return diff;	//difference in degrees
	   }
	     
	

	    public static  Object doSomethingExpensive_submitAndOrDelete() {
	 	   	
	 	   	//Log.d("TAGG","submitAndOrDelete"); 	
 	       
 	        //getMyLocation();
 	       

 	        //trick to sense if they are really trying to delete point instead of submitting one...
 	        if(pegMph>0 || pegKph>0) { 		    	  
 		      
		      	//first delete any close point. 
		      	//If you are setting a point, chances are the last winner is the one you want deleted.
		      	//Only delete it if its close to where you are right now, otherwise leave it alone.
				    double miles= haversign(pegLatitude,  theSpeedLimitIs_latitude,
				    						pegLongitude, theSpeedLimitIs_longitude);			                   	
				    //Log.d("TAGG","miles= " + Double.toString(miles));
		      	if(miles < 0.25) {
		 	        deletePoint(); //this is inside this thread, so its alright here.
		      	} else {
		      		//Log.d("TAGG","no old winner, so we dont delete anthing ");
		      	}
		      	//submit sign to wikispeedia
		      	doSomethingExpensive_submit();
	   
		      	//Log.d("TAGG","clearSigns()");
	 	        //clearSigns();
	 	        //theString=  "2";
	 	       
		      	//Log.d("TAGG","Force query so user doesn't get mad and resubmit existing signs.");
	 	        startLongRunningOperation_distance();  //misnamed: actually gets signs[]
 	        
 	        } else {
 		
	 			Log.d("TAGG","We reduced speed to zero!!, so DELETE last sign...");
		        deletePoint(); //this is inside this thread, so its alright here.
	
		        //Log.d("TAGG","clearSigns()");
	 	        //clearSigns();
	 	        //theString=  "3";

		      	//Log.d("TAGG","Force query so user doesn't get mad and resubmit existing signs.");
	 	        startLongRunningOperation_distance();  //misnamed: actually gets signs[]

 	        }
	      	    
 	        lockout=false;
 	        
			return null;
 		      
	 	}
	    


		public static Object doSomethingExpensive_submit() {
			
			if(locFound==false) {
				Log.d("TAGG","loc not found, returning");
				return null;
			}
		
	   	   final int  myDummy= 69;   //goofy necessary flag that wikispeedia uses
	       String speed;
	        
	       if(mph_kph) {
	    	   speed= Integer.toString(pegMph);               
	       } else {
	    	   speed= Integer.toString(pegKph);
	       }
	       
        	
            String latString = Double.toString(pegLatitude);
            String lonString = Double.toString(pegLongitude);
            String alt_metersString = Double.toString(pegAltitude_meters);
            String dummy=      Integer.toString(myDummy);
        	String direction=  Float.toString(pegCog);
        	String hours=      "";
        	
        	//instead of clearsigns, insert directly in the table.
		    Signs mySign = new Signs();		      
		    mySign.lat= pegLatitude;
		    mySign.lng= pegLongitude;
		    mySign.cog= Math.round(pegCog);
		    mySign.mph= pegMph;
		    mySign.kph= pegKph;
		    mySign.label= pegName;
			try {  	  
				signs.add(mySign);
			} catch (IllegalArgumentException nsme) {
				/* failure */
				//Log.d("TAGG","ERR: cant add to signs List, clearing it. That may help. Not sure if this is best...");
				clearSigns();		
				theString=  "5";
			}
			  
        	
        	

        	//before http, set off a failsafe timer to end it all so we dont get ANR hangs!!!
            if(true) {
	             //Log.d("TAGG","in thread: NOW 30 second timer!");
	             mmTimer_submit = new Timer("Submit_FailafeTimer");

	             TimerTask mmTimerTask = new TimerTask() {
	                 public void run() {
	                 		mmTimer_submit.cancel();
	                 		//Log.d("TAGG","Fail timeout: finishB");
	                 		//finish();
	                 }
	             };
	             
	             mmTimer_submit.schedule(mmTimerTask, 30000, 30000);             
            }         
        
            //Log.d("TAGG","after 30 sec timer set. Before http thingey");
   	        	
	         try {
	                HttpClient httpclient = new DefaultHttpClient();
	                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	                formparams.add(new BasicNameValuePair("mlat", latString));
	                formparams.add(new BasicNameValuePair("mlon", lonString));
	                formparams.add(new BasicNameValuePair("malt_meters", alt_metersString));

	                if(mph_kph == true) {
  	                   formparams.add(new BasicNameValuePair("mmph", speed));
  	                   formparams.add(new BasicNameValuePair("mkph", dummy));
	                } else {
	                   formparams.add(new BasicNameValuePair("mmph", dummy));
  	                   formparams.add(new BasicNameValuePair("mkph", speed));
	                }
	                
	                formparams.add(new BasicNameValuePair("mtag", pegName));
	                formparams.add(new BasicNameValuePair("mcog", direction));
	                formparams.add(new BasicNameValuePair("mhours", hours));
	                formparams.add(new BasicNameValuePair("memail", pegEmail));
	                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
	                HttpPost httppost = new HttpPost("http://www.wikispeedia.org/a/process_submit_bb.php");
	                httppost.setEntity(entity);
	                httpclient.execute(httppost);
	            	//System.out.println(httpclient.toString());
	            }catch (Exception e) {
	                e.printStackTrace();
	            }

	            //Log.d("TAGG","after http submit thingey");
			       

	            //Log.d("TAGG","since http finished before 30s, call timerCancel");
         		mmTimer_submit.cancel();
         		//Log.d("TAGG","Success. finishZ ");
         		//finish();
         
         		return null;
         		
		}
	    

	    public static void startLongRunningOperation_submit() {

	        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
	        Thread t = new Thread("startLongRunningOperation_submit") {
	            public void run() {
	            	Object mResults = doSomethingExpensive_submitAndOrDelete();
	                mHandler_submit.post(mUpdateResults_submit);
	            }
	        };
	        t.start();
	    }


	    
	    public static double haversign(double lat1, double lat2, double lon1, double lon2 ) {
	    	   //Haversign formula courtesy: 
	    	   //http://www.movable-type.co.uk/scripts/latlong.html
	 	   
	 	   double R = 6371; // km
	 	   double dLat = Math.toRadians(lat2-lat1); 
	 	   double dLon = Math.toRadians(lon2-lon1); 
	 	   double a1 = Math.sin(dLat/2) * Math.sin(dLat/2) +
	 	           Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
	 	           Math.sin(dLon/2) * Math.sin(dLon/2);
	 	   double c = 2 * Math.atan2(Math.sqrt(a1), Math.sqrt(1-a1));
	 	   double d = R * c;
	 	   if(mph_kph){
	 		   d= d * 0.621371192;    //miles= km * 0.62miles/km
	 	   } else {
	 		   //already in km		   
	 	   }
	 	   return d;
	    }

	    

	    	
	    


	    

}
