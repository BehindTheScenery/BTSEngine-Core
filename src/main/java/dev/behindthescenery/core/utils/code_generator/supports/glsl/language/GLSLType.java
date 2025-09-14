package dev.behindthescenery.core.utils.code_generator.supports.glsl.language;

import java.util.EnumSet;
import java.util.Set;

public enum GLSLType {
    BOOL("bool"),
    BVEC2("bvec2"),
    BVEC3("bvec3"),
    BVEC4("bvec4"),
    DOUBLE("double"),
    DVEC2("dvec2"),
    DVEC3("dvec3"),
    DVEC4("dvec4"),
    FLOAT("float"),
    INT("int"),
    IVEC2("ivec2"),
    IVEC3("ivec3"),
    IVEC4("ivec4"),
    UINT("uint"),
    UVEC2("uvec2"),
    UVEC3("uvec3"),
    UVEC4("uvec4"),
    VEC2("vec2"),
    VEC3("vec3"),
    VEC4("vec4"),
    VOID("void"),
    MAT2("mat2"),
    MAT3("mat3"),
    MAT4("mat4"),
    MAT2X2("mat2x2"),
    MAT2X3("mat2x3"),
    MAT2X4("mat2x4"),
    MAT3X2("mat3x2"),
    MAT3X3("mat3x3"),
    MAT3X4("mat3x4"),
    MAT4X2("mat4x2"),
    MAT4X3("mat4x3"),
    MAT4X4("mat4x4"),
    DMAT2("dmat2"),
    DMAT3("dmat3"),
    DMAT4("dmat4"),
    DMAT2X2("dmat2x2"),
    DMAT2X3("dmat2x3"),
    DMAT2X4("dmat2x4"),
    DMAT3X2("dmat3x2"),
    DMAT3X3("dmat3x3"),
    DMAT3X4("dmat3x4"),
    DMAT4X2("dmat4x2"),
    DMAT4X3("dmat4x3"),
    DMAT4X4("dmat4x4");

    private final String glslName;

    GLSLType(String glslName) {
        this.glslName = glslName;
    }

    public String getGlslName() {
        return glslName;
    }

    @Override
    public String toString() {
        return glslName;
    }

    public static GLSLType findByName(String name) {
        for (GLSLType value : values()) {
            if (value.getGlslName().equals(name)) return value;
        }
        return null;
    }

    private static final EnumSet<GLSLType> SCALAR_TYPES = EnumSet.of(BOOL, INT, UINT, FLOAT, DOUBLE);
    private static final EnumSet<GLSLType> VECTOR_TYPES = EnumSet.of(
            BVEC2, BVEC3, BVEC4,
            IVEC2, IVEC3, IVEC4,
            UVEC2, UVEC3, UVEC4,
            VEC2, VEC3, VEC4,
            DVEC2, DVEC3, DVEC4
    );
    private static final EnumSet<GLSLType> MATRIX_TYPES = EnumSet.of(
            MAT2, MAT3, MAT4,
            MAT2X2, MAT2X3, MAT2X4,
            MAT3X2, MAT3X3, MAT3X4,
            MAT4X2, MAT4X3, MAT4X4,
            DMAT2, DMAT3, DMAT4,
            DMAT2X2, DMAT2X3, DMAT2X4,
            DMAT3X2, DMAT3X3, DMAT3X4,
            DMAT4X2, DMAT4X3, DMAT4X4
    );

    private static final EnumSet<GLSLType> INT_CASTABLE_TYPES = EnumSet.of(
            INT, IVEC2, IVEC3, IVEC4,
            UINT, UVEC2, UVEC3, UVEC4,
            FLOAT, VEC2, VEC3, VEC4,
            DOUBLE, DVEC2, DVEC3, DVEC4,
            MAT2, MAT3, MAT4,
            MAT2X2, MAT2X3, MAT2X4,
            MAT3X2, MAT3X3, MAT3X4,
            MAT4X2, MAT4X3, MAT4X4,
            DMAT2, DMAT3, DMAT4,
            DMAT2X2, DMAT2X3, DMAT2X4,
            DMAT3X2, DMAT3X3, DMAT3X4,
            DMAT4X2, DMAT4X3, DMAT4X4
    );

