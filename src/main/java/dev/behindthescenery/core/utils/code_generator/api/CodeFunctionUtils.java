package dev.behindthescenery.core.utils.code_generator.api;

import dev.behindthescenery.core.utils.code_generator.basic.FunctionCodeElementImpl;

public class CodeFunctionUtils {

//    public static <T> FunctionCodeElementImpl fun(String name, T element) {}



    public static class FunParam<T> {
        protected String valueVar;
        protected Class<T> classValue;
        protected T defaultValue = null;

        public FunParam(String valueVar, Class<T> classValue) {
            this.valueVar = valueVar;
            this.classValue = classValue;
        }
    }
}
