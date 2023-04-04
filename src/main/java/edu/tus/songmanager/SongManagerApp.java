package edu.tus.songmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SongManagerApp {
	public static void main(String[] args) {
		SpringApplication.run(SongManagerApp.class, args);
		System.out.print("Song Manager App - Server Running");		
	}
}