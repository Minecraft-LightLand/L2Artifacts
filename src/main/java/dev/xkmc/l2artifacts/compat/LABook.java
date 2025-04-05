package dev.xkmc.l2artifacts.compat;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2core.compat.patchouli.PatchouliHelper;
import net.minecraft.world.item.Items;

public class LABook {

	public static void gen() {
		new PatchouliHelper(L2Artifacts.REGISTRATE, "artifact_guide")
				.buildModel().buildShapelessRecipe(e -> e
								.requires(Items.BOOK).requires(ArtifactItems.ITEM_EXP[0].asItem()),
						() -> Items.BOOK)
				.buildBook("L2Artifacts Guide",
						"Welcome to L2Artifact",
						1, ArtifactItems.TAB.key());
	}

}
