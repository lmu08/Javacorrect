package exceptions;

import java.sql.SQLException;

public class ProjectCreationException
extends SQLException {
	private static final long serialVersionUID = 1L;

	public ProjectCreationException(final Throwable cause) {
		super(cause);
	}
}
