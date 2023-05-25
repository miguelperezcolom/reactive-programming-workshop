package com.example.webflux;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * basic tests, for realizing that you need to subscribe. If not, nothing actually happens
 */
@SpringBootTest
public class Tests01Basic {

    @SpyBean
    PrinterService printerService;

    @Test
    public void runs() {
        //given
        Mono<String> mono = Mono
                .just("Hola")
                .doOnSuccess(s -> printerService.print(s));
        //when

        //then
        Mockito.verify(printerService).print("Hola");
    }

    @Test
    public void runsForEachElement() {
        //given
        Flux<String> flux = Flux
                .just("Hola", "Mundo")
                .doOnNext(s -> printerService.print(s));
        //when

        //then
        Mockito.verify(printerService).print("Hola");
        Mockito.verify(printerService).print("Mundo");
    }

}
