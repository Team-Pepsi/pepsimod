package net.daporkchop.pepsimod.util.colors.rainbow;

public class RainbowCycle implements Cloneable {
    public ColorChangeType red = ColorChangeType.INCREASE, green = ColorChangeType.NONE, blue = ColorChangeType.NONE;
    public int r = 0, g = 0, b = 0;

    public RainbowCycle clone() {
        try {
            return (RainbowCycle) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return this;
        }
    }
}
