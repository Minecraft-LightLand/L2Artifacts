package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.content.effects.core.SetEffectData;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

@SerialClass
public class AttributeSetData extends SetEffectData {

	public record AttributePair(Holder<Attribute> attr, ResourceLocation id) {

	}

	@SerialField
	public ArrayList<AttributePair> list = new ArrayList<>();

	@Override
	protected void remove(Player player) {
		for (var attr : list) {
			var ins = player.getAttribute(attr.attr());
			if (ins != null) {
				ins.removeModifier(attr.id());
			}
		}
	}
}
