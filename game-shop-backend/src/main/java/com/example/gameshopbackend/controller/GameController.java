package com.example.gameshopbackend.controller;

import com.example.gameshopbackend.dto.GameResponseDto;
import com.example.gameshopbackend.dto.GameRequestDto;
import com.example.gameshopbackend.service.GameService;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/games")
@Tag(
        name = "Game Controller",
        description = "API для управления играми"
)
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создание новой игры",
            description = "Доступно только ADMIN"
    )
    @SecurityRequirement(
            name = "bearerAuth"
    )
    public GameResponseDto createGame(@Valid @RequestBody GameRequestDto gameDto) {
        return gameService.createGame(gameDto);
    }

    @GetMapping
    @Operation(
            summary = "Получить лист всех игр (возможна фильтрация и сортировка)",
            description = "Доступно для всех"
    )
    public Page<GameResponseDto> searchGames(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        String sortField = sort[0];
        Sort.Direction sortDirection = Sort.Direction.fromString(sort[1]);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));

        return gameService.searchGames(title, genre, platform, minPrice, maxPrice, pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить игру по ID",
            description = "Доспупно для всех"
    )
    public GameResponseDto getGameById(@PathVariable Long id) {
        return gameService.findGameById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновить существующую игру",
            description = "Доступен только ADMIN"
    )
    @SecurityRequirement(
            name = "bearerAuth"
    )
    public GameResponseDto updateGame(@PathVariable Long id, @Valid @RequestBody GameRequestDto gameDto) {
        return gameService.updateGame(id, gameDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить игру",
            description = "Доступен только ADMIN"
    )
    @SecurityRequirement(
            name = "bearerAuth"
    )
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}