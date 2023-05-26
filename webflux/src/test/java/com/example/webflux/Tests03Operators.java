package com.example.webflux;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.function.Function;

/**
 * basic tests, for realizing that you need to subscribe. If not, nothing actually happens
 */
@SpringBootTest
public class Tests03Operators {

    @SpyBean
    PrinterService printerService;

    @Test
    public void filterWorksAsExpected() {
        //given
        Flux<Integer> flux = Flux
                .range(1, 10) //1..10
                //todo: add a filter which only accepts even names (e.g. i % 2 == 0)
                //.filter(i -> i % 2 == 0)
                ;
        //when

        //then
        StepVerifier.create(flux)
                .expectNext(2, 4, 6, 8, 10)
                .verifyComplete();
    }

    @Test
    public void mapWorksAsExpected() {
        //given
        Flux<Integer> flux = Flux
                .range(1, 5) //1..10
                //todo: map each element x to x * 2
                //.map(i -> i * 2)
                ;
        //when

        //then
        StepVerifier.create(flux)
                .expectNext(2, 4, 6, 8, 10)
                .verifyComplete();
    }

    @Test
    public void flatMapWorksAsExpected() {
        //given
        Flux<Object> flux = Flux
                .just(List.of(2, 4, 6), List.of(8, 10)) //1..10
                //todo: convert to a list of integers (just uncomment below if not sure about how to do it)
                //.map(l -> Flux.fromStream(l.stream()))
                //.flatMap(l -> l)
                ;
        //when

        //then
        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(2, 4, 6, 8, 10)
                .verifyComplete();
    }

    @Test
    public void concatWorksAsExpected() {
        //given
        Flux<Integer> lastNumbers = Flux.just(8, 10);
        Flux<Integer> flux = Flux.just(2, 4, 6)
                //todo: add last numbers to this flux, at the end of the sequence
                //.concatWith(lastNumbers)
                ;
        //when

        //then
        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(2, 4, 6, 8, 10)
                .verifyComplete();
    }

    @Test
    public void zipWorksAsExpected() {
        //given
        Flux<Integer> evenNumbers = Flux.just(2, 4, 6, 8, 10);
        Flux<Integer> oddNumbers = Flux.just(1, 3, 5, 7, 9);
        Flux<Integer> flux = Flux.just(0);
        //todo: replace the flux by one adding one even number to one odd number
        //flux = Flux.zip(evenNumbers, oddNumbers, (a, b) -> a + b);
                ;
        //when

        //then
        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(3, 7, 11, 15, 19)
                .verifyComplete();
    }

    @Test
    public void reduceWorksAsExpected() {
        //given
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);
        Mono<Integer> mono = Mono.just(0);
        //todo: replace the mono by one with the result of the sum of the numbers in the flux
        //mono = flux.reduce((a, b) -> a + b);

        //when

        //then
        StepVerifier.create(mono)
                .expectSubscription()
                .expectNext(15)
                .verifyComplete();
    }

    @Test
    public void distinctWorksAsExpected() {
        //given
        Flux<Integer> flux = Flux.just(1, 2, 1, 3, 3, 3, 4, 1, 5)
                //todo: remove repeated elements from the flux
                //.distinct()
                ;

        //when

        //then
        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }

}
