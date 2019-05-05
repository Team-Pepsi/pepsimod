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

package net.daporkchop.pepsimod;

import net.daporkchop.pepsimod.clickgui.ClickGUI;
import net.daporkchop.pepsimod.command.CommandRegistry;
import net.daporkchop.pepsimod.command.impl.GoToCommand;
import net.daporkchop.pepsimod.command.impl.HelpCommand;
import net.daporkchop.pepsimod.command.impl.InvSeeCommand;
import net.daporkchop.pepsimod.command.impl.ListCommand;
import net.daporkchop.pepsimod.command.impl.PeekCommand;
import net.daporkchop.pepsimod.command.impl.SaveCommand;
import net.daporkchop.pepsimod.command.impl.SetRotCommand;
import net.daporkchop.pepsimod.command.impl.SortModulesCommand;
import net.daporkchop.pepsimod.command.impl.ToggleCommand;
import net.daporkchop.pepsimod.command.impl.waypoint.WaypointAddCommand;
import net.daporkchop.pepsimod.command.impl.waypoint.WaypointClearCommand;
import net.daporkchop.pepsimod.command.impl.waypoint.WaypointHardClearCommand;
import net.daporkchop.pepsimod.command.impl.waypoint.WaypointHideCommand;
import net.daporkchop.pepsimod.command.impl.waypoint.WaypointListCommand;
import net.daporkchop.pepsimod.command.impl.waypoint.WaypointRemoveCommand;
import net.daporkchop.pepsimod.command.impl.waypoint.WaypointShowCommand;
import net.daporkchop.pepsimod.event.GuiRenderHandler;
import net.daporkchop.pepsimod.event.MiscEventHandler;
import net.daporkchop.pepsimod.gui.clickgui.WindowCombat;
import net.daporkchop.pepsimod.gui.clickgui.WindowMisc;
import net.daporkchop.pepsimod.gui.clickgui.WindowMovement;
import net.daporkchop.pepsimod.gui.clickgui.WindowPlayer;
import net.daporkchop.pepsimod.gui.clickgui.WindowRender;
import net.daporkchop.pepsimod.key.KeyRegistry;
import net.daporkchop.pepsimod.misc.data.DataLoader;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.impl.combat.AuraMod;
import net.daporkchop.pepsimod.module.impl.combat.AutoArmorMod;
import net.daporkchop.pepsimod.module.impl.combat.AutoTotemMod;
import net.daporkchop.pepsimod.module.impl.combat.BedBomberMod;
import net.daporkchop.pepsimod.module.impl.combat.CriticalsMod;
import net.daporkchop.pepsimod.module.impl.combat.CrystalAuraMod;
import net.daporkchop.pepsimod.module.impl.misc.AnnouncerMod;
import net.daporkchop.pepsimod.module.impl.misc.AntiHungerMod;
import net.daporkchop.pepsimod.module.impl.misc.AutoFishMod;
import net.daporkchop.pepsimod.module.impl.misc.AutoToolMod;
import net.daporkchop.pepsimod.module.impl.misc.ClickGuiMod;
import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.daporkchop.pepsimod.module.impl.misc.HUDMod;
import net.daporkchop.pepsimod.module.impl.misc.NoFallMod;
import net.daporkchop.pepsimod.module.impl.misc.NotificationsMod;
import net.daporkchop.pepsimod.module.impl.misc.TimerMod;
import net.daporkchop.pepsimod.module.impl.misc.WaypointsMod;
import net.daporkchop.pepsimod.module.impl.movement.AutoRespawnMod;
import net.daporkchop.pepsimod.module.impl.movement.AutoWalkMod;
import net.daporkchop.pepsimod.module.impl.movement.BoatFlyMod;
import net.daporkchop.pepsimod.module.impl.movement.ElytraFlyMod;
import net.daporkchop.pepsimod.module.impl.movement.EntitySpeedMod;
import net.daporkchop.pepsimod.module.impl.movement.FlightMod;
import net.daporkchop.pepsimod.module.impl.movement.HorseJumpPowerMod;
import net.daporkchop.pepsimod.module.impl.movement.InventoryMoveMod;
import net.daporkchop.pepsimod.module.impl.movement.JesusMod;
import net.daporkchop.pepsimod.module.impl.movement.NoClipMod;
import net.daporkchop.pepsimod.module.impl.movement.NoSlowdownMod;
import net.daporkchop.pepsimod.module.impl.movement.SafewalkMod;
import net.daporkchop.pepsimod.module.impl.movement.StepMod;
import net.daporkchop.pepsimod.module.impl.movement.VelocityMod;
import net.daporkchop.pepsimod.module.impl.player.AntiAFKMod;
import net.daporkchop.pepsimod.module.impl.player.AutoEatMod;
import net.daporkchop.pepsimod.module.impl.player.AutoMineMod;
import net.daporkchop.pepsimod.module.impl.player.FastPlaceMod;
import net.daporkchop.pepsimod.module.impl.player.ScaffoldMod;
import net.daporkchop.pepsimod.module.impl.player.SpeedmineMod;
import net.daporkchop.pepsimod.module.impl.player.SprintMod;
import net.daporkchop.pepsimod.module.impl.render.AntiBlindMod;
import net.daporkchop.pepsimod.module.impl.render.AntiInvisibleMod;
import net.daporkchop.pepsimod.module.impl.render.AntiTotemAnimationMod;
import net.daporkchop.pepsimod.module.impl.render.ESPMod;
import net.daporkchop.pepsimod.module.impl.render.FullbrightMod;
import net.daporkchop.pepsimod.module.impl.render.HealthTagsMod;
import net.daporkchop.pepsimod.module.impl.render.NameTagsMod;
import net.daporkchop.pepsimod.module.impl.render.NoHurtCamMod;
import net.daporkchop.pepsimod.module.impl.render.NoOverlayMod;
import net.daporkchop.pepsimod.module.impl.render.NoWeatherMod;
import net.daporkchop.pepsimod.module.impl.render.StorageESPMod;
import net.daporkchop.pepsimod.module.impl.render.TracersMod;
import net.daporkchop.pepsimod.module.impl.render.TrajectoriesMod;
import net.daporkchop.pepsimod.module.impl.render.UnfocusedCPUMod;
import net.daporkchop.pepsimod.module.impl.render.XrayMod;
import net.daporkchop.pepsimod.module.impl.render.ZoomMod;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.config.Config;
import net.daporkchop.pepsimod.util.config.impl.HUDTranslator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TimerTask;

