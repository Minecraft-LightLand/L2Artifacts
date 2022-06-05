package dev.xkmc.l2artifacts.init.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class EnchantmentRecipeBuilder implements RecipeBuilder {
	private final Enchantment enchantment;
	private final int level;
	private final List<String> rows = Lists.newArrayList();
	private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
	private final Advancement.Builder advancement = Advancement.Builder.advancement();
	@Nullable
	private String group;

	public EnchantmentRecipeBuilder(Enchantment p_126114_, int p_126115_) {
		this.enchantment = p_126114_;
		this.level = p_126115_;
	}

	public EnchantmentRecipeBuilder define(Character p_206417_, TagKey<Item> p_206418_) {
		return this.define(p_206417_, Ingredient.of(p_206418_));
	}

	public EnchantmentRecipeBuilder define(Character p_126128_, ItemLike p_126129_) {
		return this.define(p_126128_, Ingredient.of(p_126129_));
	}

	public EnchantmentRecipeBuilder define(Character p_126125_, Ingredient p_126126_) {
		if (this.key.containsKey(p_126125_)) {
			throw new IllegalArgumentException("Symbol '" + p_126125_ + "' is already defined!");
		} else if (p_126125_ == ' ') {
			throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
		} else {
			this.key.put(p_126125_, p_126126_);
			return this;
		}
	}

	public EnchantmentRecipeBuilder pattern(String p_126131_) {
		if (!this.rows.isEmpty() && p_126131_.length() != this.rows.get(0).length()) {
			throw new IllegalArgumentException("Pattern must be the same width on every line!");
		} else {
			this.rows.add(p_126131_);
			return this;
		}
	}

	public EnchantmentRecipeBuilder unlockedBy(String p_126133_, CriterionTriggerInstance p_126134_) {
		this.advancement.addCriterion(p_126133_, p_126134_);
		return this;
	}

	public EnchantmentRecipeBuilder group(@Nullable String p_126146_) {
		this.group = p_126146_;
		return this;
	}

	public Item getResult() {
		return Items.ENCHANTED_BOOK;
	}

	public void save(Consumer<FinishedRecipe> p_176499_) {
		this.save(p_176499_, this.enchantment.getRegistryName());
	}

	public void save(Consumer<FinishedRecipe> p_126141_, ResourceLocation p_126142_) {
		this.ensureValid(p_126142_);
		this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_126142_)).rewards(AdvancementRewards.Builder.recipe(p_126142_)).requirements(RequirementsStrategy.OR);
		p_126141_.accept(new Result(p_126142_, this.enchantment, this.level, this.group == null ? "" : this.group, this.rows, this.key, this.advancement, new ResourceLocation(p_126142_.getNamespace(), "recipes/enchantments/" + p_126142_.getPath())));
	}

	private void ensureValid(ResourceLocation p_126144_) {
		if (this.rows.isEmpty()) {
			throw new IllegalStateException("No pattern is defined for shaped recipe " + p_126144_ + "!");
		} else {
			Set<Character> set = Sets.newHashSet(this.key.keySet());
			set.remove(' ');

			for (String s : this.rows) {
				for (int i = 0; i < s.length(); ++i) {
					char c0 = s.charAt(i);
					if (!this.key.containsKey(c0) && c0 != ' ') {
						throw new IllegalStateException("Pattern in recipe " + p_126144_ + " uses undefined symbol '" + c0 + "'");
					}

					set.remove(c0);
				}
			}

			if (!set.isEmpty()) {
				throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + p_126144_);
			} else if (this.rows.size() == 1 && this.rows.get(0).length() == 1) {
				throw new IllegalStateException("Shaped recipe " + p_126144_ + " only takes in a single item - should it be a shapeless recipe instead?");
			} else if (this.advancement.getCriteria().isEmpty()) {
				throw new IllegalStateException("No way of obtaining recipe " + p_126144_);
			}
		}
	}

	public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final Enchantment ench;
		private final int lvl;
		private final String group;
		private final List<String> pattern;
		private final Map<Character, Ingredient> key;
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;

		public Result(ResourceLocation p_176754_, Enchantment p_176755_, int p_176756_, String p_176757_, List<String> p_176758_, Map<Character, Ingredient> p_176759_, Advancement.Builder p_176760_, ResourceLocation p_176761_) {
			this.id = p_176754_;
			this.ench = p_176755_;
			this.lvl = p_176756_;
			this.group = p_176757_;
			this.pattern = p_176758_;
			this.key = p_176759_;
			this.advancement = p_176760_;
			this.advancementId = p_176761_;
		}

		public void serializeRecipeData(JsonObject p_126167_) {
			if (!this.group.isEmpty()) {
				p_126167_.addProperty("group", this.group);
			}

			JsonArray jsonarray = new JsonArray();

			for (String s : this.pattern) {
				jsonarray.add(s);
			}

			p_126167_.add("pattern", jsonarray);
			JsonObject jsonobject = new JsonObject();

			for (Map.Entry<Character, Ingredient> entry : this.key.entrySet()) {
				jsonobject.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
			}

			p_126167_.add("key", jsonobject);

			JsonObject result = new JsonObject();
			result.addProperty("item", Items.ENCHANTED_BOOK.getRegistryName().toString());
			JsonObject tag = new JsonObject();
			JsonArray list = new JsonArray();
			JsonObject entry = new JsonObject();
			entry.addProperty("id", this.ench.getRegistryName().toString());
			entry.addProperty("lvl", this.lvl);
			list.add(entry);
			tag.add("Enchantments", list);
			result.add("nbt", tag);

			p_126167_.add("result", result);
		}

		public RecipeSerializer<?> getType() {
			return RecipeSerializer.SHAPED_RECIPE;
		}

		public ResourceLocation getId() {
			return this.id;
		}

		@Nullable
		public JsonObject serializeAdvancement() {
			return this.advancement.serializeToJson();
		}

		@Nullable
		public ResourceLocation getAdvancementId() {
			return this.advancementId;
		}
	}
}
