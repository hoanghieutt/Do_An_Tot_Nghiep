package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.repository.EvaluateRepository;
import com.poly.datn.sd18.service.EvaluateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluateServiceImpl implements EvaluateService {
    private final EvaluateRepository evaluateRepository;
}
