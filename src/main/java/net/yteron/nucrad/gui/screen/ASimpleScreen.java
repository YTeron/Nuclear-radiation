package net.yteron.nucrad.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.FurnaceScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.yteron.nucrad.NucRad;
import net.yteron.nucrad.gui.container.ASimpleContainer;
import net.yteron.nucrad.gui.container.LightningChannelerContainer;

public class ASimpleScreen extends ContainerScreen<ASimpleContainer> {
    int l =0;
    int k =0;

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
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI);

        int x = this.leftPos;
        int y = this.topPos;

        // Рисуем фон
        this.blit(matrixStack, x, y, 0, 0, this.imageWidth, this.imageHeight);


        // ✅ Рисуем растущий квадрат (вертикальный прогресс)
        int progressHeight = this.menu.getProgressHeight();
        if (progressHeight > 0) {
            if(progressHeight<15)
                l=progressHeight;
            else
                k=progressHeight-14;
            this.blit(matrixStack,
                    x + 79,           // X позиция
                    y + 57 - l, // Y позиция (поднимается вверх)
                    15,               // U на текстуре
                    71 - l,    // V на текстуре (смещение)
                    16,               // Ширина
                    l          // Высота (растет)
            );
            if (progressHeight>13)
                this.blit(matrixStack,
                        x + 80,           // X позиция
                        y + 43 - k, // Y позиция (поднимается вверх)
                        15,               // U на текстуре
                        71 - k,    // V на текстуре (смещение)
                        14,               // Ширина
                        k          // Высота (растет)
                );

        }
        if (progressHeight == 0){
            l=0;
            k=0;
        }
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