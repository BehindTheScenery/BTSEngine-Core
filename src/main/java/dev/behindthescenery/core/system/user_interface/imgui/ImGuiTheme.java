package dev.behindthescenery.core.system.user_interface.imgui;

import dev.behindthescenery.core.system.rendering.color.RGBA;
import dev.behindthescenery.core.system.rendering.color.SimpleColor;
import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.ImVec4;

import java.util.ArrayList;
import java.util.List;

public class ImGuiTheme {

    private final List<Fragment> colors = new ArrayList<>();
    private final List<Task> tasks = new ArrayList<>();

    public ImGuiTheme() {}

    /**
     * Additional alpha multiplier applied by BeginDisabled(). Multiply over current value of Alpha.
     */
    public ImGuiTheme disabledAlpha(final float value) {
        tasks.add(s -> s.setDisabledAlpha(value));
        return this;
    }

    /**
     * Padding within a window.
     */
    public ImGuiTheme windowPadding(final float valueX, final float valueY) {
        tasks.add(s -> s.setWindowPadding(valueX, valueY));
        return this;
    }

    /**
     * Radius of window corners rounding. Set to 0.0f to have rectangular windows.
     * Large values tend to lead to variety of artifacts and are not recommended.
     */
    public ImGuiTheme windowRounding(final float value) {
        tasks.add(s -> s.setWindowRounding(value));
        return this;
    }

    /**
     * Thickness of border around windows. Generally set to 0.0f or 1.0f. (Other values are not well tested and more CPU/GPU costly).
     */
    public ImGuiTheme windowBorderSize(final float value) {
        tasks.add(s -> s.setWindowBorderSize(value));
        return this;
    }

    /**
     * Minimum window size. This is a global setting. If you want to constrain individual windows, use SetNextWindowSizeConstraints().
     */
    public ImGuiTheme windowMinSize(final float valueX, final float valueY) {
        tasks.add(s -> s.setWindowMinSize(valueX, valueY));
        return this;
    }

    /**
     * Alignment for title bar text. Defaults to (0.0f,0.5f) for left-aligned,vertically centered.
     */
    public ImGuiTheme windowTitleAlign(final float valueX, final float valueY) {
        tasks.add(s -> s.setWindowTitleAlign(valueX, valueY));
        return this;
    }

    /**
     * Side of the collapsing/docking button in the title bar (None/Left/Right). Defaults to ImGuiDir_Left.
     */
    public ImGuiTheme windowMenuButtonPosition(final int value) {
        tasks.add(s -> s.setWindowMenuButtonPosition(value));
        return this;
    }

    /**
     * Radius of child window corners rounding. Set to 0.0f to have rectangular windows.
     */
    public ImGuiTheme childRounding(final float value) {
        tasks.add(s -> s.setChildRounding(value));
        return this;
    }

    /**
     * Thickness of border around child windows. Generally set to 0.0f or 1.0f. (Other values are not well tested and more CPU/GPU costly).
     */
    public ImGuiTheme childBorderSize(final float value) {
        tasks.add(s -> s.setChildBorderSize(value));
        return this;
    }

    /**
     * Radius of frame corners rounding. Set to 0.0f to have rectangular frame (used by most widgets).
     */
    public ImGuiTheme frameRounding(final float value) {
        tasks.add(s -> s.setFrameRounding(value));
        return this;
    }

    /**
     * Radius of popup window corners rounding. (Note that tooltip windows use WindowRounding)
     */
    public ImGuiTheme popupRounding(final float value) {
        tasks.add(s -> s.setPopupRounding(value));
        return this;
    }

    /**
     * Thickness of border around popup/tooltip windows. Generally set to 0.0f or 1.0f. (Other values are not well tested and more CPU/GPU costly).
     */
    public ImGuiTheme popupBorderSize(final float value) {
        tasks.add(s -> s.setPopupBorderSize(value));
        return this;
    }

