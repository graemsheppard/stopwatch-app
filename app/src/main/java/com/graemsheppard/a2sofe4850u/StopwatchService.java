package com.graemsheppard.a2sofe4850u;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class StopwatchService {

    private AtomicLong elapsedMillis;

    private AtomicBoolean runClock;

    private Thread clock;

    private static StopwatchService instance;

    private List<Observer> subscribers;

    private StopwatchService() {
        elapsedMillis = new AtomicLong(0);
        runClock = new AtomicBoolean(false);
        subscribers = new ArrayList<>();
    }

    public static StopwatchService getInstance() {
        if (instance == null) {
            instance = new StopwatchService();
        }
        return instance;
    }

    public void reset() {
        runClock.set(false);
        elapsedMillis.set(0);
        notifySubscribers();
    }

    public void start() {
        runClock.set(true);
        clock = new Thread(this::counter);
        clock.start();
    }

    public void stop() {
        runClock.set(false);
    }

    public void subscribe(Observer observer) {
        subscribers.add(observer);
    }

    public String millisToString(long millis) {
        long seconds = (int)(millis / 1000 % 60);
        long minutes = (int)(millis / 60000);
        long ms = (int)(millis / 10 % 100);
        return String.format(Locale.CANADA, "%02d:%02d.%02d", minutes, seconds, ms);
    }

    public long getMillis() {
        return this.elapsedMillis.get();
    }

    public boolean isRunning() {
        return runClock.get();
    }

    private void notifySubscribers() {
        subscribers.forEach(s -> s.onChange(elapsedMillis.get()));
    }

    private void counter () {
        while(runClock.get()) {
            long start = System.currentTimeMillis();
            try {
                notifySubscribers();
                Thread.sleep(10);
                if (runClock.get()) {
                    elapsedMillis.addAndGet(System.currentTimeMillis() - start);
                }
            } catch (InterruptedException ignored) {

            }
        }
        notifySubscribers();
    }

    public interface Observer {
        void onChange(long millis);
    }
}
