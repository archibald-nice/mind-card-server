package com.archie.mind_card_server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200个字符")
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Size(max = 100, message = "分类长度不能超过100个字符")
    @Column(length = 100)
    private String category;
    
    @Size(max = 500, message = "标签长度不能超过500个字符")
    @Column(length = 500)
    private String tags;
    
    @Column
    private Integer priority = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CardStatus status = CardStatus.ACTIVE;
    
    @Size(max = 20, message = "颜色代码长度不能超过20个字符")
    @Column(length = 20)
    private String color = "#FFFFFF";
    
    @Column(name = "is_favorite")
    private Boolean isFavorite = false;
    
    @Column(name = "is_public")
    private Boolean isPublic = false;
    
    @Column(name = "view_count")
    private Integer viewCount = 0;
    
    @Size(max = 100, message = "创建者长度不能超过100个字符")
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @Size(max = 100, message = "更新者长度不能超过100个字符")
    @Column(name = "updated_by", length = 100)
    private String updatedBy;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    public enum CardStatus {
        ACTIVE, ARCHIVED, DELETED
    }
    
    public enum Priority {
        LOW(0), MEDIUM(1), HIGH(2);
        
        private final int value;
        
        Priority(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
        
        public static Priority fromValue(int value) {
            for (Priority priority : Priority.values()) {
                if (priority.value == value) {
                    return priority;
                }
            }
            return LOW;
        }
    }
}
