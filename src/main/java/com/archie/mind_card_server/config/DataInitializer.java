package com.archie.mind_card_server.config;

import com.archie.mind_card_server.entity.Card;
import com.archie.mind_card_server.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final CardRepository cardRepository;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化数据...");
        
        // 检查是否已经有数据
        if (cardRepository.count() > 0) {
            log.info("数据库中已存在数据，跳过初始化");
            return;
        }
        
        // 创建示例数据
        List<Card> sampleCards = Arrays.asList(
            createSampleCard(
                "学习Java Spring Boot",
                "深入学习Spring Boot框架，掌握微服务开发技能。\n\n学习要点：\n1. Spring Boot基础配置\n2. Spring Data JPA\n3. RESTful API设计\n4. 安全配置\n5. 单元测试",
                "技术学习",
                "Java,Spring Boot,后端开发",
                2,
                "#FFE4B5",
                "admin"
            ),
            createSampleCard(
                "项目管理最佳实践",
                "项目管理的关键要素和最佳实践。\n\n核心要点：\n- 明确项目目标和范围\n- 合理分配资源\n- 定期进度跟踪\n- 风险管理\n- 团队沟通",
                "工作管理",
                "项目管理,团队协作",
                1,
                "#E6F3FF",
                "admin"
            ),
            createSampleCard(
                "健康生活方式",
                "保持健康的生活习惯和日常作息。\n\n健康要素：\n1. 规律作息，保证8小时睡眠\n2. 均衡饮食，多吃蔬菜水果\n3. 适量运动，每天至少30分钟\n4. 定期体检\n5. 保持心情愉悦",
                "生活方式",
                "健康,运动,饮食",
                1,
                "#F0FFF0",
                "admin"
            ),
            createSampleCard(
                "读书笔记 - 《深度工作》",
                "《深度工作》读书笔记和心得体会。\n\n核心观点：\n- 深度工作是一种技能\n- 需要刻意练习和培养\n- 清除干扰，专注一件事\n- 建立仪式感和专注环境\n- 定期反思和调整",
                "个人成长",
                "读书笔记,效率,专注力",
                0,
                "#FFF8DC",
                "admin"
            ),
            createSampleCard(
                "旅行计划 - 日本之行",
                "日本旅行的详细计划和准备事项。\n\n行程安排：\n第1天：东京 - 浅草寺、上野公园\n第2天：京都 - 清水寺、伏见稻荷大社\n第3天：大阪 - 大阪城、道顿堀\n\n准备事项：\n- 签证办理\n- 酒店预订\n- 交通卡购买\n- 旅行保险",
                "旅行计划",
                "旅行,日本,度假",
                1,
                "#FFE4E1",
                "admin"
            )
        );
        
        cardRepository.saveAll(sampleCards);
        log.info("初始化数据完成，创建了 {} 条示例数据", sampleCards.size());
    }
    
    private Card createSampleCard(String title, String content, String category, 
                                 String tags, Integer priority, String color, String createdBy) {
        Card card = new Card();
        card.setTitle(title);
        card.setContent(content);
        card.setCategory(category);
        card.setTags(tags);
        card.setPriority(priority);
        card.setStatus(Card.CardStatus.ACTIVE);
        card.setColor(color);
        card.setIsFavorite(false);
        card.setIsPublic(true);
        card.setViewCount(0);
        card.setCreatedBy(createdBy);
        card.setUpdatedBy(createdBy);
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());
        return card;
    }
}
