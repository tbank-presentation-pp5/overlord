package ru.pp.gamma.overlord.image.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pp.gamma.overlord.image.entity.Image;

public interface ImageRepository extends CrudRepository<Image, Long> {

}
