package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.api.CustomOption;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.OptionTypeBoolean;
import net.daporkchop.pepsimod.totally.not.skidded.GeometryMasks;
import net.daporkchop.pepsimod.totally.not.skidded.GeometryTessellator;
import net.daporkchop.pepsimod.totally.not.skidded.RenderUtils;
import net.daporkchop.pepsimod.util.ESPEntry;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class StorageESPMod extends Module {
    public static StorageESPMod INSTANCE;

    public final ArrayList<ESPEntry> basic = new ArrayList<>();
    public final ArrayList<ESPEntry> trapped = new ArrayList<>();
    public final ArrayList<ESPEntry> ender = new ArrayList<>();
    public final ArrayList<ESPEntry> hopper = new ArrayList<>();
    public final ArrayList<ESPEntry> furnace = new ArrayList<>();

    public static final int chestColor = PepsiUtils.toRGBA(196, 139, 53, 200);
    public static final int trappedColor = PepsiUtils.toRGBA(171, 121, 45, 200);
    public static final int enderColor = PepsiUtils.toRGBA(25, 35, 40, 200);
    public static final int hopperColor = PepsiUtils.toRGBA(45, 45, 45, 200);
    public static final int furnaceColor = PepsiUtils.toRGBA(151, 151, 151, 200);

    public StorageESPMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "StorageESP", key, hide);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        basic.clear();
        trapped.clear();
        ender.clear();
        hopper.clear();
        furnace.clear();

        for (TileEntity te : PepsiMod.INSTANCE.mc.world.loadedTileEntityList) {
            if ((PepsiMod.INSTANCE.espSettings.basic || PepsiMod.INSTANCE.espSettings.trapped) && te instanceof TileEntityChest) {
                TileEntityChest chestTe = (TileEntityChest) te;

                if (chestTe.adjacentChestXPos != null || chestTe.adjacentChestZPos != null) {
                    continue;
                }

                BlockPos pos = te.getPos();
                BlockPos other = null;

                if (chestTe.adjacentChestXNeg != null) {
                    other = chestTe.adjacentChestXNeg.getPos();
                } else if (chestTe.adjacentChestZNeg != null) {
                    other = chestTe.adjacentChestZNeg.getPos();
                }

                if (chestTe.getChestType() == BlockChest.Type.TRAP) {
                    if (PepsiMod.INSTANCE.espSettings.trapped) {
                        trapped.add(new ESPEntry(pos, other == null ? pos : other));
                    }
                } else {
                    if (PepsiMod.INSTANCE.espSettings.basic) {
                        basic.add(new ESPEntry(pos, other == null ? pos : other));
                    }
                }
            } else if (PepsiMod.INSTANCE.espSettings.ender && te instanceof TileEntityEnderChest) {
                ender.add(new ESPEntry(te.getPos()));
            } else if (PepsiMod.INSTANCE.espSettings.furnace && te instanceof TileEntityFurnace) {
                furnace.add(new ESPEntry(te.getPos()));
            } else if (PepsiMod.INSTANCE.espSettings.hopper && te instanceof TileEntityHopper) {
                hopper.add(new ESPEntry(te.getPos()));
            }
        }
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]   {
                new CustomOption<>(PepsiMod.INSTANCE.espSettings.basic, "normal", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.espSettings.basic = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.espSettings.basic;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.espSettings.trapped, "trapped", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.espSettings.trapped = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.espSettings.trapped;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.espSettings.ender, "ender", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.espSettings.ender = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.espSettings.ender;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.espSettings.hopper, "hopper", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.espSettings.hopper = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.espSettings.hopper;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.espSettings.furnace, "furnace", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.espSettings.furnace = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.espSettings.furnace;
                        })
        };
    }

    @Override
    public void onRender(float partialTicks) {
        BufferBuilder builder = GeometryTessellator.instance.getBuffer();
        builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        if (PepsiMod.INSTANCE.espSettings.basic) {
            basic.forEach((entry) -> {
                GeometryTessellator.instance.drawCuboid(builder, entry.start, entry.end, GeometryMasks.Line.ALL, chestColor);
            });
        }

        if (PepsiMod.INSTANCE.espSettings.trapped) {
            trapped.forEach((entry) -> {
                GeometryTessellator.instance.drawCuboid(builder, entry.start, entry.end, GeometryMasks.Line.ALL, trappedColor);
            });
        }

        if (PepsiMod.INSTANCE.espSettings.ender) {
            ender.forEach((entry) -> {
                GeometryTessellator.instance.drawCuboid(builder, entry.start, entry.end, GeometryMasks.Line.ALL, enderColor);
            });
        }

        if (PepsiMod.INSTANCE.espSettings.hopper) {
            hopper.forEach((entry) -> {
                GeometryTessellator.instance.drawCuboid(builder, entry.start, entry.end, GeometryMasks.Line.ALL, hopperColor);
            });
        }

        if (PepsiMod.INSTANCE.espSettings.furnace) {
            furnace.forEach((entry) -> {
                GeometryTessellator.instance.drawCuboid(builder, entry.start, entry.end, GeometryMasks.Line.ALL, furnaceColor);
            });
        }

        GeometryTessellator.instance.draw();
    }
}
