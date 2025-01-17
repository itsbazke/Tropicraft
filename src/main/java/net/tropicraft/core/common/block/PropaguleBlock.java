package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.tropicraft.core.common.TropicraftTags;

import java.util.List;

public final class PropaguleBlock extends WaterloggableSaplingBlock {
    public static final MapCodec<PropaguleBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            TreeGrower.CODEC.fieldOf("tree").forGetter(b -> b.treeGrower),
            propertiesCodec()
    ).apply(i, PropaguleBlock::new));

    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    private static final int GROW_CHANCE = 7;

    public static final BooleanProperty PLANTED = BooleanProperty.create("planted");

    public PropaguleBlock(TreeGrower tree, Properties properties) {
        super(tree, properties);
        registerDefaultState(stateDefinition.any().setValue(STAGE, 0).setValue(WATERLOGGED, false).setValue(PLANTED, true));
    }

    @Override
    public MapCodec<PropaguleBlock> codec() {
        return CODEC;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (state.getValue(PLANTED)) {
            BlockPos groundPos = pos.below();
            return mayPlaceOn(world.getBlockState(groundPos), world, groundPos);
        } else {
            BlockPos topPos = pos.above();
            return world.getBlockState(topPos).is(BlockTags.LEAVES);
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
        return super.mayPlaceOn(state, world, pos) || state.is(BlockTags.SAND)
                || state.is(TropicraftTags.Blocks.MUD);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(PLANTED);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!world.isAreaLoaded(pos, 1)) return;

        if (world.getMaxLocalRawBrightness(pos.above()) >= 9 && random.nextInt(GROW_CHANCE) == 0) {
            advanceTree(world, pos, state, random);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(PLANTED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
                .setValue(PLANTED, context.getClickedFace() != Direction.DOWN);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PLANTED);
    }
}
