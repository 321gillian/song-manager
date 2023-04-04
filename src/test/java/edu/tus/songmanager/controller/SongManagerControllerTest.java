package edu.tus.songmanager.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.tus.songmanager.errors.ErrorMessages;
import edu.tus.songmanager.errors.ErrorMessage;
import edu.tus.songmanager.dto.Genre;
import edu.tus.songmanager.dto.Song;
import edu.tus.songmanager.exceptions.SongValidationException;
import edu.tus.songmanager.service.SongManagerService;

@SpringBootTest
@AutoConfigureMockMvc
class SongManagerControllerTest {
	@Autowired
	SongManagerController songController;

	@MockBean
	SongManagerService songService;

	@Test
	public void getSongsOK() throws Exception {
		ArrayList<Song> songs = new ArrayList<Song>();
		songs.add(buildSong());
		songs.add(buildSong());
		when(songService.getAllSongs()).thenReturn(songs);
		ResponseEntity response = songController.getSongs();
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		ArrayList<Song> songsReturned = (ArrayList<Song>) response.getBody();
		assertTrue(songsReturned.equals(songs));
	}

	@Test
	public void getSongsEmpty() throws Exception {
		ArrayList<Song> songs = new ArrayList<Song>();
		when(songService.getAllSongs()).thenReturn(songs);
		ResponseEntity response = songController.getSongs();
		assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
		ArrayList<Song> songsReturned = (ArrayList<Song>) response.getBody();
		assertTrue(songsReturned.equals(songs));
	}

	@Test
	public void getSongByIdOK() throws Exception {
		Optional<Song> optSong = Optional.of(buildSong());
		when(songService.getSongById(1L)).thenReturn(optSong);
		ResponseEntity response = songController.getSongById(1L);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		Optional<Song> songFound = (Optional<Song>) response.getBody();
		assertTrue(songFound.equals(optSong));
	}

	@Test
	public void getSongByIdFail() throws Exception {
		Optional<Song> optSong = Optional.empty();
		when(songService.getSongById(1L)).thenReturn(optSong);
		ResponseEntity response = songController.getSongById(1L);
		assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
		Optional<Song> songFound = (Optional<Song>) response.getBody();
		assertTrue(songFound.equals(optSong));
	}

	@Test
	public void getSongsByGenreOK() throws Exception {
		ArrayList<Song> funkSongs = new ArrayList<Song>();
		Song funkSong1 = buildSong();
		funkSong1.setGenre(Genre.FUNK);
		Song funkSong2 = buildSong();
		funkSong2.setGenre(Genre.FUNK);
		funkSongs.add(funkSong1);
		funkSongs.add(funkSong2);
		when(songService.getSongsByGenre(Genre.FUNK)).thenReturn(funkSongs);
		ResponseEntity response = songController.getSongsByGenre(Genre.FUNK);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		ArrayList<Song> songsReturned = (ArrayList<Song>) response.getBody();
		assertTrue(songsReturned.equals(funkSongs));
	}

	@Test
	public void getSongsByGenreEmpty() throws Exception {
		ArrayList<Song> songs = new ArrayList<Song>();
		when(songService.getSongsByGenre(Genre.SOUL)).thenReturn(songs);
		ResponseEntity response = songController.getSongsByGenre(Genre.SOUL);
		assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
		ArrayList<Song> songsReturned = (ArrayList<Song>) response.getBody();
		assertTrue(songsReturned.equals(songs));
	}

	@Test
	public void getSongsForBPMRangeOK() throws Exception {
		ArrayList<Song> midTempoSongs = new ArrayList<Song>();
		Song midTempoSong1 = buildSong();
		midTempoSong1.setBPM(101);
		Song midTempoSong2 = buildSong();
		midTempoSong2.setBPM(199);
		midTempoSongs.add(midTempoSong1);
		midTempoSongs.add(midTempoSong2);
		when(songService.getSongsInBPMRange(100, 200)).thenReturn(midTempoSongs);
		ResponseEntity response = songController.getSongsForBPMRange(100, 200);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		ArrayList<Song> songsReturned = (ArrayList<Song>) response.getBody();
		assertTrue(songsReturned.equals(midTempoSongs));
	}

