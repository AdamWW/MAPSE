/************************************************
 * This class operates as a type of wrapper for the SpeedlimitManager class.
 * In extending the SpeedlimitManager class, we've removed the need
 * to either edit this class to implement parcelable, or create
 * a class containing a SpeedlimitManager object like a true wrapper.
 * 
 * 	This class requires a few things when instantiated;
 * 		Firstly, you MUST give it the baseContext of the instantiating activity
 * 		If you want to set the email and name when isntantiating, simply
 * 		include them in the instantiation
 * 		Example: SLMExtender slm = new SLMExtender(getBaseContext(), "name", "email");
 * 
 ****************************************************/

package mustard.mapse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.wikispeedia.speedlimit.SpeedlimitManager;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class SLMExtender extends SpeedlimitManager implements Parcelable {
	
	private String contrib_name="null";
	private String contrib_email="null";

	//Constructor, sending to the super...
	public SLMExtender(Context baseContext) {
		super(baseContext);		
	}
	//Constructor for setting strings...
	public SLMExtender(Context baseContext, String n, String e) {
		super(baseContext);	
		setContrib_name(n);
		setContrib_email(e);			
	}//end overloaded constructor
	/*********************************************************
	 * Getters & Setters for the requisite variables :)
	 */
	public String getContrib_name() {
		return contrib_name;
	}//end getContrib_name

	public void setContrib_name(String contrib_name) {
		this.contrib_name = contrib_name;
	}//end setContrib_name

	public String getContrib_email() {
		return contrib_email;
	}//end getContrib_email

	public void setContrib_email(String contrib_email) {
		this.contrib_email = contrib_email;
	}//end setContrib_email

	/**********************************************************************
	 * following methods exist for purposes of implementing Parcelable only
	 */
	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
		//always perform the default de-serialization first
	     aInputStream.defaultReadObject();
	}
	private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
		//perform the default serialization for all non-transient, non-static fields
		aOutputStream.defaultWriteObject();
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {		
	}
	
}//end SLMExtender
