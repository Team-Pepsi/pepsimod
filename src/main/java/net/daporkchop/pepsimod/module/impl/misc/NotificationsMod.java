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
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.util.config.impl.NotificationsTranslator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class NotificationsMod extends Module {
    public static NotificationsMod INSTANCE;

    public TrayIcon trayIcon;
    public SystemTray tray;
    public PopupMenu menu;

    public boolean inQueue = false;

    public NotificationsMod() {
        super("Notifications");
    }

    public static void sendNotification(String message, TrayIcon.MessageType type) {
        if (!Display.isActive() && ModuleManager.ENABLED_MODULES.contains(INSTANCE)) {
            INSTANCE.trayIcon.displayMessage("pepsimod", message, type);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        trayIcon.setToolTip("x" + Math.floor(mc.player.posX) + " y" + Math.floor(mc.player.posY) + " z" + Math.floor(mc.player.posZ));
        if (NotificationsTranslator.INSTANCE.death && mc.player.getHealth() <= 0) {
            sendNotification("You died!", TrayIcon.MessageType.WARNING);
        }
    }

    @Override
    public void init() {
        try {
            INSTANCE = this;
            tray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAACDVBMVEUAAAD////++fneAAD1j5z0+fz0+Pru9PgAWJH//////Pz82+D5uMH3pK/3oa34r7n7ztT+8vT////////84uX2nKjwWW3vSF/0g5L70Nb////////819zzd4f3q7X////////96Orze4v////8/f7////3p7LY5e7k7vP96ezyaXujwte60eH6zNLuQ1uTt9D5u8N9qcf5u8SAq8j6ztTvRVyYu9L96+7ybX8rc6PA1eP////4rLZwocHx9vn/7e7WuMZGha/H2uf////Z6fFuocFLiLG60eH////////l7vObvtRRjLM9f6t/q8jO3+r////////9/v7g6/K70uGjwtegwNawy93T4uz2+fsAWJHtNE3sJ0LsJkHsLkjsKkTrGjfrHDjrGjbsJD/rIj3rHTnrGzftOlL71drtL0nrGzjzfY3/+vv7/f7uPVX709j////9/v7sJUD2nqr//f76/P1yosLtNU7rHzvze4v+8vTf6vE2eqftNU/rIDzzcoP96uz3+vt9qscKXZTsKUT1g5L+7O7n7/WCrckQYpcMX5XtITzyTmP6r7j68/bs9PjL3emQts8/gawHXJQAVY/uP1bqS2Lbh5q+ucuWutJonb8+gKsaaJwEWpIAVpAAV5AFWpJqia0vd6USZZoAWJEAV5EBV5EUZJkAVo8IXJQjbqASY5gQYpgbaZwAAAAprTAwAAAAW3RSTlMAAAAAAAAAAAACKHrA4ufPlUIICWDO+f3jiRsHdu/7qRxW7PyRC8HoT13y/qqZ/ua4/Lf8lf3jWPD+pBm65UhO5/qHBWrp+Z4XBlPD9frbexUBH2mt0te9gjUElCjBbQAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAEDSURBVBjTY2BgYOTk4ubh5eMXEBRiZAACRmERUbHomNg4cQlJKSZGBkZpGdn4hEQgSEqWk1dgZGBWVEpJTAWBtPQMZRVVBgW1zCwwPzUxOydXXYNBUysNwk/Lyy8o1NZh0NWDCCQWFZcUlJbpMxiUg01IrKisKiiorjFkMKoFCSTW1TcUFDQ2NRszmJgCtSS0tLYVFLR3dHaZMZhbpKV19/T29U+YOGnyFEsrBmubqdOmz5g5a/acufPmL7C1Y2Cxd1i4aPHcefPmL1m6zNFJiIHR2cV1+Yp585fOn7fSzd2DFeg5Ty9vn1Wr16z19fMPYAP5lzEwKDgkNCw8ItKZnSMKAAihVlWjuDlCAAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE3LTA5LTA5VDE3OjE3OjAyKzAyOjAw4iAIuQAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNy0wOS0wOVQxNzoxNzowMiswMjowMJN9sAUAAAAASUVORK5CYII="))),
                    "pepsimod", menu = new PopupMenu());
            tray.add(trayIcon);
        } catch (IOException | AWTException e) {
            e.printStackTrace();
            ModuleManager.unRegister(this);
        }
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(NotificationsTranslator.INSTANCE.chat, "chat", OptionCompletions.BOOLEAN,
                        (value) -> {
                            NotificationsTranslator.INSTANCE.chat = value;
                            return true;
                        },
                        () -> {
                            return NotificationsTranslator.INSTANCE.chat;
                        }, "Chat"),
                new ModuleOption<>(NotificationsTranslator.INSTANCE.queue, "queue", OptionCompletions.BOOLEAN,
                        (value) -> {
                            NotificationsTranslator.INSTANCE.queue = value;
                            return true;
                        },
                        () -> {
                            return NotificationsTranslator.INSTANCE.queue;
                        }, "Queue"),
                new ModuleOption<>(NotificationsTranslator.INSTANCE.death, "death", OptionCompletions.BOOLEAN,
                        (value) -> {
                            NotificationsTranslator.INSTANCE.death = value;
                            return true;
                        },
                        () -> {
                            return NotificationsTranslator.INSTANCE.death;
                        }, "Death"),
                new ModuleOption<>(NotificationsTranslator.INSTANCE.player, "visual_range", OptionCompletions.BOOLEAN,
                        (value) -> {
                            NotificationsTranslator.INSTANCE.player = value;
                            return true;
                        },
                        () -> {
                            return NotificationsTranslator.INSTANCE.player;
                        }, "Visual Range")
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MISC;
    }

    @Override
    public boolean shouldRegister() {
        return SystemTray.isSupported();
    }

    @Override
    public void postRecievePacket(Packet<?> packet) {
        if (packet instanceof SPacketChat) {
            SPacketChat pck = (SPacketChat) packet;
            if (!pck.isSystem()) {
                String message = pck.getChatComponent().getUnformattedText().toLowerCase();
                if (NotificationsTranslator.INSTANCE.queue && message.startsWith("position in queue")) {
                    inQueue = true;
                } else if (inQueue && message.startsWith("connecting to")) {
                    sendNotification("Finished going through the queue!", TrayIcon.MessageType.INFO);
                    inQueue = false;
                } else if (NotificationsTranslator.INSTANCE.chat && message.contains(mc.getSession().getUsername().toLowerCase())) {
                    sendNotification("Your name was mentioned in chat!", TrayIcon.MessageType.INFO);
                }
            }
        } else if (NotificationsTranslator.INSTANCE.player && packet instanceof SPacketSpawnPlayer) {
            sendNotification(mc.getConnection().getPlayerInfo(((SPacketSpawnPlayer) packet).getUniqueId()).getGameProfile().getName() + " entered visual range!", TrayIcon.MessageType.INFO);
        }
    }
}
