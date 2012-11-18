package mustard.mapse;

import android.os.Parcel;
import android.os.Parcelable;

public class UserSettings implements Parcelable{
	private String userName;
	private String userEmail;
	private String alarm;
	private String cat;
	
	private String[] alarmArr = new String[]{"Alarm1", "Alarm2", "Alarm3", "Alarm4", "Alarm5"};
	private String[] catArr = new String[]{"Restaurants", "Gas Stations", "Shopping", "Parks", "Banks"};

	private int alarmThreshold;
	private int firstRun;
	
	//constructor
	public UserSettings(){
		userName = "YourName";
		userEmail = "email@domain.com";
		alarm = alarmArr[0];
		cat = null;
		alarmThreshold = 5;
		firstRun = 1;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getAlarm() {
		return alarm;
	}

	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}

	public int getAlarmThreshold() {
		return alarmThreshold;
	}

	public void setAlarmThreshold(int alarmThreshold) {
		this.alarmThreshold = alarmThreshold;
	}

	public int getFirstRun() {
		return firstRun;
	}

	public void setFirstRun(int firstRun) {
		this.firstRun = firstRun;
	}
	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}
	public String[] getAlarmArr() {
		return alarmArr;
	}

	public void setAlarmArr(String[] alarmArr) {
		this.alarmArr = alarmArr;
	}

	public String[] getCatArr() {
		return catArr;
	}

	public void setCatArr(String[] catArr) {
		this.catArr = catArr;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
