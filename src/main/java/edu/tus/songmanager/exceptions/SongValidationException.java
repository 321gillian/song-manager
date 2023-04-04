package edu.tus.songmanager.exceptions;

public class SongValidationException extends SongException {
	private static final long serialVersionUID = 1L;

	public SongValidationException(final String errorMessage) {
		super(errorMessage);
	}

}
