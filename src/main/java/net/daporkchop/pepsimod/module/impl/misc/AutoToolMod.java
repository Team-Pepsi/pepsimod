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

package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.impl.player.AutoEatMod;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.network.Packet;
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
        if (!mc.gameSettings.keyBindAttack.isKeyDown() && digging) {
            digging = false;
            mc.player.inventory.currentItem = slot;
            slot = -1;
        }
    }

    public boolean preSendPacket(Packet<?> packetIn) {
        if (AutoEatMod.INSTANCE.doneEating && packetIn instanceof CPacketPlayerDigging) {
            CPacketPlayerDigging pck = (CPacketPlayerDigging) packetIn;
            if (mc.playerController.getCurrentGameType() != GameType.CREATIVE && pck.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                digging = true;
                int bestIndex = PepsiUtils.getBestTool(mc.world.getBlockState(pck.getPosition()).getBlock());
                if (bestIndex != -1) {
                    if (slot == -1) {
                        slot = mc.player.inventory.currentItem;
                    }
                    mc.player.inventory.currentItem = bestIndex;
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