    /**
     * Padding within a framed rectangle (used by most widgets).
     */
    public ImGuiTheme framePadding(final float valueX, final float valueY) {
        tasks.add(s -> s.setFramePadding(valueX, valueY));
        return this;
    }

    /**
     * Thickness of border around frames. Generally set to 0.0f or 1.0f. (Other values are not well tested and more CPU/GPU costly).
     */
    public ImGuiTheme frameBorderSize(final float value) {
        tasks.add(s -> s.setFrameBorderSize(value));
        return this;
    }

    /**
     * Horizontal and vertical spacing between widgets/lines.
     */
    public ImGuiTheme itemSpacing(final float valueX, final float valueY) {
        tasks.add(s -> s.setItemSpacing(valueX, valueY));
        return this;
    }

    /**
     * Horizontal and vertical spacing between within elements of a composed widget (e.g. a slider and its label).
     */
    public ImGuiTheme itemInnerSpacing(final float valueX, final float valueY) {
        tasks.add(s -> s.setItemInnerSpacing(valueX, valueY));
        return this;
    }

    /**
     * Padding within a table cell. CellPadding.y may be altered between different rows.
     */
    public ImGuiTheme cellPadding(final float valueX, final float valueY) {
        tasks.add(s -> s.setCellPadding(valueX, valueY));
        return this;
    }

    /**
     * Expand reactive bounding box for touch-based system where touch position is not accurate enough. Unfortunately we don't sort widgets so priority on overlap will always be given to the first widget. So don't grow this too much!
     */
    public ImGuiTheme touchExtraPadding(final float valueX, final float valueY) {
        tasks.add(s -> s.setTouchExtraPadding(valueX, valueY));
        return this;
    }

    /**
     * Horizontal indentation when e.g. entering a tree node. Generally == (FontSize + FramePadding.x*2).
     */
    public ImGuiTheme indentSpacing(final float value) {
        tasks.add(s -> s.setIndentSpacing(value));
        return this;
    }

    /**
     * Minimum horizontal spacing between two columns. Preferably {@code >} (FramePadding.x + 1).
     */
    public ImGuiTheme columnsMinSpacing(final float value) {
        tasks.add(s -> s.setColumnsMinSpacing(value));
        return this;
    }

    /**
     * Width of the vertical scrollbar, Height of the horizontal scrollbar.
     */
    public ImGuiTheme scrollbarSize(final float value) {
        tasks.add(s -> s.setScrollbarSize(value));
        return this;
    }

    /**
     * Radius of grab corners for scrollbar.
     */
    public ImGuiTheme scrollbarRounding(final float value) {
        tasks.add(s -> s.setScrollbarRounding(value));
        return this;
    }

    /**
     * Minimum width/height of a grab box for slider/scrollbar.
     */
    public ImGuiTheme grabMinSize(final float value) {
        tasks.add(s -> s.setGrabMinSize(value));
        return this;
    }

    /**
     * Radius of grabs corners rounding. Set to 0.0f to have rectangular slider grabs.
     */
    public ImGuiTheme grabRounding(final float value) {
        tasks.add(s -> s.setGrabRounding(value));
        return this;
    }

    /**
     * The size in pixels of the dead-zone around zero on logarithmic sliders that cross zero.
     */
    public ImGuiTheme logSliderDeadzone(final float value) {
        tasks.add(s -> s.setLogSliderDeadzone(value));
        return this;
    }

    /**
     * Radius of upper corners of a tab. Set to 0.0f to have rectangular tabs.
     */
    public ImGuiTheme tabRounding(final float value) {
        tasks.add(s -> s.setTabRounding(value));
        return this;
    }

    /**
     * Thickness of border around tabs.
     */
    public ImGuiTheme tabBorderSize(final float value) {
        tasks.add(s -> s.setTabBorderSize(value));
        return this;
    }

    /**
     * Minimum width for close button to appear on an unselected tab when hovered. Set to 0.0f to always show when hovering, set to FLT_MAX to never show close button unless selected.
     */
    public ImGuiTheme tabMinWidthForCloseButton(final float value) {
        tasks.add(s -> s.setTabMinWidthForCloseButton(value));
        return this;
    }

