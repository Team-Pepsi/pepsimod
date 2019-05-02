package net.daporkchop.pepsimod.optimization;

/**
 * @author DaPorkchop_
 */
public interface OverrideCounter {
    void incrementOverride();

    void decrementOverride();

    int getOverride();

    default boolean isOverriden() {
        return this.getOverride() > 0;
    }
}
