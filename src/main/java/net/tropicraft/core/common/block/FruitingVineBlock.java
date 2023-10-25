package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public final class FruitingVineBlock extends MultifaceBlock implements BonemealableBlock {
    public static final MapCodec<FruitingVineBlock> CODEC = simpleCodec(FruitingVineBlock::new);

    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    public static final int MAX_AGE = 2;

    private static final int GROW_CHANCE = 10;

    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public FruitingVineBlock(final Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(AGE, 0));
    }

    @Override
    protected MapCodec<FruitingVineBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean isRandomlyTicking(final BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public void randomTick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
        if (random.nextInt(GROW_CHANCE) == 0) {
            level.setBlockAndUpdate(pos, state.cycle(AGE));
        }
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return spreader;
    }

    @Override
    public boolean canBeReplaced(final BlockState state, final BlockPlaceContext context) {
        return !context.getItemInHand().is(asItem()) || super.canBeReplaced(state, context);
    }

    @Override
    public boolean isValidBonemealTarget(final LevelReader level, final BlockPos pos, final BlockState state) {
        if (state.getValue(AGE) < MAX_AGE) {
            return true;
        }
        for (final Direction direction : Direction.values()) {
            if (spreader.canSpreadInAnyDirection(state, level, pos, direction.getOpposite())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBonemealSuccess(final Level level, final RandomSource random, final BlockPos pos, final BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(final ServerLevel level, final RandomSource random, final BlockPos pos, final BlockState state) {
        if (random.nextInt(2) == 0) {
            spreader.spreadFromRandomFaceTowardRandomDirection(state, level, pos, random);
        }
        if (state.getValue(AGE) < MAX_AGE && random.nextInt(2) == 0) {
            level.setBlockAndUpdate(pos, state.cycle(AGE));
        }
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }
}