
//writer.println("GEO:" + lat + ";" + lon);
public class Geo {
	private Float latitude;
	private Float longitude;
	private String geoCoordinates;
	private boolean geoError = false;
	
	public Geo() {
		this.latitude = null;
		this.longitude = null;
		this.geoCoordinates = null;
	}
	
	public Geo(String latitude, String longitude) {
		try {
			this.latitude = Float.valueOf(latitude);
			this.longitude = Float.valueOf(longitude);
		}
		catch (NumberFormatException e) {
			this.latitude = null;
			this.longitude = null;
			this.geoCoordinates = null;
			if (latitude.length() != 0 && longitude.length() != 0) {
				geoError = true;
			}
		}
	}
	
	public Float getLatitude() {
		return this.latitude;
	}
	
	public Float getLongitude() {
		return this.longitude;
	}
	
	public boolean setLatitude(Float latitude) {
		this.latitude = latitude;
		return true;
	}
	
	public boolean setLongitude(Float longitude) {
		this.longitude = longitude;
		return true;
	}
	
	public String makeCoordinates() {
		if (longitude != null & latitude != null) {
			String coordinates = "GEO:" + latitude + ";" + longitude;
			geoCoordinates = coordinates;
			return coordinates;
		}
		else
		{
			if (geoError) 
			{
				throw new NumberFormatException("");
			}
			return null;
		}
	}
}
 