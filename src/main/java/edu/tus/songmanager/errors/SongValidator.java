package edu.tus.songmanager.errors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.tus.songmanager.dao.SongRepository;
import edu.tus.songmanager.dto.Song;

@Component
public class SongValidator {
	Song song;
	
	@Autowired
	SongRepository songRepo;
	
	public boolean checkEmptyFields(Song song){
		return (song.getTitle().length() == 0) || (song.getArtist().length() == 0);
	}
	
	public boolean isSlowSong(Song song) {
		return song.getBPM() < 78;
	}
	
	public boolean longerThanBohemianRhapsody(Song song) {
		return song.getDuration() > 355;
	}
	
	public boolean checkStairwayDenied(Song song) {
		return song.getTitle().toUpperCase().equals("STAIRWAY TO HEAVEN") && song.getArtist().toUpperCase().equals("LED ZEPPELIN");
	}
}
