package dev.xkmc.l2artifacts.init.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.xkmc.l2artifacts.init.ModEntryPoint;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.codec.JsonCodec;
import dev.xkmc.l2library.serial.codec.PacketCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import java.util.stream.Stream;

@SerialClass
public class EnchantmentIngredient extends AbstractIngredient {

	@SerialClass.SerialField
	public Enchantment enchantment;
	@SerialClass.SerialField
	public int minLevel;

	@Deprecated
	public EnchantmentIngredient() {

	}

	private EnchantmentIngredient validate() {
		return new EnchantmentIngredient(enchantment, minLevel);
	}

	public EnchantmentIngredient(Enchantment enchantment, int minLevel) {
		super(Stream.of(new Ingredient.ItemValue(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, minLevel)))));
		this.enchantment = enchantment;
		this.minLevel = minLevel;
	}

	public boolean test(ItemStack stack) {
		return EnchantmentHelper.getEnchantments(stack).getOrDefault(this.enchantment, 0) >= this.minLevel;
	}

	public boolean isSimple() {
		return false;
	}

	public IIngredientSerializer<? extends Ingredient> getSerializer() {
		return EnchantmentIngredient.Serializer.INSTANCE;
	}

	public JsonElement toJson() {
		JsonObject obj = JsonCodec.toJson(this).getAsJsonObject();
		obj.addProperty("type", ModEntryPoint.MODID + ":enchantment");
		return obj;
	}

	public static class Serializer implements IIngredientSerializer<EnchantmentIngredient> {
		public static final EnchantmentIngredient.Serializer INSTANCE = new EnchantmentIngredient.Serializer();

		private Serializer() {
		}

		public EnchantmentIngredient parse(FriendlyByteBuf buffer) {
			return PacketCodec.from(buffer, EnchantmentIngredient.class, null).validate();
		}

		public EnchantmentIngredient parse(JsonObject json) {
			return JsonCodec.from(json, EnchantmentIngredient.class, null).validate();
		}

		public void write(FriendlyByteBuf buffer, EnchantmentIngredient ingredient) {
			PacketCodec.to(buffer, ingredient);
		}
	}
}
