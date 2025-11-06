package dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.node;

import dev.behindthescenery.core.system.user_interface.imgui.screen.nodes.utils.ImNodeUtils;
import imgui.ImVec4;

public class ImNodePinObjectColors {

    public static final ImVec4 WHITE = new ImVec4(1,1,1,1);;

    public static final ImVec4 BOOLEAN = new ImVec4(1.0f, 0.0f, 0.0f, 1.0f);      // Красный для Boolean
    public static final ImVec4 BYTE = new ImVec4(0.0f, 0.5f, 1.0f, 1.0f);         // Голубой для Byte (и Enum)
    public static final ImVec4 INTEGER = new ImVec4(0.0f, 1.0f, 1.0f, 1.0f);      // Бирюзовый для Integer
    public static final ImVec4 FLOAT = new ImVec4(0.0f, 1.0f, 0.0f, 1.0f);        // Зеленый для Float
    public static final ImVec4 VECTOR = new ImVec4(1.0f, 1.0f, 0.0f, 1.0f);       // Желтый для Vector
    public static final ImVec4 ROTATOR = new ImVec4(1.0f, 0.0f, 1.0f, 1.0f);      // Пурпурный для Rotator
    public static final ImVec4 TRANSFORM = new ImVec4(1.0f, 0.5f, 0.0f, 1.0f);    // Оранжевый для Transform
    public static final ImVec4 STRING = new ImVec4(1.0f, 0.0f, 0.5f, 1.0f);       // Розовый для String
    public static final ImVec4 NAME = new ImVec4(0.5f, 0.0f, 1.0f, 1.0f);         // Фиолетовый для Name
    public static final ImVec4 TEXT = new ImVec4(0.0f, 0.5f, 1.0f, 1.0f);         // Голубой для Text
    public static final ImVec4 OBJECT = new ImVec4(0.0f, 0.0f, 1.0f, 1.0f);       // Синий для Object
    public static final ImVec4 CLASS = new ImVec4(0.0f, 0.5f, 0.5f, 1.0f);        // Темно-бирюзовый для Class
    public static final ImVec4 ARRAY = new ImVec4(0.5f, 0.5f, 0.5f, 1.0f);        // Серый для Array
    public static final ImVec4 ENUM = new ImVec4(0.0f, 0.5f, 1.0f, 1.0f);         // Голубой для Enum (совпадает с Byte)
    public static final ImVec4 STRUCT = new ImVec4(0.5f, 0.5f, 0.0f, 1.0f);       // Оливковый для Struct
    public static final ImVec4 LINEAR_COLOR = new ImVec4(0.5f, 0.5f, 0.5f, 1.0f); // Серый для LinearColor

    public static final ImVec4 MATRIX4 = new ImVec4(0.8f, 0.2f, 0.6f, 1.0f);      // Пурпурно-розовый для Matrix4
    public static final ImVec4 MATRIX3 = new ImVec4(0.7f, 0.3f, 0.7f, 1.0f);      // Фиолетовый для Matrix3
    public static final ImVec4 VECTOR3 = new ImVec4(1.0f, 0.8f, 0.2f, 1.0f);      // Золотисто-желтый для Vector3
    public static final ImVec4 VECTOR2 = new ImVec4(1.0f, 0.9f, 0.4f, 1.0f);      // Светло-желтый для Vector2
    public static final ImVec4 VECTOR4 = new ImVec4(0.9f, 0.7f, 0.1f, 1.0f);      // Темно-желтый для Vector4
    public static final ImVec4 IVECTOR4 = new ImVec4(0.4f, 0.8f, 0.6f, 1.0f);     // Мятно-зеленый для IVector4
    public static final ImVec4 IVECTOR3 = new ImVec4(0.3f, 0.9f, 0.5f, 1.0f);     // Светло-зеленый для IVector3
    public static final ImVec4 IVECTOR2 = new ImVec4(0.2f, 1.0f, 0.4f, 1.0f);     // Ярко-зеленый для IVector2
    public static final ImVec4 UINT = new ImVec4(0.2f, 0.8f, 0.8f, 1.0f);

    public static ImVec4 getColorByClass(Class<?> classObj) {
        return ImNodeUtils.getColorByClass(classObj);
    }
}
