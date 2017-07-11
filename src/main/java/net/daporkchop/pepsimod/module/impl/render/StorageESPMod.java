package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.api.CustomOption;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.OptionTypeBoolean;
import net.daporkchop.pepsimod.totally.not.skidded.RenderUtils;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.RenderColor;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class StorageESPMod extends Module {
    public static final RenderColor chestColor = new RenderColor(196, 139, 53, 128);
    public static final RenderColor trappedColor = new RenderColor(81, 57, 22, 128);
    public static final RenderColor enderColor = new RenderColor(25, 35, 40, 128);
    public static final RenderColor hopperColor = new RenderColor(45, 45, 45, 128);
    public static final RenderColor furnaceColor = new RenderColor(151, 151, 151, 128);
    public static StorageESPMod INSTANCE;
    public final ArrayList<AxisAlignedBB> basic = new ArrayList<>();
    public final ArrayList<AxisAlignedBB> trapped = new ArrayList<>();
    public final ArrayList<AxisAlignedBB> ender = new ArrayList<>();
    public final ArrayList<AxisAlignedBB> hopper = new ArrayList<>();
    public final ArrayList<AxisAlignedBB> furnace = new ArrayList<>();

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

                AxisAlignedBB bb = PepsiUtils.offsetBB(PepsiUtils.cloneBB(PepsiUtils.getBoundingBox(PepsiMod.INSTANCE.mc.world, te.getPos())), te.getPos());

                if (chestTe.adjacentChestXNeg != null) {
                    PepsiUtils.unionBB(bb, PepsiUtils.offsetBB(PepsiUtils.cloneBB(PepsiUtils.getBoundingBox(PepsiMod.INSTANCE.mc.world, chestTe.adjacentChestXNeg.getPos())), chestTe.adjacentChestXNeg.getPos()));
                } else if (chestTe.adjacentChestZNeg != null) {
                    PepsiUtils.unionBB(bb, PepsiUtils.offsetBB(PepsiUtils.cloneBB(PepsiUtils.getBoundingBox(PepsiMod.INSTANCE.mc.world, chestTe.adjacentChestZNeg.getPos())), chestTe.adjacentChestZNeg.getPos()));
                }

                if (chestTe.getChestType() == BlockChest.Type.TRAP) {
                    if (PepsiMod.INSTANCE.espSettings.trapped) {
                        trapped.add(bb);
                    }
                } else {
                    if (PepsiMod.INSTANCE.espSettings.basic) {
                        basic.add(bb);
                    }
                }
            } else if (PepsiMod.INSTANCE.espSettings.ender && te instanceof TileEntityEnderChest) {
                ender.add(PepsiUtils.offsetBB(PepsiUtils.cloneBB(PepsiUtils.getBoundingBox(PepsiMod.INSTANCE.mc.world, te.getPos())), te.getPos()));
            } else if (PepsiMod.INSTANCE.espSettings.furnace && te instanceof TileEntityFurnace) {
                furnace.add(PepsiUtils.offsetBB(PepsiUtils.cloneBB(PepsiUtils.getBoundingBox(PepsiMod.INSTANCE.mc.world, te.getPos())), te.getPos()));
            } else if (PepsiMod.INSTANCE.espSettings.hopper && te instanceof TileEntityHopper) {
                hopper.add(PepsiUtils.offsetBB(PepsiUtils.cloneBB(PepsiUtils.getBoundingBox(PepsiMod.INSTANCE.mc.world, te.getPos())), te.getPos()));
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
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glPushMatrix();
        GL11.glTranslated(-PepsiMod.INSTANCE.mc.getRenderManager().renderPosX, -PepsiMod.INSTANCE.mc.getRenderManager().renderPosY, -PepsiMod.INSTANCE.mc.getRenderManager().renderPosZ);

        if (PepsiMod.INSTANCE.espSettings.basic) {
            GL11.glColor4b(chestColor.r, chestColor.g, chestColor.b, chestColor.a);

            basic.forEach((entry) -> {
                RenderUtils.drawOutlinedBox(entry);
            });
        }

        if (PepsiMod.INSTANCE.espSettings.trapped) {
            GL11.glColor4b(trappedColor.r, trappedColor.g, trappedColor.b, trappedColor.a);

            trapped.forEach((entry) -> {
                RenderUtils.drawOutlinedBox(entry);
            });
        }

        if (PepsiMod.INSTANCE.espSettings.ender) {
            GL11.glColor4b(enderColor.r, enderColor.g, enderColor.b, enderColor.a);

            ender.forEach((entry) -> {
                RenderUtils.drawOutlinedBox(entry);
            });
        }

        if (PepsiMod.INSTANCE.espSettings.hopper) {
            GL11.glColor4b(hopperColor.r, hopperColor.g, hopperColor.b, hopperColor.a);

            hopper.forEach((entry) -> {
                RenderUtils.drawOutlinedBox(entry);
            });
        }

        if (PepsiMod.INSTANCE.espSettings.furnace) {
            GL11.glColor4b(furnaceColor.r, furnaceColor.g, furnaceColor.b, furnaceColor.a);

            furnace.forEach((entry) -> {
                RenderUtils.drawOutlinedBox(entry);
            });
        }

        GL11.glPopMatrix();

        // GL resets
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }
}
