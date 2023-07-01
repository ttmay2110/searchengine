package searchengine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import searchengine.services.StatisticsService;
import searchengine.services.responses.StatisticResponseService;

@Controller
public class StatisticController {

    private final StatisticsService statistic;

    public StatisticController(StatisticsService statistic) {
        this.statistic = statistic;
    }

    @GetMapping("/api/statistics")
    public ResponseEntity<Object> getStatistics() {
        return ResponseEntity.ok(statistic.getStatistics());
    }
}
