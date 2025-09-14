package dev.behindthescenery.core.utils.code_generator.supports.glsl.language;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.joml.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GLSLLanguageContext {

    protected List<JavaToGeneratorObjectConverter> convertersForObject = new ArrayList<>();

    public GLSLLanguageContext() {
        registerConverters();
    }

    public void registerConverters() {
        registerConverter(GLSLType.INT, Integer.class, Object::toString);
        registerConverter(GLSLType.BOOL, Boolean.class, Object::toString);
        registerConverter(GLSLType.FLOAT, Float.class, Object::toString);
        registerConverter(GLSLType.DOUBLE, Double.class, Object::toString);
        registerConverter(GLSLType.VOID, Void.class, s -> "void");

        registerConverter(GLSLType.DVEC3, Vector3d.class, s -> "dec3(" + s.x + ", " + s.y + ", " + s.z + ")");
        registerConverter(GLSLType.DVEC3, Vec3d.class, s -> "dec3(" + s.x + ", " + s.y + ", " + s.z + ")");
        registerConverter(GLSLType.VEC3, Vector3f.class, s -> "vec3(" + s.x + ", " + s.y + ", " + s.z + ")");
        registerConverter(GLSLType.IVEC3, Vector3i.class, s -> "ivec3(" + s.x + ", " + s.y + ", " + s.z + ")");
        registerConverter(GLSLType.IVEC3, Vec3i.class, s -> "ivec3(" + s.getX() + ", " + s.getY() + ", " + s.getZ() + ")");

        registerConverter(GLSLType.VEC4 , Vector4f.class, s -> "vec4(" + s.x + ", " + s.y + ", " + s.z + ", " + s.w + ")");
        registerConverter(GLSLType.DVEC4, Vector4d.class, s -> "dvec4(" + s.x + ", " + s.y + ", " + s.z + ", " + s.w + ")");
        registerConverter(GLSLType.IVEC4, Vector4i.class, s -> "ivec4(" + s.x + ", " + s.y + ", " + s.z + ", " + s.w + ")");

    }

    public <T> JavaToGLSLObject<T> registerConverter(GLSLType glslType, Class<T> classObject, Function<T, String> convertToGLSL) {
        JavaToGLSLObject<T> jg = new JavaToGLSLObject<>(classObject, glslType, convertToGLSL);
        convertersForObject.add(jg);
        return jg;
    }

    public MultiJavaToGLSLObject registerConverter(GLSLType glslType, Class<?>[] classes, Function<Object, String> convertToGLSL) {
        MultiJavaToGLSLObject jg = new MultiJavaToGLSLObject(glslType, classes, convertToGLSL);
        convertersForObject.add(jg);
        return jg;
    }

    public GLSLType getGlslType(Class<?> cls) {
        final Class<?> cl = primitiveConverter(cls);

        for (JavaToGeneratorObjectConverter converter : convertersForObject) {
            if (converter.isCurrent(cl)) {
                if(converter instanceof JavaToGlslConverter glslObject) {
                    return glslObject.getGlslType();
                }
            }


        }

        return null;
    }

    public String convertToGlsl(Object obj) {
        for (JavaToGeneratorObjectConverter converter : convertersForObject) {
            if(converter.isCurrent(obj)) {
                return converter.invoke(obj);
            }
        }

        return "unknown";
    }

    protected Class<?> primitiveConverter(Class<?> cls) {
        if (cls == void.class) return Void.class;
        if (cls == int.class) return Integer.class;
        if (cls == byte.class) return Byte.class;
        if (cls == float.class) return Float.class;
        if (cls == double.class) return Double.class;
        if (cls == long.class) return Long.class;
        if (cls == short.class) return Short.class;
        if (cls == char.class) return Character.class;
        if (cls == boolean.class) return Boolean.class;

        return cls;
    }
}