    /**
     * Side of the color button in the ColorEdit4 widget (left/right). Defaults to ImGuiDir_Right.
     */
    public ImGuiTheme colorButtonPosition(final int value) {
        tasks.add(s -> s.setColorButtonPosition(value));
        return this;
    }

    /**
     * Side of the color button in the ColorEdit4 widget (left/right). Defaults to ImGuiDir_Right.
     */
    public ImGuiTheme buttonTextAlign(final float valueX, final float valueY) {
        tasks.add(s -> s.setButtonTextAlign(valueX, valueY));
        return this;
    }

    /**
     * Alignment of selectable text. Defaults to (0.0f, 0.0f) (top-left aligned). It's generally important to keep this left-aligned if you want to lay multiple items on a same line.
     */
    public ImGuiTheme selectableTextAlign(final float valueX, final float valueY) {
        tasks.add(s -> s.setSelectableTextAlign(valueX, valueY));
        return this;
    }

    /**
     * Thickkness of border in SeparatorText()
     */
    public ImGuiTheme separatorTextBorderSize(final float value) {
        tasks.add(s -> s.setSeparatorTextBorderSize(value));
        return this;
    }

    /**
     * Alignment of text within the separator. Defaults to (0.0f, 0.5f) (left aligned, center).
     */
    public ImGuiTheme separatorTextAlign(final float valueX, final float valueY) {
        tasks.add(s -> s.setSeparatorTextAlign(valueX, valueY));
        return this;
    }

    /**
     * Horizontal offset of text from each edge of the separator + spacing on other axis. Generally small values. .y is recommended to be == FramePadding.y.
     */
    public ImGuiTheme separatorTextPadding(final float valueX, final float valueY) {
        tasks.add(s -> s.setSeparatorTextPadding(valueX, valueY));
        return this;
    }

    /**
     * Window position are clamped to be visible within the display area by at least this amount. Only applies to regular windows.
     */
    public ImGuiTheme displayWindowPadding(final float valueX, final float valueY) {
        tasks.add(s -> s.setDisplayWindowPadding(valueX, valueY));
        return this;
    }

    /**
     * If you cannot see the edges of your screen (e.g. on a TV) increase the safe area padding. Apply to popups/tooltips as well regular windows. NB: Prefer configuring your TV sets correctly!
     */
    public ImGuiTheme displaySafeAreaPadding(final float valueX, final float valueY) {
        tasks.add(s -> s.setDisplaySafeAreaPadding(valueX, valueY));
        return this;
    }

    /**
     * Thickness of resizing border between docked windows
     */
    public ImGuiTheme dockingSeparatorSize(final float value) {
        tasks.add(s -> s.setDockingSeparatorSize(value));
        return this;
    }

    /**
     * Scale software rendered mouse cursor (when io.MouseDrawCursor is enabled). May be removed later.
     */
    public ImGuiTheme mouseCursorScale(final float value) {
        tasks.add(s -> s.setMouseCursorScale(value));
        return this;
    }

    /**
     * Enable anti-aliased lines/borders. Disable if you are really tight on CPU/GPU. Latched at the beginning of the frame (copied to ImDrawList).
     */
    public ImGuiTheme antiAliasedLines(final boolean value) {
        tasks.add(s -> s.setAntiAliasedLines(value));
        return this;
    }

    /**
     * Enable anti-aliased lines/borders using textures where possible. Require backend to render with bilinear filtering (NOT point/nearest filtering). Latched at the beginning of the frame (copied to ImDrawList).
     */
    public ImGuiTheme antiAliasedLinesUseTex(final boolean value) {
        tasks.add(s -> s.setAntiAliasedLinesUseTex(value));
        return this;
    }

