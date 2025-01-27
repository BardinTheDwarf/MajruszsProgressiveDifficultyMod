package com.majruszs_difficulty.features.treasure_bag;

import com.majruszs_difficulty.Instances;
import com.majruszs_difficulty.config.GameStateIntegerConfig;
import com.mlib.config.ConfigGroup;
import com.mlib.items.ItemHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.majruszs_difficulty.MajruszsDifficulty.FEATURES_GROUP;

/** Awarding player with fisherman treasure bag after certain amount of fished items. */
@Mod.EventBusSubscriber
public class FishingRewarder {
	protected static final String FISHING_TAG = "FishermanTreasureBagCounter";
	protected final ConfigGroup fishingGroup;
	protected final GameStateIntegerConfig treasureBagRequirement;

	public FishingRewarder() {
		String requirementComment = "Required amount of items fished to get Treasure Bag.";
		String groupComment = "Everything related to fishing.";
		this.treasureBagRequirement = new GameStateIntegerConfig( "RequiredItems", requirementComment, 20, 15, 10, 3, 100 );

		this.fishingGroup = FEATURES_GROUP.addGroup( new ConfigGroup( "Fishing", groupComment ) );
		this.fishingGroup.addConfig( this.treasureBagRequirement );
	}

	@SubscribeEvent
	public static void onFishing( ItemFishedEvent event ) {
		Player player = event.getPlayer();

		if( !( player.level instanceof ServerLevel ) )
			return;

		CompoundTag data = player.getPersistentData();
		int fishedItemsCounter = data.getInt( FISHING_TAG ) + 1;
		data.putInt( FISHING_TAG, fishedItemsCounter );

		if( fishedItemsCounter >= Instances.FISHING_REWARDER.getRequiredItems() ) {
			data.putInt( FISHING_TAG, fishedItemsCounter - Instances.FISHING_REWARDER.getRequiredItems() );
			if( Instances.FISHING_TREASURE_BAG.isAvailable() )
				giveTreasureBagTo( player );
		}
	}

	/** Gives a treasure bag to specified player. */
	protected static void giveTreasureBagTo( Player player ) {
		ItemHelper.giveItemStackToPlayer( new ItemStack( Instances.FISHING_TREASURE_BAG ), player, ( ServerLevel )player.level );
	}

	/** Returns how many fish player must fished to get treasure bag. */
	protected int getRequiredItems() {
		return this.treasureBagRequirement.getCurrentGameStateValue();
	}
}
