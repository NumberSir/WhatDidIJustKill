package com.tristankechlo.whatdidijustkill.config.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record PlayerOptions(boolean showToast, int timeout, FormatOption firstLine, FormatOption secondLine) {

    public static final PlayerOptions DEFAULT = new PlayerOptions(true, 2000, FormatOption.KILLED, FormatOption.DISTANCE);

    public static final Codec<PlayerOptions> CODEC = ExtraCodecs.validate(RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.fieldOf("show_toast").forGetter(PlayerOptions::showToast),
                    Codec.intRange(250, 20000).fieldOf("timeout").forGetter(PlayerOptions::timeout),
                    FormatOption.CODEC.fieldOf("first_line").forGetter(PlayerOptions::firstLine),
                    FormatOption.CODEC.fieldOf("second_line").forGetter(PlayerOptions::secondLine)
            ).apply(instance, PlayerOptions::new)
    ), PlayerOptions::verify);

    private static DataResult<PlayerOptions> verify(PlayerOptions options) {
        if (options.firstLine() == FormatOption.NONE) {
            return DataResult.error(() -> "PlayerOptions: 'first_line' can not be 'NONE'.");
        }
        return DataResult.success(options);
    }

}
