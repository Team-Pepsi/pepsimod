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

package net.daporkchop.pepsimod.event;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.clickgui.ClickGUI;
import net.daporkchop.pepsimod.misc.TickRate;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.impl.misc.HUDMod;
import net.daporkchop.pepsimod.util.BetterScaledResolution;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class GuiRenderHandler {
    public static GuiRenderHandler INSTANCE;

    {
        INSTANCE = this;
        new BetterScaledResolution();
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }

        if (PepsiMod.INSTANCE.mc.currentScreen == null || PepsiMod.INSTANCE.mc.currentScreen instanceof GuiChat || PepsiMod.INSTANCE.mc.currentScreen instanceof ClickGUI) {
            GuiIngame gui = PepsiMod.INSTANCE.mc.ingameGUI;

            BetterScaledResolution.INSTANCE.update();
            int width = BetterScaledResolution.INSTANCE.scaledWidth;
            int height = BetterScaledResolution.INSTANCE.scaledHeight;

            if (PepsiMod.INSTANCE.hudSettings.drawLogo) {
                if (PepsiMod.INSTANCE.hudSettings.rainbow) {
                    PepsiUtils.PEPSI_NAME.drawAtPos(gui, 2, 2, 0);
                } else {
                    PepsiMod.INSTANCE.hudSettings.bindColor();
                    PepsiMod.INSTANCE.mc.fontRenderer.drawString(PepsiUtils.PEPSI_NAME.text, 2, 2, PepsiMod.INSTANCE.hudSettings.getColor(), true);
                }
            }

            if (PepsiMod.INSTANCE.hudSettings.arrayList) {
                if (PepsiMod.INSTANCE.hudSettings.arrayListTop) {
                    for (int i = 0, j = 0; i < ModuleManager.ENABLED_MODULES.size(); i++) {
                        Module module = ModuleManager.ENABLED_MODULES.get(i);
                        if (module.hide) {
                            continue;
                        }

                        if (PepsiMod.INSTANCE.hudSettings.rainbow) {
                            if (module.text instanceof RainbowText) {
                                ((RainbowText) module.text).drawAtPos(gui, width - 2 - module.text.width(), 2 + j * 10, ++j * 10);
                            } else {
                                module.text.drawAtPos(gui, width - 2 - module.text.width(), 2 + ++j * 10);
                            }
                        } else {
                            PepsiMod.INSTANCE.hudSettings.bindColor();
                            PepsiMod.INSTANCE.mc.fontRenderer.drawString(module.text.getRawText(), width - 2 - module.text.width(), 2 + j * 10, PepsiMod.INSTANCE.hudSettings.getColor());
                            j++;
                        }
                    }
                } else {
                    if (!(PepsiMod.INSTANCE.mc.currentScreen instanceof GuiChat)) {
                        for (int i = 0, j = 0; i < ModuleManager.ENABLED_MODULES.size(); i++) {
                            Module module = ModuleManager.ENABLED_MODULES.get(i);
                            if (module.hide) {
                                continue;
                            }

                            if (PepsiMod.INSTANCE.hudSettings.rainbow) {
                                if (module.text instanceof RainbowText) {
                                    ((RainbowText) module.text).drawAtPos(gui, width - 2 - module.text.width(), height - 2 - j * 10, ++j * 8);
                                } else {
                                    module.text.drawAtPos(gui, width - 2 - module.text.width(), height - 2 - ++j * -10);
                                }
                            } else {
                                PepsiMod.INSTANCE.hudSettings.bindColor();
                                PepsiMod.INSTANCE.mc.fontRenderer.drawString(module.text.getRawText(), width - 2 - module.text.width(), height - 12 - j * 10, PepsiMod.INSTANCE.hudSettings.getColor());
                                j++;
                            }
                        }
                    }
                }
            }

            int i = 0;
            if (PepsiMod.INSTANCE.hudSettings.arrayListTop) {
                if (!(PepsiMod.INSTANCE.mc.currentScreen instanceof GuiChat)) {
                    if (PepsiMod.INSTANCE.hudSettings.serverBrand) {
                        String text = PepsiUtils.COLOR_ESCAPE + "7Server brand: " + PepsiUtils.COLOR_ESCAPE + "r" + HUDMod.INSTANCE.serverBrand;
                        gui.drawString(PepsiMod.INSTANCE.mc.fontRenderer, text, width - (PepsiMod.INSTANCE.mc.fontRenderer.getStringWidth("Server brand: " + HUDMod.INSTANCE.serverBrand) + 2), height - 2 - ++i * 10, Color.white.getRGB());
                    }
                    if (PepsiMod.INSTANCE.hudSettings.ping) {
                        try {
                            int ping = PepsiMod.INSTANCE.mc.getConnection().getPlayerInfo(PepsiMod.INSTANCE.mc.getConnection().getGameProfile().getId()).getResponseTime();
                            String text = PepsiUtils.COLOR_ESCAPE + "7Ping: " + PepsiUtils.COLOR_ESCAPE + "r" + ping;
                            gui.drawString(PepsiMod.INSTANCE.mc.fontRenderer, text, width - (PepsiMod.INSTANCE.mc.fontRenderer.getStringWidth("Ping: " + ping) + 2), height - 2 - ++i * 10, Color.white.getRGB());
                        } catch (NullPointerException e) {
                        }
                    }
                    if (PepsiMod.INSTANCE.hudSettings.TPS) {
                        String text = PepsiUtils.COLOR_ESCAPE + "7TPS: " + PepsiUtils.COLOR_ESCAPE + "r" + TickRate.TPS;
                        gui.drawString(PepsiMod.INSTANCE.mc.fontRenderer, text, width - (PepsiMod.INSTANCE.mc.fontRenderer.getStringWidth("TPS: " + TickRate.TPS) + 2), height - 2 - ++i * 10, Color.white.getRGB());
                    }
                    if (PepsiMod.INSTANCE.hudSettings.fps) {
                        String text = PepsiUtils.COLOR_ESCAPE + "7FPS: " + PepsiUtils.COLOR_ESCAPE + "r" + ReflectionStuff.getDebugFps();
                        gui.drawString(PepsiMod.INSTANCE.mc.fontRenderer, text, width - (PepsiMod.INSTANCE.mc.fontRenderer.getStringWidth("FPS: " + ReflectionStuff.getDebugFps()) + 2), height - 2 - ++i * 10, Color.white.getRGB());
                    }
                }
            } else {
                if (PepsiMod.INSTANCE.hudSettings.serverBrand) {
                    String text = PepsiUtils.COLOR_ESCAPE + "7Server brand: " + PepsiUtils.COLOR_ESCAPE + "r" + HUDMod.INSTANCE.serverBrand;
                    gui.drawString(PepsiMod.INSTANCE.mc.fontRenderer, text, width - (PepsiMod.INSTANCE.mc.fontRenderer.getStringWidth("Server brand: " + HUDMod.INSTANCE.serverBrand) + 2), 2 + i++ * 10, Color.white.getRGB());
                }
                if (PepsiMod.INSTANCE.hudSettings.ping) {
                    try {
                        int ping = PepsiMod.INSTANCE.mc.getConnection().getPlayerInfo(PepsiMod.INSTANCE.mc.getConnection().getGameProfile().getId()).getResponseTime();
                        String text = PepsiUtils.COLOR_ESCAPE + "7Ping: " + PepsiUtils.COLOR_ESCAPE + "r" + ping;
                        gui.drawString(PepsiMod.INSTANCE.mc.fontRenderer, text, width - (PepsiMod.INSTANCE.mc.fontRenderer.getStringWidth("Ping: " + ping) + 2), 2 + i++ * 10, Color.white.getRGB());
                    } catch (NullPointerException e) {
                    }
                }
                if (PepsiMod.INSTANCE.hudSettings.TPS) {
                    String text = PepsiUtils.COLOR_ESCAPE + "7TPS: " + PepsiUtils.COLOR_ESCAPE + "r" + TickRate.TPS;
                    gui.drawString(PepsiMod.INSTANCE.mc.fontRenderer, text, width - (PepsiMod.INSTANCE.mc.fontRenderer.getStringWidth("TPS: " + TickRate.TPS) + 2), 2 + i++ * 10, Color.white.getRGB());
                }
                if (PepsiMod.INSTANCE.hudSettings.fps) {
                    String text = PepsiUtils.COLOR_ESCAPE + "7FPS: " + PepsiUtils.COLOR_ESCAPE + "r" + ReflectionStuff.getDebugFps();
                    gui.drawString(PepsiMod.INSTANCE.mc.fontRenderer, text, width - (PepsiMod.INSTANCE.mc.fontRenderer.getStringWidth("FPS: " + ReflectionStuff.getDebugFps()) + 2), 2 + i++ * 10, Color.white.getRGB());
                }
            }

            i = 0;
            if (!(PepsiMod.INSTANCE.mc.currentScreen instanceof GuiChat)) {
                if (PepsiMod.INSTANCE.hudSettings.coords) {
                    String toRender = PepsiUtils.COLOR_ESCAPE + "7XYZ" + PepsiUtils.COLOR_ESCAPE + "f: " + PepsiUtils.COLOR_ESCAPE + "7" + PepsiUtils.roundCoords(PepsiMod.INSTANCE.mc.player.posX) + "" + PepsiUtils.COLOR_ESCAPE + "f, " + PepsiUtils.COLOR_ESCAPE + "7" + PepsiUtils.roundCoords(PepsiMod.INSTANCE.mc.player.posY) + "" + PepsiUtils.COLOR_ESCAPE + "f, " + PepsiUtils.COLOR_ESCAPE + "7" + PepsiUtils.roundCoords(PepsiMod.INSTANCE.mc.player.posZ);
                    if (PepsiMod.INSTANCE.hudSettings.netherCoords) {
                        toRender += " " + PepsiUtils.COLOR_ESCAPE + "f(" + PepsiUtils.COLOR_ESCAPE + "7" + PepsiUtils.roundCoords(PepsiMod.INSTANCE.mc.player.posX / 8) + "" + PepsiUtils.COLOR_ESCAPE + "f, " + PepsiUtils.COLOR_ESCAPE + "7" + PepsiUtils.roundCoords(PepsiMod.INSTANCE.mc.player.posY) + "" + PepsiUtils.COLOR_ESCAPE + "f, " + PepsiUtils.COLOR_ESCAPE + "7" + PepsiUtils.roundCoords(PepsiMod.INSTANCE.mc.player.posZ / 8) + "" + PepsiUtils.COLOR_ESCAPE + "f)";
                    }
                    PepsiMod.INSTANCE.mc.fontRenderer.drawString(toRender, 2, height - ++i * 10, Color.white.getRGB());
                }
                if (PepsiMod.INSTANCE.hudSettings.direction) {
                    PepsiMod.INSTANCE.mc.fontRenderer.drawString(PepsiUtils.COLOR_ESCAPE + "f[" + PepsiUtils.COLOR_ESCAPE + "f" + PepsiUtils.getFacing() + PepsiUtils.COLOR_ESCAPE + "7]", 2, height - ++i * 10, Color.white.getRGB());
                }
            }

            if (PepsiMod.INSTANCE.hudSettings.armor) {
                i = 0;
                int xPos = width / 2;
                xPos -= 103;
                for (int j = 0; j < 4; j++) {
                    ItemStack stack = PepsiUtils.getWearingArmor(j);
                    PepsiUtils.renderItem(xPos + 20 * i++, height - 40, event.getPartialTicks(), PepsiMod.INSTANCE.mc.player, stack);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event)   {
        for (Module module : ModuleManager.ENABLED_MODULES) {
            module.onRender(event.getPartialTicks());
        }
    }
}
