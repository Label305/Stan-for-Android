/*
 * |   _            _          _ ____   ___  _____   |
 * |  | |          | |        | |___ \ / _ \| ____|  |
 * |  | |      __ _| |__   ___| | __) | |_| | |__    |
 * |  | |     / _` | '_ \ / _ \ ||__ <|     |___ \   |
 * |  | |____| (_| | |_) |  __/ |___) |     |___) |  |
 * |  |______|\__,_|_.__/ \___|_|____/ \___/|____/   |
 *
 * @author Nick van den Berg <nick@label305.com>
 *
 * Copyright (c) 2013 Label305. All Right Reserved.
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */

package com.label305.stan.ui.widget.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.StateListDrawable;
import android.test.ActivityUnitTestCase;

import com.label305.stan.ui.widget.SVGImageView;

/**
 * Created by Label305 on 09/04/2014.
 */
public class SVGImageViewTest extends ActivityUnitTestCase<Activity> {

    private SVGImageView mSvgImageView;

    public SVGImageViewTest() {
        super(Activity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Intent intent = new Intent(getInstrumentation().getTargetContext(),
                Activity.class);
        startActivity(intent, null, null);
        Activity mActivity = getActivity();
        mSvgImageView = new SVGImageView(mActivity);
        mSvgImageView.setSVGResource(com.label305.stan.test.R.raw.ic_svg_test_square_red);
    }

    //Convert PictureDrawable to Bitmap
    private Bitmap pictureDrawable2Bitmap(PictureDrawable pictureDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(), pictureDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPicture(pictureDrawable.getPicture());
        return bitmap;
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            return pictureDrawable2Bitmap((PictureDrawable) drawable);
        }
    }

    private Bitmap getBitmapFromImageView() {
        return getBitmapFromDrawable(mSvgImageView.getDrawable());
    }

    public void testSimpleSVGImageView() {
        Bitmap bmp = getBitmapFromImageView();
        int pixel = bmp.getPixel(0, 0);

        assertEquals(Color.RED, pixel);
        assertNotSame(Color.BLACK, pixel);
    }

    public void testBlueSVGImageView() {

        mSvgImageView.setSvgColor(Color.BLUE);

        Bitmap bmp = getBitmapFromImageView();

        int pixel = bmp.getPixel(0, 0);
        int transparentPixel = bmp.getPixel(mSvgImageView.getDrawable().getIntrinsicWidth()-1, 0);

        assertEquals(Color.BLUE, pixel);
        assertNotSame(Color.RED, pixel);
        assertNotSame(Color.BLACK, pixel);
        assertEquals(Color.TRANSPARENT, transparentPixel);
    }

    public void testInvertSVGImageView() {

        mSvgImageView.doInvertSvg();
        Bitmap bmp = getBitmapFromImageView();

        int leftPixel = bmp.getPixel(0, 0);
        int rightPixel = bmp.getPixel(mSvgImageView.getDrawable().getIntrinsicWidth()-1, 0);

        assertEquals(Color.TRANSPARENT, leftPixel);
        assertNotSame(Color.RED, leftPixel);
        assertNotSame(Color.BLACK, leftPixel);

        assertEquals(Color.BLACK, rightPixel);
        assertNotSame(Color.TRANSPARENT, rightPixel);
        assertNotSame(Color.RED, rightPixel);
    }

    public void testInvertBlueSVGImageView() {

        mSvgImageView.doInvertSvg();
        mSvgImageView.setSvgColor(Color.BLUE);
        Bitmap bmp = getBitmapFromImageView();

        int leftPixel = bmp.getPixel(0, 0);
        int rightPixel = bmp.getPixel(mSvgImageView.getDrawable().getIntrinsicWidth()-1, 0);

        assertEquals(Color.TRANSPARENT, leftPixel);
        assertNotSame(Color.RED, leftPixel);
        assertNotSame(Color.BLACK, leftPixel);

        assertEquals(Color.BLUE, rightPixel);
        assertNotSame(Color.BLACK, rightPixel);
        assertNotSame(Color.TRANSPARENT, rightPixel);
    }

    public void testPressableSVGImageView() {

        mSvgImageView.setIsPressable();

        StateListDrawable stateListDrawable = (StateListDrawable) mSvgImageView.getDrawable();

        Bitmap bmp = getBitmapFromDrawable(stateListDrawable.getCurrent());

        int leftPixel = bmp.getPixel(0, 0);
        int rightPixel = bmp.getPixel(mSvgImageView.getDrawable().getIntrinsicWidth()-1, 0);

        assertEquals(Color.BLACK, leftPixel);
        assertNotSame(Color.RED, leftPixel);
        assertNotSame(Color.TRANSPARENT, leftPixel);

        assertEquals(Color.TRANSPARENT, rightPixel);
        assertNotSame(Color.BLACK, rightPixel);
        assertNotSame(Color.RED, rightPixel);

        mSvgImageView.setPressed(true);

        stateListDrawable = (StateListDrawable) mSvgImageView.getDrawable();

        bmp = getBitmapFromDrawable(stateListDrawable.getCurrent());

        leftPixel = bmp.getPixel(0, 0);
        rightPixel = bmp.getPixel(mSvgImageView.getDrawable().getIntrinsicWidth()-1, 0);

        assertEquals(Color.WHITE, leftPixel);
        assertNotSame(Color.RED, leftPixel);
        assertNotSame(Color.TRANSPARENT, leftPixel);

        assertEquals(Color.TRANSPARENT, rightPixel);
        assertNotSame(Color.BLACK, rightPixel);
        assertNotSame(Color.RED, rightPixel);
    }

