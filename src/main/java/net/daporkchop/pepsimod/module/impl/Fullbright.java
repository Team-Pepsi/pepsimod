package net.daporkchop.pepsimod.module.impl;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Fullbright extends Module {
    public static final Potion NIGHT_VISION = Potion.getPotionById(16);
    private static final PotionEffect BRIGHTNESS_EFFECT = new PotionEffect(NIGHT_VISION, Integer.MAX_VALUE, 1, false, false);

    public Fullbright(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "Fullbright", key, hide);
    }

    @Override
    public void onEnable() {
        if (PepsiMod.INSTANCE.mc.player == null) {
            return;
        }
        PepsiMod.INSTANCE.mc.player.addPotionEffect(BRIGHTNESS_EFFECT);
    }

    @Override
    public void onDisable() {
        if (PepsiMod.INSTANCE.mc.player == null) {
            return;
        }
        PepsiMod.INSTANCE.mc.player.removeActivePotionEffect(NIGHT_VISION);
    }

    @Override
    public void tick() {
        if (isEnabled) {
            PepsiMod.INSTANCE.mc.player.addPotionEffect(BRIGHTNESS_EFFECT);
        } else {
            PepsiMod.INSTANCE.mc.player.removeActivePotionEffect(NIGHT_VISION);
        }
    }

    @Override
    public void init() {

    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[0];
    }
}
