/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod;

import net.daporkchop.pepsimod.command.impl.ReloadCommand;
import net.daporkchop.pepsimod.gui.clickgui.ClickGUI;
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
import net.daporkchop.pepsimod.event.GuiRenderHandler;
import net.daporkchop.pepsimod.event.MiscEventHandler;
import net.daporkchop.pepsimod.gui.clickgui.window.WindowCombat;
import net.daporkchop.pepsimod.gui.clickgui.window.WindowMisc;
import net.daporkchop.pepsimod.gui.clickgui.window.WindowMovement;
import net.daporkchop.pepsimod.gui.clickgui.window.WindowPlayer;
import net.daporkchop.pepsimod.gui.clickgui.window.WindowRender;
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
        version = "v11.1",
        useMetadata = true
)
public class Pepsimod extends PepsiConstants {
    public static final String VERSION;
    public static final String CHAT_PREFIX = "\u00A70\u00A7l[\u00A7c\u00A7lpepsi\u00A79\u00A7lmod\u00A70\u00A7l]\u00A7r";
    public static final String NAME_VERSION;

    static {
        {
            String version = Pepsimod.class.getAnnotation(Mod.class).version();
            if (version.indexOf('-') == -1)  {
                version += String.format("-%s", MinecraftForge.MC_VERSION);
            }
            VERSION = version;
        }
        NAME_VERSION = String.format("pepsimod %s", VERSION);
    }

    public DataLoader data;
    public boolean isMcLeaksAccount = false;
    public Session originalSession = null;
    public boolean hasInitializedModules = false;
    public boolean isInitialized = false;

    {
        PepsiConstants.pepsimod = this;
    }

    @Mod.EventHandler
    public void construction(FMLConstructionEvent event) {
        mc = Minecraft.getMinecraft();
        pepsimod = this;

        ReflectionStuff.init();

        this.data = new DataLoader(
                "https://raw.githubusercontent.com/Team-Pepsi/pepsimod/master/resources/resources.json",
                new File(mc.gameDir, "pepsimod/resources/")
        );
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        mcStartedSuccessfully = true;

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
                new GoToCommand(),
                new ReloadCommand()
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
