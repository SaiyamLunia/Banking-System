package com.cg.banking.exceptions;
public class InvalidPinNumberException extends RuntimeException{
	public InvalidPinNumberException(){}

	public InvalidPinNumberException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public InvalidPinNumberException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidPinNumberException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidPinNumberException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
