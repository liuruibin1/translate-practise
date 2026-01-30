package com.xxx.base.poker.card;

/**
 * 卡牌对比测试类
 */
public class CardComparisonTest {

    public static void main(String[] args) {
        // 运行特定卡牌对比
        CardComparisonUtil.compareSpecificCards();
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // 演示不同创建方式
        demonstrateCardCreation();
    }
    
    /**
     * 演示不同的卡牌创建方式
     */
    private static void demonstrateCardCreation() {
        System.out.println("=== 卡牌创建方式演示 ===\n");
        
        // 方式1: 通过 cardId
        Card card7ById = Card.getInstance(7);
        Card card51ById = Card.getInstance(51);
        
        // 方式2: 通过花色和点数
        Card card7ByRankSuit = Card.getInstance(Rank.EIGHT, Suit.CLUBS);
        Card card51ByRankSuit = Card.getInstance(Rank.ACE, Suit.SPADES);
        
        // 方式3: 通过别名
        Card card7ByAlias = Card.getInstance("8c");
        Card card51ByAlias = Card.getInstance("As");
        
        System.out.println("Card ID 7 的三种创建方式:");
        System.out.println("1. Card.getInstance(7): " + card7ById);
        System.out.println("2. Card.getInstance(Rank.EIGHT, Suit.CLUBS): " + card7ByRankSuit);
        System.out.println("3. Card.getInstance(\"8c\"): " + card7ByAlias);
        System.out.println("一致性检查: " + (card7ById.equals(card7ByRankSuit) && card7ByRankSuit.equals(card7ByAlias)));
        System.out.println();
        
        System.out.println("Card ID 51 的三种创建方式:");
        System.out.println("1. Card.getInstance(51): " + card51ById);
        System.out.println("2. Card.getInstance(Rank.ACE, Suit.SPADES): " + card51ByRankSuit);
        System.out.println("3. Card.getInstance(\"As\"): " + card51ByAlias);
        System.out.println("一致性检查: " + (card51ById.equals(card51ByRankSuit) && card51ByRankSuit.equals(card51ByAlias)));
    }
} 