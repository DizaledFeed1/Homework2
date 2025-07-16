package org.example.synthetichumancorestarter.service;

import lombok.extern.slf4j.Slf4j;
import org.example.synthetichumancorestarter.dto.CommandDTO;
import org.example.synthetichumancorestarter.dto.PriorityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
public class CommandService {

    private final ThreadPoolExecutor commonExecutor;
    private Map<String, List<CommandDTO>> doneCommands = new HashMap<>();

    public CommandService() {
        this.commonExecutor = new ThreadPoolExecutor(
                1, 1,
                0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    public void commandAdd(CommandDTO commandDTO) {
        Runnable task = () -> {
            try {
                Thread.sleep(10000); // имитация выполнения задач, для заполнения очереди
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
                log.error("Queue is full, task rejected", e);
                throw new RuntimeException("Очередь переполнена, задача отклонена", e);
            }
        }
    }

    public int commandCount() {
        return commonExecutor.getQueue().size();
    }

    public Map<String, Long> getDoneCommands() {
        Map<String, Long> countByAuthor = doneCommands.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> (long) e.getValue().size()
                ));
        return countByAuthor;
    }

}