	@Test
	public void getSongsForBPMRangeEmpty() throws Exception {
		ArrayList<Song> songs = new ArrayList<Song>();
		when(songService.getSongsInBPMRange(200, 250)).thenReturn(songs);
		ResponseEntity response = songController.getSongsForBPMRange(200, 250);
		assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
		ArrayList<Song> songsReturned = (ArrayList<Song>) response.getBody();
		assertTrue(songsReturned.equals(songs));
	}

	@Test
	public void createSongTestSuccess() throws SongValidationException {
		Song song = buildSong();
		Song savedSong = buildSong();
		savedSong.setId(1L);
		when(songService.createSong(song)).thenReturn(savedSong);
		ResponseEntity response = songController.createSong(song);
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		Song songAdded = (Song) response.getBody();
		songAdded.getId();
		assertEquals(1L, songAdded.getId());
		assertTrue(songAdded.equals(savedSong));
	}

	@Test
	public void createSongTestFailEmptyFields() throws SongValidationException {
		Song song = buildSong();
		Song savedSong = buildSong();
		savedSong.setId(1L);
		when(songService.createSong(song)).thenThrow(new SongValidationException(ErrorMessages.EMPTY_FIELDS.getMsg()));
		ResponseEntity response = songController.createSong(song);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
		ErrorMessage errorMsg = (ErrorMessage) response.getBody();
		assertEquals(ErrorMessages.EMPTY_FIELDS.getMsg(), errorMsg.getErrorMessage());
	}

	@Test
	public void createSongTestFailStairwayDenied() throws SongValidationException {
		Song song = buildSong();
		Song savedSong = buildSong();
		savedSong.setId(1L);
		when(songService.createSong(song))
				.thenThrow(new SongValidationException(ErrorMessages.STAIRWAY_DENIED.getMsg()));
		ResponseEntity response = songController.createSong(song);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
		ErrorMessage errorMsg = (ErrorMessage) response.getBody();
		assertEquals(ErrorMessages.STAIRWAY_DENIED.getMsg(), errorMsg.getErrorMessage());
	}

	@Test
	public void createSongTestFailSlowSongs() throws SongValidationException {
		Song song = buildSong();
		Song savedSong = buildSong();
		savedSong.setId(1L);
		when(songService.createSong(song)).thenThrow(new SongValidationException(ErrorMessages.NO_SLOW_SONGS.getMsg()));
		ResponseEntity response = songController.createSong(song);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
		ErrorMessage errorMsg = (ErrorMessage) response.getBody();
		assertEquals(ErrorMessages.NO_SLOW_SONGS.getMsg(), errorMsg.getErrorMessage());
	}

	@Test
	public void createSongTestFailLongerThanBohemianRhapsody() throws SongValidationException {
		Song song = buildSong();
		Song savedSong = buildSong();
		savedSong.setId(1L);
		when(songService.createSong(song))
				.thenThrow(new SongValidationException(ErrorMessages.LONGER_THAN_BOHEMIAN_RHAPSODY.getMsg()));
		ResponseEntity response = songController.createSong(song);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
		ErrorMessage errorMsg = (ErrorMessage) response.getBody();
		assertEquals(ErrorMessages.LONGER_THAN_BOHEMIAN_RHAPSODY.getMsg(), errorMsg.getErrorMessage());
	}
	
	@Test
	public void deleteSongOK() throws Exception {
		ResponseEntity response = songController.deleteSong(1L);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody(), null);
	}

	@Test
	public void deleteSongFail() throws Exception {
		doThrow(new RuntimeException("Exception")).when(songService).deleteSong(1L);
		ResponseEntity response = songController.deleteSong(1L);
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
		assertEquals(response.getBody(), null);
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
