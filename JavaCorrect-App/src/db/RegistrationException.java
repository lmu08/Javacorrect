package db;

import java.sql.SQLException;

public class RegistrationException
extends SQLException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Error during registration
	 * @param cause
	 */
	public RegistrationException(final Throwable cause) {
		super(cause);
	}
}
