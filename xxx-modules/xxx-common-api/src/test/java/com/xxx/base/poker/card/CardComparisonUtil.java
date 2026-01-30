package com.xxx.base.poker.card;

/**
 * 扑克牌对比工具类
 */
public class CardComparisonUtil {

    /**
     * 对比两张卡牌的基本信息
     */
    public static String compareCards(int cardId1, int cardId2) {
        Card card1 = Card.getInstance(cardId1);
        Card card2 = Card.getInstance(cardId2);
        
        StringBuilder result = new StringBuilder();
        result.append("=== 卡牌对比结果 ===\n");
        result.append("卡牌1 (ID=").append(cardId1).append("): ").append(card1).append("\n");
        result.append("卡牌2 (ID=").append(cardId2).append("): ").append(card2).append("\n\n");
        
        // 花色对比
        result.append("【花色对比】\n");
        result.append("卡牌1花色: ").append(card1.getSuit()).append("\n");
        result.append("卡牌2花色: ").append(card2.getSuit()).append("\n");
        result.append("花色是否相同: ").append(card1.getSuit() == card2.getSuit()).append("\n\n");
        
        // 点数对比
        result.append("【点数对比】\n");
        result.append("卡牌1点数: ").append(card1.getRank()).append("\n");
        result.append("卡牌2点数: ").append(card2.getRank()).append("\n");
        result.append("点数是否相同: ").append(card1.getRank() == card2.getRank()).append("\n");
        result.append("点数大小关系: ").append(getRankComparison(card1.getRank(), card2.getRank())).append("\n\n");
        
        return result.toString();
    }

    /**
     * 获取点数大小关系
     */
    private static String getRankComparison(Rank rank1, Rank rank2) {
        if (rank1 == rank2) {
            return "相等";
        }
        return rank1.ordinal() > rank2.ordinal() ? "卡牌1 > 卡牌2" : "卡牌1 < 卡牌2";
    }

    /**
     * 获取卡牌的详细信息
     */
    public static String getCardDetails(int cardId) {
        Card card = Card.getInstance(cardId);
        
        StringBuilder details = new StringBuilder();
        details.append("卡牌ID: ").append(cardId).append("\n");
        details.append("显示名称: ").append(card).append("\n");
        details.append("花色: ").append(card.getSuit()).append("\n");
        details.append("点数: ").append(card.getRank()).append("\n");
        details.append("计算方式: cardId = ").append(card.getSuit().ordinal()).append(" + 4 * ").append(card.getRank().ordinal()).append(" = ").append(cardId);
        
        return details.toString();
    }

    /**
     * 对比特定卡牌 (cardId = 7 vs cardId = 51)
     */
    public static void compareSpecificCards() {
        System.out.println("=== 特定卡牌对比: cardId=7 vs cardId=51 ===\n");
        
        // 获取详细信息
        System.out.println("【Card ID 7 详细信息】");
        System.out.println(getCardDetails(7));
        System.out.println();
        
        System.out.println("【Card ID 51 详细信息】");
        System.out.println(getCardDetails(51));
        System.out.println();
        
        // 对比结果
        System.out.println("【对比结果】");
        System.out.println(compareCards(7, 51));
    }
} 