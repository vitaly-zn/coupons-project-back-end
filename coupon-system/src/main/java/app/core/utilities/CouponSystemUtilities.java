package app.core.utilities;

import java.util.List;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Customer;

public class CouponSystemUtilities {

	/**
	 * Prints given list on console log.
	 * 
	 * @param items
	 */
	public static void printList(List<?> items) {
		if (items.isEmpty())
			System.out.println(" - There are no items to display. - ");
		else {
			int index = 1;
			if (items.get(0) instanceof Company) {
				System.out.println("IM COMPANY");
				System.out.println(" - List of companies: - ");
				for (Object obj : items) {
					System.out.println("   " + index++ + ") " + obj);
					((Company) obj).printAllCoupons();
				}
			} else {
				if (items.get(0) instanceof Customer) {
					System.out.println("IM CUSTOMER");
					System.out.println(" - List of customers: - ");
					for (Object obj : items) {
						System.out.println("   " + index++ + ") " + obj);
						((Customer) obj).printAllCoupons();
					}
				} else {
					System.out.println(" - List of coupons: - ");
					for (Object obj : items) {
						System.out.println("   " + index++ + ") " + obj);
						((Coupon) obj).toString();
					}
				}
			}
		}
	}

	/**
	 * Slows execution process for better printing to control results.
	 * 
	 * @param millis
	 */
	public static void slowPrint(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			System.err.println("Coupon System Utility slowPrint() sleep interrupted!");
			e.printStackTrace();
		}
	}

}
