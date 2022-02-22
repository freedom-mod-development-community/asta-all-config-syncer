/// Copyright (c) 2022 anatawa12 and other contributors
/// This file is part of *All Config Syncer, released under MIT License
/// See LICENSE at https://github.com/freedom-mod-development-community/asta-all-config-syncer for more details

package xyz.fmdc.astaAllConfigSyncer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public enum AstaAllModConfigField {
    MineAllItem(AstaAllMod.MineAll, false),
    MineAllBlock(AstaAllMod.MineAll, true),
    CutAllItem(AstaAllMod.CutAll, false),
    CutAllBlock(AstaAllMod.CutAll, true),
    DigAllItem(AstaAllMod.DigAll, false),
    DigAllBlock(AstaAllMod.DigAll, true),
    ;

    public final AstaAllMod mod;
    public final boolean isBlock;

    public final int id;

    private static final Map<Integer, AstaAllModConfigField> byId = new HashMap<>();

    AstaAllModConfigField(AstaAllMod mod, boolean isBlock) {
        this.mod = mod;
        this.isBlock = isBlock;
        id = mod.id << 1 | (isBlock ? 1 : 0);
    }

    public static @Nullable AstaAllModConfigField getById(int id) {
        return byId.get(id);
    }

    static {
        for (AstaAllModConfigField value : values()) {
            byId.put(value.id, value);
        }
    }

    public String get() {
        if (mod.fields == null) return null;
        if (isBlock) {
            return mod.fields.getBlocks();
        } else {
            return mod.fields.getItems();
        }
    }

    public void set(String config) {
        if (mod.fields == null) return;
        if (isBlock) {
            mod.fields.setBlocks(config);
        } else {
            mod.fields.setItems(config);
        }
    }
}
