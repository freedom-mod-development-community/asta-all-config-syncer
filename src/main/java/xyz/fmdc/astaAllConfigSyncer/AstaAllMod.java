package xyz.fmdc.astaAllConfigSyncer;


import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public enum AstaAllMod {
    MineAll(0),
    CutAll(1),
    DigAll(2),
    ;

    public @Nullable ModFields fields;
    public final int id;

    AstaAllMod(int id) {
        this.id = id;
    }

    private static final Map<Integer, AstaAllMod> byId = new HashMap<>();

    public static @Nullable AstaAllMod getById(int id) {
        return byId.get(id);
    }

    static {
        for (AstaAllMod value : values()) {
            byId.put(value.id, value);
        }
    }
}
