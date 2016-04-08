import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class EventReader
{
	//holds list of files added by user.
	public ArrayList<String> fileNames = null;
	//array that holds all geo strings.
	public String[] geos = null;
	//2 level array, first level as big as number of files read, second level only has length of 2 (latitude/longitude).
	public Float[][] geos2 = null;
	//string that holds start time lines read in.
	public String[] dtStart = null;
	//array that holds start dates of event files read.
	public Date[] startDates = null;

	
	//Default Constructor. Creates empty list of file names.
	public EventReader()
	{
		fileNames = new ArrayList<String>();
	}
	
	
	//Constructor that takes a given list of files names and uses that to create its list.
	public EventReader(ArrayList<String> fileNames)
	{
		this.fileNames = fileNames;
	}
	
	
	/*Takes list of files already in object, reads the information from them, finds the file with the Geographic coordinate info,
	 * and adds the entire geo string read to the geos array, and the corresponding coordinates to the geos2 array (first index is
	 * the event name).
	*/
	public void getGeoInformation() throws IOException 
	{
		
		geos = new String[fileNames.size()];
		geos2 = new Float[fileNames.size()][2];
		dtStart = new String[fileNames.size()];
		startDates = new Date[fileNames.size()];
		for (int i = 0 ; i < fileNames.size() ; i++)
		{
			String[] geoCoordinates = null;
			BufferedReader br = new BufferedReader (new FileReader(fileNames.get(i)));
			String line = br.readLine();
			while (line != null)
			{
				if (line.contains("GEO"))
				{
					geos[i] = line;
					geoCoordinates = line.split("[;:]");
					geos2[i][0] = Float.valueOf(geoCoordinates[1]);
					geos2[i][1] = Float.valueOf(geoCoordinates[2]);

				}
				if (line.contains("DTSTART"))
				{
					dtStart[i] = line;
					startDates[i] = getDate(line);
				}
				line = br.readLine();
			}
		}
		sortEvents();
	}
	
	
	private Date getDate(String dt)
	{
		//this separates the "DT" start from the actual datetime information.
		String[] one = dt.split("[:]");
		//this separates the date from the time and removes the Z and the end.
		String[] two = one[1].split("[TZ]");
		String date = two[0];
		String time = two[1];
		String year = date.substring(0,4);
		String month = date.substring(4,6);
		String day = date.substring(6,8);
		String hours = time.substring(0,2);
		String minutes = time.substring(2,4);
		String seconds = time.substring(4,6);
		@SuppressWarnings("deprecation")
		Date d = new Date(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(seconds));
		return d;
	}
	
	
	private void sortEvents() 
	{
		for (int i = 1 ; i < fileNames.size() ; i++ )
		{
			
			Date tempVal = startDates[i];
			String tempFile = fileNames.get(i);
			String tempGeo = geos[i];
			Float[] tempGeo2 = geos2[i];
			String tempDT = dtStart[i];
			
			int j = i;
			while ( j > 0 && startDates[j].compareTo(startDates[j - 1]) < 0 ) {
				startDates[j] = startDates[j - 1];
				startDates[j - 1] = tempVal;
				fileNames.set(j,fileNames.get(j - 1));
				fileNames.set(j - 1, tempFile);
				geos[j] = geos[j - 1];
				geos[j - 1] = tempGeo;
				geos2[j] = geos2[j - 1];
				geos2[j - 1] = tempGeo2;
				dtStart[j] = dtStart[j - 1];
				dtStart[j - 1] = tempDT;
				j--;
			}//while
		}//for
	}
	
	
	/*
	 * 
	 * self note: Issue with this when creating it at first was that you need to convert the lat/long degrees into Radians.
	 */
	public void calculateGreatCircleDistances() throws IOException
	{
		for (int i = 0 ; i < fileNames.size() - 1 ; i++)
		{
			Double latDiff = Math.toRadians(geos2[i][0]) - Math.toRadians(geos2[i + 1][0]);
			Double longDiff = Math.toRadians(geos2[i][1]) - Math.toRadians(geos2[i + 1][1]);
			Double f = (Math.sin(Math.toRadians(geos2[i][0])) * Math.sin(Math.toRadians(geos2[i + 1][0])) + (Math.cos(Math.toRadians(geos2[i][0])) * Math.cos(Math.toRadians(geos2[i + 1][0])) * Math.cos(longDiff)));
			f = Math.acos(f);
			Double earthRadiiMiles = 3959.0;
			Double earthRadiiKilo = 6371.0;
			Double greatCircleDistanceMiles = earthRadiiMiles * f;
			Double greatCircleDistanceKilo = earthRadiiKilo * f;
			System.out.println("The great circle distance in miles is " + greatCircleDistanceMiles);
			System.out.println("The great circle distance in kilometers is " + greatCircleDistanceKilo);
			
			FileWriter fw = new FileWriter(fileNames.get(i), true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			String s = "\nCOMMENT: The Great Circle Distance from " + fileNames.get(i) + " to " + fileNames.get(i + 1) + " in miles is ";
			s = s + greatCircleDistanceMiles + ", and the Great Circle Distance in kilometers is " + greatCircleDistanceKilo;
			pw.write(s);
			pw.close();
		}
	}
	
	
	/*This method just prints the name followed by the geo coordinates of all the events in its list (note that that files have
	 * to have been already read or this will probably return gibberish or throw an exception or something).
	 */
	public void printEventInfo()
	{
		for (int i = 0 ; i < fileNames.size(); i++)
		{
			System.out.println(fileNames.get(i) + "=GEO:" + geos2[i][0] + ";" + geos2[i][1] + "," + startDates[i].toString());
		}
	}
}