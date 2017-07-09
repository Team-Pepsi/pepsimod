package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.misc.TickRate;
import net.daporkchop.pepsimod.module.api.CustomOption;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.OptionTypeBoolean;
import net.daporkchop.pepsimod.util.PepsiUtils;

import java.util.TimerTask;

public class Timer extends Module {
    public static float PROCENT = 1.0f;
    public static Timer INSTANCE;
    public boolean tps_sync = false;

    {
        INSTANCE = this;
    }

    public Timer(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "Timer", key, hide);
    }

    @Override
    public void onEnable() {
        INSTANCE = this;//adding this a bunch because it always seems to be null idk y
    }

    @Override
    public void onDisable() {
        INSTANCE = this;//adding this a bunch because it always seems to be null idk y
    }

    @Override
    public void tick() {

    }

    @Override
    public void init() {
        PROCENT = (float) getOptionByName("multiplier").getValue();
        INSTANCE = this; //adding this a bunch because it always seems to be null idk y
        PepsiUtils.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    updateName();
                } catch (NullPointerException e) {
                    //meh, minecraft isn't initialized yet
                }
            }
        }, 0, 1000);
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new CustomOption<>(1.0f, "multiplier", new String[]{"1.0", "0.0"},
                        (value) -> {
                            if (value <= 0.0f) {
                                clientMessage("Multiplier cannot be negative or 0!");
                                return;
                            }
                            Timer.PROCENT = value;
                            updateName();
                        },
                        () -> {
                            return Timer.PROCENT;
                        }),
                new CustomOption<>(false, "tps_sync", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            tps_sync = value;
                        },
                        () -> {
                            return tps_sync;
                        })
        };
    }

    @Override
    public boolean hasModeInName() {
        return true;
    }

    @Override
    public String getModeForName() {
        return TickRate.format.format(getMultiplier());
    }

    public float getMultiplier() {
        if (this.isEnabled) {
            if (tps_sync) {
                return TickRate.TPS / 20;
            } else {
                return PROCENT;
            }
        } else {
            return 1.0f;
        }
    }
}
