package com.pl.premier_league_bro.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pl.premier_league_bro.entity.Player;
import com.pl.premier_league_bro.exception.PlayerNotFoundException;
import com.pl.premier_league_bro.repository.PlayerRepository;

@Service 
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    public List<Player> getPlayersByTheirTeam(String teamName) {
        return playerRepository.findByTeam(teamName);
    }

    public List<Player> getPlayersByName(String searchText) {
        return playerRepository.findByPlayerContainingIgnoreCase(searchText);
    }

    public List<Player> getPlayersByPos(String searchText) {
        return playerRepository.findByPosContainingIgnoreCase(searchText);
    }

    public List<Player> getPlayersByNation(String searchText) {
        return playerRepository.findByNationContainingIgnoreCase(searchText);
    }

    public List<Player> getPlayersByTeamAndPostion(String team, String postion) {
        return playerRepository.findByTeamAndPos(team, postion);
    }

    public Player addPlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player updatePlayer(Player updatedPlayer) {
        Player playerToUpdate = playerRepository.findByPlayer(updatedPlayer.getPlayer())
                .orElseThrow(() -> new PlayerNotFoundException(
                        "Player not found with name: " + updatedPlayer.getPlayer()));

        playerToUpdate.setTeam(updatedPlayer.getTeam());
        playerToUpdate.setPos(updatedPlayer.getPos());
        playerToUpdate.setNation(updatedPlayer.getNation());

        return playerRepository.save(playerToUpdate);
    }

    @Transactional
    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }

    public List<Player> searchPlayers(String team, String name, String position, String nation) {
        return playerRepository.searchPlayers(team, name, position, nation);
    }
}