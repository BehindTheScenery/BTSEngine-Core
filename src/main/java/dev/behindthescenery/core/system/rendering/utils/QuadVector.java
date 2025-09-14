package dev.behindthescenery.core.system.rendering.utils;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class QuadVector {

    protected final Vector2f pos1;
    protected final Vector2f pos2;
    protected final Vector2f pos3;
    protected final Vector2f pos4;

    protected QuadVector(Vector2f pos1, Vector2f pos2, Vector2f pos3, Vector2f pos4) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.pos3 = pos3;
        this.pos4 = pos4;
    }


    public static List<QuadVector> create(Vector2f... positions) {
        int count = positions.length / 3;
        if(count == 0) return new ArrayList<>();

        List<QuadVector> result = new ArrayList<>();

        List<Vector2f> d1 = new ArrayList<>();
        for (Vector2f position : positions) {
            if (d1.size() == 4) {
                result.add(QuadVector.create(d1.get(0), d1.get(1), d1.get(2), d1.get(3)));
                d1.clear();
            }
            d1.add(position);
        }

        return result;
    }

    public Vector2f getPos1() {
        return pos1;
    }

    public Vector2f getPos2() {
        return pos2;
    }

    public Vector2f getPos3() {
        return pos3;
    }

    public Vector2f getPos4() {
        return pos4;
    }

    public static QuadVector create(Vector2f pos1, Vector2f pos2, Vector2f pos3, Vector2f pos4){
        return new QuadVector(pos1, pos2, pos3, pos4);
    }
}
