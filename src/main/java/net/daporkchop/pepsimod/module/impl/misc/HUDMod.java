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

package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.misc.TickRate;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.daporkchop.pepsimod.util.config.impl.GeneralTranslator;
import net.daporkchop.pepsimod.util.config.impl.HUDTranslator;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketCustomPayload;

import java.awt.*;

public class HUDMod extends Module {
    public static HUDMod INSTANCE;
    public String serverBrand = "";

    public HUDMod(boolean isEnabled, int key) {
        super(isEnabled, "HUD", key, true);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean shouldTick() {
        return true;
    }

    @Override
    public void tick() {
        for (Module module : ModuleManager.ENABLED_MODULES) {
            module.updateName();
        }

        ModuleManager.sortModules(GeneralTranslator.INSTANCE.sortType);
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(HUDTranslator.INSTANCE.drawLogo, "draw_logo", OptionCompletions.BOOLEAN,
                        (value) -> {
                            HUDTranslator.INSTANCE.drawLogo = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.drawLogo;
                        }, "Watermark"),
                new ModuleOption<>(HUDTranslator.INSTANCE.arrayList, "arraylist", OptionCompletions.BOOLEAN,
                        (value) -> {
                            HUDTranslator.INSTANCE.arrayList = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.arrayList;
                        }, "ArrayList"),
                new ModuleOption<>(HUDTranslator.INSTANCE.TPS, "tps", OptionCompletions.BOOLEAN,
                        (value) -> {
                            HUDTranslator.INSTANCE.TPS = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.TPS;
                        }, "TPS"),
                new ModuleOption<>(HUDTranslator.INSTANCE.coords, "coords", OptionCompletions.BOOLEAN,
                        (value) -> {
                            HUDTranslator.INSTANCE.coords = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.coords;
                        }, "Coords"),
                new ModuleOption<>(HUDTranslator.INSTANCE.netherCoords, "nether_coords", OptionCompletions.BOOLEAN,
                        (value) -> {
                            HUDTranslator.INSTANCE.netherCoords = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.netherCoords;
                        }, "NetherCoords"),
                new ModuleOption<>(HUDTranslator.INSTANCE.arrayListTop, "arraylist_top", OptionCompletions.BOOLEAN,
                        (value) -> {
                            HUDTranslator.INSTANCE.arrayListTop = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.arrayListTop;
                        }, "ArrayListOnTop"),
                new ModuleOption<>(HUDTranslator.INSTANCE.serverBrand, "server_brand", OptionCompletions.BOOLEAN,
                        (value) -> {
                            HUDTranslator.INSTANCE.serverBrand = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.serverBrand;
                        }, "ServerBrand"),
                new ModuleOption<>(HUDTranslator.INSTANCE.rainbow, "rainbow", OptionCompletions.BOOLEAN,
                        (value) -> {
                            HUDTranslator.INSTANCE.rainbow = value;
                            for (Module module : ModuleManager.AVALIBLE_MODULES) {
                                module.updateName();
                            }
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.rainbow;
                        }, "Rainbow"),
                new ModuleOption<>(HUDTranslator.INSTANCE.direction, "direction", OptionCompletions.BOOLEAN,
                        (value) -> {
                            HUDTranslator.INSTANCE.direction = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.direction;
                        }, "Direction"),
                new ModuleOption<>(HUDTranslator.INSTANCE.armor, "armor", OptionCompletions.BOOLEAN,
                        (value) -> {
                            HUDTranslator.INSTANCE.armor = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.armor;
                        }, "Armor"),
                new ModuleOption<>(HUDTranslator.INSTANCE.effects, "effects", OptionCompletions.BOOLEAN,
                        (value) -> {
                            HUDTranslator.INSTANCE.effects = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.effects;
                        }, "Effects"),
                new ModuleOption<>(HUDTranslator.INSTANCE.fps, "fps", OptionCompletions.BOOLEAN,
                        (value) -> {
                            HUDTranslator.INSTANCE.fps = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.fps;
                        }, "FPS"),
                new ModuleOption<>(HUDTranslator.INSTANCE.ping, "ping", OptionCompletions.BOOLEAN,
                        (value) -> {
                            HUDTranslator.INSTANCE.ping = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.ping;
                        }, "Ping"),
                new ModuleOption<>(HUDTranslator.INSTANCE.r, "r", new String[]{"0", "128", "255"},
                        (value) -> {
                            HUDTranslator.INSTANCE.r = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.r;
                        }, "Red", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 255, 1)),
                new ModuleOption<>(HUDTranslator.INSTANCE.g, "g", new String[]{"0", "128", "255"},
                        (value) -> {
                            HUDTranslator.INSTANCE.g = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.g;
                        }, "Green", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 255, 1)),
                new ModuleOption<>(HUDTranslator.INSTANCE.b, "b", new String[]{"0", "128", "255"},
                        (value) -> {
                            HUDTranslator.INSTANCE.b = value;
                            return true;
                        },
                        () -> {
                            return HUDTranslator.INSTANCE.b;
                        }, "Blue", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 255, 1))
        };
    }

    @Override
    public boolean preRecievePacket(Packet packet)  {
        if (packet instanceof SPacketCustomPayload) {
            if (((SPacketCustomPayload) packet).getChannelName().equals("MC|Brand"))    {
                serverBrand = ((SPacketCustomPayload) packet).getBufferData().readString(32767);
            }
        }
        return false;
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MISC;
    }

    public void registerKeybind(String name, int key) {
    }

    @Override
    public void onRenderGUI(float partialTicks, int width, int height, GuiIngame gui) {
        if (HUDTranslator.INSTANCE.drawLogo) {
            if (HUDTranslator.INSTANCE.rainbow) {
                PepsiUtils.PEPSI_NAME.drawAtPos(gui, 2, 2, 0);
            } else {
                HUDTranslator.INSTANCE.bindColor();
                mc.fontRenderer.drawString(PepsiUtils.PEPSI_NAME.text, 2, 2, HUDTranslator.INSTANCE.getColor(), true);
            }
        }

        if (HUDTranslator.INSTANCE.arrayList) {
            if (HUDTranslator.INSTANCE.arrayListTop) {
                for (int i = 0, j = 0; i < ModuleManager.ENABLED_MODULES.size(); i++) {
                    Module module = ModuleManager.ENABLED_MODULES.get(i);
                    if (module.state.hidden) {
                        continue;
                    }

                    if (HUDTranslator.INSTANCE.rainbow) {
                        if (module.text instanceof RainbowText) {
                            ((RainbowText) module.text).drawAtPos(gui, width - 2 - module.text.width(), 2 + j * 10, ++j * 10);
                        } else {
                            module.text.drawAtPos(gui, width - 2 - module.text.width(), 2 + ++j * 10);
                        }
                    } else {
                        HUDTranslator.INSTANCE.bindColor();
                        mc.fontRenderer.drawString(module.text.getRawText(), width - 2 - module.text.width(), 2 + j * 10, HUDTranslator.INSTANCE.getColor());
                        j++;
                    }
                }
            } else {
                if (!(mc.currentScreen instanceof GuiChat)) {
                    for (int i = 0, j = 0; i < ModuleManager.ENABLED_MODULES.size(); i++) {
                        Module module = ModuleManager.ENABLED_MODULES.get(i);
                        if (module.state.hidden) {
                            continue;
                        }

                        if (HUDTranslator.INSTANCE.rainbow) {
                            if (module.text instanceof RainbowText) {
                                ((RainbowText) module.text).drawAtPos(gui, width - 2 - module.text.width(), height - 2 - j * 10, ++j * 8);
                            } else {
                                module.text.drawAtPos(gui, width - 2 - module.text.width(), height - 2 - ++j * -10);
                            }
                        } else {
                            HUDTranslator.INSTANCE.bindColor();
                            mc.fontRenderer.drawString(module.text.getRawText(), width - 2 - module.text.width(), height - 12 - j * 10, HUDTranslator.INSTANCE.getColor());
                            j++;
                        }
                    }
                }
            }
        }

        int i = 0;
        if (HUDTranslator.INSTANCE.arrayListTop) {
            if (!(mc.currentScreen instanceof GuiChat)) {
                if (HUDTranslator.INSTANCE.serverBrand) {
                    String text = PepsiUtils.COLOR_ESCAPE + "7Server brand: " + PepsiUtils.COLOR_ESCAPE + "r" + HUDMod.INSTANCE.serverBrand;
                    gui.drawString(mc.fontRenderer, text, width - (mc.fontRenderer.getStringWidth("Server brand: " + HUDMod.INSTANCE.serverBrand) + 2), height - 2 - ++i * 10, Color.white.getRGB());
                }
                if (HUDTranslator.INSTANCE.ping) {
                    try {
                        int ping = mc.getConnection().getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime();
                        String text = PepsiUtils.COLOR_ESCAPE + "7Ping: " + PepsiUtils.COLOR_ESCAPE + "r" + ping;
                        gui.drawString(mc.fontRenderer, text, width - (mc.fontRenderer.getStringWidth("Ping: " + ping) + 2), height - 2 - ++i * 10, Color.white.getRGB());
                    } catch (NullPointerException e) {
                    }
                }
                if (HUDTranslator.INSTANCE.TPS) {
                    String text = PepsiUtils.COLOR_ESCAPE + "7TPS: " + PepsiUtils.COLOR_ESCAPE + "r" + TickRate.TPS;
                    gui.drawString(mc.fontRenderer, text, width - (mc.fontRenderer.getStringWidth("TPS: " + TickRate.TPS) + 2), height - 2 - ++i * 10, Color.white.getRGB());
                }
                if (HUDTranslator.INSTANCE.fps) {
                    String text = PepsiUtils.COLOR_ESCAPE + "7FPS: " + PepsiUtils.COLOR_ESCAPE + "r" + ReflectionStuff.getDebugFps();
                    gui.drawString(mc.fontRenderer, text, width - (mc.fontRenderer.getStringWidth("FPS: " + ReflectionStuff.getDebugFps()) + 2), height - 2 - ++i * 10, Color.white.getRGB());
                }
            }
        } else {
            if (HUDTranslator.INSTANCE.serverBrand) {
                String text = PepsiUtils.COLOR_ESCAPE + "7Server brand: " + PepsiUtils.COLOR_ESCAPE + "r" + HUDMod.INSTANCE.serverBrand;
                gui.drawString(mc.fontRenderer, text, width - (mc.fontRenderer.getStringWidth("Server brand: " + HUDMod.INSTANCE.serverBrand) + 2), 2 + i++ * 10, Color.white.getRGB());
            }
            if (HUDTranslator.INSTANCE.ping) {
                try {
                    int ping = mc.getConnection().getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime();
                    String text = PepsiUtils.COLOR_ESCAPE + "7Ping: " + PepsiUtils.COLOR_ESCAPE + "r" + ping;
                    gui.drawString(mc.fontRenderer, text, width - (mc.fontRenderer.getStringWidth("Ping: " + ping) + 2), 2 + i++ * 10, Color.white.getRGB());
                } catch (NullPointerException e) {
                }
            }
            if (HUDTranslator.INSTANCE.TPS) {
                String text = PepsiUtils.COLOR_ESCAPE + "7TPS: " + PepsiUtils.COLOR_ESCAPE + "r" + TickRate.TPS;
                gui.drawString(mc.fontRenderer, text, width - (mc.fontRenderer.getStringWidth("TPS: " + TickRate.TPS) + 2), 2 + i++ * 10, Color.white.getRGB());
            }
            if (HUDTranslator.INSTANCE.fps) {
                String text = PepsiUtils.COLOR_ESCAPE + "7FPS: " + PepsiUtils.COLOR_ESCAPE + "r" + ReflectionStuff.getDebugFps();
                gui.drawString(mc.fontRenderer, text, width - (mc.fontRenderer.getStringWidth("FPS: " + ReflectionStuff.getDebugFps()) + 2), 2 + i++ * 10, Color.white.getRGB());
            }
        }

        i = mc.currentScreen instanceof GuiChat ? 14 : 0;
        if (HUDTranslator.INSTANCE.coords) {
            String toRender = PepsiUtils.COLOR_ESCAPE + "7XYZ" + PepsiUtils.COLOR_ESCAPE + "f: " + PepsiUtils.COLOR_ESCAPE + "7" + PepsiUtils.roundCoords(mc.player.posX) + "" + PepsiUtils.COLOR_ESCAPE + "f, " + PepsiUtils.COLOR_ESCAPE + "7" + PepsiUtils.roundCoords(mc.player.posY) + "" + PepsiUtils.COLOR_ESCAPE + "f, " + PepsiUtils.COLOR_ESCAPE + "7" + PepsiUtils.roundCoords(mc.player.posZ);
            if (HUDTranslator.INSTANCE.netherCoords && mc.player.dimension != 1) {
                toRender += " " + PepsiUtils.COLOR_ESCAPE + "f(" + PepsiUtils.COLOR_ESCAPE + "7" + PepsiUtils.roundCoords(PepsiUtils.getDimensionCoord(mc.player.posX)) + "" + PepsiUtils.COLOR_ESCAPE + "f, " + PepsiUtils.COLOR_ESCAPE + "7" + PepsiUtils.roundCoords(mc.player.posY) + "" + PepsiUtils.COLOR_ESCAPE + "f, " + PepsiUtils.COLOR_ESCAPE + "7" + PepsiUtils.roundCoords(PepsiUtils.getDimensionCoord(mc.player.posZ)) + "" + PepsiUtils.COLOR_ESCAPE + "f)";
            }
            mc.fontRenderer.drawString(toRender, 2, height - (i += 10), Color.white.getRGB(), true);
        }
        if (HUDTranslator.INSTANCE.direction) {
            mc.fontRenderer.drawString(PepsiUtils.COLOR_ESCAPE + "7[" + PepsiUtils.COLOR_ESCAPE + "f" + PepsiUtils.getFacing() + PepsiUtils.COLOR_ESCAPE + "7]", 2, height - (i += 10), Color.white.getRGB(), true);
        }


        if (HUDTranslator.INSTANCE.armor) {
            i = 0;
            int xPos = width / 2;
            xPos -= 103;
            for (int j = 0; j < 4; j++) {
                ItemStack stack = PepsiUtils.getWearingArmor(j);
                PepsiUtils.renderItem(xPos + 20 * i++, height - 40, partialTicks, mc.player, stack);
            }
        }
    }
}
