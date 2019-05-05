/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
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
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.RenderColor;
import net.daporkchop.pepsimod.util.config.impl.ESPTranslator;
import net.daporkchop.pepsimod.util.config.impl.TracersTranslator;
import net.daporkchop.pepsimod.util.render.Renderer;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class StorageESPMod extends Module {
    public static final RenderColor chestColor = new RenderColor(196, 139, 53, 128);
    public static final RenderColor trappedColor = new RenderColor(81, 57, 22, 128);
    public static final RenderColor enderColor = new RenderColor(25, 35, 40, 128);
    public static final RenderColor hopperColor = new RenderColor(45, 45, 45, 128);
    public static final RenderColor furnaceColor = new RenderColor(151, 151, 151, 128);
    public static StorageESPMod INSTANCE;

    public static AxisAlignedBB getBoundingBox(World world, BlockPos pos) {
        return world.getBlockState(pos).getBoundingBox(world, pos);
    }

    public final ArrayList<AxisAlignedBB> basic = new ArrayList<>();
    public final ArrayList<AxisAlignedBB> trapped = new ArrayList<>();
    public final ArrayList<AxisAlignedBB> ender = new ArrayList<>();
    public final ArrayList<AxisAlignedBB> hopper = new ArrayList<>();
    public final ArrayList<AxisAlignedBB> furnace = new ArrayList<>();

    {
        INSTANCE = this;
    }

    public StorageESPMod() {
        super("StorageESP");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void tick() {
        this.basic.clear();
        this.trapped.clear();
        this.ender.clear();
        this.hopper.clear();
        this.furnace.clear();

        for (TileEntity te : mc.world.loadedTileEntityList) {
            if ((ESPTranslator.INSTANCE.basic || ESPTranslator.INSTANCE.trapped) && te instanceof TileEntityChest) {
                TileEntityChest chestTe = (TileEntityChest) te;

                if (chestTe.adjacentChestXPos != null || chestTe.adjacentChestZPos != null) {
                    continue;
                }

                AxisAlignedBB bb = PepsiUtils.offsetBB(PepsiUtils.cloneBB(getBoundingBox(mc.world, te.getPos())), te.getPos());

                if (chestTe.adjacentChestXNeg != null) {
                    ReflectionStuff.setMinX(bb, bb.minX - 1);
                    //PepsiUtils.unionBB(bb, PepsiUtils.offsetBB(PepsiUtils.cloneBB(getBoundingBox(mc.world, chestTe.adjacentChestXNeg.getPos())), chestTe.adjacentChestXNeg.getPos()));
                } else if (chestTe.adjacentChestZNeg != null) {
                    ReflectionStuff.setMinZ(bb, bb.minZ - 1);
                    //PepsiUtils.unionBB(bb, PepsiUtils.offsetBB(PepsiUtils.cloneBB(getBoundingBox(mc.world, chestTe.adjacentChestZNeg.getPos())), chestTe.adjacentChestZNeg.getPos()));
                }

                if (chestTe.getChestType() == BlockChest.Type.TRAP) {
                    if (ESPTranslator.INSTANCE.trapped) {
                        this.trapped.add(bb);
                    }
                } else {
                    if (ESPTranslator.INSTANCE.basic) {
                        this.basic.add(bb);
                    }
                }
            } else if (ESPTranslator.INSTANCE.ender && te instanceof TileEntityEnderChest) {
                this.ender.add(PepsiUtils.offsetBB(PepsiUtils.cloneBB(getBoundingBox(mc.world, te.getPos())), te.getPos()));
            } else if (ESPTranslator.INSTANCE.furnace && te instanceof TileEntityFurnace) {
                this.furnace.add(PepsiUtils.offsetBB(PepsiUtils.cloneBB(getBoundingBox(mc.world, te.getPos())), te.getPos()));
            } else if (ESPTranslator.INSTANCE.hopper && te instanceof TileEntityHopper) {
                this.hopper.add(PepsiUtils.offsetBB(PepsiUtils.cloneBB(getBoundingBox(mc.world, te.getPos())), te.getPos()));
            }
        }
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(ESPTranslator.INSTANCE.basic, "normal", OptionCompletions.BOOLEAN,
                        (value) -> {
                            ESPTranslator.INSTANCE.basic = value;
                            return true;
                        },
                        () -> {
                            return ESPTranslator.INSTANCE.basic;
                        }, "Normal"),
                new ModuleOption<>(ESPTranslator.INSTANCE.trapped, "trapped", OptionCompletions.BOOLEAN,
                        (value) -> {
                            ESPTranslator.INSTANCE.trapped = value;
                            return true;
                        },
                        () -> {
                            return ESPTranslator.INSTANCE.trapped;
                        }, "Trapped"),
                new ModuleOption<>(ESPTranslator.INSTANCE.ender, "ender", OptionCompletions.BOOLEAN,
                        (value) -> {
                            ESPTranslator.INSTANCE.ender = value;
                            return true;
                        },
                        () -> {
                            return ESPTranslator.INSTANCE.ender;
                        }, "Ender"),
                new ModuleOption<>(ESPTranslator.INSTANCE.hopper, "hopper", OptionCompletions.BOOLEAN,
                        (value) -> {
                            ESPTranslator.INSTANCE.hopper = value;
                            return true;
                        },
                        () -> {
                            return ESPTranslator.INSTANCE.hopper;
                        }, "Hopper"),
                new ModuleOption<>(ESPTranslator.INSTANCE.furnace, "furnace", OptionCompletions.BOOLEAN,
                        (value) -> {
                            ESPTranslator.INSTANCE.furnace = value;
                            return true;
                        },
                        () -> {
                            return ESPTranslator.INSTANCE.furnace;
                        }, "Furnace")
        };
    }

    @Override
    public void renderWorld(Renderer renderer) {
        renderer.width(TracersTranslator.INSTANCE.width);

        if (ESPTranslator.INSTANCE.basic) {
            renderer.color(chestColor);
            //this.basic.forEach(bb -> renderer.line(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ));
            this.basic.forEach(renderer::outline);
        }

        if (ESPTranslator.INSTANCE.trapped) {
            renderer.color(trappedColor);
            this.trapped.forEach(renderer::outline);
        }

        if (ESPTranslator.INSTANCE.ender) {
            renderer.color(enderColor);
            this.ender.forEach(renderer::outline);
        }

        if (ESPTranslator.INSTANCE.hopper) {
            renderer.color(hopperColor);
            this.hopper.forEach(renderer::outline);
        }

        if (ESPTranslator.INSTANCE.furnace) {
            renderer.color(furnaceColor);
            this.furnace.forEach(renderer::outline);
        }
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.RENDER;
    }
}
