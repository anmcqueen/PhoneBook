package ru.nastya.phonebook.aspect;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestCounterAspect {

    private final Counter requestCounter;

    public RequestCounterAspect(MeterRegistry meterRegistry) {
        this.requestCounter = Counter.builder("custom.request.counter")
                .description("Количество входящих запросов")
                .tag("type", "http")
                .register(meterRegistry);
    }

    @Before("execution(* ru.nastya.phonebook.controller.*.*(..))") // Перехват всех методов в контроллерах
    public void countRequest() {
        requestCounter.increment();
    }
}