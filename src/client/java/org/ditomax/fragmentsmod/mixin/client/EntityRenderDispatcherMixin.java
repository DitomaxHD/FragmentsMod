package org.ditomax.fragmentsmod.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class EntityRenderDispatcherMixin {

    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    private void checkRendererOnEntityAdd(Entity entity, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client != null && client.getEntityRenderDispatcher() != null) {
            try {
                EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
                EntityRenderer<?, ?> renderer = dispatcher.getRenderer(entity);

                if (renderer == null) {
                    ci.cancel();
                }
            } catch (Exception e) {
                ci.cancel();
            }
        }
    }
}