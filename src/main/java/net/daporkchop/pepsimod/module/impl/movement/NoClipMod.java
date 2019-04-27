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

package net.daporkchop.pepsimod.module.impl.movement;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.util.ReflectionStuff;

public class NoClipMod extends Module {
    public static NoClipMod INSTANCE;

    {
        INSTANCE = this;
    }

    public NoClipMod() {
        super("NoClip");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        if (pepsimod.isInitialized) {
            mc.player.noClip = false;
        }
    }

    @Override
    public void tick() {
        mc.player.noClip = true;
        mc.player.fallDistance = 0;
        mc.player.onGround = false;

        mc.player.capabilities.isFlying = false;
        mc.player.motionX = 0;
        mc.player.motionY = 0;
        mc.player.motionZ = 0;

        float speed = 0.2F;
        mc.player.jumpMovementFactor = speed;
        if (ReflectionStuff.getPressed(mc.gameSettings.keyBindJump)) {
            mc.player.motionY += speed;
        }

        if (ReflectionStuff.getPressed(mc.gameSettings.keyBindSneak)) {
            mc.player.motionY -= speed;
        }
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
        return ModuleCategory.MOVEMENT;
    }
}
