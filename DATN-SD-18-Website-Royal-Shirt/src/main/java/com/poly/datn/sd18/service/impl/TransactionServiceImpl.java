package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.repository.TransactionRepository;
import com.poly.datn.sd18.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
}
