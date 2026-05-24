package ru.pp.gamma.overlord.image.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pp.gamma.overlord.image.entity.Image;

import java.util.Optional;

public interface ImageRepository extends CrudRepository<Image, Long> {

    Optional<Image> findByName(String name);
}
