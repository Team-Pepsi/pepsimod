/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.module.impl.player;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.optimization.OverrideCounter;
import net.daporkchop.pepsimod.the.wurst.pkg.name.RotationUtils;
import net.daporkchop.pepsimod.util.config.impl.AntiAFKTranslator;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AntiAFKMod extends Module {
    public static AntiAFKMod INSTANCE;
    protected long lastRun = System.currentTimeMillis();
    protected Runnable cleaner = null;

    {
        INSTANCE = this;
    }

    public AntiAFKMod() {
        super("AntiAFK");
    }

    @Override
    public void onEnable() {
        this.lastRun = System.currentTimeMillis();
    }

    @Override
    public void onDisable() {
        this.clean();
    }

    @Override
    public void tick() {
        if (AntiAFKTranslator.INSTANCE.requireInactive && mc.inGameHasFocus) {
            this.lastRun = System.currentTimeMillis();
        } else if (this.isAnyKeyPressed()) {
            this.clean();
            this.lastRun = System.currentTimeMillis();
        } else if (this.lastRun + AntiAFKTranslator.INSTANCE.delay <= System.currentTimeMillis()) {
            this.clean();
            this.lastRun = System.currentTimeMillis();
            List<Tuple<Runnable, Runnable>> functions = new ArrayList<>();

            if (AntiAFKTranslator.INSTANCE.spin) {
                functions.add(new Tuple<>(
                        () -> RotationUtils.faceVectorClient(mc.player.getPositionVector().add(
                                ThreadLocalRandom.current().nextDouble(-5.0d, 5.0d),
                                ThreadLocalRandom.current().nextDouble(-5.0d, 5.0d),
                                ThreadLocalRandom.current().nextDouble(-5.0d, 5.0d)
                        )),
                        () -> {
                        }
                ));
            }
            if (AntiAFKTranslator.INSTANCE.sneak) {
                functions.add(new Tuple<>(
                        () -> ((OverrideCounter) mc.gameSettings.keyBindSneak).incrementOverride(),
                        () -> ((OverrideCounter) mc.gameSettings.keyBindSneak).decrementOverride()
                ));
            }
            if (AntiAFKTranslator.INSTANCE.swingArm) {
                functions.add(new Tuple<>(
                        () -> mc.player.swingArm(EnumHand.MAIN_HAND),
                        () -> {
                        }
                ));
            }
            if (AntiAFKTranslator.INSTANCE.strafe) {
                int flag = ThreadLocalRandom.current().nextInt(2);
                functions.add(new Tuple<>(
                        () -> {
                            switch (flag) {
                                case 0:
                                    ((OverrideCounter) mc.gameSettings.keyBindLeft).incrementOverride();
                                    break;
                                case 1:
                                    ((OverrideCounter) mc.gameSettings.keyBindRight).incrementOverride();
                                    break;
                            }
                        },
                        () -> {
                            switch (flag) {
                                case 0:
                                    ((OverrideCounter) mc.gameSettings.keyBindLeft).decrementOverride();
                                    break;
                                case 1:
                                    ((OverrideCounter) mc.gameSettings.keyBindRight).decrementOverride();
                                    break;
                            }
                        }
                ));
            } else if (AntiAFKTranslator.INSTANCE.move) {
                int flag = ThreadLocalRandom.current().nextInt(4);
                functions.add(new Tuple<>(
                        () -> {
                            switch (flag) {
                                case 0:
                                    ((OverrideCounter) mc.gameSettings.keyBindForward).incrementOverride();
                                    break;
                                case 1:
                                    ((OverrideCounter) mc.gameSettings.keyBindBack).incrementOverride();
                                    break;
                                case 2:
                                    ((OverrideCounter) mc.gameSettings.keyBindLeft).incrementOverride();
                                    break;
                                case 3:
                                    ((OverrideCounter) mc.gameSettings.keyBindRight).incrementOverride();
                                    break;
                            }
                        },
                        () -> {
                            switch (flag) {
                                case 0:
                                    ((OverrideCounter) mc.gameSettings.keyBindForward).decrementOverride();
                                    break;
                                case 1:
                                    ((OverrideCounter) mc.gameSettings.keyBindBack).decrementOverride();
                                    break;
                                case 2:
                                    ((OverrideCounter) mc.gameSettings.keyBindLeft).decrementOverride();
                                    break;
                                case 3:
                                    ((OverrideCounter) mc.gameSettings.keyBindRight).decrementOverride();
                                    break;
                            }
                        }
                ));
            }

            if (!functions.isEmpty()) {
                Tuple<Runnable, Runnable> tuple = functions.get(ThreadLocalRandom.current().nextInt(functions.size()));
                tuple.getFirst().run();
                this.cleaner = tuple.getSecond();
            }
        }
    }

    protected void clean() {
        if (this.cleaner != null) {
            this.cleaner.run();
            this.cleaner = null;
        }
    }

    protected boolean isAnyKeyPressed() {
        if (!Display.isActive()) {
            return false;
        }
        for (KeyBinding keyBind : mc.gameSettings.keyBindings) {
            if (keyBind.isKeyDown()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(AntiAFKTranslator.INSTANCE.delay, "delay", new String[]{"1000", "2000", "3000", "4000", "5000"},
                        val -> {
                            AntiAFKTranslator.INSTANCE.delay = val;
                            return true;
                        },
                        () -> AntiAFKTranslator.INSTANCE.delay,
                        "Delay", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 10000, 500)
                ),
                new ModuleOption<>(AntiAFKTranslator.INSTANCE.spin, "spin", OptionCompletions.BOOLEAN,
                        val -> {
                            AntiAFKTranslator.INSTANCE.spin = val;
                            this.clean();
                            return true;
                        },
                        () -> AntiAFKTranslator.INSTANCE.spin,
                        "Spin"
                ),
                new ModuleOption<>(AntiAFKTranslator.INSTANCE.sneak, "sneak", OptionCompletions.BOOLEAN,
                        val -> {
                            AntiAFKTranslator.INSTANCE.sneak = val;
                            this.clean();
                            return true;
                        },
                        () -> AntiAFKTranslator.INSTANCE.sneak,
                        "Sneak"
                ),
                new ModuleOption<>(AntiAFKTranslator.INSTANCE.swingArm, "swingArm", OptionCompletions.BOOLEAN,
                        val -> {
                            AntiAFKTranslator.INSTANCE.swingArm = val;
                            this.clean();
                            return true;
                        },
                        () -> AntiAFKTranslator.INSTANCE.swingArm,
                        "Swing arm"
                ),
                new ModuleOption<>(AntiAFKTranslator.INSTANCE.move, "move", OptionCompletions.BOOLEAN,
                        val -> {
                            AntiAFKTranslator.INSTANCE.move = val;
                            this.clean();
                            return true;
                        },
                        () -> AntiAFKTranslator.INSTANCE.move,
                        "Move"
                ),
                new ModuleOption<>(AntiAFKTranslator.INSTANCE.strafe, "strafe", OptionCompletions.BOOLEAN,
                        val -> {
                            AntiAFKTranslator.INSTANCE.strafe = val;
                            this.clean();
                            return true;
                        },
                        () -> AntiAFKTranslator.INSTANCE.strafe,
                        "Strafe"
                ),
                new ModuleOption<>(AntiAFKTranslator.INSTANCE.requireInactive, "requireInactive", OptionCompletions.BOOLEAN,
                        val -> {
                            AntiAFKTranslator.INSTANCE.requireInactive = val;
                            return true;
                        },
                        () -> AntiAFKTranslator.INSTANCE.requireInactive,
                        "Require inactive"
                )
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.PLAYER;
    }
}
