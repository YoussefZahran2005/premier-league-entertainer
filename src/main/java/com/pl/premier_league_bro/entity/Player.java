package com.pl.premier_league_bro.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "players")
public class Player {

    public Player() {
    }

    public Player(String player, String nation, String pos, BigDecimal age, Integer mp, Integer starts, BigDecimal min,
            BigDecimal gls, BigDecimal ast, BigDecimal pk, BigDecimal crdy, BigDecimal crdr, BigDecimal xg,
            BigDecimal xag, String team) {
        this.player = player;
        this.nation = nation;
        this.pos = pos;
        this.age = age;
        this.mp = mp;
        this.starts = starts;
        this.min = min;
        this.gls = gls;
        this.ast = ast;
        this.pk = pk;
        this.crdy = crdy;
        this.crdr = crdr;
        this.xg = xg;
        this.xag = xag;
        this.team = team;
    }

    @Id
    private String player;
    private String nation;
    private String pos;
    private BigDecimal age;
    private Integer mp;
    private Integer starts;
    private BigDecimal min;
    private BigDecimal gls;
    private BigDecimal ast;
    private BigDecimal pk;
    private BigDecimal crdy;
    private BigDecimal crdr;
    private BigDecimal xg;
    private BigDecimal xag;
    private String team;

    // Getters and Setters
    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public BigDecimal getAge() {
        return age;
    }

    public void setAge(BigDecimal age) {
        this.age = age;
    }

    public Integer getMp() {
        return mp;
    }

    public void setMp(Integer mp) {
        this.mp = mp;
    }

    public Integer getStarts() {
        return starts;
    }

    public void setStarts(Integer starts) {
        this.starts = starts;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getGls() {
        return gls;
    }

    public void setGls(BigDecimal gls) {
        this.gls = gls;
    }

    public BigDecimal getAst() {
        return ast;
    }

    public void setAst(BigDecimal ast) {
        this.ast = ast;
    }

    public BigDecimal getPk() {
        return pk;
    }

    public void setPk(BigDecimal pk) {
        this.pk = pk;
    }

    public BigDecimal getCrdy() {
        return crdy;
    }

    public void setCrdy(BigDecimal crdy) {
        this.crdy = crdy;
    }

    public BigDecimal getCrdr() {
        return crdr;
    }

    public void setCrdr(BigDecimal crdr) {
        this.crdr = crdr;
    }

    public BigDecimal getXg() {
        return xg;
    }

    public void setXg(BigDecimal xg) {
        this.xg = xg;
    }

    public BigDecimal getXag() {
        return xag;
    }

    public void setXag(BigDecimal xag) {
        this.xag = xag;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "Player{player='" + player + "', nation='" + nation + "', team='" + team + "'}";
    }
}