    /**
     * Enable anti-aliased edges around filled shapes (rounded rectangles, circles, etc.). Disable if you are really tight on CPU/GPU. Latched at the beginning of the frame (copied to ImDrawList).
     */
    public ImGuiTheme antiAliasedFill(final boolean value) {
        tasks.add(s -> s.setAntiAliasedFill(value));
        return this;
    }

    /**
     * Tessellation tolerance when using PathBezierCurveTo() without a specific number of segments. Decrease for highly tessellated curves (higher quality, more polygons), increase to reduce quality.
     */
    public ImGuiTheme curveTessellationTol(final float value) {
        tasks.add(s -> s.setCurveTessellationTol(value));
        return this;
    }

    /**
     * Maximum error (in pixels) allowed when using AddCircle()/AddCircleFilled() or drawing rounded corner rectangles with no explicit segment count specified. Decrease for higher quality but more geometry.
     */
    public ImGuiTheme circleTessellationMaxError(final float value) {
        tasks.add(s -> s.setCircleTessellationMaxError(value));
        return this;
    }

    /**
     * Delay for IsItemHovered(ImGuiHoveredFlags_Stationary). Time required to consider mouse stationary.
     */
    public ImGuiTheme hoverStationaryDelay(final float value) {
        tasks.add(s -> s.setHoverStationaryDelay(value));
        return this;
    }

    /**
     * Delay for IsItemHovered(ImGuiHoveredFlags_DelayShort). Usually used along with HoverStationaryDelay.
     */
    public ImGuiTheme hoverDelayShort(final float value) {
        tasks.add(s -> s.setHoverDelayShort(value));
        return this;
    }

    /**
     * Delay for IsItemHovered(ImGuiHoveredFlags_DelayNormal).
     */
    public ImGuiTheme hoverDelayNormal(final float value) {
        tasks.add(s -> s.setHoverDelayNormal(value));
        return this;
    }

    /**
     * Default flags when using IsItemHovered(ImGuiHoveredFlags_ForTooltip) or BeginItemTooltip()/SetItemTooltip() while using mouse.
     */
    public ImGuiTheme hoverFlagsForTooltipMouse(final int value) {
        tasks.add(s -> s.setHoverFlagsForTooltipMouse(value));
        return this;
    }

    /**
     * Default flags when using IsItemHovered(ImGuiHoveredFlags_ForTooltip) or BeginItemTooltip()/SetItemTooltip() while using keyboard/gamepad.
     */
    public ImGuiTheme hoverFlagsForTooltipNav(final int value) {
        tasks.add(s -> s.setHoverFlagsForTooltipMouse(value));
        return this;
    }

    public ImGuiTheme scaleAllSizes(final float scaleFactor) {
        tasks.add(s -> s.scaleAllSizes(scaleFactor));
        return this;
    }

    public ImGuiTheme color(int imGuiCol, int r, int g, int b, int a) {
        this.colors.add(new Fragment(imGuiCol, RGBA.of(r, g, b ,a)));
        return this;
    }

    public ImGuiTheme color(int imGuiCol, float r, float g, float b, float a) {
        this.colors.add(new Fragment(imGuiCol, RGBA.of(r, g, b ,a)));
        return this;
    }

    public ImGuiTheme color(int imGuiCol, ImVec4 color) {
        this.colors.add(new Fragment(imGuiCol, RGBA.of(color.x, color.y, color.z , color.w)));
        return this;
    }

    public ImGuiTheme color(int imGuiCol, SimpleColor color) {
        this.colors.add(new Fragment(imGuiCol, color));
        return this;
    }

    public void apply() {
        ImGuiStyle style = ImGui.getStyle();
        tasks.forEach(s -> s.accept(style));
        for (Fragment color : colors) {
            final SimpleColor col = color.simpleColor;
            style.setColor(color.flag, col.getRedF(), col.getGreenF(), col.getBlueF(), col.getAlphaF());
        }
    }

    private record Fragment(int flag, SimpleColor simpleColor) {
    }

    private interface Task {
        void accept(ImGuiStyle style);
    }
}
