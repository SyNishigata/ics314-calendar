import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.management.InvalidAttributeValueException;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/*
 * Created by Sy on 2/22/2016.
 */

public class Calendar implements ActionListener {

    /* Create a new Date Format without seconds */
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /* Create a new Date Format with seconds */
    DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /* Create a new Date Format without seconds or GMT */
    DateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /* Create a new Date variable recording the dateTime as soon as the program starts */
    Date date = new Date();

    /* Create a new verifier using the xVerifier.java class */
    InputVerifier numVerifier = new NumericVerifier();
    InputVerifier stringVerifier = new StringVerifier();

    /* Create a linked list to hold events for import button function*/
    List<IcsEvent> Events = new LinkedList<IcsEvent>();




    /* Input Fields */
    JTextField email = null;
    JTextField eventTitle = null;
    JTextField dateTimeStart = null;
    JTextField dateTimeEnd = null;
    JTextField location = null;
    JTextField description = null;
    JComboBox status = null;
    JTextField latitude = null;
    JTextField longitude = null;
    //@author: Ming
    JComboBox cb = null;
    //add on to try implement tzid
    JComboBox timeZones = null;

    /*@author Kalen
     * I added in this boolean to use to test whether or not the user is adding geographic coordinates. I also added a second boolean to test whether or not
     * the user made a error in entering the geographic coordinates.
     *
    boolean useGeo = false;
    boolean geoError = false;
    */

    /* Buttons */
    JButton submit = null;
    JButton importICS = null;
    JButton calculate = null;
    JButton quit = null;
    JFrame frame = null;
    JTextField newFileName = null;///k

    /* Panel */
    Panel panel = null;


    /* Constructor */
    public Calendar() {
        /* Create the text fields */
    	email = new JTextField("");
        eventTitle = new JTextField("Untitled Event");
        dateTimeStart = new JTextField(dateFormat.format(date));
        dateTimeEnd = new JTextField(dateFormat.format(date));
        location = new JTextField("University of Hawaii at Manoa, 2500 Campus Rd, " +
                "Honolulu, HI 96822, United States");
        description = new JTextField("");
        String[] statuses = {"TENTATIVE","CONFIRMED","CANCELLED"};
        status = new JComboBox(statuses);
        latitude = new JTextField("");
        longitude = new JTextField("");
        //@author: Ming
        String[] choices = { "PUBLIC","PRIVATE", "CONFIDENTIAL"};
        cb = new JComboBox(choices);
        String[] tZones = {"US/Hawaii","US/Alaska","US/Pacific","US/Mountain","US/Central","US/Eastern"};
        timeZones = new JComboBox(tZones);

        /* Create the buttons with action listeners on these objects */
        submit = new JButton("Submit data entered into new file");
        submit.addActionListener(this);
        newFileName = new JTextField("");///k
        importICS = new JButton("Import file to calculate GCD");
        importICS.addActionListener(this);
        calculate = new JButton("Calculate GCD");
        calculate.addActionListener(this);
        quit = new JButton("Quit");
        quit.addActionListener(this);

        /* Create a panel implemented in the Panel class at the bottom. */
        panel = new Panel();

        /* Create a Swing frame */
        frame = new JFrame();

        /* Setting a simple layout for the frame*/
        frame.setLayout(new GridLayout(0, 2));

        /* Verifying the fields */
        eventTitle.setInputVerifier(stringVerifier);
        latitude.setInputVerifier(numVerifier);
        longitude.setInputVerifier(numVerifier);

        /* Add all the above widgets to the frame, one after the other */
        //text fields
        frame.add(new JLabel("Email address: "));
        frame.add(email);
        frame.add(new JLabel("Event Title: "));
        frame.add(eventTitle);
        frame.add(new JLabel("Time zone (US only): "));
        frame.add(timeZones);
        frame.add(new JLabel("From: "));
        frame.add(dateTimeStart);
        frame.add(new JLabel("To: "));
        frame.add(dateTimeEnd);
        frame.add(new JLabel("Where: "));
        frame.add(location);
        frame.add(new JLabel("Latitude: "));
        frame.add(latitude);
        frame.add(new JLabel("Longitude: "));
        frame.add(longitude);
        frame.add(new JLabel("Description: "));
        frame.add(description);
        frame.add(new JLabel("Status: "));
        frame.add(status);
        //@author: Ming
        frame.add(new JLabel("Classification"));
        frame.add(cb);

        //buttons
        frame.add(importICS);
        frame.add(calculate);
        frame.add(submit);
        frame.add(newFileName);///k
        frame.add(quit);

        /* Pack then render the frame */
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Calendar();
    }


