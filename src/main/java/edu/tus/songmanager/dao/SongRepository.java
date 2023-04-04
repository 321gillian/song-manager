package edu.tus.songmanager.dao;

import org.springframework.stereotype.Repository;

import edu.tus.songmanager.dto.Genre;
import edu.tus.songmanager.dto.Song;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

	Optional<Song> findByTitle(String title);

	List<Song> findByGenre(Genre genre);

	List<Song> findByBPMBetween(double min, double max);

}
