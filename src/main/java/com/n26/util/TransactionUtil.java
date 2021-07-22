package com.n26.util;

import com.n26.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;


@Service
public class TransactionUtil {
    private final ConcurrentNavigableMap<Instant, List<Transaction>> transactions = new ConcurrentSkipListMap<>(Instant::compareTo);

    public ConcurrentNavigableMap<Instant, List<Transaction>> getTransactions() {
        return transactions;
    }

    /**
     * The method can be used to add new {@link Transaction} to the map
     *
     * @param transaction represents the Transaction
     * @param instant represents the time instant
     */
    public void addTransaction(Transaction transaction, Instant instant) {
        if (transactions.containsKey(instant))
            transactions.get(instant).add(transaction);
        else {
            List<Transaction> transactionList = new ArrayList<>();
            transactionList.add(transaction);
            transactions.put(instant, transactionList);
        }
    }

    /**
     * he method can be used to clear the map
     */
    public void deleteTransactions() {
        transactions.clear();
    }
}
