package edu.tus.songmanager.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import edu.tus.songmanager.dto.Genre;
import edu.tus.songmanager.dto.Song;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SongManagerAppIT {

	@Value(value = "${local.server.port}")
	private int port;

	TestRestTemplate restTemplate;
	HttpHeaders headers;

	@BeforeEach
	void setUp() throws Exception {
		restTemplate = new TestRestTemplate();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}
	

	@Test
	public void createSongSuccessIntTest() {
		HttpEntity<Song> request = new HttpEntity<Song>(buildSong(), headers);
		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/songs", request,
				String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	@Test
	public void createSongEmptyFieldsNotAllowedIntTest() {
		Song song = buildSong();
		song.setTitle("");
		HttpEntity<Song> request = new HttpEntity<Song>(song, headers);
		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/songs",
				request, String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void createSongSlowSongNotAllowedIntTest() {
		Song song = buildSong();
		song.setBPM(76);
		HttpEntity<Song> request = new HttpEntity<Song>(song, headers);
		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/songs",
				request, String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	
	@Test
	public void createSongStairwayDeniedIntTest() {
		Song song = buildSong();
		song.setTitle("Stairway to Heaven");
		song.setArtist("Led Zeppelin");
		HttpEntity<Song> request = new HttpEntity<Song>(song, headers);
		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/songs",
				request, String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void createSongNotLongerThanBohemianRhapsodyIntTest() {
		Song song = buildSong();
		song.setDuration(500);
		HttpEntity<Song> request = new HttpEntity<Song>(song, headers);
		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/songs",
				request, String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
