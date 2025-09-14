package dev.behindthescenery.core.utils.code_generator.api;

import java.util.Collection;

public interface CodeChangeFunction {

    void addCodeElement(CodeElement element);

    void addCodeElement(Collection<? extends CodeElement> element);
}
