package edu.tus.songmanager.exceptions;

public class SongNotFoundException extends SongException {

	private static final long serialVersionUID = 1L;

	public SongNotFoundException(String message) {
		super(message);
	}
}
