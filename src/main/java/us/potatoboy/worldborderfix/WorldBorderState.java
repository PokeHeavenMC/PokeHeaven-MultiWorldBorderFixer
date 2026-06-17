package us.potatoboy.worldborderfix;

import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.PersistentState;
import net.minecraft.world.border.WorldBorder;

public class WorldBorderState extends PersistentState {
    public static final String ID = "worldBorder";

    private double centerX = WorldBorder.DEFAULT_BORDER.getCenterX();
    private double centerZ = WorldBorder.DEFAULT_BORDER.getCenterZ();
    private double size = WorldBorder.DEFAULT_BORDER.getSize();
    private double buffer = WorldBorder.DEFAULT_BORDER.getSafeZone();
    private double damagePerBlock = WorldBorder.DEFAULT_BORDER.getDamagePerBlock();
    private int warningBlocks = WorldBorder.DEFAULT_BORDER.getWarningBlocks();
    private int warningTime = WorldBorder.DEFAULT_BORDER.getWarningTime();

    public static final PersistentState.Type<WorldBorderState> TYPE = new PersistentState.Type<>(
        WorldBorderState::new,
        WorldBorderState::readNbt,
        DataFixTypes.LEVEL
    );

    public WorldBorderState() {}

    public static WorldBorderState readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        WorldBorderState state = new WorldBorderState();
        if (nbt.contains("BorderCenterX")) state.centerX = nbt.getDouble("BorderCenterX");
        if (nbt.contains("BorderCenterZ")) state.centerZ = nbt.getDouble("BorderCenterZ");
        if (nbt.contains("BorderSize")) state.size = nbt.getDouble("BorderSize");
        if (nbt.contains("BorderSafeZone")) state.buffer = nbt.getDouble("BorderSafeZone");
        if (nbt.contains("BorderDamagePerBlock")) state.damagePerBlock = nbt.getDouble("BorderDamagePerBlock");
        if (nbt.contains("BorderWarningBlocks")) state.warningBlocks = nbt.getInt("BorderWarningBlocks");
        if (nbt.contains("BorderWarningTime")) state.warningTime = nbt.getInt("BorderWarningTime");
        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putDouble("BorderCenterX", centerX);
        nbt.putDouble("BorderCenterZ", centerZ);
        nbt.putDouble("BorderSize", size);
        nbt.putDouble("BorderSafeZone", buffer);
        nbt.putDouble("BorderDamagePerBlock", damagePerBlock);
        nbt.putInt("BorderWarningBlocks", warningBlocks);
        nbt.putInt("BorderWarningTime", warningTime);
        return nbt;
    }

    public double getCenterX() {
        return centerX;
    }


    public double getCenterZ() {
        return centerZ;
    }

    public double getSize() {
        return size;
    }

    public double getBuffer() {
        return buffer;
    }

    public double getDamagePerBlock() {
        return damagePerBlock;
    }

    public int getWarningBlocks() {
        return warningBlocks;
    }

    public int getWarningTime() {
        return warningTime;
    }

    public void fromBorder(WorldBorder worldBorder) {
        centerX = worldBorder.getCenterX();
        centerZ = worldBorder.getCenterZ();
        size = worldBorder.getSize();
        buffer = worldBorder.getSafeZone();
        damagePerBlock = worldBorder.getDamagePerBlock();
        warningBlocks = worldBorder.getWarningBlocks();
        warningTime = worldBorder.getWarningTime();

        markDirty();
    }
}
