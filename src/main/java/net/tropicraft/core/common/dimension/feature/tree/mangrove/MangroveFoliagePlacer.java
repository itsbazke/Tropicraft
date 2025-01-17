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

public final class MangroveFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<MangroveFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).apply(i, MangroveFoliagePlacer::new));

    public MangroveFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TropicraftFoliagePlacers.MANGROVE.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter setter, RandomSource random, TreeConfiguration config, int p_225617_, FoliageAttachment node, int p_225619_, int p_225620_, int p_225621_) {
        placeLeavesRow(level, setter, random, config, node.pos(), node.radiusOffset(), 1, node.doubleTrunk());
        placeLeavesRow(level, setter, random, config, node.pos(), node.radiusOffset() + 1, 0, node.doubleTrunk());
        placeLeavesRow(level, setter, random, config, node.pos(), node.radiusOffset(), -1, node.doubleTrunk());
    }

    @Override
    public int foliageHeight(RandomSource random, int p_230374_2_, TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return radius != 0 && dx == radius && dz == radius && (random.nextInt(2) == 0 || y == 0);
    }
}
