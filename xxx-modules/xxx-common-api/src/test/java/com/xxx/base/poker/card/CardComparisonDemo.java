package com.xxx.base.poker.card;

/**
 * 扑克牌对比演示
 * 对比 cardId = 7 和 cardId = 51 的扑克牌
 */
public class CardComparisonDemo {

    public static void main(String[] args) {
        // 创建两张扑克牌
        Card card7 = Card.getInstance(7);
        Card card51 = Card.getInstance(51);
        
        System.out.println("=== 扑克牌对比分析 ===");
        System.out.println();
        
        // 基本信息对比
        System.out.println("【基本信息】");
        System.out.println("Card ID 7: " + card7 + " (cardId=" + card7.getCardId() + ")");
        System.out.println("Card ID 51: " + card51 + " (cardId=" + card51.getCardId() + ")");
        System.out.println();
        
        // 详细属性对比
        System.out.println("【详细属性】");
        System.out.println("Card ID 7:");
        System.out.println("  - 花色: " + card7.getSuit() + " (" + card7.getSuit().getAlias() + ")");
        System.out.println("  - 点数: " + card7.getRank() + " (" + card7.getRank().getAlias() + ")");
        System.out.println("  - 花色序号: " + card7.getSuit().ordinal());
        System.out.println("  - 点数序号: " + card7.getRank().ordinal());
        System.out.println();
        
        System.out.println("Card ID 51:");
        System.out.println("  - 花色: " + card51.getSuit() + " (" + card51.getSuit().getAlias() + ")");
        System.out.println("  - 点数: " + card51.getRank() + " (" + card51.getRank().getAlias() + ")");
        System.out.println("  - 花色序号: " + card51.getSuit().ordinal());
        System.out.println("  - 点数序号: " + card51.getRank().ordinal());
        System.out.println();
        
        // 计算验证
        System.out.println("【计算验证】");
        System.out.println("Card ID 7 的计算:");
        System.out.println("  cardId = suit.ordinal() + Suit.values().length * rank.ordinal()");
        System.out.println("  cardId = " + card7.getSuit().ordinal() + " + 4 * " + card7.getRank().ordinal());
        System.out.println("  cardId = " + card7.getSuit().ordinal() + " + " + (4 * card7.getRank().ordinal()));
        System.out.println("  cardId = " + (card7.getSuit().ordinal() + 4 * card7.getRank().ordinal()));
        System.out.println();
        
        System.out.println("Card ID 51 的计算:");
        System.out.println("  cardId = suit.ordinal() + Suit.values().length * rank.ordinal()");
        System.out.println("  cardId = " + card51.getSuit().ordinal() + " + 4 * " + card51.getRank().ordinal());
        System.out.println("  cardId = " + card51.getSuit().ordinal() + " + " + (4 * card51.getRank().ordinal()));
        System.out.println("  cardId = " + (card51.getSuit().ordinal() + 4 * card51.getRank().ordinal()));
        System.out.println();
        
        // 牌型对比
        System.out.println("【牌型对比】");
        System.out.println("Card ID 7 (" + card7 + "):");
        System.out.println("  - 这是一张梅花8");
        System.out.println("  - 在扑克牌中属于中等点数");
        System.out.println("  - 花色为黑色");
        System.out.println();
        
        System.out.println("Card ID 51 (" + card51 + "):");
        System.out.println("  - 这是一张黑桃A");
        System.out.println("  - 在扑克牌中是最高点数");
        System.out.println("  - 花色为黑色");
        System.out.println();
        
        // 游戏价值对比
        System.out.println("【游戏价值】");
        System.out.println("Card ID 7 的游戏价值:");
        System.out.println("  - 点数: 8");
        System.out.println("  - 在大多数扑克游戏中属于中等价值");
        System.out.println("  - 在21点游戏中价值为8");
        System.out.println();
        
        System.out.println("Card ID 51 的游戏价值:");
        System.out.println("  - 点数: A (通常为1或11)");
        System.out.println("  - 在大多数扑克游戏中属于最高价值");
        System.out.println("  - 在21点游戏中价值为1或11");
        System.out.println();
        
        // 使用示例
        System.out.println("【使用示例】");
        System.out.println("// 通过 cardId 创建卡牌");
        System.out.println("Card card7 = Card.getInstance(7);");
        System.out.println("Card card51 = Card.getInstance(51);");
        System.out.println();
        System.out.println("// 通过花色和点数创建卡牌");
        System.out.println("Card card7Alt = Card.getInstance(Rank.EIGHT, Suit.CLUBS);");
        System.out.println("Card card51Alt = Card.getInstance(Rank.ACE, Suit.SPADES);");
        System.out.println();
        System.out.println("// 通过别名创建卡牌");
        System.out.println("Card card7Alias = Card.getInstance(\"8c\");");
        System.out.println("Card card51Alias = Card.getInstance(\"As\");");
        System.out.println();
        
        // 验证创建方式的一致性
        System.out.println("【验证创建方式的一致性】");
        Card card7Alt = Card.getInstance(Rank.EIGHT, Suit.CLUBS);
        Card card51Alt = Card.getInstance(Rank.ACE, Suit.SPADES);
        Card card7Alias = Card.getInstance("8c");
        Card card51Alias = Card.getInstance("As");
        
        System.out.println("Card ID 7 的多种创建方式:");
        System.out.println("  Card.getInstance(7): " + card7);
        System.out.println("  Card.getInstance(Rank.EIGHT, Suit.CLUBS): " + card7Alt);
        System.out.println("  Card.getInstance(\"8c\"): " + card7Alias);
        System.out.println("  是否相等: " + (card7.equals(card7Alt) && card7Alt.equals(card7Alias)));
        System.out.println();
        
        System.out.println("Card ID 51 的多种创建方式:");
        System.out.println("  Card.getInstance(51): " + card51);
        System.out.println("  Card.getInstance(Rank.ACE, Suit.SPADES): " + card51Alt);
        System.out.println("  Card.getInstance(\"As\"): " + card51Alias);
        System.out.println("  是否相等: " + (card51.equals(card51Alt) && card51Alt.equals(card51Alias)));
    }
} 