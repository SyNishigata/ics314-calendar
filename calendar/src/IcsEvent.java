/**
 * Created by Sy on 4/7/2016.
 */
public class IcsEvent {

    private String ics;
    private String title;
    private String date;
    private String latitude;
    private String longitude;

    public IcsEvent(String ics, String title, String date, String latitude, String longitude) {
        this.ics = ics;
        this.title = title;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
