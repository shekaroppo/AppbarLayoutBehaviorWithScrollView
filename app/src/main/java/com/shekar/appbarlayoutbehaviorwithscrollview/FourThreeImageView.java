package com.shekar.appbarlayoutbehaviorwithscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * A extension of ForegroundImageView that is always 4:3 aspect ratio.
 */
public class FourThreeImageView extends ImageView {

    public FourThreeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int fourThreeHeight = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthSpec) * 3 / 4,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, fourThreeHeight);
    }
}
