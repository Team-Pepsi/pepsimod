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

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.clickgui.ClickGUI;
import net.daporkchop.pepsimod.clickgui.Window;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.TimeModule;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.misc.announcer.MessagePrefixes;
import net.daporkchop.pepsimod.util.misc.announcer.QueuedTask;
import net.daporkchop.pepsimod.util.misc.announcer.TaskType;
import net.daporkchop.pepsimod.util.misc.announcer.impl.TaskBasic;
import net.daporkchop.pepsimod.util.misc.announcer.impl.TaskBlock;
import net.daporkchop.pepsimod.util.misc.announcer.impl.TaskMove;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AnnouncerMod extends TimeModule {
    public static AnnouncerMod INSTANCE;
    public Queue<QueuedTask> toSend = new ConcurrentLinkedQueue<>();

    public AnnouncerMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "Announcer", key, false);
    }

    @Override
    public void onEnable() {
        for (Window window : ClickGUI.INSTANCE.windows) {
            window.openGui();
        }

        PepsiMod.INSTANCE.mc.displayGuiScreen(ClickGUI.INSTANCE);
    }

    @Override
    public void onDisable() {
        if (PepsiMod.INSTANCE.mc.currentScreen instanceof ClickGUI) {
            PepsiMod.INSTANCE.mc.displayGuiScreen(null);
        }
    }

    @Override
    public void tick() {
        if (PepsiMod.INSTANCE.mc.world != null && PepsiMod.INSTANCE.mc.world.isRemote) {
            updateMS();
            if (hasTimePassedM(PepsiMod.INSTANCE.announcerSettings.delay)) {
                updateLastMS();
                MAINLOOP:
                while (toSend.size() > 0) {
                    QueuedTask task = toSend.poll();
                    if (task != null) {
                        String msg = task.getMessage();
                        if (msg != null) {
                            if (PepsiMod.INSTANCE.announcerSettings.clientSide) {
                                PepsiMod.INSTANCE.mc.player.sendMessage(new TextComponentString(PepsiUtils.COLOR_ESCAPE + "a" + msg));
                            } else {
                                PepsiMod.INSTANCE.mc.player.sendChatMessage(msg);
                            }
                            break MAINLOOP;
                        } else {
                            continue MAINLOOP;
                        }
                    }
                }
            }

            if (PepsiMod.INSTANCE.announcerSettings.walk) {
                Iterator<QueuedTask> iterator = toSend.iterator();
                boolean hasMoveValue = false;
                while (iterator.hasNext()) {
                    if (iterator.next() instanceof TaskMove) {
                        hasMoveValue = true;
                    }
                }
                if (!hasMoveValue) {
                    toSend.add(new TaskMove(TaskType.WALK));
                }
            }
        }
    }

    @Override
    public void init() {
        INSTANCE = this;
        MinecraftForge.EVENT_BUS.register(this);
        updateLastMS();
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(PepsiMod.INSTANCE.announcerSettings.clientSide, "client", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.announcerSettings.clientSide = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.announcerSettings.clientSide;
                        }, "Client Sided"),
                new ModuleOption<>(PepsiMod.INSTANCE.announcerSettings.join, "join", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.announcerSettings.join = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.announcerSettings.join;
                        }, "Join"),
                new ModuleOption<>(PepsiMod.INSTANCE.announcerSettings.leave, "leave", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.announcerSettings.leave = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.announcerSettings.leave;
                        }, "Leave"),
                new ModuleOption<>(PepsiMod.INSTANCE.announcerSettings.eat, "eat", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.announcerSettings.eat = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.announcerSettings.eat;
                        }, "Food"),
                new ModuleOption<>(PepsiMod.INSTANCE.announcerSettings.walk, "walk", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.announcerSettings.walk = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.announcerSettings.walk;
                        }, "Walk"),
                new ModuleOption<>(PepsiMod.INSTANCE.announcerSettings.mine, "mine", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.announcerSettings.mine = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.announcerSettings.mine;
                        }, "Mined"),
                new ModuleOption<>(PepsiMod.INSTANCE.announcerSettings.place, "place", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.announcerSettings.place = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.announcerSettings.place;
                        }, "Place"),
                new ModuleOption<>(PepsiMod.INSTANCE.announcerSettings.delay, "delay", OptionCompletions.INTEGER,
                        (value) -> {
                            PepsiMod.INSTANCE.announcerSettings.delay = Math.max(value, 0);
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.announcerSettings.delay;
                        }, "Delay", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 10000, 500))
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MISC;
    }

    public void onBreakBlock(IBlockState state) {
        if (PepsiMod.INSTANCE.announcerSettings.mine) {
            Iterator<QueuedTask> iterator = toSend.iterator();
            while (iterator.hasNext()) {
                QueuedTask task = iterator.next();
                if (task.type == TaskType.BREAK) {
                    TaskBlock taskBlock = (TaskBlock) task;
                    if (taskBlock.block == state.getBlock()) {
                        taskBlock.count++;
                        return;
                    }
                }
            }
            toSend.add(new TaskBlock(TaskType.BREAK, state.getBlock()));
            tick();
        }
    }

    public void onPlaceBlock(BlockEvent.PlaceEvent event) {
        if (PepsiMod.INSTANCE.announcerSettings.place && event.getPlayer() == PepsiMod.INSTANCE.mc.player) {
            Iterator<QueuedTask> iterator = toSend.iterator();
            while (iterator.hasNext()) {
                QueuedTask task = iterator.next();
                if (task.type == TaskType.PLACE) {
                    TaskBlock taskBlock = (TaskBlock) task;
                    if (taskBlock.block == event.getState().getBlock()) {
                        taskBlock.count++;
                        return;
                    }
                }
            }
            toSend.add(new TaskBlock(TaskType.PLACE, event.getState().getBlock()));
            tick();
        }
    }

    public void onPlayerJoin(String name) {
        if (PepsiMod.INSTANCE.announcerSettings.join) {
            QueuedTask task = new TaskBasic(TaskType.JOIN, MessagePrefixes.getMessage(TaskType.JOIN, name));
            if (hasTimePassedM(2000))   {
                updateLastMS();
                String msg = task.getMessage();
                if (msg != null) {
                    if (PepsiMod.INSTANCE.announcerSettings.clientSide) {
                        PepsiMod.INSTANCE.mc.player.sendMessage(new TextComponentString(PepsiUtils.COLOR_ESCAPE + "a" + msg));
                    } else {
                        PepsiMod.INSTANCE.mc.player.sendChatMessage(msg);
                    }
                    return;
                }
            }
            toSend.add(task);
            tick();
        }
    }

    public void onPlayerLeave(String name) {
        if (PepsiMod.INSTANCE.announcerSettings.leave) {
            QueuedTask task = new TaskBasic(TaskType.LEAVE, MessagePrefixes.getMessage(TaskType.LEAVE, name));
            if (hasTimePassedM(2000))   {
                updateLastMS();
                String msg = task.getMessage();
                if (msg != null) {
                    if (PepsiMod.INSTANCE.announcerSettings.clientSide) {
                        PepsiMod.INSTANCE.mc.player.sendMessage(new TextComponentString(PepsiUtils.COLOR_ESCAPE + "a" + msg));
                    } else {
                        PepsiMod.INSTANCE.mc.player.sendChatMessage(msg);
                    }
                    return;
                }
            }
            toSend.add(task);
            tick();
        }
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        toSend.clear();
    }
}
