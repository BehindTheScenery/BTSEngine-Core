package dev.behindthescenery.core.utils.code_generator.api.generator;

public interface GeneratorCodeMessageBox {

    void info(String message, Object... arg);

    void debug(String message, Object... arg);

    void warn(String message, Object... arg);

    void error(String message, Object... arg);
}
