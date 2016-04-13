package com.shekar.appbarlayoutbehaviorwithscrollview.scrollutils;

/**
 * Callbacks for Scrollable widgets.
 */
public interface ObservableScrollViewCallbacks {
    /**
     * Called when the scroll change events occurred.
     * This won't be called just after the view is laid out, so if you'd like to
     * initialize the position of your views with this method, you should call this manually
     * or invoke scroll as appropriate.
     *
     * @param scrollY     scroll position in Y axis
     * @param firstScroll true when this is called for the first time in the consecutive motion events
     * @param dragging    true when the view is dragged and false when the view is scrolled in the inertia
     */
    void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging);

    /**
     * Called when the down motion event occurred.
     */
    void onDownMotionEvent();

    /**
     * Called when the dragging ended or canceled.
     *
     * @param scrollState state to indicate the scroll direction
     */
    void onUpOrCancelMotionEvent(ScrollState scrollState);
}
