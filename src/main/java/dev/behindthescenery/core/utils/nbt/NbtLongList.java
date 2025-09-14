package dev.behindthescenery.core.utils.nbt;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NbtLongList extends NbtList {

    public static NbtLongList getFrom(NbtCompound compound, String key) {
        return create(compound.getList(key, NbtElement.LONG_TYPE));
    }

    public static NbtLongList create(NbtList list) {
        List<Long> values = new ArrayList<>();

        for (NbtElement nbtElement : list) {
            if (nbtElement instanceof NbtLong nbtLong) {
                values.add(nbtLong.longValue());
            }
        }

        return new NbtLongList(values);
    }

    public NbtLongList() {}

    public NbtLongList(List<Long> list) {
        if (!addFull(list))
            throw new RuntimeException("Error when try create " + getClass().getName());
    }

    public boolean add(long value) {
        return add(NbtLong.of(value));
    }

    public boolean addFull(Collection<Long> list) {
        try {
            for (Long l : list) {
                add(l);
            }
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public NbtElement set(int i, long value) {
        return this.set(i, NbtLong.of(value));
    }

    public long getLong(int i) {
        return get(i) instanceof NbtLong nbtLong ? nbtLong.longValue() : 0L;
    }

    public List<Long> toLongList() {
        List<Long> list = new ArrayList<>();
        
        for (NbtElement nbtElement : this) {
            list.add(((NbtLong)nbtElement).longValue());
        }
        
        return list;
    }
}
