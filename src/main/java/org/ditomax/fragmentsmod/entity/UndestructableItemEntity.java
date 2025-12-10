package org.ditomax.fragmentsmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;

public class UndestructableItemEntity extends ItemEntity {

    public UndestructableItemEntity(EntityType<? extends UndestructableItemEntity> entityType, World world) {
        super(entityType, world);
        this.setNeverDespawn();
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public void remove(RemovalReason reason) {
        if (reason == RemovalReason.KILLED || reason == RemovalReason.DISCARDED || reason == RemovalReason.UNLOADED_TO_CHUNK || reason == RemovalReason.UNLOADED_WITH_PLAYER) {
            return;
        }

        super.remove(reason);
    }

}
