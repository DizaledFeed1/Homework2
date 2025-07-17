package org.example.synthetichumancorestarter.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.example.synthetichumancorestarter.anotation.WeylandWatchingYou;
import org.example.synthetichumancorestarter.dto.CommandDTO;
import org.example.synthetichumancorestarter.dto.PriorityType;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Slf4j
@Component
public class CommandService {

    private final ThreadPoolExecutor commonExecutor;
    private Map<String, List<CommandDTO>> doneCommands = new HashMap<>();
    private MeterRegistry meterRegistry;

    private final  AtomicInteger  testCounter;


    public CommandService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.commonExecutor = new ThreadPoolExecutor(
                1, 1,
                0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                new ThreadPoolExecutor.AbortPolicy()
        );
        testCounter = meterRegistry.gauge("queue_size", new AtomicInteger(0));
    }

    @WeylandWatchingYou
    public void commandAdd(CommandDTO commandDTO) {
        Runnable task = () -> {
            try {
                Thread.sleep(10000); // имитация выполнения задач, для заполнения очереди
                Counter.builder("done_commands_counter")
                        .tags("author", commandDTO.getAuthor())
                        .register(meterRegistry)
                        .increment();

                doneCommands.computeIfAbsent(commandDTO.getAuthor(), k -> new ArrayList<>()).add(commandDTO);

                log.info("description: " + commandDTO.getDescription() +
                        " priority: " + commandDTO.getPriority() +
                        " author: " + commandDTO.getAuthor() +
                        " time: " + commandDTO.getTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Task interrupted", e);
            }
        };

        if (commandDTO.getPriority() == PriorityType.CRITICAL) {
            task.run();
        } else {
            try {
                commonExecutor.submit(task);
            } catch (RejectedExecutionException e) {
                log.error("Queue is full, task rejected");
                throw new RuntimeException("Очередь переполнена, задача отклонена", e);
            }
        }
    }

    @WeylandWatchingYou
    public void commandPurge(){
        commonExecutor.getQueue().clear();
    }

    @Scheduled(fixedRateString = "1000", initialDelayString = "0")
    public void commandCount() {
        int queueSize = commonExecutor.getQueue().size();
        testCounter.set(queueSize);
    }

    @WeylandWatchingYou
    public Map<String, Long> getDoneCommands() {
        Map<String, Long> countByAuthor = doneCommands.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> (long) e.getValue().size()
                ));
        return countByAuthor;
    }
}
