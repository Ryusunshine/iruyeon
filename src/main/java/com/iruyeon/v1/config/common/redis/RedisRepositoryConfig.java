//package com.iruyeon.v1.config.common.redis;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.JsonTypeInfo;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
//
//@Configuration // 이 클래스가  스프링 설정 파일임을 나타내고 스프링부트가 시작될때 객체 주입한다.
//@EnableRedisRepositories //redis의 Repository 기능을 활성화하고 Redis 기반의 Repository를 자동으로 스캔하고 관리
//public class RedisRepositoryConfig {
//
//    @Value("${spring.redis.host}")
//    private String redisHost;
//
//    @Value("${spring.redis.port}")
//    private int redisPort;
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper()
//                .registerModule(new JavaTimeModule()) // LocalDateTime 직렬화 지원
//                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // ISO-8601 날짜 형식 사용
//                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY) // 모든 필드 직렬화 허용
//                .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY); // 타입 정보를 포함한 직렬화
//    }
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() { //Redis 서버와의 연결을 관리하는 RedisConnectionFactory 빈을 생성
//        //LettuceConnectionFactory를 사용하여 비동기 방식으로 Redis와 연결한다.
//        //Redis 드라이버에는 Lettuce와 Jedis가 있는데, Lettuce는 비동기 방식이라 성능이 더 좋다.
//        return new LettuceConnectionFactory(redisHost, redisPort);
//    }
//
//    //RedisTemplate란?
//    //Redis 데이터를 쉽게 저장하고 조회할 수 있도록 도와주는 스프링 클래스
//    //RedisConnectionFactory를 설정하여 Redis와 연결
//    @Bean
//    public RedisTemplate<?, ?> redisTemplate() {
//        //Redis는 Key-Value 저장소이므로 데이터를 저장할 때 직렬화(Serialization)가 필요하다.
//        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer()); // Key를 문자열로 저장
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer()); //Hash 구조의 Key를 문자열로 저장
//        redisTemplate.setHashValueSerializer(new StringRedisSerializer()); //Hash 구조의 Value를 문자열로 저장
//        return redisTemplate;
//    }
//
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10))  // TTL 설정 (10분)
//                .disableCachingNullValues()        // null 값 캐싱 방지
//                // Key는 문자열(String)로 저장
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                // Value는 JSON 형식으로 직렬화
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//
//        // Redis를 캐시 저장소로 사용하도록 설정
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(cacheConfig)  // 위에서 설정한 cacheConfig 사용
//                .build();
//    }
//}
