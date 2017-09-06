/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.daporkchop.pepsimod.mixin.util;

import net.daporkchop.pepsimod.module.impl.misc.TimerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Timer.class)
public abstract class MixinTimer {
    @Shadow
    public int elapsedTicks;

    @Shadow
    public float renderPartialTicks;

    @Shadow
    public float elapsedPartialTicks;

    @Shadow
    private long lastSyncSysClock;

    @Shadow
    private float tickLength;

    /**
     * this hides a stupid warning
     */
    @Overwrite
    public void updateTimer() {
        float timerSpeed = (TimerMod.INSTANCE == null ?
                1.0f :
                TimerMod.INSTANCE.getMultiplier());

        long i = Minecraft.getSystemTime();
        this.elapsedPartialTicks = (float) (i - this.lastSyncSysClock) / this.tickLength * timerSpeed;
        this.lastSyncSysClock = i;
        this.renderPartialTicks += elapsedPartialTicks;
        this.elapsedTicks = (int) this.renderPartialTicks;
        this.renderPartialTicks -= this.elapsedTicks;
    }
}
