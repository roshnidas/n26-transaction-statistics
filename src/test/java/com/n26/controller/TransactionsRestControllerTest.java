/**
 *
 */
package com.n26.controller;

import com.n26.model.Statistics;
import com.n26.model.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TransactionsRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetStats() {

        assertEquals(HttpStatus.CREATED,
                restTemplate.postForEntity("/transactions",
                        new Transaction("100.0", Instant.now().minusSeconds(10).toString()), Object.class)
                        .getStatusCode());
        assertEquals(HttpStatus.CREATED, restTemplate.postForEntity("/transactions",
                new Transaction("100.0", Instant.now().toString()), Object.class).getStatusCode());
        assertEquals("200.00",
                restTemplate.getForEntity("/statistics", Statistics.class).getBody().getSum());
    }

    @Test
    public void testGetStatsForOldTimestamp() {

        assertEquals(HttpStatus.NO_CONTENT,
                restTemplate.postForEntity("/transactions",
                        new Transaction("100.0", Instant.now().minusSeconds(70).toString()), Object.class)
                        .getStatusCode());
        assertEquals(HttpStatus.CREATED, restTemplate.postForEntity("/transactions",
                new Transaction("100.0", Instant.now().toString()), Object.class).getStatusCode());
        assertEquals("100.00",
                restTemplate.getForEntity("/statistics", Statistics.class).getBody().getSum());
    }

    @Test
    public void testForInvalidAmount() {

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY,
                restTemplate.postForEntity("/transactions",
                        new Transaction("", Instant.now().minusSeconds(20).toString()), Object.class)
                        .getStatusCode());
    }

    @Test
    public void testForFutureTimestamp() {

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY,
                restTemplate.postForEntity("/transactions",
                        new Transaction("100", Instant.now().plusSeconds(20).toString()), Object.class)
                        .getStatusCode());
    }

    @Test
    public void testForDeleteTransaction() {

        ResponseEntity<String> exchange = restTemplate.exchange(
                "/transactions",
                HttpMethod.DELETE,
                new HttpEntity<String>(""),
                String.class);
        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
    }

}
