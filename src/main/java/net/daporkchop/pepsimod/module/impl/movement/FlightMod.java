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

package net.daporkchop.pepsimod.module.impl.movement;

import net.daporkchop.pepsimod.command.impl.DamageCommand;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.totally.not.skidded.RenderUtils;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.config.impl.FlightTranslator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class FlightMod extends Module {
    public static FlightMod INSTANCE;
    public boolean wasOnGround = true;
    public double roofY = -1.0D;

    public FlightMod() {
        super("Flight");
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            DamageCommand.damage(5);
        }
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        EntityPlayer player = mc.player;

        player.motionX = 0;
        player.motionY = 0;
        player.motionZ = 0;
        ReflectionStuff.setLandMovementFactor(player, FlightTranslator.INSTANCE.speed);
        player.jumpMovementFactor = FlightTranslator.INSTANCE.speed;
        ReflectionStuff.setInWater(player, false);

        if (mc.inGameHasFocus) {
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
                player.motionY += FlightTranslator.INSTANCE.speed / 2 + 0.2F;
            }
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                player.motionY -= FlightTranslator.INSTANCE.speed / 2 + 0.2F;
            }
        }

    }

    @Override
    public void onRender(float partialTicks) {
        if (this.roofY == -1.0D) {
            return;
        }
        double y = this.roofY - ReflectionStuff.getRenderPosY() + mc.player.height;

        int color = 16711680;

        PepsiUtils.glColor(new Color(553582592));
        RenderUtils.drawSolidBox(new AxisAlignedBB(-2.0D, 0.0D + y, -2.0D, 2.0D, 0.0D + y, 2.0D));
        PepsiUtils.glColor(new Color(16711680));
        RenderUtils.drawOutlinedBox(new AxisAlignedBB(-2.0D, 0.0D + y, -2.0D, 2.0D, 0.0D + y, 2.0D));
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(FlightTranslator.INSTANCE.speed, "speed", OptionCompletions.FLOAT,
                        (value) -> {
                            FlightTranslator.INSTANCE.speed = Math.max(0, value);
                            return true;
                        },
                        () -> {
                            return FlightTranslator.INSTANCE.speed;
                        }, "Speed", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0.1f, 10f, 0.1f))
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MOVEMENT;
    }
}
