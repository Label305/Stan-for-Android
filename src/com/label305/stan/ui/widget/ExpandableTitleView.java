package com.label305.stan.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.label305.stan.R;
import com.label305.stan.ui.anim.ExpandViewAnimation;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class ExpandableTitleView extends LinearLayout {

	private static final int ANIMATIONDURATION = 400;

	private View mBannerView;
	private CustomFontTextView mTitleTV;
	private ImageView mIconIV;
	private ImageView mMoreImageButton;
	private ProgressBar mProgressBar;
	private ViewGroup mContentVG;
	private boolean mExpanded = false;
	private int mBackgroundColor;
	private int mBackgroundDrawableResId;
	private int mIconResId;
	private String mText;
	private int mTextColor;
	private String mTextFont;
	private int mTextSize;
	private int mTextGravity;
	private int mArrowResId;
	private boolean mContentVisible;
	private boolean mDataAvailable = true;

	private Context mContext;

	public ExpandableTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_expandabletitle, this);
		setOrientation(VERTICAL);

		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ExpandableTitleView);
		if (a != null) {
			try {
				mBackgroundColor = a.getColor(R.styleable.ExpandableTitleView_background, R.color.transparent);
			} catch (Resources.NotFoundException e) {
				mBackgroundDrawableResId = a.getResourceId(R.styleable.ExpandableTitleView_background, 0);
			}

			mIconResId = a.getResourceId(R.styleable.ExpandableTitleView_icon, 0);
			mText = a.getString(R.styleable.ExpandableTitleView_titleText);
			mTextColor = a.getColor(R.styleable.ExpandableTitleView_titleTextColor, R.color.black);
			mTextFont = a.getString(R.styleable.ExpandableTitleView_titleTextFont);
			mTextSize = a.getDimensionPixelSize(R.styleable.ExpandableTitleView_titleTextSize, mContext.getResources().getDimensionPixelSize(R.dimen.textsize_medium));
			mTextGravity = a.getInt(R.styleable.ExpandableTitleView_titleGravity, Gravity.LEFT);
			mArrowResId = a.getResourceId(R.styleable.ExpandableTitleView_arrow, 0);
			mContentVisible = a.getBoolean(R.styleable.ExpandableTitleView_contentVisible, true);
			mExpanded = mContentVisible;
			a.recycle();

			setupViews();
		}
	}

	private void setupViews() {
		mBannerView = findViewById(R.id.view_expandabletitle_banner);

		if (mBackgroundColor != 0) {
			mBannerView.setBackgroundColor(mBackgroundColor);
		} else {
			mBannerView.setBackgroundResource(mBackgroundDrawableResId);
		}
		mBannerView.setOnClickListener(new BannerOnClickListener());

		mTitleTV = (CustomFontTextView) findViewById(R.id.view_expandabletitle_titletv);
		mTitleTV.setFont(mTextFont);
		mTitleTV.setTextColor(mTextColor);
		mTitleTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
		mTitleTV.setText(mText);
		mTitleTV.setGravity(mTextGravity);

		mIconIV = (ImageView) findViewById(R.id.view_expandabletitle_iconiv);
		mIconIV.setImageResource(mIconResId);

		mMoreImageButton = (ImageView) findViewById(R.id.view_expandabletitle_morebutton);
		if (mArrowResId != 0) {
			mMoreImageButton.setImageResource(mArrowResId);
		} else {
			mMoreImageButton.setImageDrawable(null);
		}

		mProgressBar = (ProgressBar) findViewById(R.id.view_expandabletitle_progressbar);

		mContentVG = (ViewGroup) findViewById(R.id.view_expandabletitle_expandedcontent);

		if (mContentVisible) {
			mContentVG.setVisibility(View.VISIBLE);
			Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.rotatecw90);
			if (animation != null) {
				mMoreImageButton.startAnimation(animation);
			}
		} else {
			mContentVG.setVisibility(View.GONE);
		}
	}

	public void setTitle(String title) {
		mTitleTV.setText(title);
	}

	public void setIcon(int resId) {
		mIconIV.setImageResource(resId);
	}

	public void setIcon(Drawable drawable) {
		mIconIV.setImageDrawable(drawable);
	}

	public void setIcon(Bitmap bitmap) {
		mIconIV.setImageBitmap(bitmap);
	}

	public ViewGroup getContent() {
		return mContentVG;
	}

	public void setContent(View view) {
		mContentVG.removeAllViews();
		mContentVG.addView(view);
	}

	/**
	 * If !mDataAvailable, will show a indeterminate spinner instead of an arrow
	 * until setDataAvailable(true);
	 */
	public void setDataAvailable(boolean dataAvailable) {
		this.mDataAvailable = dataAvailable;

		if (dataAvailable) {
			mProgressBar.setVisibility(View.GONE);
			mMoreImageButton.setVisibility(View.VISIBLE);
		} else {
			mProgressBar.setVisibility(View.VISIBLE);
			mMoreImageButton.setVisibility(View.GONE);
		}
	}

	public boolean hasDataAvailable() {
		return mDataAvailable;
	}

	public void expandContent() {
		final int widthSpec = MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY);
		final int heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		mContentVG.measure(widthSpec, heightSpec);

		ExpandViewAnimation a = new ExpandViewAnimation(mContentVG, ANIMATIONDURATION, ExpandViewAnimation.EXPAND);
		a.setHeight(mContentVG.getMeasuredHeight());
		mContentVG.startAnimation(a);

		mMoreImageButton.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.rotatecw90));
		mExpanded = true;
	}

	public void collapseContent() {
		ExpandViewAnimation a = new ExpandViewAnimation(mContentVG, ANIMATIONDURATION, ExpandViewAnimation.COLLAPSE);
		mContentVG.startAnimation(a);

		mMoreImageButton.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.rotateccw90));
		mExpanded = false;
	}

	@Override
	public void setVisibility(int visibility) {
		if (visibility == View.GONE && getVisibility() != View.GONE) {
			collapseETV();
		} else if (visibility == View.VISIBLE && getVisibility() != View.VISIBLE) {
			expandETV();
		} else {
			super.setVisibility(visibility);
		}
	}

	private void collapseETV() {
		LinearLayout.LayoutParams layoutParams = ((LinearLayout.LayoutParams) getLayoutParams());
		final int origHeight = getHeight();
		final int origTopMargin = layoutParams == null ? 0 : layoutParams.topMargin;
		final int origBottomMargin = layoutParams == null ? 0 : layoutParams.bottomMargin;

		ValueAnimator etvAnimator = ValueAnimator.ofInt(origHeight, 0);
		etvAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				if (getLayoutParams() == null) {
					return;
				}

				LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
				layoutParams.height = (Integer) animator.getAnimatedValue();
				setLayoutParams(layoutParams);
			}
		});

		ValueAnimator topMarginAnimator = ValueAnimator.ofInt(origTopMargin, 0);
		topMarginAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				if (getLayoutParams() == null) {
					return;
				}

				LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
				layoutParams.topMargin = (Integer) animator.getAnimatedValue();
				setLayoutParams(layoutParams);
			}
		});

		ValueAnimator bottomMarginAnimator = ValueAnimator.ofInt(origBottomMargin, 0);
		bottomMarginAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				if (getLayoutParams() == null) {
					return;
				}

				LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
				layoutParams.bottomMargin = (Integer) animator.getAnimatedValue();
				setLayoutParams(layoutParams);
			}
		});

		AnimatorSet set = new AnimatorSet();
		set.playTogether(etvAnimator, topMarginAnimator, bottomMarginAnimator);
		set.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				ExpandableTitleView.super.setVisibility(View.GONE);
				LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
				layoutParams.height = origHeight;
				layoutParams.topMargin = origTopMargin;
				layoutParams.bottomMargin = origBottomMargin;
				setLayoutParams(layoutParams);
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
			}
		});
		set.start();
	}

	private void expandETV() {
		super.setVisibility(View.VISIBLE);

		int origHeight = getLayoutParams().height;
		int origTopMargin = ((LinearLayout.LayoutParams) getLayoutParams()).topMargin;
		int origBottomMargin = ((LinearLayout.LayoutParams) getLayoutParams()).bottomMargin;

		ValueAnimator etvAnimator = ValueAnimator.ofInt(0, origHeight);
		etvAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				if (getLayoutParams() == null) {
					return;
				}

				LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
				layoutParams.height = (Integer) animator.getAnimatedValue();
				setLayoutParams(layoutParams);
			}
		});

		ValueAnimator topMarginAnimator = ValueAnimator.ofInt(0, origTopMargin);
		topMarginAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				if (getLayoutParams() == null) {
					return;
				}

				LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
				layoutParams.topMargin = (Integer) animator.getAnimatedValue();
				setLayoutParams(layoutParams);
			}
		});

		ValueAnimator bottomMarginAnimator = ValueAnimator.ofInt(0, origBottomMargin);
		bottomMarginAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				if (getLayoutParams() == null) {
					return;
				}

				LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
				layoutParams.bottomMargin = (Integer) animator.getAnimatedValue();
				setLayoutParams(layoutParams);
			}
		});

		AnimatorSet set = new AnimatorSet();
		set.playTogether(etvAnimator, topMarginAnimator, bottomMarginAnimator);
		set.start();
	}

	private class BannerOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (mDataAvailable) {
				if (mExpanded) {
					collapseContent();
				} else {
					expandContent();
				}
			}
		}
	}
}