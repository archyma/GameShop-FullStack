package com.example.gameshopbackend.mapper;

import com.example.gameshopbackend.dto.GameRequestDto;
import com.example.gameshopbackend.dto.GameResponseDto;
import com.example.gameshopbackend.entity.Game;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    public GameResponseDto toDto(Game game) {
        GameResponseDto gameResponseDto = new GameResponseDto();
        gameResponseDto.setId(game.getId());
        gameResponseDto.setTitle(game.getTitle());
        gameResponseDto.setPlatform(game.getPlatform());
        gameResponseDto.setGenre(game.getGenre());
        gameResponseDto.setDescription(game.getDescription());
        gameResponseDto.setPrice(game.getPrice());
        gameResponseDto.setImageUrl(game.getImageUrl());
        return gameResponseDto;
    }

    public Game toEntity(GameRequestDto gameRequestDto) {
        Game game = new Game();
        game.setTitle(gameRequestDto.getTitle());
        game.setPlatform(gameRequestDto.getPlatform());
        game.setGenre(gameRequestDto.getGenre());
        game.setDescription(gameRequestDto.getDescription());
        game.setPrice(gameRequestDto.getPrice());
        game.setImageUrl(gameRequestDto.getImageUrl());
        return game;
    }
}
