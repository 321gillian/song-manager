package edu.tus.songmanager.errors;

public enum ErrorMessages {
	EMPTY_FIELDS("One or more empty fields"),
	ALREADY_EXISTS("Title already exists"),
	SONG_NOT_FOUND("No song found"),
	STAIRWAY_DENIED("No Stairway! Denied"),
	NO_SLOW_SONGS("No slow sets allowed"),
	LONGER_THAN_BOHEMIAN_RHAPSODY("Longer than Bohemian Rhapsody");
	
	private String errorMessage;
	
	ErrorMessages(String errMsg){
		this.errorMessage=errMsg;
	}
	
	public String getMsg(){
		return errorMessage;
	}
}
