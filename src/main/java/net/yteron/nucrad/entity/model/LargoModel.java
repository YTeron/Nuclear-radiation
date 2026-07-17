package net.yteron.nucrad.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LargoModel<T extends Entity> extends EntityModel<T> {
	private final ModelRenderer head;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer tors;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer legsl;
	private final ModelRenderer cube_r5;
	private final ModelRenderer legsr;
	private final ModelRenderer cube_r6;
	private final ModelRenderer handsl;
	private final ModelRenderer handsr;
	private final ModelRenderer cube_r7;

	public LargoModel() {
		texWidth = 64;
		texHeight = 64;

		// ===== ГОЛОВА =====
		// Было: PartPose.offset(0.25F, 9.0F, -3.0F)
		head = new ModelRenderer(this);
		head.setPos(0.25F, 9.0F, -3.0F);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(0.25F, 0.0F, 0.0F);
		head.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.3054F, 0.0F, 0.0F);
		cube_r1.texOffs(0, 24).addBox(-2.0F, -1.0419F, -7.4637F, 3.0F, 2.0F, 8.0F, 0.0F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setPos(-0.25F, 0.0F, 0.0F);
		head.addChild(cube_r2);
		setRotationAngle(cube_r2, -0.0873F, 0.0F, 0.0F);
		cube_r2.texOffs(0, 13).addBox(-2.0F, -2.059F, -7.3713F, 4.0F, 3.0F, 8.0F, 0.0F, false);

		// ===== ТОРС =====
		// Было: PartPose.offset(0.0F, 11.0F, 1.5F)
		tors = new ModelRenderer(this);
		tors.setPos(0.0F, 11.0F, 1.5F);

		// Было: PartPose.offsetAndRotation(0.0F, -2.0F, -6.5F, 1.4835F, 0.0F, 0.0F)
		cube_r3 = new ModelRenderer(this);
		cube_r3.setPos(0.0F, -2.0F, -6.5F);
		tors.addChild(cube_r3);
		setRotationAngle(cube_r3, 1.4835F, 0.0F, 0.0F);
		cube_r3.texOffs(0, 0).addBox(-3.0F, 0.8717F, -3.7173F, 6.0F, 9.0F, 6.0F, 0.0F, false);

		// Было: PartPose.offsetAndRotation(0.0F, 2.0F, 6.5F, 1.0036F, 0.0F, 0.0F)
		cube_r4 = new ModelRenderer(this);
		cube_r4.setPos(0.0F, 2.0F, 6.5F);
		tors.addChild(cube_r4);
		setRotationAngle(cube_r4, 1.0036F, 0.0F, 0.0F);
		cube_r4.texOffs(24, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 9.0F, 4.0F, 0.0F, false);

		// ===== ЛЕВАЯ НОГА =====
		// Было: PartPose.offset(3.4F, 13.3F, 10.0F)
		legsl = new ModelRenderer(this);
		legsl.setPos(3.4F, 13.3F, 10.0F);

		// Было: PartPose.offsetAndRotation(0.0F, 2.0F, 1.0F, 0.6109F, 0.0F, 0.0F)
		cube_r5 = new ModelRenderer(this);
		cube_r5.setPos(0.0F, 2.0F, 1.0F);
		legsl.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.6109F, 0.0F, 0.0F);
		cube_r5.texOffs(34, 38).addBox(-1.4F, -2.2457F, -1.5161F, 3.0F, 13.0F, 3.0F, 0.0F, false);

		// ===== ПРАВАЯ НОГА =====
		// Было: PartPose.offset(-3.6F, 13.3F, 10.0F)
		legsr = new ModelRenderer(this);
		legsr.setPos(-3.6F, 13.3F, 10.0F);

		// Было: PartPose.offsetAndRotation(0.0F, 2.0F, 1.0F, 0.6109F, 0.0F, 0.0F)
		cube_r6 = new ModelRenderer(this);
		cube_r6.setPos(0.0F, 2.0F, 1.0F);
		legsr.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.6109F, 0.0F, 0.0F);
		cube_r6.texOffs(34, 38).addBox(-1.4F, -2.2457F, -1.5161F, 3.0F, 12.0F, 3.0F, 0.0F, false);

		// ===== ЛЕВАЯ РУКА =====
		// Было: PartPose.offset(4.5F, 8.7931F, -3.1637F)
		handsl = new ModelRenderer(this);
		handsl.setPos(4.5F, 8.7931F, -3.1637F);
		handsl.texOffs(0, 34).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 16.0F, 3.0F, 0.0F, false);

		// ===== ПРАВАЯ РУКА =====
		// Было: PartPose.offset(-4.6F, 9.0F, -3.0F)
		handsr = new ModelRenderer(this);
		handsr.setPos(-4.6F, 9.0F, -3.0F);

		// Было: PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -0.0436F, 0.0F, 0.0F)
		cube_r7 = new ModelRenderer(this);
		cube_r7.setPos(0.0F, 1.0F, 0.0F);
		handsr.addChild(cube_r7);
		setRotationAngle(cube_r7, -0.0436F, 0.0F, 0.0F);
		cube_r7.texOffs(0, 34).addBox(-1.4F, -2.205F, -1.751F, 3.0F, 16.0F, 3.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// ===== АНИМАЦИЯ ХОДЬБЫ (как в BipedModel) =====
		float f = 1.0F;

		// Ноги
		this.legsr.xRot = MathHelper.cos(limbSwing * 0.6662F)
				* 1.4F * limbSwingAmount / f;
		this.legsl.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI)
				* 1.4F * limbSwingAmount / f;

		// Руки (в противофазе с ногами)
		this.handsr.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI)
				* 2.0F * limbSwingAmount * 0.5F / f;
		this.handsl.xRot = MathHelper.cos(limbSwing * 0.6662F)
				* 2.0F * limbSwingAmount * 0.5F / f;

		// ===== ПОВОРОТ ГОЛОВЫ =====
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);

		// ===== СИНХРОНИЗАЦИЯ ДОЧЕРНИХ КУБОВ =====
		this.cube_r1.xRot = this.head.xRot + 0.3054F;
		this.cube_r1.yRot = this.head.yRot;

		this.cube_r2.xRot = this.head.xRot - 0.0873F;
		this.cube_r2.yRot = this.head.yRot;

		this.cube_r5.xRot = this.legsl.xRot + 0.6109F;
		this.cube_r6.xRot = this.legsr.xRot + 0.6109F;

		this.cube_r7.xRot = this.handsr.xRot - 0.0436F;
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		tors.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		legsl.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		legsr.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		handsl.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		handsr.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}