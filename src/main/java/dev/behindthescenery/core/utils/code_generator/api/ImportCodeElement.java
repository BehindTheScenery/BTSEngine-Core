package dev.behindthescenery.core.utils.code_generator.api;

public interface ImportCodeElement extends CodeElement {

    String getPrefix();

    String getName();

    boolean useEndSymbol();

    default int getPriority() {
        return 0;
    }
}
