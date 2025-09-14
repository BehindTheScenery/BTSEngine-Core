package dev.behindthescenery.core.system.profiler;

import dev.behindthescenery.core.BtsCore;
import net.neoforged.fml.loading.FMLPaths;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Простой логер для записи ошибок в файл
 */
public class ErrorThreadingDetector {
    public static final Path DEFAULT_PATH = FMLPaths.GAMEDIR.get().resolve("threadingErrors.log");

    protected BlockingQueue<MessageDetector> messages = new LinkedBlockingQueue<>();

    /**
     * Путь по которому будет записан журнал, если null берётся путь из {@link ErrorThreadingDetector#DEFAULT_PATH}
     */
    public final @Nullable Path filePatch;

    public ErrorThreadingDetector() {
        this(null);
    }

    public ErrorThreadingDetector(Path filePatch) {
        this.filePatch = filePatch;
    }

    /**
     * Добавляет ошибку в журнал
     */
    public void addMessage(MessageDetector messageDetector) {
        if(!messages.add(messageDetector)) {
            BtsCore.LOGGER.error("Can't add message to ErrorThreadingDetector");
        }
    }

    /**
     * Записывает данные в файл указаный в поле {@link ErrorThreadingDetector#filePatch}
     * @throws IOException если файла не было и его нужно было создать но не удалось этого сделать
     */
    public void report() throws IOException {
        Path path = filePatch == null ? DEFAULT_PATH : filePatch;

        if(!path.toFile().exists())
            path.toFile().createNewFile();

        try(PrintWriter writer = new PrintWriter(path.toFile())){
            writer.write(messagesToString());
        }
    }

    protected String messagesToString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Error History").append("\n").append("\n");

        int i = 0;
        for (MessageDetector message : messages) {
            builder.append("[").append(i).append("] ").append(message).append("\n").append("\n");
        }

        return builder.toString();
    }

    public static class MessageDetector {

        protected String errorName;
        protected @Nullable Exception exception;
        protected Thread executorThread;

        public MessageDetector(String errorName) {
            this(errorName, null, Thread.currentThread());
        }

        public MessageDetector(String errorName, Thread executorThread) {
            this(errorName, null, executorThread);
        }

        public MessageDetector(String errorName, Exception e) {
            this(errorName, e, Thread.currentThread());
        }

        public MessageDetector(String errorName, @Nullable Exception e, Thread executorThread) {
            this.errorName = errorName;
            this.exception = e;
            this.executorThread = executorThread;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            printName(builder);
            printThread(builder);
            printStacktrace(builder);
            return builder.toString();
        }

        protected void printName(StringBuilder builder) {
            builder.append("Error Name: ").append(errorName).append("\n");
        }

        protected void printThread(StringBuilder builder) {
            builder.append("Thread: ").append(executorThread).append("\n");
        }

        protected void printStacktrace(StringBuilder builder) {
            if(exception != null) {
                builder.append("StackTrace: ").append("\n").append(printStackTrace(exception)).append("\n");
            }
        }

        public static StringBuilder printStackTrace(Throwable s){
            StringBuilder strBuilder = new StringBuilder();
            for (StackTraceElement stackTraceElement : s.getStackTrace()) {
                strBuilder.append("\t").append(" ").append("at").append(" ").append(stackTraceElement).append("\n");
            }

            for (Throwable throwable : s.getSuppressed()) {
                strBuilder.append(printStackTrace(throwable));
            }

            Throwable ourCause = s.getCause();
            if(ourCause != null){
                strBuilder.append(printStackTrace(ourCause));
            }

            return strBuilder;
        }
    }

}
