package net.daporkchop.pepsimod.mixin.client.renderer.entity;

import net.daporkchop.pepsimod.module.impl.render.HealthTagsMod;
import net.daporkchop.pepsimod.util.Friends;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Render.class)
public abstract class MixinRender<T extends Entity> {
    @Shadow
    @Final
    protected RenderManager renderManager;

    @Shadow
    public FontRenderer getFontRendererFromRenderManager() {
        return null;
    }

    @Overwrite
    protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {
        double d0 = entityIn.getDistanceSqToEntity(this.renderManager.renderViewEntity);

        if (d0 <= (double) (maxDistance * maxDistance)) {
            boolean flag = entityIn.isSneaking();
            float f = this.renderManager.playerViewY;
            float f1 = this.renderManager.playerViewX;
            boolean flag1 = this.renderManager.options.thirdPersonView == 2;
            float f2 = entityIn.height + 0.5F - (flag ? 0.25F : 0.0F);
            int i = "deadmau5".equals(str) ? -10 : 0;

            if (entityIn instanceof EntityLivingBase) {
                if (entityIn instanceof EntityPlayer && Friends.isFriend(entityIn.getUniqueID().toString())) {
                    str = PepsiUtils.COLOR_ESCAPE + "b" + str;
                }

                if (HealthTagsMod.INSTANCE.isEnabled) {
                    str += " ";
                    int health = (int) ((EntityLivingBase) entityIn).getHealth();
                    if (health <= 5) {
                        str += "§4";
                    } else if (health <= 10) {
                        str += "§6";
                    } else if (health <= 15) {
                        str += "§e";
                    } else {
                        str += "§a";
                    }
                    str += health;
                }
            }

            EntityRenderer.drawNameplate(this.getFontRendererFromRenderManager(), str, (float) x, (float) y + f2, (float) z, i, f, f1, flag1, flag);
        }
    }
}
