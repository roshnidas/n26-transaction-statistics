package com.n26.controller;

import com.n26.model.Transaction;
import com.n26.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeParseException;


@RestController
@RequestMapping("/")
public class TransactionsRestController {

    @Autowired
    TransactionUtil transactionUtil;

    @RequestMapping(value = "transactions", method = RequestMethod.POST)
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {

        try {
            new BigDecimal(transaction.getAmount());
            Instant.parse(transaction.getTimestamp());
        } catch (NumberFormatException | DateTimeParseException | NullPointerException ex) {
            return new ResponseEntity<Transaction>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Instant transactionInstant = Instant.parse(transaction.getTimestamp());

        if (transactionInstant.isBefore(Instant.now().minusSeconds(60))) {
            return new ResponseEntity<Transaction>(HttpStatus.NO_CONTENT);
        }

        if (transactionInstant.isAfter(Instant.now())) {
            return new ResponseEntity<Transaction>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        transactionUtil.addTransaction(transaction, Instant.parse(transaction.getTimestamp()));
        return new ResponseEntity<Transaction>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "transactions", method = RequestMethod.DELETE)
    public ResponseEntity<Transaction> deleteTransaction() {
        transactionUtil.deleteTransactions();
        return new ResponseEntity<Transaction>(HttpStatus.NO_CONTENT);
    }
}