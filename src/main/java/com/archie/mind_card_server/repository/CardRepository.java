package com.archie.mind_card_server.repository;

import com.archie.mind_card_server.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    
    // 根据标题查找
    List<Card> findByTitleContainingIgnoreCase(String title);
    
    // 根据分类查找
    List<Card> findByCategoryIgnoreCase(String category);
    
    // 根据状态查找
    List<Card> findByStatus(Card.CardStatus status);
    
    // 根据创建者查找
    List<Card> findByCreatedBy(String createdBy);
    
    // 分页查找激活的卡片
    Page<Card> findByStatusOrderByCreatedAtDesc(Card.CardStatus status, Pageable pageable);
    
    // 根据优先级查找
    List<Card> findByPriorityGreaterThanEqualOrderByPriorityDesc(Integer priority);
    
    // 查找收藏的卡片
    List<Card> findByIsFavoriteAndStatus(Boolean isFavorite, Card.CardStatus status);
    
    // 查找公开的卡片
    List<Card> findByIsPublicAndStatus(Boolean isPublic, Card.CardStatus status);
    
    // 自定义查询：根据关键词搜索标题和内容
    @Query("SELECT c FROM Card c WHERE " +
           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "c.status = 'ACTIVE'")
    List<Card> searchByKeyword(@Param("keyword") String keyword);
    
    // 分页搜索
    @Query("SELECT c FROM Card c WHERE " +
           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "c.status = 'ACTIVE'")
    Page<Card> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // 根据标签查找
    @Query("SELECT c FROM Card c WHERE " +
           "LOWER(c.tags) LIKE LOWER(CONCAT('%', :tag, '%')) AND " +
           "c.status = 'ACTIVE'")
    List<Card> findByTagsContaining(@Param("tag") String tag);
    
    // 统计激活的卡片数量
    long countByStatus(Card.CardStatus status);
    
    // 根据分类统计
    @Query("SELECT c.category, COUNT(c) FROM Card c WHERE c.status = 'ACTIVE' GROUP BY c.category")
    List<Object[]> countByCategory();
    
    // 根据优先级统计
    @Query("SELECT c.priority, COUNT(c) FROM Card c WHERE c.status = 'ACTIVE' GROUP BY c.priority")
    List<Object[]> countByPriority();
    
    // 查找热门卡片（按查看次数排序）
    @Query("SELECT c FROM Card c WHERE c.status = 'ACTIVE' ORDER BY c.viewCount DESC")
    Page<Card> findPopularCards(Pageable pageable);
}
