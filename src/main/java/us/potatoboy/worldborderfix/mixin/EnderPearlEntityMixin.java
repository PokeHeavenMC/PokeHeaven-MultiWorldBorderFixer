package us.potatoboy.worldborderfix.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlEntityMixin {
    // Pearls are projectiles and fly through the border, so vanilla would teleport the owner outside of it.
    // The landing position (used for the teleport and its sound) is pulled back inside the border of the
    // pearl's own world, which works for any dimension regardless of which mod created it.
    @Redirect(
            method = "onCollision",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/EnderPearlEntity;getPos()Lnet/minecraft/util/math/Vec3d;")
    )
    private Vec3d keepInsideBorder(EnderPearlEntity pearl) {
        Vec3d pos = pearl.getPos();
        World world = pearl.getWorld();
        if (world.isClient) return pos;

        Entity owner = pearl.getOwner();
        // Vanilla only teleports an owner that is in the same world as the pearl.
        if (owner == null || owner.getWorld() != world) owner = null;

        WorldBorder border = world.getWorldBorder();
        Box box = owner != null ? getBoxAt(owner, pos) : pearl.getBoundingBox();
        if (border.contains(box)) return pos;

        double margin = (owner != null ? owner.getWidth() : pearl.getWidth()) / 2 + 0.5;
        double x = clampToBorder(pos.x, border.getBoundWest() + margin, border.getBoundEast() - margin, border.getCenterX());
        double z = clampToBorder(pos.z, border.getBoundNorth() + margin, border.getBoundSouth() - margin, border.getCenterZ());
        Vec3d clamped = new Vec3d(x, pos.y, z);
        if (owner == null) return clamped;

        // Moving the destination sideways can put it inside a wall, so look upwards for free space.
        for (double y = clamped.y; y < world.getTopY(); y++) {
            Vec3d candidate = new Vec3d(x, y, z);
            if (world.isSpaceEmpty(owner, getBoxAt(owner, candidate))) {
                return candidate;
            }
        }

        // No free space: leave the owner where they are.
        return owner.getPos();
    }

    @Unique
    private static Box getBoxAt(Entity entity, Vec3d pos) {
        return entity.getDimensions(entity.getPose()).getBoxAt(pos);
    }

    @Unique
    private static double clampToBorder(double value, double min, double max, double center) {
        return min > max ? center : MathHelper.clamp(value, min, max);
    }
}
