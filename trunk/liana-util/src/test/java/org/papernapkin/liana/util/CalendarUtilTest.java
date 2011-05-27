package org.papernapkin.liana.util;

import java.util.Calendar;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author pchapman
 */
public class CalendarUtilTest {
	@Test
	public void testGetDays() {
		Calendar cal = CalendarUtil.getWeekStart(21, 2011);
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(Calendar.MAY, cal.get(Calendar.MONTH));
		assertEquals(Calendar.SUNDAY, cal.get(Calendar.DAY_OF_WEEK));
		assertEquals(22, cal.get(Calendar.DAY_OF_MONTH));
		cal = CalendarUtil.getWeekEnd(21, 2011);
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(Calendar.MAY, cal.get(Calendar.MONTH));
		assertEquals(Calendar.SATURDAY, cal.get(Calendar.DAY_OF_WEEK));
		assertEquals(28, cal.get(Calendar.DAY_OF_MONTH));
	}

}
