package dev.andante.teamtextresolve.mixin.client;

import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.StructureBlockBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(StructureBlockBlockEntityRenderer.class)
public class StructureBlockBlockEntityRendererMixin {
	@Inject(method = "render(Lnet/minecraft/block/entity/StructureBlockBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void onShouldShowBoundingBox(StructureBlockBlockEntity structureBlockBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci, BlockPos blockPos, Vec3i vec3i, double d, double e, double k, double l, double m, double g, double n, double o, double h, double p, float q, float r, float s) {
        if (!(structureBlockBlockEntity.getMode() == StructureBlockMode.SAVE || structureBlockBlockEntity.shouldShowBoundingBox())) {
            WorldRenderer.drawBox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), m, g, n, o, h, p, 0.9F, 0.9F, 0.9F, 1.0F, 0.5F, 0.5F, 0.5F);
        }
	}

    @Redirect(method = "render(Lnet/minecraft/block/entity/StructureBlockBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/StructureBlockBlockEntity;getMode()Lnet/minecraft/block/enums/StructureBlockMode;"))
    private StructureBlockMode onGetMode(StructureBlockBlockEntity instance) {
        return StructureBlockMode.LOAD;
    }
}
