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

package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleLaunchState;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.EntityFakePlayer;
import net.daporkchop.pepsimod.util.config.impl.FreecamTranslator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class FreecamMod extends Module {
    public static FreecamMod INSTANCE;
    public EntityFakePlayer fakePlayer;

    {
        INSTANCE = this;
    }

    public FreecamMod() {
        super("Freecam");
    }

    @Override
    public void onEnable() {
        INSTANCE = this;//adding this a bunch because it always seems to be null idk y
        if (pepsimod.hasInitializedModules) {
            this.fakePlayer = new EntityFakePlayer();
        }
    }

    @Override
    public void onDisable() {
        INSTANCE = this; //adding this a bunch because it always seems to be null idk y
        if (pepsimod.hasInitializedModules) {
            this.fakePlayer.resetPlayerPosition();
            this.fakePlayer.despawn();
        }
    }

    @Override
    public void tick() {
        float speed = FreecamTranslator.INSTANCE.speed;
        mc.player.motionX = 0.0d;
        mc.player.motionY = 0.0d;
        mc.player.motionZ = 0.0d;

        if (mc.gameSettings.keyBindJump.isKeyDown())    {
            mc.player.motionY += speed;
        }
        if (mc.gameSettings.keyBindSneak.isKeyDown())    {
            mc.player.motionY -= speed;
        }

        float forward = 0.0f;
        if (mc.gameSettings.keyBindForward.isKeyDown())    {
            forward += speed;
        }
        if (mc.gameSettings.keyBindBack.isKeyDown())    {
            forward -= speed;
        }

        float strafe = 0.0f;
        if (mc.gameSettings.keyBindLeft.isKeyDown())    {
            strafe += speed;
        }
        if (mc.gameSettings.keyBindRight.isKeyDown())    {
            strafe -= speed;
        }

        float yaw = mc.player.rotationYaw;
        mc.player.motionX = (forward * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * Math.sin(Math.toRadians(yaw + 90.0F)));
        mc.player.motionZ = (forward * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * Math.cos(Math.toRadians(yaw + 90.0F)));
    }

    @Override
    public void init() {
        INSTANCE = this; //adding this a bunch because it always seems to be null idk y
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(1.0f, "speed", new String[]{"1.0", "0.0"},
                        (value) -> {
                            if (value <= 0.0f) {
                                clientMessage("Speed cannot be negative or 0!");
                                return false;
                            }
                            FreecamTranslator.INSTANCE.speed = value;
                            return true;
                        },
                        () -> {
                            return FreecamTranslator.INSTANCE.speed;
                        }, "Speed", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0.0f, 1.0f, 0.1f))
        };
    }

    @Override
    public boolean preSendPacket(Packet<?> packetIn) {
        return packetIn instanceof CPacketPlayer;
    }

    @Override
    public ModuleLaunchState getLaunchState() {
        return ModuleLaunchState.DISABLED;
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MISC;
    }
}
