package com.genpause.app.service

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo

/**
 * UiTreeScanner: Recursively scans the Accessibility UI tree
 * to identify addictive elements within apps (e.g., Instagram Reels,
 * YouTube Shorts, Facebook News Feed).
 *
 * Used for surgical blocking — allowing messaging/utility while
 * blocking infinite-scroll elements.
 */
object UiTreeScanner {

    private const val TAG = "UiTreeScanner"

    /**
     * Known addictive element patterns by package name.
     * Each entry maps a package to a list of (resource ID pattern, text pattern) pairs.
     */
    private val surgicalPatterns = mapOf(
        "com.instagram.android" to listOf(
            SurgicalTarget("reels_tab", null, "Reels tab"),
            SurgicalTarget("clips_tab", null, "Clips tab"),
            SurgicalTarget(null, "Reels", "Reels text")
        ),
        "com.google.android.youtube" to listOf(
            SurgicalTarget("shorts_pivot_button", null, "Shorts button"),
            SurgicalTarget("reel_player_page_container", null, "Shorts player"),
            SurgicalTarget(null, "Shorts", "Shorts text")
        ),
        "com.facebook.katana" to listOf(
            SurgicalTarget("newsfeed_tab", null, "News Feed tab"),
            SurgicalTarget(null, "Reels", "Reels text")
        ),
        "com.zhiliaoapp.musically" to listOf( // TikTok
            SurgicalTarget("bottom_follow", null, "Following tab"),
            SurgicalTarget("bottom_for_you", null, "For You tab")
        )
    )

    data class SurgicalTarget(
        val resourceIdPattern: String?,
        val textPattern: String?,
        val description: String
    )

    data class ScanResult(
        val found: Boolean,
        val matchedTarget: SurgicalTarget? = null,
        val matchedNode: AccessibilityNodeInfo? = null
    )

    /**
     * Scan the current window's UI tree for known addictive elements.
     * @param rootNode The root AccessibilityNodeInfo to scan from
     * @param packageName The package name of the current foreground app
     * @return ScanResult with matched target info if found
     */
    fun scanForAddictiveElements(
        rootNode: AccessibilityNodeInfo?,
        packageName: String
    ): ScanResult {
        if (rootNode == null) return ScanResult(false)

        val targets = surgicalPatterns[packageName] ?: return ScanResult(false)

        for (target in targets) {
            val matched = findNode(rootNode, target)
            if (matched != null) {
                Log.d(TAG, "Found addictive element: ${target.description} in $packageName")
                return ScanResult(true, target, matched)
            }
        }

        return ScanResult(false)
    }

    /**
     * Recursively search the UI tree for a node matching the target pattern.
     */
    private fun findNode(
        node: AccessibilityNodeInfo,
        target: SurgicalTarget
    ): AccessibilityNodeInfo? {
        // Check resource ID
        if (target.resourceIdPattern != null) {
            val viewId = node.viewIdResourceName
            if (viewId != null && viewId.contains(target.resourceIdPattern, ignoreCase = true)) {
                return node
            }
        }

        // Check text content
        if (target.textPattern != null) {
            val text = node.text?.toString()
            val contentDesc = node.contentDescription?.toString()
            if ((text != null && text.equals(target.textPattern, ignoreCase = true)) ||
                (contentDesc != null && contentDesc.equals(target.textPattern, ignoreCase = true))) {
                // Only match if the node is clickable (likely a tab/button)
                if (node.isClickable) {
                    return node
                }
            }
        }

        // Recurse into children
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            val found = findNode(child, target)
            if (found != null) return found
        }

        return null
    }

    /**
     * Get the list of packages that have surgical blocking rules.
     */
    fun getSupportedPackages(): Set<String> = surgicalPatterns.keys
}
