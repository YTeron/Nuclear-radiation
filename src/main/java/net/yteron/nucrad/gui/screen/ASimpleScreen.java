package net.yteron.nucrad.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.yteron.nucrad.NucRad;
import net.yteron.nucrad.gui.container.ASimpleContainer;
import net.yteron.nucrad.gui.container.LightningChannelerContainer;

public class ASimpleScreen extends ContainerScreen<ASimpleContainer> {

    private final ResourceLocation GUI = new ResourceLocation(NucRad.MOD_ID,
            "textures/gui/conc_gui.png");

    public ASimpleScreen(ASimpleContainer container,
                         PlayerInventory inventory,
                         ITextComponent title) {
        super(container, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY); // ✅ Исправлено
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) { // ✅ Исправлено имя
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI); // ✅ Исправлено

        int x = (this.width - this.imageWidth) / 2;  // ✅ Вместо guiLeft
        int y = (this.height - this.imageHeight) / 2; // ✅ Вместо guiTop

        // Рисуем фон
        this.blit(matrixStack, x, y, 0, 0, this.imageWidth, this.imageHeight);

    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        // Название контейнера
        this.font.draw(matrixStack, this.title,
                (float) this.titleLabelX, (float) this.titleLabelY, 0x404040);

        // Название инвентаря игрока
        this.font.draw(matrixStack, "Inventory",
                8.0F, (float) (this.imageHeight - 94), 0x404040);
    }
}