package com.example.gameshopbackend.specification;

import com.example.gameshopbackend.entity.Game;
import org.springframework.data.jpa.domain.Specification;

public class GameSpecification {

    public static Specification<Game> titleContains(String title) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Game> hasGenre(String genre) {
        return (root, query, cb) -> cb.equal(root.get("genre"), genre);
    }

    public static Specification<Game> hasPlatform(String platform) {
        return (root, query, cb) -> cb.equal(root.get("platform"), platform);
    }

    public static Specification<Game> priceGreaterThanOrEqual(Double price) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Game> priceLessThanOrEqual(Double price) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Game> inNotDeleted(){
        return (root, query, cb) -> cb.equal(root.get("deleted"), false);
    }
}