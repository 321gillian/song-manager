package edu.tus.songmanager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tus.songmanager.dto.Genre;
import edu.tus.songmanager.dto.Song;
import edu.tus.songmanager.errors.ErrorMessage;
import edu.tus.songmanager.errors.SongValidator;
import edu.tus.songmanager.exceptions.SongException;
import edu.tus.songmanager.service.SongManagerService;


@RestController
@Service
@RequestMapping("/songs")
public class SongManagerController {
	@Autowired
	SongValidator songValidator;
	
	@Autowired
	SongManagerService songService;
	
	@GetMapping() 
	public ResponseEntity getSongs(){
		ArrayList<Song> songs = (ArrayList<Song>) songService.getAllSongs();
		if (songs.size()==0) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(songs);
		}
		else {
			return (ResponseEntity) ResponseEntity.status(HttpStatus.OK).body(songs);
		}
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable(value = "id") Long songId) {
        Optional<Song> song = songService.getSongById(songId);
        if (song.isEmpty()) {
        	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(song);
        }
        else {
        	return ResponseEntity.status(HttpStatus.OK).body(song);
        }
     }
	
	@GetMapping("/genre/{genre}")
	public ResponseEntity<?> getSongsByGenre(@PathVariable("genre") Genre genre) {
		List<Song> songs = songService.getSongsByGenre(genre);
		if (songs.size()==0) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(songs);
		}
		else {
			return (ResponseEntity) ResponseEntity.status(HttpStatus.OK).body(songs);
		}
	}
	
	@GetMapping("/bpm/{min}/{max}")
	public ResponseEntity<?> getSongsForBPMRange(@PathVariable("min") double min, @PathVariable("max") double max) {
		List<Song> songs = songService.getSongsInBPMRange(min, max);
		if (songs.size()==0) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(songs);
		}
		else {
			return (ResponseEntity) ResponseEntity.status(HttpStatus.OK).body(songs);
		}
	}
	
	@PostMapping()
	ResponseEntity<?> createSong(@Valid @RequestBody Song song) {
		try {	
			Song savedSong = songService.createSong(song);
			return new ResponseEntity<Song>(savedSong, HttpStatus.CREATED);
		} catch(SongException e) {
			ErrorMessage errorMessage=new ErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteSong(@PathVariable("id") Long id) {
		try {
			songService.deleteSong(id);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}
