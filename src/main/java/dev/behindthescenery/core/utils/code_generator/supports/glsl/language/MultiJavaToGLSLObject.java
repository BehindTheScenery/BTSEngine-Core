package dev.behindthescenery.core.utils.code_generator.supports.glsl.language;

import java.util.function.Function;

public class MultiJavaToGLSLObject implements JavaToGlslConverter {

    private final Function<Object, String> convertToGLSL;
    protected Class<?>[] classes;
    protected GLSLType glslType;


    public MultiJavaToGLSLObject(GLSLType glslType, Class<?>[] classes, Function<Object, String> convertToGLSL) {
        this.classes = classes;
        this.glslType = glslType;
        this.convertToGLSL = convertToGLSL;
    }

    @Override
    public String invoke(Object obj) {
        return convertToGLSL.apply(obj);
    }

    @Override
    public boolean isCurrent(Object obj) {
        return isCurrent(obj.getClass());
    }

    @Override
    public boolean isCurrent(Class<?> cls) {
        for (Class<?> aClass : classes) {
            if(aClass.equals(cls)) return true;
        }
        return false;
    }

    @Override
    public Object createDefault(Class<?> cls) {
        return null;
    }

    @Override
    public Class<?>[] getSupportedClasses() {
        return classes;
    }

    @Override
    public GLSLType getGlslType() {
        return glslType;
    }
}