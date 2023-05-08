package com.example.demo.Config.Scheduled;

import com.example.demo.BackEnd.Repository.TokenRepository;
import com.example.demo.entity.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.List;

@EnableAsync
@RequiredArgsConstructor
@Slf4j
@Component
public class CheckExpToken {
    private static final long EXPIRATION = 864000000;

    @Autowired
    private TokenRepository tokenRepository;


    @Scheduled(cron = "0 0/1 * * * *")
    public void checkExpTokenJob() {
        log.info("time to run log:{}", new Date());
        List<Token> tokensExp = tokenRepository.findAllByExpirationIsAfter(new Date(System.currentTimeMillis() + EXPIRATION));
        tokenRepository.deleteAll(tokensExp);
        log.info("number token to delete : {}", tokensExp.size());
    }
}
