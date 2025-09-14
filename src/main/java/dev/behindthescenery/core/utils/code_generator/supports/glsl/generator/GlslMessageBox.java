package dev.behindthescenery.core.utils.code_generator.supports.glsl.generator;

import dev.behindthescenery.core.utils.code_generator.api.generator.GeneratorCodeMessageBox;

public class GlslMessageBox implements GeneratorCodeMessageBox {
    @Override
    public void info(String message, Object... arg) {
        System.out.println(message);
    }

    @Override
    public void debug(String message, Object... arg) {
        System.out.println(message);
    }

    @Override
    public void warn(String message, Object... arg) {
        System.out.println(message);
    }

    @Override
    public void error(String message, Object... arg) {
        System.out.println(message);
    }
}
