package com.javaee.hometask7.alleshop.repositories;

import com.javaee.hometask7.alleshop.models.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PictureRepository extends JpaRepository<Picture, Long> {

    List<Picture> findAllByShopItemId(Long shopItemId);

}
