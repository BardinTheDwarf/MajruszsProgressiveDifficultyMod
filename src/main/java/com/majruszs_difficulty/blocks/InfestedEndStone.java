package com.majruszs_difficulty.blocks;

import com.majruszs_difficulty.Instances;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

/** New end block which works like End Stone block but spawns Endermite when destroyed. */
public class InfestedEndStone extends Block {
	public InfestedEndStone() {
		super( Properties.of( Material.CLAY, MaterialColor.COLOR_YELLOW )
			.strength( 0.0f, 0.75f )
			.sound( SoundType.STONE ) );
	}

	@Override
	public void spawnAfterBreak( BlockState state, ServerLevel worldIn, BlockPos pos, ItemStack stack ) {
		super.spawnAfterBreak( state, worldIn, pos, stack );

		GameRules gameRules = worldIn.getGameRules();
		if( gameRules.getBoolean( GameRules.RULE_DOBLOCKDROPS ) && EnchantmentHelper.getItemEnchantmentLevel( Enchantments.SILK_TOUCH, stack ) == 0 )
			this.spawnEndermite( worldIn, pos );
	}

	@Override
	public void wasExploded( Level world, BlockPos position, Explosion explosion ) {
		if( world instanceof ServerLevel )
			this.spawnEndermite( ( ServerLevel )world, position );
	}

	/** Spawns a Endermite at given position. */
	private void spawnEndermite( ServerLevel world, BlockPos position ) {
		Endermite endermite = EntityType.ENDERMITE.create( world );
		if( endermite == null )
			return;

		endermite.moveTo( position.getX() + 0.5, position.getY(), position.getZ() + 0.5, 0.0f, 0.0f );
		world.addFreshEntity( endermite );
		endermite.spawnAnim();
	}

	public static class InfestedEndStoneItem extends BlockItem {
		public InfestedEndStoneItem() {
			super( Instances.INFESTED_END_STONE, ( new Properties() ).stacksTo( 64 )
				.tab( Instances.ITEM_GROUP ) );
		}
	}
}
