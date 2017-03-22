package com.dts.DTSAPI;

public class ApplicationException extends Exception{

	private static final long serialVersionUID = -5247632606669801670L;
	private String exceptionMsg;
	private String error;

	public ApplicationException() {
		super();
	}
	
	public ApplicationException(String error) {
		super(error);
		//super.printStackTrace();
		this.error = error;
		System.out.println(error);
	}
	
	public ApplicationException(Throwable cause) {
		super(cause);
	}
	@Override
	public String toString() {
		return error;
	}
	@Override
	public String getMessage() {
		return error;
	}


}
