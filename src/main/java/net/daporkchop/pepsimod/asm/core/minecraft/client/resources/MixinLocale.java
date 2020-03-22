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

package net.daporkchop.pepsimod.asm.core.minecraft.client.resources;

import net.daporkchop.pepsimod.util.PepsiConstants;
import net.minecraft.client.resources.Locale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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
