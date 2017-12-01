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

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.TimeModule;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.config.impl.AnnouncerTranslator;
import net.daporkchop.pepsimod.util.misc.announcer.MessagePrefixes;
import net.daporkchop.pepsimod.util.misc.announcer.QueuedTask;
import net.daporkchop.pepsimod.util.misc.announcer.TaskType;
import net.daporkchop.pepsimod.util.misc.announcer.impl.TaskBasic;
import net.daporkchop.pepsimod.util.misc.announcer.impl.TaskBlock;
import net.daporkchop.pepsimod.util.misc.announcer.impl.TaskMove;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AnnouncerMod extends TimeModule {
    public static AnnouncerMod INSTANCE;

    {
        INSTANCE = this;
    }

    public Queue<QueuedTask> toSend = new ConcurrentLinkedQueue<>();

    public AnnouncerMod() {
        super("Announcer");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        if (mc.world != null && mc.world.isRemote) {
            updateMS();
            if (hasTimePassedM(AnnouncerTranslator.INSTANCE.delay)) {
                updateLastMS();
                MAINLOOP:
                while (toSend.size() > 0) {
                    QueuedTask task = toSend.poll();
                    if (task != null) {
                        String msg = task.getMessage();
                        if (msg != null) {
                            if (AnnouncerTranslator.INSTANCE.clientSide) {
                                mc.player.sendMessage(new TextComponentString(PepsiUtils.COLOR_ESCAPE + "a" + msg));
                            } else {
                                mc.player.sendChatMessage(msg);
                            }
                            break MAINLOOP;
                        } else {
                            continue MAINLOOP;
                        }
                    }
                }
            }

            if (AnnouncerTranslator.INSTANCE.walk) {
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
                new ModuleOption<>(AnnouncerTranslator.INSTANCE.clientSide, "client", OptionCompletions.BOOLEAN,
                        (value) -> {
                            AnnouncerTranslator.INSTANCE.clientSide = value;
                            return true;
                        },
                        () -> {
                            return AnnouncerTranslator.INSTANCE.clientSide;
                        }, "Client Sided"),
                new ModuleOption<>(AnnouncerTranslator.INSTANCE.join, "join", OptionCompletions.BOOLEAN,
                        (value) -> {
                            AnnouncerTranslator.INSTANCE.join = value;
                            return true;
                        },
                        () -> {
                            return AnnouncerTranslator.INSTANCE.join;
                        }, "Join"),
                new ModuleOption<>(AnnouncerTranslator.INSTANCE.leave, "leave", OptionCompletions.BOOLEAN,
                        (value) -> {
                            AnnouncerTranslator.INSTANCE.leave = value;
                            return true;
                        },
                        () -> {
                            return AnnouncerTranslator.INSTANCE.leave;
                        }, "Leave"),
                new ModuleOption<>(AnnouncerTranslator.INSTANCE.eat, "eat", OptionCompletions.BOOLEAN,
                        (value) -> {
                            AnnouncerTranslator.INSTANCE.eat = value;
                            return true;
                        },
                        () -> {
                            return AnnouncerTranslator.INSTANCE.eat;
                        }, "Food"),
                new ModuleOption<>(AnnouncerTranslator.INSTANCE.walk, "walk", OptionCompletions.BOOLEAN,
                        (value) -> {
                            AnnouncerTranslator.INSTANCE.walk = value;
                            return true;
                        },
                        () -> {
                            return AnnouncerTranslator.INSTANCE.walk;
                        }, "Walk"),
                new ModuleOption<>(AnnouncerTranslator.INSTANCE.mine, "mine", OptionCompletions.BOOLEAN,
                        (value) -> {
                            AnnouncerTranslator.INSTANCE.mine = value;
                            return true;
                        },
                        () -> {
                            return AnnouncerTranslator.INSTANCE.mine;
                        }, "Mined"),
                new ModuleOption<>(AnnouncerTranslator.INSTANCE.place, "place", OptionCompletions.BOOLEAN,
                        (value) -> {
                            AnnouncerTranslator.INSTANCE.place = value;
                            return true;
                        },
                        () -> {
                            return AnnouncerTranslator.INSTANCE.place;
                        }, "Place"),
                new ModuleOption<>(AnnouncerTranslator.INSTANCE.delay, "delay", OptionCompletions.INTEGER,
                        (value) -> {
                            AnnouncerTranslator.INSTANCE.delay = Math.max(value, 0);
                            return true;
                        },
                        () -> {
                            return AnnouncerTranslator.INSTANCE.delay;
                        }, "Delay", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 10000, 500))
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MISC;
    }

    public void onBreakBlock(IBlockState state) {
        if (this.state.enabled && AnnouncerTranslator.INSTANCE.mine) {
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

    public void onPlaceBlock(Block block) {
        if (this.state.enabled && AnnouncerTranslator.INSTANCE.place) {
            Iterator<QueuedTask> iterator = toSend.iterator();
            while (iterator.hasNext()) {
                QueuedTask task = iterator.next();
                if (task.type == TaskType.PLACE) {
                    TaskBlock taskBlock = (TaskBlock) task;
                    if (taskBlock.block == block) {
                        taskBlock.count++;
                        return;
                    }
                }
            }
            toSend.add(new TaskBlock(TaskType.PLACE, block));
            tick();
        }
    }

    public void onPlayerJoin(String name) {
        if (this.state.enabled && AnnouncerTranslator.INSTANCE.join) {
            QueuedTask task = new TaskBasic(TaskType.JOIN, MessagePrefixes.getMessage(TaskType.JOIN, name));
            if (hasTimePassedM(2000))   {
                updateLastMS();
                String msg = task.getMessage();
                if (msg != null) {
                    if (AnnouncerTranslator.INSTANCE.clientSide) {
                        mc.player.sendMessage(new TextComponentString(PepsiUtils.COLOR_ESCAPE + "a" + msg));
                    } else {
                        mc.player.sendChatMessage(msg);
                    }
                    return;
                }
            }
        }
    }

    public void onPlayerLeave(String name) {
        if (this.state.enabled && AnnouncerTranslator.INSTANCE.leave) {
            QueuedTask task = new TaskBasic(TaskType.LEAVE, MessagePrefixes.getMessage(TaskType.LEAVE, name));
            if (hasTimePassedM(2000))   {
                updateLastMS();
                String msg = task.getMessage();
                if (msg != null) {
                    if (AnnouncerTranslator.INSTANCE.clientSide) {
                        mc.player.sendMessage(new TextComponentString(PepsiUtils.COLOR_ESCAPE + "a" + msg));
                    } else {
                        mc.player.sendChatMessage(msg);
                    }
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        toSend.clear();
    }
}
