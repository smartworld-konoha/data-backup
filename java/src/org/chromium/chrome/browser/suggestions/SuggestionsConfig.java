// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.suggestions;

import android.content.res.Resources;

import org.chromium.base.ApiCompatibilityUtils;
import org.chromium.chrome.R;
import org.chromium.chrome.browser.ChromeFeatureList;
import org.chromium.chrome.browser.util.FeatureUtilities;
import org.chromium.chrome.browser.widget.displaystyle.UiConfig;

/**
 * Provides configuration details for suggestions.
 */
public final class SuggestionsConfig {
    /**
     * Experiment parameter for whether to use the condensed tile layout on small screens.
     */
    private static final String PARAM_CONDENSED_TILE_LAYOUT_FOR_SMALL_SCREENS_ENABLED =
            "condensed_tile_layout_for_small_screens_enabled";

    /**
     * Experiment parameter for whether to use the condensed tile layout on large screens.
     */
    private static final String PARAM_CONDENSED_TILE_LAYOUT_FOR_LARGE_SCREENS_ENABLED =
            "condensed_tile_layout_for_large_screens_enabled";

    private SuggestionsConfig() {}

    /**
     * @return Whether scrolling to the bottom of suggestions triggers a load.
     */
    public static boolean scrollToLoad() {
        return FeatureUtilities.isChromeHomeModernEnabled()
                && ChromeFeatureList.isEnabled(
                           ChromeFeatureList.CONTENT_SUGGESTIONS_SCROLL_TO_LOAD);
    }

    /**
     * @return Whether to use the Sites exploration UI to display the site suggestions.
     */
    public static boolean useSitesExplorationUi() {
        return ChromeFeatureList.isEnabled(ChromeFeatureList.SITE_EXPLORATION_UI);
    }

    /**
     * @param resources The resources to fetch the color from.
     * @return The background color for the suggestions sheet content.
     */
    public static int getBackgroundColor(Resources resources) {
        return FeatureUtilities.isChromeHomeModernEnabled()
                ? ApiCompatibilityUtils.getColor(resources, R.color.suggestions_modern_bg)
                : ApiCompatibilityUtils.getColor(resources, R.color.ntp_bg);
    }

    /**
     * Returns the current tile style, that depends on the enabled features and the screen size.
     */
    @TileView.Style
    public static int getTileStyle(UiConfig uiConfig) {
        boolean small = uiConfig.getCurrentDisplayStyle().isSmall();
        if (FeatureUtilities.isChromeHomeModernEnabled()) {
            return small ? TileView.Style.MODERN_CONDENSED : TileView.Style.MODERN;
        }
        if (FeatureUtilities.isChromeHomeEnabled()) return TileView.Style.CLASSIC;
        if (useCondensedTileLayout(small)) return TileView.Style.CLASSIC_CONDENSED;
        return TileView.Style.CLASSIC;
    }

    private static boolean useCondensedTileLayout(boolean isScreenSmall) {
        if (isScreenSmall) {
            return ChromeFeatureList.getFieldTrialParamByFeatureAsBoolean(
                    ChromeFeatureList.NTP_CONDENSED_TILE_LAYOUT,
                    PARAM_CONDENSED_TILE_LAYOUT_FOR_SMALL_SCREENS_ENABLED, false);
        }

        return ChromeFeatureList.getFieldTrialParamByFeatureAsBoolean(
                ChromeFeatureList.NTP_CONDENSED_TILE_LAYOUT,
                PARAM_CONDENSED_TILE_LAYOUT_FOR_LARGE_SCREENS_ENABLED, false);
    }
}
