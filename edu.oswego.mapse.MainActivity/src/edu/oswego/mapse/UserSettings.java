package edu.oswego.mapse;

public class UserSettings {
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
		userName = "Name";
		userEmail = "address@domain.com";
		alarm = alarmArr[0];
		alarmThreshold = 5;
		firstRun = 1;
		cat = null;
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

}
