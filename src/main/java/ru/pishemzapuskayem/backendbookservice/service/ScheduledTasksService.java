package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduledTasksService {
    private final TaskScheduler taskScheduler;

    /**
     * не для задач с большим объёмом данных
     * по хорошему нужно в бд аргументы хранить в виде json например
     */
    public void scheduleTask(TimeUnit timeUnit, int units, Runnable runnable) {
        ZonedDateTime dateTime = ZonedDateTime.now().plus(units, timeUnit.toChronoUnit());
        taskScheduler.schedule(runnable, dateTime.toInstant());
    }
}
