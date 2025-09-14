package dev.behindthescenery.core.system.rendering.assimp.utils;

import dev.behindthescenery.core.system.rendering.assimp.resource.model.basic.Node;
import org.joml.*;
import org.lwjgl.assimp.*;

import java.util.Objects;

@SuppressWarnings("unused")
public class AssimpUtils {

    // Conversion methods

    public static Matrix4f toMatrix4f(AIMatrix4x4 aiMatrix) {
        return new Matrix4f(
                aiMatrix.a1(), aiMatrix.b1(), aiMatrix.c1(), aiMatrix.d1(),
                aiMatrix.a2(), aiMatrix.b2(), aiMatrix.c2(), aiMatrix.d2(),
                aiMatrix.a3(), aiMatrix.b3(), aiMatrix.c3(), aiMatrix.d3(),
                aiMatrix.a4(), aiMatrix.b4(), aiMatrix.c4(), aiMatrix.d4()
        );
    }

    public static Matrix3f toMatrix3f(AIMatrix3x3 aiMatrix) {
        return new Matrix3f(
                aiMatrix.a1(), aiMatrix.b1(), aiMatrix.c1(),
                aiMatrix.a2(), aiMatrix.b2(), aiMatrix.c2(),
                aiMatrix.a3(), aiMatrix.b3(), aiMatrix.c3()
        );
    }

    public static Vector3f toVector3f(AIVector3D vector) {
        return new Vector3f(vector.x(), vector.y(), vector.z());
    }

    public static Vector2f toVector2f(AIVector2D vector) {
        return new Vector2f(vector.x(), vector.y());
    }

    public static Quaternionf toQuaternionf(AIQuaternion quaternion) {
        return new Quaternionf(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
    }

    // Equals methods

    public static boolean AINodeEqualsAINode(AINode node1, AINode node2) {
        boolean namesEqual = Objects.equals(node1.mName().dataString(), node2.mName().dataString());
        boolean parentsEqual = (node1.mParent() != null) == (node2.mParent() != null);
        if (node1.mParent() != null && node2.mParent() != null) {
            parentsEqual = Objects.equals(node1.mParent().mName().dataString(), node2.mParent().mName().dataString());
        }
        boolean childrenEqual = node1.mNumChildren() == node2.mNumChildren();
        boolean meshesEqual = node1.mNumMeshes() == node2.mNumMeshes();
        return namesEqual && parentsEqual && childrenEqual && meshesEqual;
    }

    public static boolean AINodeEqualsNode(AINode aiNode, Node node) {
        boolean namesEqual = Objects.equals(aiNode.mName().dataString(), node.getName());
        boolean parentsEqual = (aiNode.mParent() != null) == (node.getParent() != null);
        if (aiNode.mParent() != null && node.getParent() != null) {
            parentsEqual = Objects.equals(aiNode.mParent().mName().dataString(), node.getParent().getName());
        }
        boolean childrenEqual = aiNode.mNumChildren() == node.getChildren().size();
        boolean meshesEqual = aiNode.mNumMeshes() == node.getMeshIndexes().size();
        return namesEqual && parentsEqual && childrenEqual && meshesEqual;
    }
}
