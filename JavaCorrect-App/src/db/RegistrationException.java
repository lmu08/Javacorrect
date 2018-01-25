package db;

import java.sql.SQLException;

public class RegistrationException
extends SQLException {
	private static final long serialVersionUID = 1L;

	public RegistrationException(final Throwable cause) {
		super(cause);
	}
}