    private static final EnumSet<GLSLType> UINT_CASTABLE_TYPES = EnumSet.of(
            UINT, UVEC2, UVEC3, UVEC4,
            FLOAT, VEC2, VEC3, VEC4,
            DOUBLE, DVEC2, DVEC3, DVEC4,
            MAT2, MAT3, MAT4,
            MAT2X2, MAT2X3, MAT2X4,
            MAT3X2, MAT3X3, MAT3X4,
            MAT4X2, MAT4X3, MAT4X4,
            DMAT2, DMAT3, DMAT4,
            DMAT2X2, DMAT2X3, DMAT2X4,
            DMAT3X2, DMAT3X3, DMAT3X4,
            DMAT4X2, DMAT4X3, DMAT4X4
    );

    private static final EnumSet<GLSLType> FLOAT_CASTABLE_TYPES = EnumSet.of(
            FLOAT, VEC2, VEC3, VEC4,
            DOUBLE, DVEC2, DVEC3, DVEC4,
            MAT2, MAT3, MAT4,
            MAT2X2, MAT2X3, MAT2X4,
            MAT3X2, MAT3X3, MAT3X4,
            MAT4X2, MAT4X3, MAT4X4,
            DMAT2, DMAT3, DMAT4,
            DMAT2X2, DMAT2X3, DMAT2X4,
            DMAT3X2, DMAT3X3, DMAT3X4,
            DMAT4X2, DMAT4X3, DMAT4X4
    );

    private static final EnumSet<GLSLType> DOUBLE_CASTABLE_TYPES = EnumSet.of(
            DOUBLE, DVEC2, DVEC3, DVEC4,
            DMAT2, DMAT3, DMAT4,
            DMAT2X2, DMAT2X3, DMAT2X4,
            DMAT3X2, DMAT3X3, DMAT3X4,
            DMAT4X2, DMAT4X3, DMAT4X4
    );

    private static final EnumSet<GLSLType> BOOL_CASTABLE_TYPES = EnumSet.of(BOOL, BVEC2, BVEC3, BVEC4);

    /**
     * Возвращает множество типов, в которые можно привести текущий тип в GLSL.
     * Учитывает неявные приведения (например, int -> float, float -> double) и
     * совместимость с векторными и матричными типами через конструкторы.
     *
     * @return неизменяемое множество совместимых типов
     */
    public Set<GLSLType> getCastableTypes() {
        return switch (this) {
            case INT -> EnumSet.copyOf(INT_CASTABLE_TYPES);
            case UINT -> EnumSet.copyOf(UINT_CASTABLE_TYPES);
            case FLOAT -> EnumSet.copyOf(FLOAT_CASTABLE_TYPES);
            case DOUBLE -> EnumSet.copyOf(DOUBLE_CASTABLE_TYPES);
            case BOOL -> EnumSet.copyOf(BOOL_CASTABLE_TYPES);
            case BVEC2, BVEC3, BVEC4 -> EnumSet.of(BOOL, BVEC2, BVEC3, BVEC4);
            case IVEC2, IVEC3, IVEC4 -> EnumSet.of(INT, IVEC2, IVEC3, IVEC4);
            case UVEC2, UVEC3, UVEC4 -> EnumSet.of(UINT, UVEC2, UVEC3, UVEC4);
            case VEC2, VEC3, VEC4 -> EnumSet.of(FLOAT, VEC2, VEC3, VEC4);
            case DVEC2, DVEC3, DVEC4 -> EnumSet.of(DOUBLE, DVEC2, DVEC3, DVEC4);
            case MAT2, MAT3, MAT4, MAT2X2, MAT2X3, MAT2X4, MAT3X2, MAT3X3, MAT3X4, MAT4X2, MAT4X3, MAT4X4 ->
                    EnumSet.of(FLOAT, MAT2, MAT3, MAT4,
                            MAT2X2, MAT2X3, MAT2X4,
                            MAT3X2, MAT3X3, MAT3X4,
                            MAT4X2, MAT4X3, MAT4X4);
            case DMAT2, DMAT3, DMAT4, DMAT2X2, DMAT2X3, DMAT2X4, DMAT3X2, DMAT3X3, DMAT3X4, DMAT4X2, DMAT4X3, DMAT4X4 ->
                    EnumSet.of(DOUBLE, DMAT2, DMAT3, DMAT4,
                            DMAT2X2, DMAT2X3, DMAT2X4,
                            DMAT3X2, DMAT3X3, DMAT3X4,
                            DMAT4X2, DMAT4X3, DMAT4X4);
            case VOID -> EnumSet.noneOf(GLSLType.class);
            default -> EnumSet.of(this);
        };
    }
}
