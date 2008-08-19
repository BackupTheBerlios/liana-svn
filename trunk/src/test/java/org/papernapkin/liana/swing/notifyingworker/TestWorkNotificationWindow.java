package org.papernapkin.liana.swing.notifyingworker;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A test suite to test the WorkNoticiationWindow.
 * 
 * @author pchapman
 */
public class TestWorkNotificationWindow
{
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	public void testDialog() {
		Collection<Runnable> runnables = new LinkedList<Runnable>();
		int count = computeRandom(5, 20);
		long mills;
		int type;
		for (int i = 0; i < count; i++) {
			// Between one half minute to 3 minutes
			mills = computeRandom(30000, 180000);
			type = computeRandom(0, 2);
			switch (type) {
			case 0:
				runnables.add(new TestWorkerThread(mills));
				logger.debug("NotifyingThreadWorker added.");
				break;
			case 1:
				runnables.add(new TestRunnable(mills));
				logger.debug("Runnable added.");
				break;
			default:
				runnables.add(new TestThread(mills));
				logger.debug("Thread added.");
				break;
			}
		}
		WorkNotificationWindow.showWorkNotificationWindow(null, "Work Notificaiton Window Test", runnables);
	}
	
	private class TestWorkerThread extends NotifyingWorkerThread {
		private long runmills;
		
		TestWorkerThread(long runmills) {
			this.runmills = runmills;
			logger.debug("{} created with runtime of {}", this.getName(), runmills);
		}
		
		public boolean work() {
			boolean retvalue = true;
			logger.debug("{} started", getName());
			int max = (int)runmills / 1000;
			int cur = 0;
			notifyListeners(new WorkerThreadEvent(this, max, ++cur));
			while (runmills > 0) {
				try {
					sleep(1000);
					runmills-=1000;
					if (computeRandom(0, 4) == 2) {
						logger.debug("Reporting exception");
						notifyListeners(new WorkerThreadEvent(this, new Throwable("Test Exception")));
						retvalue = false;
					} else {
						notifyListeners(new WorkerThreadEvent(this, "Mills: " + runmills));
						retvalue = true;
					}
//					logger.debug("{} Mills: {}    Status: {} of {}", new Object[]{getName(), runmills, cur, max});
					notifyListeners(new WorkerThreadEvent(this, max, ++cur));
				} catch (InterruptedException e) {}
			}
			logger.debug("{} done", getName());
			return retvalue;
		}
	}
	
	private class TestRunnable implements Runnable {
		private long runmills;
		
		TestRunnable(long runmills) {
			this.runmills = runmills;
		}
		
		public void run() {
			try {
				Thread.sleep(runmills);
			} catch (InterruptedException e) {}
		}
	}
	
	private class TestThread extends Thread {
		private long runmills;
		
		TestThread(long runmills) {
			this.runmills = runmills;
		}
		
		public void run() {
			try {
				sleep(runmills);
			} catch (InterruptedException e) {}
		}
	}

	/**
	 * The Random class used to generate random values.
	 */
	private static final Random RANDOM = new Random();

	/**
	 * Uses @see #RANDOM to calculate a random integer number.
	 * @param minValue The minimum random number to be generated.  Should be
	 *                 zero or positive and less than the maxValue parameter.
	 * @param maxValue The maximum random number to be generated.  Should be
	 *                 positive and greater than the minValue parameter.
	 * @return
	 */
	public int computeRandom(int minValue, int maxValue)
	{
		if (minValue < 0) {
			throw new IllegalArgumentException(
					"A minimum value less than zero is not supported."
				);
		} else if (maxValue < 1) {
			throw new IllegalArgumentException(
					"A maximum value less than one is not supported."
				);
		} else if (maxValue < minValue) {
			throw new IllegalArgumentException(
					"The maximum value cannot be less than the minimum value."
				);
		}
		int r = (int)(RANDOM.nextFloat() * maxValue + minValue);
		if (r < minValue || r > maxValue) {
			// Because Random returns a value between 0 and 1 inclusive
			// and because of rounding, the above calculation can
			// sometimes produce values outside the range we want.  If it
			// does, try again.  This call will be recursive until a
			// suitable value is generated.
			r = computeRandom(minValue, maxValue);
		}
		return r;
	}
}
