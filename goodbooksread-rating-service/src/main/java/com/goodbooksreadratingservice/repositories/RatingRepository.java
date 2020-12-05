package com.goodbooksreadratingservice.repositories;

import org.springframework.data.repository.CrudRepository;
import com.goodbooksreadratingservice.entity.Rating;

public interface RatingRepository extends CrudRepository<Rating, Integer>{

}
