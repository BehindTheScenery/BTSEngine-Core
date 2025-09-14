package dev.behindthescenery.core.system.logger;

import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Логер который хранит историю событий для каждого потока с возможность сохранения в файл как один общий так и на несколько мелких
 */
public class ThreadLogger {
    public static final Path DEFAULT_PATH = FMLPaths.GAMEDIR.get().resolve("threads_log.txt");

    protected final boolean useTime;
    protected final boolean printToConsole;
    protected boolean active = true;
    @Nullable protected Logger logger;
    protected final Lock fileWriteLock = new ReentrantLock();

    protected final GlobalThreadData<Data> threadsExecuteData = new GlobalThreadData<>(Data::new);

    public ThreadLogger() {
        this(true, false);
    }

    public ThreadLogger(boolean useTime, boolean printToConsole) {
        this.useTime = useTime;
        this.printToConsole = printToConsole;
    }

    public ThreadLogger log(String message) {

        if(active) {
            String str = addThreadInfo(message);
            threadsExecuteData.get().addMessage(new SimpleMessage(str));
            printMessage(str);
        }
        return this;
    }

    public ThreadLogger setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public ThreadLogger setActive(boolean value) {
        this.active = value;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    protected void printMessage(String message) {
        if (printToConsole) {
            if (logger == null) {
                System.out.println(message);
            } else {
                logger.info(message);
            }
        }
    }

    /**
     * Записывает все логи в файл (по умолчанию в threads_log.txt)
     */
    public void writeToFile() throws IOException {
        writeToFile(DEFAULT_PATH.toFile());
    }

    /**
     * Записывает логи в указанный файл
     */
    public void writeToFile(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8, true)) {
            writeToFile(writer);
        }
    }

    /**
     * Записывает логи через предоставленный Writer
     */
    public void writeToFile(Writer writer) throws IOException {
        fileWriteLock.lock();
        try {
            boolean start = true;

            for (Map.Entry<Thread, Optional<Data>> entry :
                    threadsExecuteData.getAllThreadValues().entrySet()) {
                if (entry.getValue().isPresent()) {
                    if(!start) {
                        writer.write(System.lineSeparator());
                        writer.write(System.lineSeparator());
                    } else start = false;

                    Data dataHistory = entry.getValue().get();
                    for (IMessage msg : dataHistory.history) {
                        writer.write(msg.getString() + System.lineSeparator());
                    }
                }
            }
            writer.flush();
        } finally {
            fileWriteLock.unlock();
        }
    }

    /**
     * Записывает лони потоков для каждого потока в отдельный файл
     * @param logDirectory Папка в которой будут созданы логи
     */
    public void writeThreadLogsToSeparateFiles(Path logDirectory) throws IOException {
        fileWriteLock.lock();
        try {
            for (Map.Entry<Thread, Optional<Data>> entry :
                    threadsExecuteData.getAllThreadValues().entrySet()) {
                Thread thread = entry.getKey();
                String fileName = logDirectory + File.separator + "thread_" + thread.getId() + ".log";
                try (FileWriter writer = new FileWriter(fileName, StandardCharsets.UTF_8, true)) {
                    if (entry.getValue().isPresent()) {
                        Data dataHistory = entry.getValue().get();
                        for (IMessage msg : dataHistory.history) {
                            writer.write(msg.getString() + System.lineSeparator());
                        }
                    }
                }
            }
        } finally {
            fileWriteLock.unlock();
        }
    }

    protected String addThreadInfo(String message) {
        return Thread.currentThread() + " " + addTimeToMessage(message);
    }

    protected String addTimeToMessage(String message) {
        if (useTime) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(new Date());
            return "[" + timestamp + "] " + message;
        }
        return message;
    }

    protected static class Data {
        protected final List<IMessage> history;

        public Data() {
            this.history = new ArrayList<>();
        }

        public Data addMessage(IMessage message) {
            this.history.add(message);
            return this;
        }
    }

    protected record SimpleMessage(String message) implements IMessage {

        @Override
        public String getString() {
            return message;
        }
    }

    protected interface IMessage {
        String getString();
    }
}
