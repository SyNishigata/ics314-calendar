/**
 * Created by Sy on 4/7/2016.
 */
public class IcsEvent {

    private String ics;
    private String title;
    private String startDate;
    private String endDate;
    private String latitude;
    private String longitude;

    public IcsEvent(String ics, String title, String sdate,String edate, String latitude, String longitude) {
        this.ics = ics;
        this.title = title;
        this.startDate = sdate;
        this.endDate = edate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getICS() {
        return this.ics;
    }

    public String getTitle() {
        return this.title;
    }

    public String getStartDate() {
        return this.startDate;
    }
    
    public String getEndDate() {
      return this.endDate;
  }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }
}
