package com.example.gameshopbackend.service;

import com.example.gameshopbackend.dto.GameRequestDto;
import com.example.gameshopbackend.dto.GameResponseDto;
import com.example.gameshopbackend.exception.ResourceNotFoundException;
import com.example.gameshopbackend.mapper.GameMapper;
import com.example.gameshopbackend.entity.Game;
import com.example.gameshopbackend.repository.GameRepository;
import com.example.gameshopbackend.specification.GameSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    public GameServiceImpl(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    @Override
    public Page<GameResponseDto> searchGames(String title, String genre, String platform, Double minPrice, Double maxPrice,
                                             Pageable pageable) {

        List<Specification<Game>> specs = new ArrayList<>();

        specs.add(GameSpecification.inNotDeleted());

        if(title != null && !title.isEmpty()){
            specs.add(GameSpecification.titleContains(title));
        }
        if(genre != null && !genre.isEmpty()){
            specs.add(GameSpecification.hasGenre(genre));
        }
        if(platform != null && !platform.isEmpty()){
            specs.add(GameSpecification.hasPlatform(platform));
        }
        if(minPrice != null){
            specs.add(GameSpecification.priceGreaterThanOrEqual(minPrice));
        }
        if(maxPrice != null){
            specs.add(GameSpecification.priceLessThanOrEqual(maxPrice));
        }

        Specification<Game> finalSpec = Specification.allOf(specs);

        Page<Game> gamePage = gameRepository.findAll(finalSpec, pageable);

        return gamePage.map(gameMapper::toDto);
    }

    @Override
    public GameResponseDto findGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Игра не найдена с ID: " + id));

        return gameMapper.toDto(game);
    }

    @Override
    public GameResponseDto createGame(GameRequestDto gameDto) {
        Game game = gameMapper.toEntity(gameDto);
        Game savedGame = gameRepository.save(game);
        return gameMapper.toDto(savedGame);
    }

    @Override
    public GameResponseDto updateGame(Long id, GameRequestDto gameDto) {
        Game oldGame = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Игра не найдена с ID: " + id));


        oldGame.setTitle(gameDto.getTitle());
        oldGame.setDescription(gameDto.getDescription());
        oldGame.setPrice(gameDto.getPrice());
        oldGame.setGenre(gameDto.getGenre());
        oldGame.setPlatform(gameDto.getPlatform());
        oldGame.setImageUrl(gameDto.getImageUrl());

        Game updatedGame = gameRepository.save(oldGame);
        return gameMapper.toDto(updatedGame);
    }

    @Override
    @Transactional
    public void deleteGame(Long id) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        game.setDeleted(true);
        gameRepository.save(game);
    }
}