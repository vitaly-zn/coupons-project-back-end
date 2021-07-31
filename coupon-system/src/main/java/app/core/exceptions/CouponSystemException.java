package app.core.exceptions;

/**
 * The class {@code CouponSystemException} and its subclasses from
 * {@code Exception} class.
 * 
 * <p>
 * The class {@code CouponSystemException} and any subclasses that are not also
 * subclasses of {@linkplain RuntimeException} are <i>checked exceptions</i>
 * 
 * @author VItaly Zlobin
 *
 */
public class CouponSystemException extends Exception {

	private static final long serialVersionUID = 1L;

	public CouponSystemException() {
		super();
	}

	public CouponSystemException(String message) {
		super(message);
	}

	public CouponSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponSystemException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CouponSystemException(Throwable cause) {
		super(cause);
	}

}