@Mod(
        modid = "pepsimod",
        name = "pepsimod",
        version = "11.1"
)
public class Pepsimod extends PepsiConstants {
    public static final String VERSION = "11.1";
    public static final String chatPrefix = PepsiUtils.COLOR_ESCAPE + "0" + PepsiUtils.COLOR_ESCAPE + "l[" + PepsiUtils.COLOR_ESCAPE + "c" + PepsiUtils.COLOR_ESCAPE + "lpepsi" + PepsiUtils.COLOR_ESCAPE + "9" + PepsiUtils.COLOR_ESCAPE + "lmod" + PepsiUtils.COLOR_ESCAPE + "0" + PepsiUtils.COLOR_ESCAPE + "l]" + PepsiUtils.COLOR_ESCAPE + "r ";
    public static final String NAME_VERSION = String.format("pepsimod v%s", VERSION);

    public DataLoader data = new DataLoader("https://raw.githubusercontent.com/Team-Pepsi/pepsimod/master/resources/resources.json");
    public boolean isMcLeaksAccount = false;
    public Session originalSession = null;
    public boolean hasInitializedModules = false;
    public boolean isInitialized = false;

    {
        PepsiConstants.pepsimod = this;
    }

    @Mod.EventHandler
    public void construction(FMLConstructionEvent event) {
        PepsiConstants.mc = Minecraft.getMinecraft();
        PepsiConstants.pepsimod = this;

        ReflectionStuff.init();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new KeyRegistry());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        this.data.load();

