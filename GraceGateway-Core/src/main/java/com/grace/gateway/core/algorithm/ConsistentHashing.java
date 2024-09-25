package com.grace.gateway.core.algorithm;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashing {

    private final int virtualNodeNum;

    // 哈希环
    private final SortedMap<Integer, String> hashCircle = new TreeMap<>();

    // 构造函数，初始化一致性哈希环
    public ConsistentHashing(List<String> nodes, int virtualNodeNum) {
        this.virtualNodeNum = virtualNodeNum;
        for (String node : nodes) {
            addNode(node);
        }
    }

    public void addNode(String node) {
        for (int i = 0; i < virtualNodeNum; i++) {
            String virtualNode = node + "&&VN" + i;
            hashCircle.put(getHash(virtualNode), node);
        }
    }

    public String getNode(String key) {
        if (hashCircle.isEmpty()) {
            return null;
        }
        int hash = getHash(key);
        SortedMap<Integer, String> tailMap = hashCircle.tailMap(hash);
        Integer nodeHash = tailMap.isEmpty() ? hashCircle.firstKey() : tailMap.firstKey();
        return hashCircle.get(nodeHash);
    }

    private int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }

}