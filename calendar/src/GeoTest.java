import static org.junit.Assert.*;

import org.junit.Test;


public class GeoTest {

	@Test
	public void testGeoStringString() {
		Geo g = new Geo();
		assertEquals(g.getLatitude(),null);
		assertEquals(g.getLongitude(),null);
		assertEquals(g.getGeoError(), false);
		Geo gg = new Geo("21.28612","-157.82161");
		Float lat = (float) 21.28612;
		Float lon = (float) -157.82161;
		assertEquals(gg.getLatitude(), lat);
		assertEquals(gg.getLongitude(), lon);
		assertEquals(gg.getGeoError(), false);
		Geo ggg = new Geo("Kalen", "Bagano");
		assertEquals(ggg.getLatitude(),null);
		assertEquals(ggg.getLongitude(),null);
		assertEquals(ggg.getGeoError(), true);
	}

	@Test
	public void testMakeCoordinates() {
		Geo g = new Geo();
		assertEquals(g.makeCoordinates(), null);
		Geo gg = new Geo("21.28612","-157.82161");
		assertEquals(gg.makeCoordinates(),"GEO:21.28612;-157.82161");
		Geo ggg = new Geo("Kalen","Bagano");
		try {
			assertEquals(ggg.makeCoordinates(), null);
			fail("Should have thrown a NumberFormatException");
		} catch (NumberFormatException e) {
			String warning = "invalid input for coordinates";
			assertEquals("Exception message is correct", warning, e.getMessage());
		}
	}

}
