package exceptions;

import java.sql.SQLException;

public class CSVSavingException
extends SQLException {
	private static final long serialVersionUID = 1L;
	
	public CSVSavingException(final Throwable cause) {
		super(cause);
	}
}
