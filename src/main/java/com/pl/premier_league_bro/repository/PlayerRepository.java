package com.pl.premier_league_bro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pl.premier_league_bro.entity.Player;

// way better.. 

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {

    void deleteByName(String name);

    Optional<Player> findByName(String name);

    // In PlayerRepository.java
    List<Player> findByTeam(String team);

    List<Player> findByPosIgnoreCase(String pos);

    List<Player> findByPlayerContainingIgnoreCase(String name);

    List<Player> findByPosContainingIgnoreCase(String pos);

    List<Player> findByNationContainingIgnoreCase(String nation);

    List<Player> findByTeamAndPos(String team, String pos);

    @Query("SELECT p FROM Player p WHERE " +
            "(:team IS NULL OR p.team = :team) AND " +
            "(:name IS NULL OR LOWER(p.player) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:pos IS NULL OR LOWER(p.pos) LIKE LOWER(CONCAT('%', :pos, '%'))) AND " +
            "(:nation IS NULL OR LOWER(p.nation) LIKE LOWER(CONCAT('%', :nation, '%')))")
    List<Player> searchPlayers(String team, String name, String pos, String nation);
}
