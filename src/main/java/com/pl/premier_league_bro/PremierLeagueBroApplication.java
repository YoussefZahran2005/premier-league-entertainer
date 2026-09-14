package com.pl.premier_league_bro;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.pl.premier_league_bro.repository.PlayerRepository;

@SpringBootApplication
public class PremierLeagueBroApplication {

	public static void main(String[] args) {
		SpringApplication.run(PremierLeagueBroApplication.class, args);
	}


}
