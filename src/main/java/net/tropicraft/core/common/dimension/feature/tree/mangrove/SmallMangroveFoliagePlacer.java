package net.tropicraft.core.common.dimension.feature.tree.mangrove;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftFoliagePlacers;

public final class SmallMangroveFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<SmallMangroveFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return foliagePlacerParts(instance).apply(instance, SmallMangroveFoliagePlacer::new);
    });

    public SmallMangroveFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TropicraftFoliagePlacers.SMALL_MANGROVE.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader world, FoliageSetter setter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        placeLeavesRow(world, setter, random, config, attachment.pos(), foliageRadius, 0, attachment.doubleTrunk());
        placeLeavesRow(world, setter, random, config, attachment.pos(), foliageRadius, 1, attachment.doubleTrunk());
    }

    @Override
    public int foliageHeight(RandomSource random, int p_230374_2_, TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        if (y == 0) {
            return radius != 0 && dx == radius && dz == radius && random.nextInt(2) == 0;
        }

        if (dx == 0 && dz == 0) {
            return false;
        }

        if (random.nextBoolean()) {
            return true;
        }

        return dx == radius && dz == radius;
    }
}
