package edu.tus.songmanager.exceptions;

public abstract class SongException extends Exception {
	private static final long serialVersionUID = 1L;

	protected SongException(final String message) {
		super(message);
	}
}
