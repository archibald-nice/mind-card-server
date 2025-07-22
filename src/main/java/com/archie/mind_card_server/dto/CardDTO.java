package com.archie.mind_card_server.dto;

import com.archie.mind_card_server.entity.Card;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    
    private Long id;
    
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200个字符")
    private String title;
    
    private String content;
    
    @Size(max = 100, message = "分类长度不能超过100个字符")
    private String category;
    
    @Size(max = 500, message = "标签长度不能超过500个字符")
    private String tags;
    
    private Integer priority;
    
    private Card.CardStatus status;
    
    @Size(max = 20, message = "颜色代码长度不能超过20个字符")
    private String color;
    
    private Boolean isFavorite;
    
    private Boolean isPublic;
    
    private Integer viewCount;
    
    @Size(max = 100, message = "创建者长度不能超过100个字符")
    private String createdBy;
    
    @Size(max = 100, message = "更新者长度不能超过100个字符")
    private String updatedBy;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private LocalDateTime deletedAt;
}
