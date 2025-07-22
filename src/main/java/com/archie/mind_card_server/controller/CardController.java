package com.archie.mind_card_server.controller;

import com.archie.mind_card_server.dto.ApiResponse;
import com.archie.mind_card_server.dto.CardDTO;
import com.archie.mind_card_server.entity.Card;
import com.archie.mind_card_server.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CardController {
    
    private final CardService cardService;
    
    /**
     * 创建新卡片
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CardDTO>> createCard(@Valid @RequestBody CardDTO cardDTO) {
        log.info("创建卡片请求: {}", cardDTO.getTitle());
        
        CardDTO createdCard = cardService.createCard(cardDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdCard, "卡片创建成功"));
    }
    
    /**
     * 根据ID获取卡片
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CardDTO>> getCard(@PathVariable Long id) {
        log.info("获取卡片请求: {}", id);
        
        CardDTO card = cardService.getCardById(id);
        // 增加查看次数
        cardService.incrementViewCount(id);
        
        return ResponseEntity.ok(ApiResponse.success(card, "获取卡片成功"));
    }
    
    /**
     * 更新卡片
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CardDTO>> updateCard(
            @PathVariable Long id,
            @Valid @RequestBody CardDTO cardDTO) {
        log.info("更新卡片请求: {}", id);
        
        CardDTO updatedCard = cardService.updateCard(id, cardDTO);
        return ResponseEntity.ok(ApiResponse.success(updatedCard, "卡片更新成功"));
    }
    
    /**
     * 删除卡片（软删除）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCard(@PathVariable Long id) {
        log.info("删除卡片请求: {}", id);
        
        cardService.deleteCard(id);
        return ResponseEntity.ok(ApiResponse.success(null, "卡片删除成功"));
    }
    
    /**
     * 物理删除卡片
     */
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<ApiResponse<Void>> hardDeleteCard(@PathVariable Long id) {
        log.info("物理删除卡片请求: {}", id);
        
        cardService.hardDeleteCard(id);
        return ResponseEntity.ok(ApiResponse.success(null, "卡片永久删除成功"));
    }
    
    /**
     * 获取所有卡片
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CardDTO>>> getAllCards() {
        log.info("获取所有卡片请求");
        
        List<CardDTO> cards = cardService.getAllCards();
        return ResponseEntity.ok(ApiResponse.success(cards, "获取卡片列表成功"));
    }
    
    /**
     * 分页获取卡片
     */
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<CardDTO>>> getCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("分页获取卡片请求: page={}, size={}", page, size);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<CardDTO> cards = cardService.getCards(pageable);
        return ResponseEntity.ok(ApiResponse.success(cards, "获取卡片分页成功"));
    }
    
    /**
     * 根据状态分页获取卡片
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Page<CardDTO>>> getCardsByStatus(
            @PathVariable Card.CardStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("根据状态获取卡片请求: {}", status);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CardDTO> cards = cardService.getCardsByStatus(status, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(cards, "获取卡片成功"));
    }
    
    /**
     * 搜索卡片
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CardDTO>>> searchCards(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("搜索卡片请求: {}", keyword);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CardDTO> cards = cardService.searchByKeyword(keyword, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(cards, "搜索卡片成功"));
    }
    
    /**
     * 根据分类获取卡片
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<CardDTO>>> getCardsByCategory(@PathVariable String category) {
        log.info("根据分类获取卡片请求: {}", category);
        
        List<CardDTO> cards = cardService.getCardsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(cards, "获取分类卡片成功"));
    }
    
    /**
     * 根据标签搜索卡片
     */
    @GetMapping("/tag/{tag}")
    public ResponseEntity<ApiResponse<List<CardDTO>>> getCardsByTag(@PathVariable String tag) {
        log.info("根据标签获取卡片请求: {}", tag);
        
        List<CardDTO> cards = cardService.searchByTag(tag);
        return ResponseEntity.ok(ApiResponse.success(cards, "获取标签卡片成功"));
    }
    
    /**
     * 根据优先级获取卡片
     */
    @GetMapping("/priority/{priority}")
    public ResponseEntity<ApiResponse<List<CardDTO>>> getCardsByPriority(@PathVariable Integer priority) {
        log.info("根据优先级获取卡片请求: {}", priority);
        
        List<CardDTO> cards = cardService.getCardsByPriority(priority);
        return ResponseEntity.ok(ApiResponse.success(cards, "获取优先级卡片成功"));
    }
    
    /**
     * 获取收藏的卡片
     */
    @GetMapping("/favorites")
    public ResponseEntity<ApiResponse<List<CardDTO>>> getFavoriteCards() {
        log.info("获取收藏卡片请求");
        
        List<CardDTO> cards = cardService.getFavoriteCards();
        return ResponseEntity.ok(ApiResponse.success(cards, "获取收藏卡片成功"));
    }
    
    /**
     * 获取公开的卡片
     */
    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<CardDTO>>> getPublicCards() {
        log.info("获取公开卡片请求");
        
        List<CardDTO> cards = cardService.getPublicCards();
        return ResponseEntity.ok(ApiResponse.success(cards, "获取公开卡片成功"));
    }
    
    /**
     * 获取热门卡片
     */
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<Page<CardDTO>>> getPopularCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("获取热门卡片请求");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<CardDTO> cards = cardService.getPopularCards(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(cards, "获取热门卡片成功"));
    }
    
    /**
     * 切换收藏状态
     */
    @PutMapping("/{id}/favorite")
    public ResponseEntity<ApiResponse<CardDTO>> toggleFavorite(@PathVariable Long id) {
        log.info("切换收藏状态请求: {}", id);
        
        CardDTO card = cardService.toggleFavorite(id);
        return ResponseEntity.ok(ApiResponse.success(card, "收藏状态切换成功"));
    }
    
    /**
     * 切换公开状态
     */
    @PutMapping("/{id}/public")
    public ResponseEntity<ApiResponse<CardDTO>> togglePublic(@PathVariable Long id) {
        log.info("切换公开状态请求: {}", id);
        
        CardDTO card = cardService.togglePublic(id);
        return ResponseEntity.ok(ApiResponse.success(card, "公开状态切换成功"));
    }
    
    /**
     * 更新卡片状态
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<CardDTO>> updateStatus(
            @PathVariable Long id,
            @RequestParam Card.CardStatus status) {
        log.info("更新卡片状态请求: {} -> {}", id, status);
        
        CardDTO card = cardService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(card, "状态更新成功"));
    }
    
    /**
     * 批量删除卡片
     */
    @DeleteMapping("/batch")
    public ResponseEntity<ApiResponse<Void>> batchDeleteCards(@RequestBody List<Long> ids) {
        log.info("批量删除卡片请求: {}", ids);
        
        cardService.batchDeleteCards(ids);
        return ResponseEntity.ok(ApiResponse.success(null, "批量删除成功"));
    }
    
    /**
     * 批量更新状态
     */
    @PutMapping("/batch/status")
    public ResponseEntity<ApiResponse<Void>> batchUpdateStatus(
            @RequestBody List<Long> ids,
            @RequestParam Card.CardStatus status) {
        log.info("批量更新状态请求: {} -> {}", ids, status);
        
        cardService.batchUpdateStatus(ids, status);
        return ResponseEntity.ok(ApiResponse.success(null, "批量状态更新成功"));
    }
    
    /**
     * 获取统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        log.info("获取统计信息请求");
        
        Map<String, Object> stats = cardService.getStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "获取统计信息成功"));
    }
}
