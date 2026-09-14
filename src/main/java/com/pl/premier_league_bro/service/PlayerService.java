package com.pl.premier_league_bro.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.pl.premier_league_bro.entity.Player;
import com.pl.premier_league_bro.exception.PlayerNotFoundException;
import com.pl.premier_league_bro.repository.PlayerRepository;

public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    public List<Player> getPlayersByTheirTeam(String teamName) {
        return playerRepository.findAll().stream()
                .filter(player -> teamName.equals(player.getTeam()))
                .collect(Collectors.toList());
    }

    public List<Player> getPlayersByName(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getPlayer().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Player> getPlayersByPos(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getPos().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Player> getPlayersByNation(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getNation().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Player> getPlayersByTeamAndPostion(String team, String postion) {
        return playerRepository.findAll().stream()
                .filter(player -> team.equals(player.getTeam()) && postion.equals(player.getPos()))
                .collect(Collectors.toList());
    }

    public Player addPlayer(Player player) {
        playerRepository.save(player);
        return player;
    }

    // enhanced method,  rather than just returning 'null'
    public Player updatePlayer(Player updatedPlayer) {
        Player playerToUpdate = playerRepository.findByName(updatedPlayer.getPlayer())
                .orElseThrow(() -> new PlayerNotFoundException(
                        "Player not found with name: " + updatedPlayer.getPlayer()));


        playerToUpdate.setTeam(updatedPlayer.getTeam());
        playerToUpdate.setPos(updatedPlayer.getPos());
        playerToUpdate.setNation(updatedPlayer.getNation());

        return playerRepository.save(playerToUpdate);
    }

    @Transactional 
    public void deletePlayer(Player player){
        playerRepository.delete(player);
    }

}
