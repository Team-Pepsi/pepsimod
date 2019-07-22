/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2016-2019 DaPorkchop_
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

package net.daporkchop.pepsimod.asm.tweaks.minecraft.client.resources;

import net.daporkchop.pepsimod.util.PepsiConstants;
import net.minecraft.client.resources.Locale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

/**
 * @author DaPorkchop_
 */
@Mixin(Locale.class)
abstract class MixinLocale {
    @Shadow
    Map<String, String> properties;

    /*@Inject(
            method = "Lnet/minecraft/client/resources/Locale;loadLocaleDataFiles(Lnet/minecraft/client/resources/IResourceManager;Ljava/util/List;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/Locale;checkUnicode()V"
            ))
    private void injectPepsimodResources(CallbackInfo ci) {
        PepsiConstants.pepsimod.resources().lang().inject(this.properties);
    }*/

    /**
     * Lots of injects would be slower lol
     *
     * @return efficiency
     * @author DaPorkchop_
     */
    @Overwrite
    private String translateKeyPrivate(String translateKey) {
        String s = this.properties.get(translateKey);
        return s != null ? s : PepsiConstants.pepsimod.resources().lang().translations().getOrDefault(translateKey, translateKey);
    }

    /**
     * Lots of injects would be slower lol
     *
     * @return efficiency
     * @author DaPorkchop_
     */
    @Overwrite
    public boolean hasKey(String key) {
        return this.properties.containsKey(key) || PepsiConstants.pepsimod.resources().lang().translations().containsKey(key);
    }
}
