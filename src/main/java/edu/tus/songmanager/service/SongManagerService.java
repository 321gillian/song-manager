package edu.tus.songmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.tus.songmanager.dao.SongRepository;
import edu.tus.songmanager.dto.Genre;
import edu.tus.songmanager.dto.Song;
import edu.tus.songmanager.errors.ErrorMessages;
import edu.tus.songmanager.errors.SongValidator;
import edu.tus.songmanager.exceptions.SongNotFoundException;
import edu.tus.songmanager.exceptions.SongValidationException;

@Service
public class SongManagerService {
	Song song;
	
	@Autowired
	SongValidator songValidator;
	
	@Autowired
	SongRepository songRepo;
	
	public List<Song> getAllSongs() {
		return songRepo.findAll();
	}
	
	public Optional<Song> getSongById(Long id) {
		return songRepo.findById(id);
	}
	
	public List<Song> getSongsByGenre(Genre genre) {
		return songRepo.findByGenre(genre);
	}
	
	public List<Song> getSongsInBPMRange(double min, double max) {
		return songRepo.findByBPMBetween(min, max);
	}
	
	public Song createSong(Song song) throws SongValidationException {
		this.song = song;
		if (songValidator.checkEmptyFields(song)) {
			throw new SongValidationException(ErrorMessages.EMPTY_FIELDS.getMsg());
		}
		
		if (songValidator.checkStairwayDenied(song)) {
			throw new SongValidationException(ErrorMessages.STAIRWAY_DENIED.getMsg());
		}
		
		if (songValidator.isSlowSong(song)) {
			throw new SongValidationException(ErrorMessages.NO_SLOW_SONGS.getMsg());
		}
		
		if (songValidator.longerThanBohemianRhapsody(song)) {
			throw new SongValidationException(ErrorMessages.LONGER_THAN_BOHEMIAN_RHAPSODY.getMsg());
		}
		
		return songRepo.save(song);
	}
	
	public void deleteSong(Long id) throws SongNotFoundException {
		try {
			Song song = songRepo.findById(id).get();
			songRepo.delete(song);
		} catch (Exception e) {
			throw new SongNotFoundException("Song Not Found");
		}
	}
}
