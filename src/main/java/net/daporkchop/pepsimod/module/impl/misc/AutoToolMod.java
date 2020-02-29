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

package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.impl.player.AutoEatMod;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.world.GameType;

public class AutoToolMod extends Module {
    public static AutoToolMod INSTANCE;
    public boolean digging = false;
    public int slot = -1;

    {
        INSTANCE = this;
    }

    public AutoToolMod() {
        super("AutoTool");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void tick() {
        synchronized (this) {
            if (!mc.gameSettings.keyBindAttack.isKeyDown() && this.digging) {
                this.digging = false;
                if (this.slot != -1) {
                    ReflectionStuff.setCurrentPlayerItem(mc.player.inventory.currentItem = this.slot);
                    mc.getConnection().sendPacket(new CPacketHeldItemChange(this.slot));
                    this.slot = -1;
                }
            }
        }
    }

    public boolean preSendPacket(Packet<?> packetIn) {
        if (!this.digging && AutoEatMod.INSTANCE.doneEating && packetIn instanceof CPacketPlayerDigging) {
            synchronized (this) {
                CPacketPlayerDigging pck = (CPacketPlayerDigging) packetIn;
                if (!this.digging && mc.playerController.getCurrentGameType() != GameType.CREATIVE && pck.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    this.digging = true;
                    int bestIndex = PepsiUtils.getBestTool(mc.world.getBlockState(pck.getPosition()).getBlock());
                    if (bestIndex != -1 && bestIndex != mc.player.inventory.currentItem) {
                        if (this.slot == -1) {
                            this.slot = mc.player.inventory.currentItem;
                        }
                        ReflectionStuff.setCurrentPlayerItem(mc.player.inventory.currentItem = bestIndex);
                        mc.getConnection().sendPacket(new CPacketHeldItemChange(bestIndex));
                        mc.getConnection().sendPacket(packetIn);
                        return true;
                    }
                }
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
        return new ModuleOption[0];
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MISC;
    }
}
