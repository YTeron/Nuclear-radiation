package net.yteron.nucrad.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;


import net.yteron.nucrad.NucRad;
import net.yteron.nucrad.entity.custom.BuffZombieEntity;
import net.yteron.nucrad.entity.model.BuffZombieModel;

public class BuffZombieRenderer extends MobRenderer<BuffZombieEntity, BuffZombieModel<BuffZombieEntity>> {
    protected static final ResourceLocation TEXTURE =
            new ResourceLocation(NucRad.MOD_ID, "textures/entity/buff_zombie.png");

    public BuffZombieRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BuffZombieModel<>(), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(BuffZombieEntity p_110775_1_) {
        return TEXTURE;
    }
}
