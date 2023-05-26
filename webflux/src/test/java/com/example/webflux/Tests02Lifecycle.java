package com.example.webflux;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

/**
 * we have several lifecycle callbacks we can use for performing actions without "touching" the events flow.
 * E.g. doOnSubscribe, doOnSuccess, doOnNext, ...
 */
@SpringBootTest
public class Tests02Lifecycle {

    @SpyBean
    PrinterService printerService;

    @Test
    public void onSubscriptionWorks() {
        //given
        Mono<String> mono = Mono
                .just("Hola")
                //.doOnSubscribe(s -> printerService.print("suscrito"))
                ;

        //when
        mono.subscribe();

        //then
        Mockito.verify(printerService).print("suscrito");
    }

    @Test
    public void onNextWorks() {
        //given
        Flux<String> flux = Flux
                .just("Hola", "Mundo")
                // .doOnNext(s -> printerService.print(s))
                ;

        //when
        flux.subscribe();

        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(printerService, times(2)).print(captor.capture());
        assertThat(captor.getAllValues()).contains("Hola", "Mundo");
    }

    @Test
    public void onCompleteWorks() {
        //given
        Flux<String> flux = Flux
                .just("Hola", "Mundo")
                //.doOnComplete(() -> printerService.print("Hecho"))
                ;

        //when
        flux.subscribe();

        //then
        Mockito.verify(printerService).print("Hecho");
    }

    @Test
    public void onErrorWorks() {
        //given
        Flux<String> flux = Flux
                .just("Hola", "Mundo")
                .map(s -> {
                    if ("Mundo".equals(s)) {
                        throw new RuntimeException("error x");
                    }
                    return s;
                })
                //.doOnError(t -> printerService.print(t.getMessage()))
                ;

        //when
        flux.subscribe();

        //then
        Mockito.verify(printerService).print("error x");
    }

    @Test
    public void onCancelIsCalledWhenDisposed() throws InterruptedException {
        //given
        Flux<String> flux = Flux
                .just("Hola", "Mundo")
                .delayElements(Duration.ofSeconds(10))
                //.doOnCancel(() -> printerService.print("cancelled"))
                ;

        //when
        Disposable subscription = flux.subscribe();
        subscription.dispose();

        //then
        Mockito.verify(printerService).print("cancelled");
    }

    @Test
    public void doFinallyIsCalledWhenDisposed() throws InterruptedException {
        //given
        Flux<String> flux = Flux
                .just("Hola", "Mundo")
                .delayElements(Duration.ofSeconds(10))
                //.doFinally(s -> printerService.print(s.name()))
                ;

        //when
        Disposable subscription = flux.subscribe();
        subscription.dispose();

        //then
        Mockito.verify(printerService).print(SignalType.CANCEL.name());
    }

}
