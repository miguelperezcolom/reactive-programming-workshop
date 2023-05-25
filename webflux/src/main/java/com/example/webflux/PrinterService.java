package com.example.webflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrinterService {

    public void print(String msg) {
        log.info(msg);
    }

}
