package com.pl.premier_league_bro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pl.premier_league_bro.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {

    void deleteByName(String name);

    Optional<Player> findByName(String name);
}
