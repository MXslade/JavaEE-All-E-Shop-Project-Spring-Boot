package com.javaee.hometask7.alleshop.repositories;

import com.javaee.hometask7.alleshop.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByShopItemId(Long shopItemId);

}
