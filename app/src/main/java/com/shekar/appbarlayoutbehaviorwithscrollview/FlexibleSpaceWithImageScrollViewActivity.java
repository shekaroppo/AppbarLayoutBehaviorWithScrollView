package com.shekar.appbarlayoutbehaviorwithscrollview;

import android.animation.ValueAnimator;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shekar.appbarlayoutbehaviorwithscrollview.ScrollUtils.ObservableScrollView;
import com.shekar.appbarlayoutbehaviorwithscrollview.ScrollUtils.ObservableScrollViewCallbacks;
import com.shekar.appbarlayoutbehaviorwithscrollview.ScrollUtils.ScrollState;
import com.shekar.appbarlayoutbehaviorwithscrollview.ScrollUtils.ScrollUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FlexibleSpaceWithImageScrollViewActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    @Bind(R.id.image)
    ImageView mImageView;
    @Bind(R.id.scroll)
    ObservableScrollView mScrollView;
    @Bind(R.id.title)
    TextView mTitleView;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.toolbar)
    Toolbar mToolbarView;
    @Bind(R.id.overlay)
    View overlayView;

    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private boolean mFabIsShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


        setSupportActionBar(mToolbarView);
        // Set the padding to match the Status Bar height
        //mToolbarView.setPadding(0, getStatusBarHeight(), 0, 0);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (mToolbarView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //getWindow().setStatusBarColor(Color.TRANSPARENT);
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mToolbarView.setPadding(0, getStatusBarHeight(), 0, 0);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

//        Window window = getWindow();
//
//// clear FLAG_TRANSLUCENT_STATUS flag:
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//// finally change the color
//        window.setStatusBarColor(Color.TRANSPARENT);


        mTitleView.setText(getTitle());
        setTitle(null);


        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mActionBarSize = getActionBarSize() + getStatusBarHeight();

        mScrollView.setScrollViewCallbacks(this);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FlexibleSpaceWithImageScrollViewActivity.this, "FAB is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
        mFab.setScaleX(0);
        mFab.setScaleY(0);

//        Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//            public void onGenerated(Palette palette) {
//                applyPalette(palette);
//            }
//        });

        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                // mScrollView.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize);

                // If you'd like to start from scrollY == 0, don't write like this:
                //mScrollView.scrollTo(0, 0);
                // The initial scrollY is 0, so it won't invoke onScrollChanged().
                // To do this, use the following:
                onScrollChanged(0, false, false);

                // You can also achieve it with the following codes.
                // This causes scroll change from 1 to 0.
                //mScrollView.scrollTo(0, 1);
                //mScrollView.scrollTo(0, 0);
            }
        });
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.primary_dark);
        WindowCompatUtils.setStatusBarcolor(getWindow(), palette.getDarkMutedColor(primaryDark));
    }

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        Log.d("Scroll = ",scrollY+"");
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        if(scrollY>=flexibleRange){
            mToolbarView.setBackgroundColor(getResources().getColor(R.color.primary));
        }
        else{
            mToolbarView.setBackgroundColor(Color.TRANSPARENT);
        }
        int minOverlayTransitionY = mActionBarSize - mImageView.getHeight();
        mImageView.setTranslationY(ScrollUtils.getFloat(-scrollY/2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        // mOverlayView.setAlpha( ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
        // mToolbarView.setAlpha( ScrollUtils.getFloat((float) scrollY / mFlexibleSpaceImageHeight, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        mTitleView.setPivotX(0);
        mTitleView.setPivotY(0);
        mTitleView.setScaleX(scale);
        mTitleView.setScaleY(scale);

        overlayView.setTranslationY(ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));

//        double alpha = (1 - (((double) flexibleRange - (double) scrollY) / (double) flexibleRange)) * 255.0;
//        alpha = alpha < 0 ? 0 : alpha;
//        alpha = alpha > 100 ? 100 : alpha;
//        Log.d("alpha==", alpha + "");
//        float scrollRatio = (float) (alpha / 100);
//        getWindow().setStatusBarColor(getAlphaColor(Color.TRANSPARENT,scrollRatio));


//        Log.d("flexibleRange==", flexibleRange + "");
//        Log.d("scrollY==", scrollY + "");
//        Log.d("scale==", scale + "");
//        Log.d("min==", (flexibleRange - scrollY) / flexibleRange + "");


        double alpha = (1 - (((double) flexibleRange - (double) scrollY) / (double) flexibleRange)) * 255.0;
        alpha = alpha < 0 ? 0 : alpha;
        alpha = alpha > 50 ? 50 : alpha;
        float scrollRatio = (float) (alpha / 50);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(Utils.getAlphaColor(Color.TRANSPARENT, scrollRatio));
//        }


        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY > getStatusBarHeight() ? maxTitleTranslationY - scrollY : getStatusBarHeight();

        mTitleView.setTranslationY(titleTranslationY);

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
            // which causes FAB's OnClickListener not working.
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
            lp.leftMargin = mImageView.getWidth() - mFabMargin - mFab.getWidth();
            lp.topMargin = (int) fabTranslationY;
            mFab.requestLayout();
        } else {
            mFab.setTranslationX(mImageView.getWidth() - mFabMargin - mFab.getWidth());
            mFab.setTranslationY(fabTranslationY);
        }

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }
    }
    private int getAlphaColor(int color, float scrollRatio) {
        return Color.argb((int) (scrollRatio * 100f), Color.red(color), Color.green(color), Color.blue(color));
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void showFab() {
        if (!mFabIsShown) {
            mFab.animate().cancel();
            tintSystemBars( getResources().getColor(R.color.primary_dark),Color.TRANSPARENT);
            overlayView.animate().alpha(0.0f).setDuration(300);
            mFab.animate().scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void tintSystemBars(final int statusBarColor,final int statusBarToColor) {

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Use animation position to blend colors.
                float position = animation.getAnimatedFraction();

                // Apply blended color to the status bar.
                int blended = blendColors(statusBarColor, statusBarToColor, position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(blended);
                }
            }
        });

        anim.setDuration(300).start();
    }

    private int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;
        final float a = Color.alpha(to) * ratio + Color.alpha(from) * inverseRatio;
        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void hideFab() {
        if (mFabIsShown) {
            tintSystemBars(Color.TRANSPARENT, getResources().getColor(R.color.primary_dark));
            mFab.animate().cancel();
            overlayView.animate().alpha(1.0f).setDuration(300);
            mFab.animate().scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }
}
