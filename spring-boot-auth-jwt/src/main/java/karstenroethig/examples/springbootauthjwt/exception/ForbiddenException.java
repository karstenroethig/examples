package karstenroethig.examples.springbootauthjwt.exception;

public class ForbiddenException extends RuntimeException
{
	public ForbiddenException(String message)
	{
		this(message, null);
	}

	public ForbiddenException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
