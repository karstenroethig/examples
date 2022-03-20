package karstenroethig.examples.springbootauthjwt.exception;

public class UnauthorizedException extends RuntimeException
{
	public UnauthorizedException(String message)
	{
		this(message, null);
	}

	public UnauthorizedException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
