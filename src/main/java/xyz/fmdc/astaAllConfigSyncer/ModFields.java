package xyz.fmdc.astaAllConfigSyncer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

public final class ModFields {
    private final @Nonnull Field blocks;
    private final @Nonnull Field items;
    private final @Nonnull Field _blocks;
    private final @Nonnull Field _items;

    private ModFields(Class<?> clazz) throws NoSuchFieldException {
        this.blocks = clazz.getDeclaredField("blockIds");
        this.items = clazz.getDeclaredField("itemIds");
        this._blocks = clazz.getDeclaredField("_blockIds");
        this._items = clazz.getDeclaredField("_itemIds");
        this.blocks.setAccessible(true);
        this.items.setAccessible(true);
        this._blocks.setAccessible(true);
        this._items.setAccessible(true);
        checkString(blocks);
        checkString(items);
        checkObject(_blocks);
        checkObject(_items);
    }

    private void checkString(Field f) throws NoSuchFieldException {
        if (f.getType() != String.class)
            throw new NoSuchFieldException(f.getDeclaringClass().getTypeName() + "." + f.getName() + " is not of string");
    }

    private void checkObject(Field f) throws NoSuchFieldException {
        if (f.getType().isPrimitive())
            throw new NoSuchFieldException(f.getDeclaringClass().getTypeName() + "." + f.getName() + " is not of object");
    }

    public String getBlocks() {
        try {
            return (String) this.blocks.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBlocks(String config) {
        try {
            this.blocks.set(null, config);
            this._blocks.set(null, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String getItems() {
        try {
            return (String) this.items.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void setItems(String config) {
        try {
            this.items.set(null, config);
            this._items.set(null, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static @Nullable ModFields getMod(String clazz) {
        try {
            return new ModFields(Class.forName(clazz));
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            AstaAllConfigSyncer.LOGGER.warn("mod not found " + clazz, e);
            return null;
        }
    }
}
