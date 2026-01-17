package com.example.gameshopbackend.service;

import com.example.gameshopbackend.dto.GameResponseDto;
import com.example.gameshopbackend.dto.GameRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GameService {

    Page<GameResponseDto> searchGames(String title, String genre, String platform, Double minPrice, Double maxPrice,
                                      Pageable pageable);

    GameResponseDto findGameById(Long id);

    GameResponseDto createGame(GameRequestDto gameDto);

    GameResponseDto updateGame(Long id, GameRequestDto gameDto);

    void deleteGame(Long id);
}
