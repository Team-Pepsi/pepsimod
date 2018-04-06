/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2018 DaPorkchop_
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original author of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.util.RenderColor;
import net.daporkchop.pepsimod.util.config.impl.ESPTranslator;

public class ESPMod extends Module {
    public static final RenderColor friendColor = new RenderColor(76, 144, 255, 255);
    public static final RenderColor monsterColor = new RenderColor(128, 0, 0, 255);
    public static final RenderColor animalColor = new RenderColor(0, 0, 204, 255);
    public static final RenderColor golemColor = new RenderColor(179, 179, 179, 255);
    public static final RenderColor playerColor = new RenderColor(255, 255, 0, 255);

    public static ESPMod INSTANCE;

    {
        INSTANCE = this;
    }

    public ESPMod() {
        super("ESP");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(ESPTranslator.INSTANCE.monsters, "monsters", OptionCompletions.BOOLEAN,
                        (value) -> {
                            ESPTranslator.INSTANCE.monsters = value;
                            return true;
                        },
                        () -> {
                            return ESPTranslator.INSTANCE.monsters;
                        }, "Monsters"),
                new ModuleOption<>(ESPTranslator.INSTANCE.animals, "animals", OptionCompletions.BOOLEAN,
                        (value) -> {
                            ESPTranslator.INSTANCE.animals = value;
                            return true;
                        },
                        () -> {
                            return ESPTranslator.INSTANCE.animals;
                        }, "Animals"),
                new ModuleOption<>(ESPTranslator.INSTANCE.players, "players", OptionCompletions.BOOLEAN,
                        (value) -> {
                            ESPTranslator.INSTANCE.players = value;
                            return true;
                        },
                        () -> {
                            return ESPTranslator.INSTANCE.players;
                        }, "Players"),
                new ModuleOption<>(ESPTranslator.INSTANCE.golems, "golems", OptionCompletions.BOOLEAN,
                        (value) -> {
                            ESPTranslator.INSTANCE.golems = value;
                            return true;
                        },
                        () -> {
                            return ESPTranslator.INSTANCE.golems;
                        }, "Golems"),
                new ModuleOption<>(ESPTranslator.INSTANCE.invisible, "invisible", OptionCompletions.BOOLEAN,
                        (value) -> {
                            ESPTranslator.INSTANCE.invisible = value;
                            return true;
                        },
                        () -> {
                            return ESPTranslator.INSTANCE.invisible;
                        }, "Invisible"),
                new ModuleOption<>(ESPTranslator.INSTANCE.friendColors, "friendColors", OptionCompletions.BOOLEAN,
                        (value) -> {
                            ESPTranslator.INSTANCE.friendColors = value;
                            return true;
                        },
                        () -> {
                            return ESPTranslator.INSTANCE.friendColors;
                        }, "FriendColors")
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.RENDER;
    }
}
