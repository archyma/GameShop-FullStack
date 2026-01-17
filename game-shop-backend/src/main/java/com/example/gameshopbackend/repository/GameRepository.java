package com.example.gameshopbackend.repository;

import com.example.gameshopbackend.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {
}