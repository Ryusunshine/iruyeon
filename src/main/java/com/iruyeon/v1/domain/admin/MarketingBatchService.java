//package com.iruyeon.v1.domain.admin;
//
//import com.iruyeon.v1.domain.member.entity.Member;
//import jakarta.persistence.EntityManagerFactory;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.database.JpaPagingItemReader;
//import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Configuration
//@Slf4j
//@EnableBatchProcessing
//@RequiredArgsConstructor
//public class MarketingBatchConfig {
//
//    private final EntityManagerFactory entityManagerFactory;
//
//    @Bean
//    public Job marketingJob(JobRepository jobRepository, Step marketingStep) {
//        return new JobBuilder("marketingJob", jobRepository)
//                .start(marketingStep)
//                .build();
//    }
//
//    @Bean
//    public Step marketingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("marketingStep", jobRepository)
//                .<Member, Member>chunk(1000, transactionManager)  // 한 번에 1000개씩 처리
//                .reader(memberReader())
//                .processor(memberProcessor())
//                .writer(memberWriter())
//                .build();
//    }
//
//    @Bean
//    public JpaPagingItemReader<Member> memberReader() {
//        return new JpaPagingItemReaderBuilder<Member>()
//                .name("memberReader")
//                .entityManagerFactory(entityManagerFactory)
//                .queryString("SELECT m FROM Member m WHERE m.active = true")
//                .pageSize(1000) // 페이징으로 데이터 로드
//                .build();
//    }
//
//    @Bean
//    public ItemProcessor<Member, Member> memberProcessor() {
//        return member -> {
//            notificationService.sendNotification(member);
//            return member;
//        };
//    }
//
//    @Bean
//    public ItemWriter<Member> memberWriter() {
//        return members -> log.info("{}명의 마케팅 알림 발송 완료", members.size());
//    }
//}
