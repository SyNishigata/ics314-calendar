/**
 * Created by Sy on 4/7/2016.
 */
public class IcsEvent {
	
	/*
	 * Kalen note to self: the String variable "ics" is the entire content to the .ics file
	 */
    private String ics;
    private String title;
    private String date;
    private String latitude;
    private String longitude;
    //Kalen addition
    private String fileName;

    public IcsEvent(String ics, String title, String date, String latitude, String longitude, String fileName) {
        this.ics = ics;
        this.title = title;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fileName = fileName;
    }

    public String getICS() {
        return this.ics;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDate() {
        return this.date;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }
    
    //Kalen addition
    public String getFileName() {
    	return this.fileName;
    }
}
