package com.xxx.chain.ethereum.abi;

/**
 * USDT合约ABI常量
 * 包含Transfer事件的ABI定义
 */
public class USDT_ABI {
    
    /**
     * Transfer事件ABI
     * event Transfer(address indexed from, address indexed to, uint256 value)
     */
    public static final String TRANSFER_EVENT_ABI = "Transfer(address,address,uint256)";
    
    /**
     * Transfer事件签名
     */
    public static final String TRANSFER_EVENT_SIGNATURE = "Transfer(address,address,uint256)";
    
    /**
     * 完整的USDT合约ABI（简化版，只包含必要的方法和事件）
     */
    public static final String USDT_CONTRACT_ABI = "[\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"name\",\n" +
            "    \"outputs\": [{\"name\": \"\", \"type\": \"string\"}],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"symbol\",\n" +
            "    \"outputs\": [{\"name\": \"\", \"type\": \"string\"}],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [],\n" +
            "    \"name\": \"decimals\",\n" +
            "    \"outputs\": [{\"name\": \"\", \"type\": \"uint8\"}],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": true,\n" +
            "    \"inputs\": [{\"name\": \"_owner\", \"type\": \"address\"}],\n" +
            "    \"name\": \"balanceOf\",\n" +
            "    \"outputs\": [{\"name\": \"balance\", \"type\": \"uint256\"}],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"view\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"constant\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\"name\": \"_to\", \"type\": \"address\"},\n" +
            "      {\"name\": \"_value\", \"type\": \"uint256\"}\n" +
            "    ],\n" +
            "    \"name\": \"transfer\",\n" +
            "    \"outputs\": [{\"name\": \"\", \"type\": \"bool\"}],\n" +
            "    \"payable\": false,\n" +
            "    \"stateMutability\": \"nonpayable\",\n" +
            "    \"type\": \"function\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"anonymous\": false,\n" +
            "    \"inputs\": [\n" +
            "      {\"indexed\": true, \"name\": \"from\", \"type\": \"address\"},\n" +
            "      {\"indexed\": true, \"name\": \"to\", \"type\": \"address\"},\n" +
            "      {\"indexed\": false, \"name\": \"value\", \"type\": \"uint256\"}\n" +
            "    ],\n" +
            "    \"name\": \"Transfer\",\n" +
            "    \"type\": \"event\"\n" +
            "  }\n" +
            "]";
}
