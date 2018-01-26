package db;

import java.sql.SQLException;

public class ProjectCreationException
extends SQLException {
	private static final long serialVersionUID = 1L;

	/**
	 * occurs when there is a problem during project creation
	 * @param cause
	 */
	public ProjectCreationException(final Throwable cause) {
		super(cause);
	}
}
