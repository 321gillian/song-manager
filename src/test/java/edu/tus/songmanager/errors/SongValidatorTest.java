package edu.tus.songmanager.errors;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import edu.tus.songmanager.dto.Genre;
import edu.tus.songmanager.dto.Song;

class SongValidatorTest {
	SongValidator songValidator;
	Song song;

	@BeforeEach
	void setUp() throws Exception {
		songValidator = new SongValidator();
		song = buildSong();
	}

	@Test
	void noEmptyFieldsValid() {
		assertFalse(songValidator.checkEmptyFields(song));
	}

	@Test
	void emptyArtistFieldInvalid() {
		song.setArtist("");
		assertTrue(songValidator.checkEmptyFields(song));
	}

	@Test
	void emptyTitleFieldInvalid() {
		song.setTitle("");
		assertTrue(songValidator.checkEmptyFields(song));
	}
		
	@ParameterizedTest(name="BPM = {0} should not be ok")
	@ValueSource(doubles= {1,75,76})
	void slowSongNotAllowed(double bPM) {
		song.setBPM(bPM);
		assertTrue(songValidator.isSlowSong(song));
	}
	
	@ParameterizedTest(name="BPM = {0} should be ok")
	@ValueSource(doubles= {78,78,80})
	void mediumTempSongAllowed(double bPM) {
		song.setBPM(bPM);
		assertFalse(songValidator.isSlowSong(song));
	}
	
	@ParameterizedTest(name="Duration = {0} should not be ok")
	@ValueSource(ints= {356,357,358})
	void longerThanBohemianRhapsodyNotAllowed(int duration) {
		song.setDuration(duration);
		assertTrue(songValidator.longerThanBohemianRhapsody(song));
	}
	
	@ParameterizedTest(name="Duration = {0} should be ok")
	@ValueSource(ints= {353,354,355})
	void shorterThanBohemianRhapsodyNotAllowed(int duration) {
		song.setDuration(duration);
		assertFalse(songValidator.longerThanBohemianRhapsody(song));
	}
	
	@Test
	void notStairwayAllowed() {
		assertFalse(songValidator.checkStairwayDenied(song));
	}
	
	@Test
	void stairwayDenied() {
		song.setTitle("Stairway to heaven");
		song.setArtist("Led Zeppelin");
		assertTrue(songValidator.checkStairwayDenied(song));
	}

	Song buildSong() {
		Song song = new Song();
		song.setTitle("99 Red Balloons");
		song.setArtist("Nena");
		song.setBPM(97);
		song.setDuration(334);
		song.setGenre(Genre.POP);
		return song;
	}

}
