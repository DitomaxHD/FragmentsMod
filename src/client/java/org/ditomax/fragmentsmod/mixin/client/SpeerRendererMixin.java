package org.ditomax.fragmentsmod.mixin.client;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.ditomax.fragmentsmod.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class SpeerRendererMixin {

    @Shadow
    public abstract void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (player.isUsingItem() && player.getActiveItem().isOf(ModItems.SPEER_FRAGMENT)) {
            boolean isMainHand = (hand == Hand.MAIN_HAND);
            Arm arm = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();
            int sign = (arm == Arm.RIGHT) ? 1 : -1;

            matrices.push();

            matrices.translate(sign * 0.25F, -0.52F, -0.72F);
            matrices.translate(0.0F, 0.35F, 0.1F);

            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(sign * 10.0F));

            float m = item.getMaxUseTime(player) - player.getItemUseTimeLeft() - tickDelta + 1.0F;
            float fx = m / 10.0F;
            if (fx > 1.0F) fx = 1.0F;
            matrices.scale(1.0F, 1.0F, 1.0F + fx * 0.2F);

            this.renderItem(
                    player,
                    item,
                    isMainHand ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
                    arm == Arm.LEFT,
                    matrices,
                    vertexConsumers,
                    light
            );

            matrices.pop();
            ci.cancel();
        }
    }
}