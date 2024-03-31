package com.tristankechlo.whatdidijustkill.config.types;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tristankechlo.whatdidijustkill.IPlatformHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.Optional;

public record EntityOptions(ShowToastOption showToast, int timeout, FormatOption firstLine, FormatOption secondLine,
                            ToastTheme theme, List<Either<ResourceLocation, ModWildcard>> excludes) {

    public static final EntityOptions DEFAULT = new EntityOptions(ShowToastOption.NOT_EXCLUDED, 2000, FormatOption.KILLED_DISTANCE, FormatOption.ENTITY_TYPE, ToastTheme.ADVANCEMENT, List.of(Either.left(new ResourceLocation("bat"))));

    public static final Codec<EntityOptions> CODEC = RecordCodecBuilder.<EntityOptions>mapCodec(
            instance -> instance.group(
                    ShowToastOption.CODEC.fieldOf("show_toast").forGetter(EntityOptions::showToast),
                    Codec.intRange(250, 20000).fieldOf("timeout").forGetter(EntityOptions::timeout),
                    FormatOption.CODEC.fieldOf("first_line").forGetter(EntityOptions::firstLine),
                    FormatOption.CODEC.fieldOf("second_line").forGetter(EntityOptions::secondLine),
                    ToastTheme.CODEC.fieldOf("theme").forGetter(EntityOptions::theme),
                    Codec.either(ResourceLocation.CODEC, ModWildcard.CODEC).listOf().fieldOf("excludes").forGetter(EntityOptions::excludes)
            ).apply(instance, EntityOptions::new)
    ).flatXmap(EntityOptions::verify, EntityOptions::verify).codec();

    private static DataResult<EntityOptions> verify(EntityOptions options) {
        if (options.firstLine() == FormatOption.NONE) {
            return DataResult.error("EntityOptions: 'first_line' can not be 'NONE'.");
        }
        return DataResult.success(options);
    }

    public boolean isEntityExcluded(ResourceLocation entityType) {
        for (Either<ResourceLocation, ModWildcard> element : excludes()) {
            Optional<ModWildcard> wildcard = element.right();
            if (wildcard.isPresent() && wildcard.get().modid().equalsIgnoreCase(entityType.getNamespace())) {
                return true;
            }

            Optional<ResourceLocation> string = element.left();
            if (string.isPresent() && string.get().equals(entityType)) {
                return true;
            }
        }
        return false;
    }

    public enum ShowToastOption implements StringRepresentable {

        ALL("ALL"), // show toast for all mobs
        ONLY_NAMED("ONLY_NAMED"), // only show toast for mobs with special names
        NOT_EXCLUDED("NOT_EXCLUDED"), // only show toast for mobs that are not in the exclude list
        NONE("NONE"); // no toast

        public static final Codec<ShowToastOption> CODEC = StringRepresentable.fromEnum(ShowToastOption::values);
        private final String key;

        ShowToastOption(String key) {
            this.key = key;
        }

        @Override
        public String getSerializedName() {
            return this.key;
        }
    }

    public record ModWildcard(String modid) {

        private static final Codec<ModWildcard> CODEC = RecordCodecBuilder.<ModWildcard>mapCodec(
                instance -> instance.group(
                        Codec.STRING.fieldOf("wildcard").forGetter(ModWildcard::modid)
                ).apply(instance, ModWildcard::new)
        ).flatXmap(ModWildcard::verify, ModWildcard::verify).codec();

        private static DataResult<ModWildcard> verify(ModWildcard wildcard) {
            if (!IPlatformHelper.INSTANCE.isModLoaded(wildcard.modid())) {
                return DataResult.error(String.format("Not a valid wildcard: \"%s\"", wildcard.modid()));
            }
            return DataResult.success(wildcard);
        }
    }

}
