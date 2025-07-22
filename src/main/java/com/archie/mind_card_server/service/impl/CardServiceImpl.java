package com.archie.mind_card_server.service.impl;

import com.archie.mind_card_server.dto.CardDTO;
import com.archie.mind_card_server.entity.Card;
import com.archie.mind_card_server.exception.ResourceNotFoundException;
import com.archie.mind_card_server.repository.CardRepository;
import com.archie.mind_card_server.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CardServiceImpl implements CardService {
    
    private final CardRepository cardRepository;
    
    @Override
    public CardDTO createCard(CardDTO cardDTO) {
        log.info("创建新卡片: {}", cardDTO.getTitle());
        
        Card card = convertToEntity(cardDTO);
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());
        
        Card savedCard = cardRepository.save(card);
        return convertToDTO(savedCard);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CardDTO getCardById(Long id) {
        log.info("获取卡片: {}", id);
        
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("卡片未找到，ID: " + id));
        
        return convertToDTO(card);
    }
    
    @Override
    public CardDTO updateCard(Long id, CardDTO cardDTO) {
        log.info("更新卡片: {}", id);
        
        Card existingCard = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("卡片未找到，ID: " + id));
        
        // 更新字段
        existingCard.setTitle(cardDTO.getTitle());
        existingCard.setContent(cardDTO.getContent());
        existingCard.setCategory(cardDTO.getCategory());
        existingCard.setTags(cardDTO.getTags());
        existingCard.setPriority(cardDTO.getPriority());
        existingCard.setColor(cardDTO.getColor());
        existingCard.setUpdatedBy(cardDTO.getUpdatedBy());
        existingCard.setUpdatedAt(LocalDateTime.now());
        
        Card savedCard = cardRepository.save(existingCard);
        return convertToDTO(savedCard);
    }
    
    @Override
    public void deleteCard(Long id) {
        log.info("软删除卡片: {}", id);
        
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("卡片未找到，ID: " + id));
        
        card.setStatus(Card.CardStatus.DELETED);
        card.setDeletedAt(LocalDateTime.now());
        cardRepository.save(card);
    }
    
    @Override
    public void hardDeleteCard(Long id) {
        log.info("物理删除卡片: {}", id);
        
        if (!cardRepository.existsById(id)) {
            throw new ResourceNotFoundException("卡片未找到，ID: " + id);
        }
        
        cardRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> getAllCards() {
        log.info("获取所有卡片");
        
        List<Card> cards = cardRepository.findAll();
        return cards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CardDTO> getCards(Pageable pageable) {
        log.info("分页获取卡片");
        
        Page<Card> cards = cardRepository.findAll(pageable);
        return cards.map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CardDTO> getCardsByStatus(Card.CardStatus status, Pageable pageable) {
        log.info("根据状态分页获取卡片: {}", status);
        
        Page<Card> cards = cardRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        return cards.map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> searchByTitle(String title) {
        log.info("根据标题搜索卡片: {}", title);
        
        List<Card> cards = cardRepository.findByTitleContainingIgnoreCase(title);
        return cards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> getCardsByCategory(String category) {
        log.info("根据分类获取卡片: {}", category);
        
        List<Card> cards = cardRepository.findByCategoryIgnoreCase(category);
        return cards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> searchByKeyword(String keyword) {
        log.info("根据关键词搜索卡片: {}", keyword);
        
        List<Card> cards = cardRepository.searchByKeyword(keyword);
        return cards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CardDTO> searchByKeyword(String keyword, Pageable pageable) {
        log.info("分页搜索卡片: {}", keyword);
        
        Page<Card> cards = cardRepository.searchByKeyword(keyword, pageable);
        return cards.map(this::convertToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> searchByTag(String tag) {
        log.info("根据标签搜索卡片: {}", tag);
        
        List<Card> cards = cardRepository.findByTagsContaining(tag);
        return cards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> getCardsByPriority(Integer priority) {
        log.info("根据优先级获取卡片: {}", priority);
        
        List<Card> cards = cardRepository.findByPriorityGreaterThanEqualOrderByPriorityDesc(priority);
        return cards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> getFavoriteCards() {
        log.info("获取收藏的卡片");
        
        List<Card> cards = cardRepository.findByIsFavoriteAndStatus(true, Card.CardStatus.ACTIVE);
        return cards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> getPublicCards() {
        log.info("获取公开的卡片");
        
        List<Card> cards = cardRepository.findByIsPublicAndStatus(true, Card.CardStatus.ACTIVE);
        return cards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public CardDTO toggleFavorite(Long id) {
        log.info("切换收藏状态: {}", id);
        
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("卡片未找到，ID: " + id));
        
        card.setIsFavorite(!card.getIsFavorite());
        card.setUpdatedAt(LocalDateTime.now());
        
        Card savedCard = cardRepository.save(card);
        return convertToDTO(savedCard);
    }
    
    @Override
    public CardDTO togglePublic(Long id) {
        log.info("切换公开状态: {}", id);
        
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("卡片未找到，ID: " + id));
        
        card.setIsPublic(!card.getIsPublic());
        card.setUpdatedAt(LocalDateTime.now());
        
        Card savedCard = cardRepository.save(card);
        return convertToDTO(savedCard);
    }
    
    @Override
    public CardDTO updateStatus(Long id, Card.CardStatus status) {
        log.info("更新卡片状态: {} -> {}", id, status);
        
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("卡片未找到，ID: " + id));
        
        card.setStatus(status);
        card.setUpdatedAt(LocalDateTime.now());
        
        if (status == Card.CardStatus.DELETED) {
            card.setDeletedAt(LocalDateTime.now());
        }
        
        Card savedCard = cardRepository.save(card);
        return convertToDTO(savedCard);
    }
    
    @Override
    public CardDTO incrementViewCount(Long id) {
        log.info("增加查看次数: {}", id);
        
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("卡片未找到，ID: " + id));
        
        card.setViewCount(card.getViewCount() + 1);
        
        Card savedCard = cardRepository.save(card);
        return convertToDTO(savedCard);
    }
    
    @Override
    public void batchDeleteCards(List<Long> ids) {
        log.info("批量删除卡片: {}", ids);
        
        List<Card> cards = cardRepository.findAllById(ids);
        cards.forEach(card -> {
            card.setStatus(Card.CardStatus.DELETED);
            card.setDeletedAt(LocalDateTime.now());
        });
        
        cardRepository.saveAll(cards);
    }
    
    @Override
    public void batchUpdateStatus(List<Long> ids, Card.CardStatus status) {
        log.info("批量更新状态: {} -> {}", ids, status);
        
        List<Card> cards = cardRepository.findAllById(ids);
        cards.forEach(card -> {
            card.setStatus(status);
            card.setUpdatedAt(LocalDateTime.now());
            
            if (status == Card.CardStatus.DELETED) {
                card.setDeletedAt(LocalDateTime.now());
            }
        });
        
        cardRepository.saveAll(cards);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistics() {
        log.info("获取统计信息");
        
        Map<String, Object> stats = new HashMap<>();
        
        // 总数统计
        stats.put("totalCards", cardRepository.count());
        stats.put("activeCards", cardRepository.countByStatus(Card.CardStatus.ACTIVE));
        stats.put("archivedCards", cardRepository.countByStatus(Card.CardStatus.ARCHIVED));
        stats.put("deletedCards", cardRepository.countByStatus(Card.CardStatus.DELETED));
        
        // 分类统计
        List<Object[]> categoryStats = cardRepository.countByCategory();
        Map<String, Long> categoryMap = new HashMap<>();
        for (Object[] stat : categoryStats) {
            categoryMap.put((String) stat[0], (Long) stat[1]);
        }
        stats.put("categoryStats", categoryMap);
        
        // 优先级统计
        List<Object[]> priorityStats = cardRepository.countByPriority();
        Map<Integer, Long> priorityMap = new HashMap<>();
        for (Object[] stat : priorityStats) {
            priorityMap.put((Integer) stat[0], (Long) stat[1]);
        }
        stats.put("priorityStats", priorityMap);
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CardDTO> getPopularCards(Pageable pageable) {
        log.info("获取热门卡片");
        
        Page<Card> cards = cardRepository.findPopularCards(pageable);
        return cards.map(this::convertToDTO);
    }
    
    // 实体转DTO
    private CardDTO convertToDTO(Card card) {
        CardDTO dto = new CardDTO();
        dto.setId(card.getId());
        dto.setTitle(card.getTitle());
        dto.setContent(card.getContent());
        dto.setCategory(card.getCategory());
        dto.setTags(card.getTags());
        dto.setPriority(card.getPriority());
        dto.setStatus(card.getStatus());
        dto.setColor(card.getColor());
        dto.setIsFavorite(card.getIsFavorite());
        dto.setIsPublic(card.getIsPublic());
        dto.setViewCount(card.getViewCount());
        dto.setCreatedBy(card.getCreatedBy());
        dto.setUpdatedBy(card.getUpdatedBy());
        dto.setCreatedAt(card.getCreatedAt());
        dto.setUpdatedAt(card.getUpdatedAt());
        dto.setDeletedAt(card.getDeletedAt());
        return dto;
    }
    
    // DTO转实体
    private Card convertToEntity(CardDTO dto) {
        Card card = new Card();
        card.setId(dto.getId());
        card.setTitle(dto.getTitle());
        card.setContent(dto.getContent());
        card.setCategory(dto.getCategory());
        card.setTags(dto.getTags());
        card.setPriority(dto.getPriority() != null ? dto.getPriority() : 0);
        card.setStatus(dto.getStatus() != null ? dto.getStatus() : Card.CardStatus.ACTIVE);
        card.setColor(dto.getColor() != null ? dto.getColor() : "#FFFFFF");
        card.setIsFavorite(dto.getIsFavorite() != null ? dto.getIsFavorite() : false);
        card.setIsPublic(dto.getIsPublic() != null ? dto.getIsPublic() : false);
        card.setViewCount(dto.getViewCount() != null ? dto.getViewCount() : 0);
        card.setCreatedBy(dto.getCreatedBy());
        card.setUpdatedBy(dto.getUpdatedBy());
        return card;
    }
}
