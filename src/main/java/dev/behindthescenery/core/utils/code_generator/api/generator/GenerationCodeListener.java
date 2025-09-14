package dev.behindthescenery.core.utils.code_generator.api.generator;

import dev.behindthescenery.core.utils.code_generator.api.FunctionCodeElement;

public interface GenerationCodeListener {

    void generateFragment(FunctionCodeElement function, GenerationCodeContext context);

}
