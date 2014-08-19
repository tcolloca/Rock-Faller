package model.utils;

import java.io.IOException;

public class InvalidBoardException extends IOException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidBoardException() {
		super();
	}
	
	public InvalidBoardException(String error) {
		super(error);
	}
}
