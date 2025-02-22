package com.majruszs_difficulty.features.monster_spawn;

import com.majruszs_difficulty.GameState;
import com.majruszs_difficulty.goals.FollowGroupLeaderGoal;
import com.majruszs_difficulty.goals.TargetAsLeaderGoal;
import com.mlib.MajruszLibrary;
import com.mlib.items.ItemHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/** Spawning enemies in groups with leader and followers. */
public abstract class SpawnEnemyGroupBase extends OnEnemyToBeSpawnedBase {
	private static final String SIDEKICK_TAG = "MajruszsDifficultySidekick";
	protected final int minimumAmountOfChildren;
	protected final int maximumAmountOfChildren;
	protected final Item[] leaderArmor;

	public SpawnEnemyGroupBase( String configName, String configComment, GameState.State minimumState, boolean shouldChanceBeMultipliedByCRD,
		int minimumAmountOfChildren, int maximumAmountOfChildren, Item[] leaderArmor
	) {
		super( configName, configComment, 0.25, minimumState, shouldChanceBeMultipliedByCRD );
		this.minimumAmountOfChildren = minimumAmountOfChildren;
		this.maximumAmountOfChildren = maximumAmountOfChildren;
		this.leaderArmor = leaderArmor;
	}

	/** Called when all requirements were met. */
	@Override
	public void onExecute( LivingEntity entity, ServerLevel world ) {
		int childrenAmount = this.minimumAmountOfChildren + MajruszLibrary.RANDOM.nextInt(
			this.maximumAmountOfChildren - this.minimumAmountOfChildren + 1 );

		if( isSidekick( entity ) )
			return;

		if( this.leaderArmor != null )
			giveArmorToLeader( entity );

		spawnChildren( childrenAmount, entity, world );
	}

	protected abstract PathfinderMob spawnChild( ServerLevel world );

	/** Generates weapon for child. */
	protected ItemStack generateWeaponForChild() {
		return null;
	}

	/** Sets tag that informs this entity is a sidekick. */
	protected void markAsSidekick( LivingEntity entity ) {
		CompoundTag data = entity.getPersistentData();
		data.putBoolean( SIDEKICK_TAG, true );
	}

	/** Checks whether given entity is a sidekick. */
	protected boolean isSidekick( LivingEntity entity ) {
		CompoundTag data = entity.getPersistentData();
		return data.contains( SIDEKICK_TAG ) && data.getBoolean( SIDEKICK_TAG );
	}

	/**
	 Gives full armor to leader.

	 @param leader Entity to give an armor.
	 */
	private void giveArmorToLeader( LivingEntity leader ) {
		double clampedRegionalDifficulty = GameState.getRegionalDifficulty( leader );

		List< ItemStack > itemStacks = new ArrayList<>();
		for( Item item : this.leaderArmor )
			itemStacks.add( ItemHelper.damageAndEnchantItem( new ItemStack( item ), clampedRegionalDifficulty, true, 0.5 ) );

		leader.setItemSlot( EquipmentSlot.FEET, itemStacks.get( 0 ) );
		leader.setItemSlot( EquipmentSlot.LEGS, itemStacks.get( 1 ) );
		leader.setItemSlot( EquipmentSlot.CHEST, itemStacks.get( 2 ) );
		leader.setItemSlot( EquipmentSlot.HEAD, itemStacks.get( 3 ) );
	}

	/** Gives weapon from generateWeapon method to given entity. */
	private void giveWeaponTo( LivingEntity child ) {
		double clampedRegionalDifficulty = GameState.getRegionalDifficulty( child );

		ItemStack weapon = generateWeaponForChild();
		if( weapon != null )
			child.setItemSlot( EquipmentSlot.MAINHAND, ItemHelper.damageAndEnchantItem( weapon, clampedRegionalDifficulty, true, 0.5 ) );
	}

	/** Setting up AI goals like following leader. */
	private void setupGoals( PathfinderMob leader, PathfinderMob follower, int goalPriority, int targetPriority ) {
		follower.goalSelector.addGoal( goalPriority, new FollowGroupLeaderGoal( follower, leader, 1.0D, 6.0f, 5.0f ) );
		follower.targetSelector.addGoal( targetPriority, new TargetAsLeaderGoal( follower, leader ) );
	}

	/** Spawns given amount of children near the leader. */
	private void spawnChildren( int amount, LivingEntity leader, ServerLevel world ) {
		Vec3 spawnPosition = leader.position();

		if( !( leader instanceof PathfinderMob ) )
			return;

		for( int childID = 0; childID < amount; childID++ ) {
			PathfinderMob child = spawnChild( world );
			double x = spawnPosition.x - 3 + MajruszLibrary.RANDOM.nextInt( 7 );
			double y = spawnPosition.y + 0.5;
			double z = spawnPosition.z - 3 + MajruszLibrary.RANDOM.nextInt( 7 );
			child.setPos( x, y, z );
			setupGoals( ( PathfinderMob )leader, child, 9, 9 );
			giveWeaponTo( child );
			markAsSidekick( child );

			world.addFreshEntity( child );
		}
	}

	protected static class Armors {
		public static Item[] leather = new Item[]{ Items.LEATHER_BOOTS, Items.LEATHER_LEGGINGS, Items.LEATHER_CHESTPLATE, Items.LEATHER_HELMET };
		public static Item[] iron = new Item[]{ Items.IRON_BOOTS, Items.IRON_LEGGINGS, Items.IRON_CHESTPLATE, Items.IRON_HELMET };
		public static Item[] golden = new Item[]{ Items.GOLDEN_BOOTS, Items.GOLDEN_LEGGINGS, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_HELMET };
	}
}
