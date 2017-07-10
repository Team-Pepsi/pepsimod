package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.api.CustomOption;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleLaunchState;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.util.EntityFakePlayer;

public class FreecamMod extends Module {
    public static float SPEED = 1.0f;
    public static FreecamMod INSTANCE;
    public EntityFakePlayer fakePlayer;

    {
        INSTANCE = this;
    }

    public FreecamMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "Freecam", key, hide);
    }

    @Override
    public void onEnable() {
        INSTANCE = this;//adding this a bunch because it always seems to be null idk y
        if (PepsiMod.INSTANCE.hasInitializedModules) {
            fakePlayer = new EntityFakePlayer();
        }
    }

    @Override
    public void onDisable() {
        INSTANCE = this;//adding this a bunch because it always seems to be null idk y
        if (PepsiMod.INSTANCE.hasInitializedModules) {
            fakePlayer.resetPlayerPosition();
            fakePlayer.despawn();

            //PepsiMod.INSTANCE.mc.renderGlobal.loadRenderers();
        }
    }

    @Override
    public void tick() {
        PepsiMod.INSTANCE.mc.player.motionX = 0;
        PepsiMod.INSTANCE.mc.player.motionZ = 0;

        PepsiMod.INSTANCE.mc.player.jumpMovementFactor = SPEED / 10;

        if (PepsiMod.INSTANCE.mc.gameSettings.keyBindJump.isKeyDown()) {
            PepsiMod.INSTANCE.mc.player.motionY = SPEED;
        } else if (PepsiMod.INSTANCE.mc.gameSettings.keyBindSneak.isKeyDown()) {
            PepsiMod.INSTANCE.mc.player.motionY = -SPEED;
        } else {
            PepsiMod.INSTANCE.mc.player.motionY = 0;
        }
    }

    @Override
    public void init() {
        SPEED = PepsiMod.INSTANCE.dataTag.getFloat("Freecam_speed", 1.0f);
        INSTANCE = this; //adding this a bunch because it always seems to be null idk y
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new CustomOption<>(1.0f, "speed", new String[]{"1.0", "0.0"},
                        (value) -> {
                            if (value <= 0.0f) {
                                clientMessage("Speed cannot be negative or 0!");
                                return;
                            }
                            FreecamMod.SPEED = value;
                            updateName();
                        },
                        () -> {
                            return FreecamMod.SPEED;
                        })
        };
    }

    @Override
    public ModuleLaunchState getLaunchState() {
        return ModuleLaunchState.DISABLED;
    }
}
