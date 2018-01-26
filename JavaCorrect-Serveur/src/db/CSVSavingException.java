package db;

import java.sql.SQLException;

public class CSVSavingException
extends SQLException {
	private static final long serialVersionUID = 1L;
	/**
	 * occurs when a CSV can't be saved
	 * @param cause
	 */
	public CSVSavingException(final Throwable cause) {
		super(cause);
	}
}
