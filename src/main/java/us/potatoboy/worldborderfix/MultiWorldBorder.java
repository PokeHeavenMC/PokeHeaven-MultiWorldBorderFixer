package us.potatoboy.worldborderfix;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import us.potatoboy.worldborderfix.mixin.PersistentStateManagerAccessor;

public class MultiWorldBorder implements ModInitializer {
	@Override
	public void onInitialize() {
		// Fires for every ServerWorld, including Fantasy runtime dimensions created by
		// Multiworld (the published Fantasy lib invokes this event when adding a world).
		ServerWorldEvents.LOAD.register((server, world) -> {
			// The overworld is restored later, once vanilla has read level.dat (see MinecraftServerMixin).
			if (world.getRegistryKey() != World.OVERWORLD) {
				loadBorder(world, true);
			}

			world.getWorldBorder().addListener(new PerWorldBorderListener(world));
		});
	}

	/**
	 * Restores a world's border from its {@link WorldBorderState}.
	 *
	 * @param createIfMissing when false and no state was saved yet, the border keeps its current values.
	 *                        Used for the overworld, whose border is also stored in level.dat. Our own file
	 *                        wins when present, since level.dat can be rewritten by other mods (Multiworld
	 *                        writes it with a default border) and is only saved every autosave.
	 */
	public static void loadBorder(ServerWorld world, boolean createIfMissing) {
		WorldBorder border = world.getWorldBorder();
		PersistentStateManager manager = world.getPersistentStateManager();
		WorldBorderState state = createIfMissing
			? manager.getOrCreate(WorldBorderState.TYPE, WorldBorderState.ID)
			: manager.get(WorldBorderState.TYPE, WorldBorderState.ID);

		if (state != null) {
			state.applyTo(border);
		}

		((BorderWithWorld) border).setLoaded(true);
	}

	/**
	 * Writes a world's border to disk right away, so a change survives a crash that happens before
	 * the next autosave.
	 */
	public static void saveBorder(ServerWorld world) {
		WorldBorder border = world.getWorldBorder();
		if (!((BorderWithWorld) border).isLoaded() || world.savingDisabled) return;

		PersistentStateManager manager = world.getPersistentStateManager();
		WorldBorderState state = manager.getOrCreate(WorldBorderState.TYPE, WorldBorderState.ID);
		state.fromBorder(border);
		state.save(((PersistentStateManagerAccessor) manager).invokeGetFile(WorldBorderState.ID), world.getRegistryManager());
	}
}
