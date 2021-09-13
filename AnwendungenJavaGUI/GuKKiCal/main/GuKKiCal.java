package main;
/**
 * 
 * @author gukkdevel <br><br>
 *  
 * Die Klasse GuKKKalender enthält alle Daten für einen Kalender im iCal Format
 *
 */
public class GuKKiCal {
/*
 * Daten für die KalenderDatei
 */
	String kalendersource = null;
/**
 * Daten für das VCALENDAR- Element
 */
	String prodid = null;
	String version = null;
/**
 * Daten für das iCal-Element VTIMEZONE <br>
 * inclusive der iCal-Elemente STANDARD und DAYLIGHT
 * 
 */
	protected class VTIMEZONE {
		protected class DAYLIGHT {
			private DAYLIGHT () {}
		}
		protected class STANDARD {
			private STANDARD () {}
		}
		private VTIMEZONE () {}
	}

/**
 * Daten für das iCal-Element VEVENT
 * 
 * 
 * 
 */
	protected class VEVENT {
		private VEVENT () {}
	}
	
	public GuKKiCal(String prodid, String version) {
		this.prodid = prodid;
		this.version = version;
	}

}
