package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class InfoService {
    private final Logger logger = LoggerFactory.getLogger(InfoService.class);

    public Integer getReduce() {
        long startTime = System.nanoTime();
        int sum = IntStream.iterate(1, a -> a + 1).
                limit(1_000_000).
                sum();
        long endTime = System.nanoTime();
        logger.info("{}", endTime - startTime);
        return sum;
    }
}