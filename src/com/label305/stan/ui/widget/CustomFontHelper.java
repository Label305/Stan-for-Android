package com.label305.stan.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.label305.stan.R;
import com.label305.stan.utils.StringUtils;

public class CustomFontHelper {
	
	public static void init(CustomFontInterface cfi, AttributeSet attrs){
		setFont(cfi,attrs);
		setCapitalize(cfi,attrs);
		setLowercase(cfi,attrs);
		setText(cfi,attrs);
		cfi.setIncludeFontPadding(false);
	}
	
	private static  void setFont(CustomFontInterface cfi, AttributeSet attrs) {
		final TypedArray a = cfi.getContext().obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
		String font = a.getString(R.styleable.CustomFontTextView_font);
		if (!StringUtils.isNullOrEmpty(font)) {
			cfi.setTypeface(Typeface.createFromAsset(cfi.getContext().getAssets(), font));
		} else {
			System.err.println("WARNING: No font specified for CustomFontInterface!");
//			new RuntimeException().printStackTrace();
		}
		a.recycle();
	}

	private static void setCapitalize(CustomFontInterface cfi, AttributeSet attrs) {
		final TypedArray a = cfi.getContext().obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
		cfi.setShouldCapitalize(a.getBoolean(R.styleable.CustomFontTextView_capitalize, false));
		a.recycle();
	}

	private static void setLowercase(CustomFontInterface cfi, AttributeSet attrs) {
		final TypedArray a = cfi.getContext().obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
		cfi.setShouldLowercase(a.getBoolean(R.styleable.CustomFontTextView_lowercase, false));
		a.recycle();
	}

	private static void setText(CustomFontInterface cfi, AttributeSet attrs) {
		final TypedArray a = cfi.getContext().obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
		CharSequence text = a.getText(R.styleable.CustomFontTextView_text);
		if (text != null)
			cfi.	setText(text.toString());
		a.recycle();
	}
	
	public interface CustomFontInterface{
		public void setTypeface(Typeface tf);
		
		public void setIncludeFontPadding(boolean include);
		
		public void setShouldLowercase(boolean shouldLowercase);
		
		public void setShouldCapitalize(boolean shouldCapitalize);
		
		public Context getContext();
		
		public void setText(String text);
		
	}
}