        new ClickGUI();
        ClickGUI.INSTANCE.setWindows(
                new WindowRender(),
                new WindowCombat(),
                new WindowMisc(),
                new WindowMovement(),
                new WindowPlayer()
        );

        this.loadConfig();
        ModuleManager.registerModules(
                new NoFallMod(),
                new AntiHungerMod(),
                new FullbrightMod(),
                new CriticalsMod(),
                new AuraMod(),
                new VelocityMod(),
                new TimerMod(),
                new XrayMod(),
                new AntiBlindMod(),
                new StorageESPMod(),
                new FreecamMod(),
                new HealthTagsMod(),
                new NameTagsMod(),
                new NoHurtCamMod(),
                new NoOverlayMod(),
                new NoWeatherMod(),
                new AntiInvisibleMod(),
                new TrajectoriesMod(),
                new TracersMod(),
                new ClickGuiMod(false, Keyboard.KEY_RSHIFT),
                new HUDMod(true, -1),
                new ZoomMod(-1),
                new ElytraFlyMod(),
                new AutoWalkMod(),
                new EntitySpeedMod(),
                new SafewalkMod(),
                new InventoryMoveMod(),
                new HorseJumpPowerMod(),
                new AutoTotemMod(),
                new CrystalAuraMod(),
                new AntiTotemAnimationMod(),
                new AnnouncerMod(),
                new AutoRespawnMod(),
                new JesusMod(),
                new SprintMod(),
                new NoSlowdownMod(),
                new FlightMod(),
                new FastPlaceMod(),
                new SpeedmineMod(),
                new AutoEatMod(),
                new StepMod(),
                new AutoMineMod(),
                new ScaffoldMod(),
                new UnfocusedCPUMod(),
                new ESPMod(),
                new WaypointsMod(),
                new NoClipMod(),
                new BoatFlyMod(),
                new NotificationsMod(),
                new BedBomberMod(),
                new AutoFishMod(),
                new AutoToolMod(),
                new AutoArmorMod(),
                new AntiAFKMod()
        );

        ClickGUI.INSTANCE.initWindows();
        HUDTranslator.INSTANCE.parseConfigLate();

        CommandRegistry.registerCommands(
                new HelpCommand(),
                new SetRotCommand(),
                new ToggleCommand(),
                new SortModulesCommand(),
                new SaveCommand(),
                new ListCommand(),
                new InvSeeCommand(),
                new PeekCommand(),
                new WaypointAddCommand(),
                new WaypointClearCommand(),
                new WaypointHardClearCommand(),
                new WaypointHideCommand(),
                new WaypointListCommand(),
                new WaypointRemoveCommand(),
                new WaypointShowCommand(),
                new GoToCommand()
        );
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new GuiRenderHandler());
        MinecraftForge.EVENT_BUS.register(new MiscEventHandler());
        PepsiUtils.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                PepsiConstants.pepsimod.saveConfig();
            }
        }, 360000, 360000);
    }

    public void loadConfig() {
        String launcherJson = "{}";
        File file = new File(mc.gameDir, "pepsimodConf.json");
        try (InputStream in = new FileInputStream(file)) {
            launcherJson = IOUtils.toString(in, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Config.loadConfig(launcherJson);
    }

    public void saveConfig() {
        String config = Config.saveConfig();
        try {
            File file = new File(mc.gameDir, "pepsimodConf.json");
            if (!file.exists() && !file.createNewFile()) {
                throw new IllegalStateException(String.format("Unable to create file: %s", file.getAbsolutePath()));
            }
            try (OutputStream out = new FileOutputStream(file)) {
                out.write(config.getBytes("UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
