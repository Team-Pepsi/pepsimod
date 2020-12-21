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

package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.PepsiConfig;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.Modules;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class XrayMod extends Module {
    public static XrayMod INSTANCE;

    {
        INSTANCE = this;
    }

    public XrayMod() {
        super("Xray");
    }

    @Override
    public void onEnable() {
        try {
            mc.renderGlobal.loadRenderers();
        } catch (NullPointerException e) {
            //we don't care, mc isn't initialized yet
        }
    }

    @Override
    public void onDisable() {
        try {
            mc.renderGlobal.loadRenderers();
        } catch (NullPointerException e) {
            //we don't care, mc isn't initialized yet
        }
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
                new ModuleOption<>(0, "add", new String[0],
                        (value) -> {
                            return true;
                        },
                        () -> {
                            return 0;
                        }, "add", false),
                new ModuleOption<>(0, "remove", new String[0],
                        (value) -> {
                            return true;
                        },
                        () -> {
                            return 0;
                        }, "remove", false)
        };
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        if (args.length >= 2) {
            switch (args[1]) {
                case "add":
                    switch (args.length) {
                        case 3:
                            if (!args[2].isEmpty()) {
                                String arg = args[2];
                                for (Block b : Block.REGISTRY) {
                                    String s = b.getRegistryName().toString();
                                    if (s.startsWith(arg)) {
                                        return args[0] + " " + args[1] + " " + s;
                                    }
                                }

                                return "";
                            }
                        case 2:
                            return args[0] + " " + args[1] + " " + Block.REGISTRY.getObjectById(7).getRegistryName().toString();
                    }
                    break;
                case "remove":
                    switch (args.length) {
                        case 3:
                            if (!args[2].isEmpty()) {
                                return args[0] + " " + args[1] + " " + Arrays.stream(Modules.xray.blocks)
                                        .filter(s -> s.startsWith(args[2]))
                                        .findFirst().orElse("");
                            }
                        case 2:
                            if (Modules.xray.blocks.length == 0) {
                                return "";
                            } else {
                                return args[0] + " " + args[1] + " " + Modules.xray.blocks[0];
                            }
                    }
                    break;
            }
        }

        return super.getSuggestion(cmd, args);
    }

    @Override
    public void execute(String cmd, String[] args) {
        if (args.length == 3 && !args[2].isEmpty() && cmd.startsWith(".xray add ")) {
            ResourceLocation id = new ResourceLocation(args[2]);
            if (!Block.REGISTRY.containsKey(id)) {
                clientMessage("Invalid id: " + PepsiUtils.COLOR_ESCAPE + "o" + args[2]);
            } else {
                Modules.xray.add(id);
                clientMessage("Added " + PepsiUtils.COLOR_ESCAPE + "o" + id + PepsiUtils.COLOR_ESCAPE + "r to the Xray list");
                if (this.state.enabled) {
                    mc.renderGlobal.loadRenderers();
                }
            }
            return;
        } else if (args.length == 3 && !args[2].isEmpty() && cmd.startsWith(".xray remove ")) {
            ResourceLocation id = new ResourceLocation(args[2]);
            if (!Block.REGISTRY.containsKey(id)) {
                clientMessage("Invalid id: " + PepsiUtils.COLOR_ESCAPE + "o" + args[2]);
            } else {
                if (Modules.xray.remove(id)) {
                    clientMessage("Removed " + PepsiUtils.COLOR_ESCAPE + "o" + args[2] + PepsiUtils.COLOR_ESCAPE + "r from the Xray list");
                    if (this.state.enabled) {
                        mc.renderGlobal.loadRenderers();
                    }
                } else {
                    clientMessage("Block ID " + PepsiUtils.COLOR_ESCAPE + "o" + args[2] + PepsiUtils.COLOR_ESCAPE + "r is not on the Xray list!");
                }
            }
            return;
        }

        super.execute(cmd, args);
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.RENDER;
    }

    //TODO: make the state be part of the module instance itself
    public static final class State implements PepsiConfig.ConfigNode {
        public String[] blocks = Stream.of(
                Blocks.COAL_ORE,
                Blocks.IRON_ORE,
                Blocks.GOLD_ORE,
                Blocks.DIAMOND_ORE,
                Blocks.EMERALD_ORE,
                Blocks.LAPIS_ORE,
                Blocks.REDSTONE_ORE,
                Blocks.LIT_REDSTONE_ORE
        ).map(Block::getRegistryName).map(Objects::toString).toArray(String[]::new);

        private final Set<ResourceLocation> fastLookup = new LinkedHashSet<>();

        public boolean isVisible(Block block) {
            return this.fastLookup.contains(block.getRegistryName());
        }

        public boolean add(ResourceLocation id) {
            if (this.fastLookup.add(id)) {
                PepsiConfig.sync(); //save config
                return true;
            } else {
                return false;
            }
        }

        public boolean remove(ResourceLocation id) {
            if (this.fastLookup.remove(id)) {
                PepsiConfig.sync(); //save config
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void save() {
            PepsiConfig.ConfigNode.super.save();

            this.blocks = this.fastLookup.stream().map(Objects::toString).toArray(String[]::new);
        }

        @Override
        public void load() {
            PepsiConfig.ConfigNode.super.load();

            this.fastLookup.clear();
            Stream.of(this.blocks).map(ResourceLocation::new).forEach(this.fastLookup::add);
        }
    }
}
