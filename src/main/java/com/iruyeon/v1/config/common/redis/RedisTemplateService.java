//package com.iruyeon.v1.config.common.redis;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class RedisTemplateService {
//    private final RedisTemplate<String, String> redisTemplate;
//    private final ObjectMapper objectMapper;
//
//    private static final String CACHE_KEY = "CHAT:";
//
//    private HashOperations<String, String, String> hashOperations;
//
//    @PostConstruct
//    public void init() {
//        String result = redisTemplate.getConnectionFactory().getConnection().ping();
//        log.info("Redis connection test: {}", result);
//        this.hashOperations = redisTemplate.opsForHash();
//    }
//
//    // key -> value
//    // word -> clientId
//    // 검색어에 맞는 clientId를 저장한다.
//    public void addSearchWordCache(Long clientId, List<String> words) {
//        var ops = redisTemplate.opsForSet();
//
//        words.forEach(word -> {
//            ops.add(word, clientId.toString());
//        });
//
//    }
//
//    public void removeSearchWordCache(Long clientId, List<String> words) {
//        var ops = redisTemplate.opsForSet();
//
//        words.forEach(word -> {
//            ops.remove(word, clientId.toString());
//        });
//    }
//
//    // 유저가 특정 키워드로 검색하면 clientId 조회
//    public List<Long> getClientIdBySearchWordCache(String word) {
//        var ops = redisTemplate.opsForSet();
//
//        //.members = Redis의 Set 자료구조에서 특정 키(key)에 저장된 모든 값을 가져오는 메서드
//        // key(word), value(clientId)
//        var values = ops.members(word);
//
//        if (values == null) {
//            return Collections.emptyList();
//        } else {
//            return values.stream().map(Long::parseLong).toList();
//        }
//    }
//
//
////    public void save(Long roomId, ChatCache chat) {
////        if (Objects.isNull(chat) || Objects.isNull(chat.getChatId())) {
////            log.error("Required values must not be null");
////            return;
////        }
////
////        try {
////            hashOperations.put(CACHE_KEY + String.valueOf(roomId),
////                    String.valueOf(chat.getChatId()),
////                    serializeDto(chat));
////            log.info("[RedisTemplateService save success] id: {}", chat.getChatId());
////        } catch (JsonProcessingException e) {
////            log.error("[RedisTemplateService save error] {}", e.getMessage());
////        }
////    }
////
////    public Map<Long, ChatCache> findByRoomAndChat(Long roomId, Long chatId) {
////        try {
////            Map<String, String> entries = hashOperations.entries(CACHE_KEY + roomId);
////            Map<String, String> sortedMap = new TreeMap<>(entries);
////            Map<Long, ChatCache> chats = new HashMap<>();
////            for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
////                Long chatCacheKey = Long.parseLong(entry.getKey());
////                if (chatCacheKey <= chatId) {
////                    ChatCache chatCache = deserializeDto(entry.getValue());
////                    chats.put(chatCacheKey, chatCache);
////                }
////            }
////            return chats;
////
////        } catch (JsonProcessingException e) {
////            log.error("[RedisTemplateService findByRoomAndChat error]: {}", e.getMessage());
////            return null;
////        }
////    }
////
////    public void delete(Long roomId, Long chatId) {
////        hashOperations.delete(CACHE_KEY + roomId, String.valueOf(chatId));
////        log.info("[RedisTemplateService delete]: {}", chatId);
////    }
////
////    public boolean existByRoomAndChat(Long roomId, Long chatId) {
////        String chatCache = hashOperations.get(CACHE_KEY + roomId, String.valueOf(chatId));
////        return chatCache != null;
////    }
////
////    private String serializeDto(ChatCache chat) throws JsonProcessingException {
////        return objectMapper.writeValueAsString(chat);
////    }
////
////    private ChatCache deserializeDto(String value) throws JsonProcessingException {
////        return objectMapper.readValue(value, ChatCache.class);
////    }
//
//
//    }
