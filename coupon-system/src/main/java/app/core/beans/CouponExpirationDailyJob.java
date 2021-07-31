package app.core.beans;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import app.core.exceptions.CouponSystemException;
import app.core.services.DailyJobService;

/**
 * The class {@code CouponExpirationDailyJob} and its implements
 * {@code Runnable} interface.
 * 
 * <p>
 * The class {@code CouponExpirationDailyJob} is part of coupon system
 * application and its main purpose is to scan storage for available expired
 * coupons and if found - remove it and its purchase records from the storage.
 * 
 * @author Vitaly Zlobin
 *
 */
@Component
public class CouponExpirationDailyJob implements Runnable {

	private DailyJobService dailyJobService;

	private boolean quit;
	private Thread jobThread;
//	private long sleepDurationInHours = 24;

	public CouponExpirationDailyJob(DailyJobService dailyJobService) {
		this.dailyJobService = dailyJobService;
	}

	/**
	 * Sets the thread that started this runnable.
	 * 
	 * @param jobThread specified reference to the thread that started this
	 *                  runnable.
	 */
	public void setThread(Thread jobThread) {
		this.jobThread = jobThread;
	}

	/**
	 * Returns {@code Thread} that represents the thread that started this runnable.
	 * 
	 * @return thread of this runnable.
	 */
	public Thread getJobThread() {
		return jobThread;
	}

	@Override
	public void run() {
		System.out.println(" ---> Coupon expiration daily job started <--- ");

		while (!quit) {
			try {
				long deletedCoupons = dailyJobService.deleteExpiredCoupons(LocalDate.now());

				System.out.println("\n*********************************************************"
						+ "\nDaily Job Service remomoved " + deletedCoupons + " expired coupons"
						+ "\n*********************************************************\n");

				Thread.sleep(TimeUnit.DAYS.toMillis(1));

			} catch (InterruptedException e) {
				System.out.println(" >>> Job sleep interrupted!");
			} catch (CouponSystemException e1) {
				System.err.println(" >>> Daily job failed: " + e1.getMessage());
				e1.printStackTrace();
				stopDailyJob();
			} catch (Exception e2) {
				System.err.println(" >>> Daily job failed: " + e2.getMessage());
				e2.printStackTrace();
				stopDailyJob();
			}

		}

		System.out.println(" ---> Coupon expiration daily job ended <--- ");
	}

	@PostConstruct
	public void startDailyJob() {
		this.jobThread = new Thread(this);
		this.jobThread.start();
	}

	@PreDestroy
	public void stopDailyJob() {
		this.quit = true;
		this.jobThread.interrupt();
	}

}
