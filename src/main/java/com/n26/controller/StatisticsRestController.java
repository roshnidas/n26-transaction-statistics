package com.n26.controller;

import com.n26.model.Statistics;
import com.n26.util.StatisticsCalculatorUtil;
import com.n26.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/")
public class StatisticsRestController {

    @Autowired
    TransactionUtil transactionUtil;

    @RequestMapping(value = "statistics", method = RequestMethod.GET)
    public Statistics getStatistics() {
        return StatisticsCalculatorUtil.getStatisticsData(Instant.now(), transactionUtil);
    }
}
