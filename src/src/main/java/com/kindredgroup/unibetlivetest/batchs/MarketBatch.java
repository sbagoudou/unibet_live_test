package com.kindredgroup.unibetlivetest.batchs;


import com.kindredgroup.unibetlivetest.service.BetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Log4j2
@RequiredArgsConstructor
public class MarketBatch {

    private final BetService betService;

    @Scheduled(fixedRateString = "${close-bets.fixed-rate:5000}")
    public void closeBets() {
        var stopWatch = new StopWatch();
        stopWatch.start();
        var betProcessed = betService.closeBets();
        stopWatch.stop();
        log.info("Closed {} bets in {}ms", betProcessed, stopWatch.getTotalTimeMillis());
    }

}
