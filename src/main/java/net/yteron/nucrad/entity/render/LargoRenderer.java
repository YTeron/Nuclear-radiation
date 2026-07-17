package net.yteron.nucrad.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.yteron.nucrad.entity.custom.LargoEntity;
import net.yteron.nucrad.entity.model.LargoModel;

public class LargoRenderer extends MobRenderer<LargoEntity, LargoModel<LargoEntity>> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("nucrad", "textures/entity/largo.png");

    public LargoRenderer(EntityRendererManager manager) {
        super(manager, new LargoModel<>(), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(LargoEntity entity) {
        return TEXTURE;
    }
}