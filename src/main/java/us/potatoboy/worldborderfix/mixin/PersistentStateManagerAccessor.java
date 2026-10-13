package us.potatoboy.worldborderfix.mixin;

import net.minecraft.world.PersistentStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.File;

@Mixin(PersistentStateManager.class)
public interface PersistentStateManagerAccessor {
    @Invoker("getFile")
    File invokeGetFile(String id);
}
