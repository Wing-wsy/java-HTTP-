package com.annet.common.wxException;

public class RequestFailException extends RuntimeException   {
	private static final long serialVersionUID = -5423452810358016621L;

	public RequestFailException( String message ) {
		super( message);
	}

}
