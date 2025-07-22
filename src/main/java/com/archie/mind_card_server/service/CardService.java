package com.archie.mind_card_server.service;

import com.archie.mind_card_server.dto.CardDTO;
import com.archie.mind_card_server.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CardService {
    
    /**
     * 创建新卡片
     */
    CardDTO createCard(CardDTO cardDTO);
    
    /**
     * 根据ID获取卡片
     */
    CardDTO getCardById(Long id);
    
    /**
     * 更新卡片
     */
    CardDTO updateCard(Long id, CardDTO cardDTO);
    
    /**
     * 删除卡片（软删除）
     */
    void deleteCard(Long id);
    
    /**
     * 物理删除卡片
     */
    void hardDeleteCard(Long id);
    
    /**
     * 获取所有卡片
     */
    List<CardDTO> getAllCards();
    
    /**
     * 分页获取卡片
     */
    Page<CardDTO> getCards(Pageable pageable);
    
    /**
     * 根据状态分页获取卡片
     */
    Page<CardDTO> getCardsByStatus(Card.CardStatus status, Pageable pageable);
    
    /**
     * 根据标题搜索卡片
     */
    List<CardDTO> searchByTitle(String title);
    
    /**
     * 根据分类获取卡片
     */
    List<CardDTO> getCardsByCategory(String category);
    
    /**
     * 根据关键词搜索卡片
     */
    List<CardDTO> searchByKeyword(String keyword);
    
    /**
     * 分页搜索卡片
     */
    Page<CardDTO> searchByKeyword(String keyword, Pageable pageable);
    
    /**
     * 根据标签搜索卡片
     */
    List<CardDTO> searchByTag(String tag);
    
    /**
     * 根据优先级获取卡片
     */
    List<CardDTO> getCardsByPriority(Integer priority);
    
    /**
     * 获取收藏的卡片
     */
    List<CardDTO> getFavoriteCards();
    
    /**
     * 获取公开的卡片
     */
    List<CardDTO> getPublicCards();
    
    /**
     * 切换收藏状态
     */
    CardDTO toggleFavorite(Long id);
    
    /**
     * 切换公开状态
     */
    CardDTO togglePublic(Long id);
    
    /**
     * 更新卡片状态
     */
    CardDTO updateStatus(Long id, Card.CardStatus status);
    
    /**
     * 增加查看次数
     */
    CardDTO incrementViewCount(Long id);
    
    /**
     * 批量删除卡片
     */
    void batchDeleteCards(List<Long> ids);
    
    /**
     * 批量更新状态
     */
    void batchUpdateStatus(List<Long> ids, Card.CardStatus status);
    
    /**
     * 获取统计信息
     */
    Map<String, Object> getStatistics();
    
    /**
     * 获取热门卡片
     */
    Page<CardDTO> getPopularCards(Pageable pageable);
}
