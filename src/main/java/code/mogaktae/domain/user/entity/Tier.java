package code.mogaktae.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Tier {
    UNRATED(0, 29),

    BRONZE_V(30, 59),
    BRONZE_IV(60, 89),
    BRONZE_III(90, 119),
    BRONZE_II(120, 149),
    BRONZE_I(150, 199),

    SILVER_V(200, 299),
    SILVER_IV(300, 399),
    SILVER_III(400, 499),
    SILVER_II(500, 649),
    SILVER_I(650, 799),

    GOLD_V(800, 949),
    GOLD_IV(950, 1099),
    GOLD_III(1100, 1249),
    GOLD_II(1250, 1399),
    GOLD_I(1400, 1599),

    PLATINUM_V(1600, 1749),
    PLATINUM_IV(1750, 1899),
    PLATINUM_III(1900, 1999),
    PLATINUM_II(2000, 2099),
    PLATINUM_I(2100, 2199),

    DIAMOND_V(2200, 2299),
    DIAMOND_IV(2300, 2399),
    DIAMOND_III(2400, 2499),
    DIAMOND_II(2500, 2599),
    DIAMOND_I(2600, 2699),

    RUBY_V(2700, 2799),
    RUBY_IV(2800, 2849),
    RUBY_III(2850, 2899),
    RUBY_II(2900, 2949),
    RUBY_I(2950, 2999),

    MASTER(3000, Integer.MAX_VALUE);

    private final int minRating;
    private final int maxRating;

    public static Tier fromRating(Long rating) {
        for (Tier tier : values()) {
            if (rating >= tier.minRating && rating <= tier.maxRating) {
                return tier;
            }
        }
        return UNRATED;
    }
}
