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
import net.daporkchop.pepsimod.optimization.BlockID;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Objects;
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
                                return args[0] + " " + args[1] + " " + Modules.xray.fastLookup.stream()
                                        .mapToObj(i -> Block.REGISTRY.getObjectById(i).getRegistryName().toString())
                                        .filter(s -> s.startsWith(args[2]))
                                        .findFirst()
                                        .orElse("");
                            }
                        case 2:
                            if (Modules.xray.fastLookup.isEmpty()) {
                                return "";
                            } else {
                                return args[0] + " " + args[1] + " " + Block.REGISTRY.getObjectById(Modules.xray.fastLookup.nextSetBit(0)).getRegistryName();
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
            String s = args[2].toLowerCase();
            try {
                int id = Integer.parseInt(s);
                Block block = Block.REGISTRY.getObjectById(id);
                if (block == null) {
                    clientMessage("Not a valid block ID: " + PepsiUtils.COLOR_ESCAPE + "o" + args[2]);
                } else {
                    Modules.xray.add(id);
                    clientMessage("Added " + PepsiUtils.COLOR_ESCAPE + "o" + block.getRegistryName().toString() + PepsiUtils.COLOR_ESCAPE + "r to the Xray list");
                    if (this.state.enabled) {
                        mc.renderGlobal.loadRenderers();
                    }
                }
            } catch (NumberFormatException e) {
                if (s.contains(":") && !s.endsWith(":") && !s.startsWith(":")) {
                    String[] split = s.split(":");
                    Block block = Block.REGISTRY.getObject(new ResourceLocation(split[0], split[1]));
                    if (block == null) {
                        clientMessage("Invalid id: " + PepsiUtils.COLOR_ESCAPE + "o" + s);
                    } else {
                        Modules.xray.add(((BlockID) block).getBlockId());
                        clientMessage("Added " + PepsiUtils.COLOR_ESCAPE + "o" + block.getRegistryName().toString() + PepsiUtils.COLOR_ESCAPE + "r to the Xray list");
                        if (this.state.enabled) {
                            mc.renderGlobal.loadRenderers();
                        }
                    }
                } else {
                    clientMessage("Invalid id: " + PepsiUtils.COLOR_ESCAPE + "o" + s);
                }
            }
            return;
        } else if (args.length == 3 && !args[2].isEmpty() && cmd.startsWith(".xray remove ")) {
            String s = args[2].toLowerCase();
            try {
                int id = Integer.parseInt(s);
                if (Modules.xray.remove(id)) {
                    clientMessage("Removed " + PepsiUtils.COLOR_ESCAPE + "o" + id + PepsiUtils.COLOR_ESCAPE + "r from the Xray list");
                    if (this.state.enabled) {
                        mc.renderGlobal.loadRenderers();
                    }
                } else {
                    clientMessage("Block ID " + PepsiUtils.COLOR_ESCAPE + "o" + args[2] + PepsiUtils.COLOR_ESCAPE + "r is not on the Xray list!");
                }
            } catch (NumberFormatException e) {
                if (s.contains(":") && !s.endsWith(":") && !s.startsWith(":")) {
                    String[] split = s.split(":");
                    Block block = Block.REGISTRY.getObject(new ResourceLocation(split[0], split[1]));
                    if (block == null) {
                        clientMessage("Invalid id: " + PepsiUtils.COLOR_ESCAPE + "o" + s);
                    } else {
                        int id = ((BlockID) block).getBlockId();
                        if (Modules.xray.remove(id)) {
                            clientMessage("Removed " + PepsiUtils.COLOR_ESCAPE + "o" + s + PepsiUtils.COLOR_ESCAPE + "r from the Xray list");
                            if (this.state.enabled) {
                                mc.renderGlobal.loadRenderers();
                            }
                        } else {
                            clientMessage("Block ID " + PepsiUtils.COLOR_ESCAPE + "o" + s + PepsiUtils.COLOR_ESCAPE + "r is not on the Xray list!");
                        }
                    }
                } else {
                    clientMessage("Invalid id: " + PepsiUtils.COLOR_ESCAPE + "o" + s);
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
        private static String[] toStrings(Stream<Block> blocks) {
            return blocks.map(Block::getRegistryName).filter(Objects::nonNull).map(ResourceLocation::toString).toArray(String[]::new);
        }

        public String[] blocks = toStrings(Stream.of(
                Blocks.COAL_ORE,
                Blocks.IRON_ORE,
                Blocks.GOLD_ORE,
                Blocks.DIAMOND_ORE,
                Blocks.EMERALD_ORE,
                Blocks.LAPIS_ORE,
                Blocks.REDSTONE_ORE,
                Blocks.LIT_REDSTONE_ORE
        ));

        private final BitSet fastLookup = new BitSet();

        public boolean isVisible(Block block) {
            return this.isVisible(((BlockID) block).getBlockId());
        }

        public boolean isVisible(int blockId) {
            return this.fastLookup.get(blockId);
        }

        public boolean add(int blockId) {
            if (!this.fastLookup.get(blockId)) {
                this.fastLookup.set(blockId);
                PepsiConfig.sync(); //save config
                return true;
            } else {
                return false;
            }
        }

        public boolean remove(int blockId) {
            if (this.fastLookup.get(blockId)) {
                this.fastLookup.clear(blockId);
                PepsiConfig.sync(); //save config
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void save() {
            PepsiConfig.ConfigNode.super.save();

            if (this.blocks == null) { //don't load from config the first time around
                this.blocks = toStrings(this.fastLookup.stream().mapToObj(Block::getBlockById));
            }
        }

        @Override
        public void load() {
            PepsiConfig.ConfigNode.super.save();

            this.fastLookup.clear();
            if (this.blocks != null) {
                Stream.of(this.blocks)
                        .map(Block::getBlockFromName)
                        .filter(Objects::nonNull)
                        .mapToInt(BlockID.GET_BLOCK_ID)
                        .forEach(this.fastLookup::set);
                this.blocks = null;
            }
        }
    }
}