    /* Method to convert hours to Google calendar .ics file format ?GMT?(w/e timezone) = 10+ HST*/
    public int hourConversion(int hour){
        if(hour < 14){
            hour += 10;  // if am += 10
        }
        else{
            hour -= 14; // if pm -= 14, essentially +10 -24. Ex: 3pm = 15pm + 10 = 25pm - 24 = 1pm
        }
        return hour;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.quit) {
            // If the quit button is clicked, then quit
            System.exit(0);
        } else if (event.getSource() == this.submit) {
            // If the submit button is clicked, then...

            // Records dateTime when user clicks submit
            Date date2 = new Date();

            // Sets Time Zone to Google calendar default: GMT
            //dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            //dateFormat2.setTimeZone(TimeZone.getTimeZone("GMT"));

            //Submitting process begin:

            try {
                // For DTSTART
                Date startDate = dateFormat3.parse(dateTimeStart.getText());
                String startYear = dateFormat.format(startDate).substring(0, 4);
                String startMonth = dateFormat.format(startDate).substring(5, 7);
                String startDay = dateFormat.format(startDate).substring(8, 10);
                String startHour = dateFormat.format(startDate).substring(11, 13);
                String startMinute = dateFormat.format(startDate).substring(14, 16);

                // For DTEND
                Date endDate = dateFormat3.parse(dateTimeEnd.getText());
                String endYear = dateFormat.format(endDate).substring(0, 4);
                String endMonth = dateFormat.format(endDate).substring(5, 7);
                String endDay = dateFormat.format(endDate).substring(8, 10);
                String endHour = dateFormat.format(endDate).substring(11, 13);
                String endMinute = dateFormat.format(endDate).substring(14, 16);

                // For DTSTAMP ***fix this. what's the difference between this and CREATED??***
                String currentYear = dateFormat2.format(date).substring(0, 4);
                String currentMonth = dateFormat2.format(date).substring(5, 7);
                String currentDay = dateFormat2.format(date).substring(8, 10);
                String currentHour = dateFormat2.format(date).substring(11, 13);
                String currentMinute = dateFormat2.format(date).substring(14, 16);
                String currentSecond = dateFormat2.format(date).substring(17, 19);

                // For CREATED and LAST-MODIFIED ***maybe separate these two later***
                String submitYear = dateFormat2.format(date2).substring(0, 4);
                String submitMonth = dateFormat2.format(date2).substring(5, 7);
                String submitDay = dateFormat2.format(date2).substring(8, 10);
                String submitHour = dateFormat2.format(date2).substring(11, 13);
                String submitMinute = dateFormat2.format(date2).substring(14, 16);
                String submitSecond = dateFormat2.format(date2).substring(17, 19);

                
                /*
                 * implementation for version. According to my research, .ics files correspond to version 2, and .vcs files correspond to version 1, so depending
                 * on whether or not the user make the file name .ics or .vcs would determine what the version is. Of course, we've been designing this program
                 * to create a .ics file, so there may be a little (or a lot) wrong with having data we designed for a .ics file put into a .vcs file, but to
                 * go deeper than this is beyond the scope of what I believe the project is asking for, and beyond my ability to implement in a short time.
                 */
            	String s = newFileName.getText();///k
            	String filetype = s.substring(s.length() - 3, s.length());
            	String version = null;
            	if (filetype.equalsIgnoreCase("vcs")) {
            		version = "VERSION:1.0\n";
            	}
            	else {
            		version = "VERSION:2.0\n";
            	}

                // Variables to be written into .ics file
                String startText = "BEGIN:VCALENDAR\nPRODID:-//Google Inc//Google Calendar 70.9054//EN\n" +
                        version + "CALSCALE:GREGORIAN\nMETHOD:PUBLISH\n" +
                        "X-WR-CALNAME:"+ email.getText() + "\n" + 
                        "X-WR-TIMEZONE:Pacific/Honolulu\nBEGIN:VEVENT\n";

                String DTSTART = startYear + startMonth + startDay + "T" +
                        startHour + startMinute + "00Z";

                System.out.println(DTSTART);

                String DTEND = endYear + endMonth + endDay + "T" +
                        endHour + endMinute + "00";

                String DTSTAMP = currentYear + currentMonth + currentDay + "T" +
                        currentHour + currentMinute + currentSecond + "Z";
                
                /*This sets the UID to something that should be next to impossible to replicated somewhere else. The RFC suggests using the domain name
                 * along with the date time value and a unique identifer. I used the data from DTSTAMP plus the given email address as it's highly unlikely
                 * someone could create an event at the exact same second in time, and used a random integer from 0-1000 plus a random character.
                 */
                Random random = new Random();
                Integer i = random.nextInt(1001);
                Integer j = random.nextInt(26);
                int c = 'a';
                c = c + j;
                String UID = DTSTAMP + i.toString() + String.valueOf(c) + email.getText();

                String CREATED = submitYear + submitMonth + submitDay + "T" +
                        submitHour + submitMinute + submitSecond + "Z";

                String DESCRIPTION = this.description.getText();

                String LASTMODIFIED = submitYear + submitMonth + submitDay + "T" +
                        submitHour + submitMinute + submitSecond + "Z";

                String LOCATION = location.getText().replaceAll(",", "\\\\,");
                
                //@author: Ming
                //Getting choice of CLASSIFICATION
                String choice = (String) cb.getSelectedItem();

                String timeZone = (String) timeZones.getSelectedItem();

                //***should be zero. Sequence is incremented when changes are made to a existing event in iCal application.***
                String SEQUENCE = "0";

                /* @author Kalen
                 * Status is used for group events to indicate whether the event is Confirmed (definitely happening), tentative (might not happen), or
                 * cancelled (not happening).
                 */
                String STATUS = (String) status.getSelectedItem();
                
                /* Note:this is the old thing for status. I'm leaving it here in case I want to see it again.
                if (!(STATUS.equalsIgnoreCase("Tentative") || STATUS.equalsIgnoreCase("Confirmed") || STATUS.equalsIgnoreCase("Cancelled"))) {
                	throw new InvalidAttributeValueException("wrong");
                }
                STATUS = STATUS.toUpperCase();
                */
                
                
                /*@author Kalen
                 * This block of code is for my implementation of the geographic coordinate thing. First it gets the text from the latitude and longitude
                 * input. It checks whether or not the length of either is 0, which means the user didn't enter anything, and sets a the boolean useGeo
                 * accordingly. This boolean is used to determine whether or not the code for adding the geo coordinates will be used. If the user did enter
                 * something, it tests to see whether or not it is valid input and sets another boolean, geoError, accordingly. If the input was invalid, the 
                 * block catches it, sets the geoError boolean to true, and throws another exception to be caught by the main catch block for the program.
                 *
                String lat = latitude.getText();
                String lon = longitude.getText();
                Float latAsFloat = null;
                Float lonAsFloat = null;
                if (lat.length() > 0 && lon.length() > 0) {
                	useGeo = true;
                }
                if (useGeo) {
					try {
						latAsFloat = Float.valueOf(lat);
						lonAsFloat = Float.valueOf(lon);
					} catch (NumberFormatException e) {
						geoError = true;
						throw new NumberFormatException("");
					}
				}*/
                String lat = latitude.getText();
                String lon = longitude.getText();
                Geo g = new Geo(lat,lon);
                String coordinates = g.makeCoordinates();
                
                
				String SUMMARY = this.eventTitle.getText();

                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(s);///k
                    writer.print(startText);
                    System.out.println(DTSTART);
                    writer.println("DTSTART;TZID=" + timeZone + ":" + DTSTART);
                    System.out.println(DTSTART);
                    writer.println("DTEND;TZID=" + timeZone + ":" + DTEND);
                    writer.println("DTSTAMP:" + DTSTAMP);
                    writer.println("UID:" + UID);
                    writer.println("CREATED:" + CREATED);
                    writer.println("DESCRIPTION:" + DESCRIPTION);
                    writer.println("LAST-MODIFIED:" + LASTMODIFIED);
                    writer.println("LOCATION:" + LOCATION);
                    
                    /*@author: Kalen
                     * if user entered in valid coordinates, then the program will write the geo coordinate data. If the user didn't input anything,
                     * it won't be used.
                     *
                    if (geoError == false && useGeo == true) {
                    	writer.println("GEO:" + lat + ";" + lon);
                    }*/
                    if (coordinates != null) {
                    	writer.println(coordinates);
                    }
                    
                    //@author: Ming
                    writer.println("CLASS:" + choice); //Adding CLASS (classification) to the .ics
                    writer.println("SEQUENCE:" + SEQUENCE);
                    writer.println("STATUS:" + STATUS);
                    writer.println("SUMMARY:" + SUMMARY);
                    writer.println("TRANSP:OPAQUE\n" +
                            "END:VEVENT\n" +
                            "END:VCALENDAR");
                    writer.close();
                    System.out.println("Submitted.");
                } catch (Exception e) {
                    System.out.println("Error!");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println("Reading text field error");
                
                /*@author Kalen
                 * If the user inputted something that was invalid, the geo coordinate code block will set geoError to true, so the user knows that was
                 * the source of the error.
                 *
                if (geoError == true) {
                	System.out.println("Not valid geographic coordinates");
                }*/
            }
			newFileName.setText("");
			email.setText("");
        }
        else if (event.getSource() == this.importICS) {
          // If the import button is clicked, then...
          JFileChooser importFile = new JFileChooser();
          int value = importFile.showOpenDialog(this.frame);
          if (value == JFileChooser.APPROVE_OPTION) {
            String path = importFile.getSelectedFile().getAbsolutePath();

            try {
              BufferedReader br = new BufferedReader(new FileReader(path));
              if (br.ready()) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                String title = "SUMMARY";
                String date = "DTSTART";
                String geo = "GEO";
                String userICS = "", userTitle = "", userDate = "", userLatitude = "", userLongitude = "";

                while (line != null) {
                  sb.append(line);
                  sb.append("\n");
                  if (line.contains(title)) {
                    userTitle = line.substring(8, line.length());
                    // System.out.println(userTitle);
                  }
                  if (line.contains(date)) {
                    userDate = line.substring(8, line.length());
                    // System.out.println(userDate);
                  }
                  if (line.contains(geo)) {
                    String[] coordinates = line.split(";");
                    userLatitude = coordinates[0].substring(4);
                    // System.out.println(userLatitude);
                    userLongitude = coordinates[1];
                    // System.out.println(userLongitude);
                  }
                  line = br.readLine();
                }

                userICS = sb.toString();
                IcsEvent userEvent = new IcsEvent(userICS, userTitle, userDate, userLatitude, userLongitude, path);

                if (!(userDate.equals(""))) {
                  Events.add(userEvent);
                  Collections.sort(Events, new Comparator<IcsEvent>() {
                    @Override
                    public int compare(IcsEvent e1, IcsEvent e2) {
                      return e1.getDate().compareTo(e2.getDate());
                    }
                  });
                }
                else {
                  System.err.println("This file does not have a datetime specified");
                }

                br.close();

                System.out.println("List of imported events sorted by datetime: ");
                for (IcsEvent Event : Events) {
                  System.out.println(Event.getTitle());
                }
              }
            }
            catch (FileNotFoundException e) {
              e.printStackTrace();
            }
            catch (IOException e) {
              e.printStackTrace();
            }
          }
          /*else if(value == JFileChooser.CANCEL_OPTION){
            JOptionPane.showMessageDialog(null, "You did not select any files");
          }*/

        
        }
        else if (event.getSource() == this.calculate) {
            String gcd = "";
            if(Events.size() > 1){
                System.out.println("Calculating Great Circle Distances");
                for (int i = 1; i < Events.size() + 1; i++) {
                    IcsEvent e1 = Events.get(i - 1);


                    if(i == Events.size()){
                        gcd = "COMMENT:Great Circle Distance cannot be calculated because this event does not" +
                                " have any events following it (for the date specified) ";
                    }
                    else {
                        IcsEvent e2 = Events.get(i);
                        if (!(e1.getLatitude().equals("") || e1.getLongitude().equals(""))) {
                            if (!(e2.getLatitude().equals("") || e2.getLongitude().equals(""))) {

                                /* This code is from: http://introcs.cs.princeton.edu/java/12types/GreatCircle.java.html */
                                double x1 = Math.toRadians(Double.parseDouble(e1.getLatitude()));
                                double y1 = Math.toRadians(Double.parseDouble(e1.getLongitude()));
                                double x2 = Math.toRadians(Double.parseDouble(e2.getLatitude()));
                                double y2 = Math.toRadians(Double.parseDouble(e2.getLongitude()));

                                // great circle distance in radians
                                double angle1 = Math.acos(Math.sin(x1) * Math.sin(x2)
                                        + Math.cos(x1) * Math.cos(x2) * Math.cos(y1 - y2));

                                // convert back to degrees
                                angle1 = Math.toDegrees(angle1);

                                // each degree on a great circle of Earth is 60 nautical miles
                                double distance = 60 * angle1;
                                /* end code */

                                // 1 nautical mile = 1.1508 statute miles
                                double distance1 = distance * 1.1508;

                                // 1 nautical mile = 1.852 kilometers
                                double distance2 = distance * 1.852;

                                gcd = "COMMENT:Great Circle Distance to next upcoming event = " + distance1
                                        + " statute miles or " + distance2 + " kilometers.";
                            } else {
                                gcd = "COMMENT:Great Circle Distance cannot be calculated because the" +
                                        " event following this does not have a geographic position specified ";
                            }
                        } else {
                            gcd = "COMMENT:Great Circle Distance cannot be calculated because this" +
                                    " event does not have a geographic position specified ";
                        }
                    }
                    //System.out.println(gcd);
                    //System.out.println(Events.get(i).getDate());
                    
                    /*
                     * Kalen note to self. What this block of code is doing is getting the entire content of the .ics file, and then splitting in into a
                     * String[]  of size 2, with all the content before the word "LOCATION" into the zeroth index of the array, and all the contents after the
                     * word "LOCATION" into the first index of the array. It then puts the Great Circle Distance comment in between these two strings and
                     * combines them all and writes it back out to the file.
                     */
                    String[] icsParts = e1.getICS().split("LOCATION");
                    String part1 = icsParts[0];
                    String part2 = icsParts[1];
                    String combined = part1 + gcd + "\n" + "LOCATION" + part2;
                    PrintWriter writer = null;
                    try {
                        writer = new PrintWriter(e1.getFileName());
                        writer.print(combined);
                        writer.close();
                        System.out.println("File: " + e1.getFileName() + ".ics has been modified to" +
                                " include Great Circle Distance in a COMMENT");
                    } catch (Exception e) {
                    System.out.println("Error!");
                    e.printStackTrace();
                    }
                }//end for loop that cycles through events
            }
            else{
                System.err.println("Not enough .ics events were imported.");
                System.err.println("At least 2 events are required to compute a Great Circle Distance");
            }
        }
    }

    private class Panel extends JPanel {
        static final int PANEL_WIDTH = 500;     // width of the panel in pixels
        static final int PANEL_HEIGHT = 500;    // height of the panel in pixels

        /* Constructor */
        public Panel() {
            this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        }
    }
}