    public void testPressableColorSVGImageView() {

        mSvgImageView.setSvgColor(Color.BLUE);
        mSvgImageView.setIsPressable();

        StateListDrawable stateListDrawable = (StateListDrawable) mSvgImageView.getDrawable();

        Bitmap bmp = getBitmapFromDrawable(stateListDrawable.getCurrent());

        int leftPixel = bmp.getPixel(0, 0);
        int rightPixel = bmp.getPixel(mSvgImageView.getDrawable().getIntrinsicWidth()-1, 0);

        assertEquals(Color.BLUE, leftPixel);
        assertNotSame(Color.RED, leftPixel);
        assertNotSame(Color.BLUE, leftPixel);
        assertNotSame(Color.TRANSPARENT, leftPixel);

        assertEquals(Color.TRANSPARENT, rightPixel);
        assertNotSame(Color.BLACK, rightPixel);
        assertNotSame(Color.RED, rightPixel);

        mSvgImageView.setPressed(true);

        stateListDrawable = (StateListDrawable) mSvgImageView.getDrawable();

        bmp = getBitmapFromDrawable(stateListDrawable.getCurrent());

        leftPixel = bmp.getPixel(0, 0);
        rightPixel = bmp.getPixel(mSvgImageView.getDrawable().getIntrinsicWidth()-1, 0);

        assertEquals(Color.WHITE, leftPixel);
        assertNotSame(Color.RED, leftPixel);
        assertNotSame(Color.TRANSPARENT, leftPixel);

        assertEquals(Color.TRANSPARENT, rightPixel);
        assertNotSame(Color.BLACK, rightPixel);
        assertNotSame(Color.RED, rightPixel);
    }

    public void testPressableColorsSVGImageView() {

        mSvgImageView.setSvgColor(Color.BLUE);
        mSvgImageView.setPressedSvgColor(Color.GREEN);
        mSvgImageView.setIsPressable();

        StateListDrawable stateListDrawable = (StateListDrawable) mSvgImageView.getDrawable();

        Bitmap bmp = getBitmapFromDrawable(stateListDrawable.getCurrent());

        int leftPixel = bmp.getPixel(0, 0);
        int rightPixel = bmp.getPixel(mSvgImageView.getDrawable().getIntrinsicWidth()-1, 0);

        assertEquals(Color.BLUE, leftPixel);
        assertNotSame(Color.RED, leftPixel);
        assertNotSame(Color.BLUE, leftPixel);
        assertNotSame(Color.TRANSPARENT, leftPixel);

        assertEquals(Color.TRANSPARENT, rightPixel);
        assertNotSame(Color.BLACK, rightPixel);
        assertNotSame(Color.RED, rightPixel);

        mSvgImageView.setPressed(true);

        stateListDrawable = (StateListDrawable) mSvgImageView.getDrawable();

        bmp = getBitmapFromDrawable(stateListDrawable.getCurrent());

        leftPixel = bmp.getPixel(0, 0);
        rightPixel = bmp.getPixel(mSvgImageView.getDrawable().getIntrinsicWidth()-1, 0);

        assertEquals(Color.GREEN, leftPixel);
        assertNotSame(Color.RED, leftPixel);
        assertNotSame(Color.TRANSPARENT, leftPixel);

        assertEquals(Color.TRANSPARENT, rightPixel);
        assertNotSame(Color.BLACK, rightPixel);
        assertNotSame(Color.RED, rightPixel);
    }

    public void testPressableInvertedColorsSVGImageView() {

        mSvgImageView.doInvertSvg();
        mSvgImageView.setSvgColor(Color.BLUE);
        mSvgImageView.setPressedSvgColor(Color.GREEN);
        mSvgImageView.setIsPressable();

        StateListDrawable stateListDrawable = (StateListDrawable) mSvgImageView.getDrawable();

        Bitmap bmp = getBitmapFromDrawable(stateListDrawable.getCurrent());

        int leftPixel = bmp.getPixel(0, 0);
        int rightPixel = bmp.getPixel(mSvgImageView.getDrawable().getIntrinsicWidth()-1, 0);

        assertEquals(Color.BLUE, rightPixel);
        assertNotSame(Color.RED, rightPixel);
        assertNotSame(Color.BLUE, rightPixel);
        assertNotSame(Color.TRANSPARENT, rightPixel);

        assertEquals(Color.TRANSPARENT, leftPixel);
        assertNotSame(Color.BLACK, leftPixel);
        assertNotSame(Color.RED, leftPixel);

        mSvgImageView.setPressed(true);

        stateListDrawable = (StateListDrawable) mSvgImageView.getDrawable();

        bmp = getBitmapFromDrawable(stateListDrawable.getCurrent());

        leftPixel = bmp.getPixel(0, 0);
        rightPixel = bmp.getPixel(mSvgImageView.getDrawable().getIntrinsicWidth()-1, 0);

        assertEquals(Color.GREEN, rightPixel);
        assertNotSame(Color.RED, rightPixel);
        assertNotSame(Color.TRANSPARENT, rightPixel);

        assertEquals(Color.TRANSPARENT, leftPixel);
        assertNotSame(Color.BLACK, leftPixel);
        assertNotSame(Color.RED, leftPixel);
    }
}
