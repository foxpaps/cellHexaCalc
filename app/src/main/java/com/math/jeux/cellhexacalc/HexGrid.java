/**
 * Copyright 2013 C. A. Fitzgerald
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.math.jeux.cellhexacalc;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews.RemoteView;

@RemoteView
public class HexGrid extends ViewGroup {

	@SuppressWarnings("unused")
	private static final String TAG = HexGrid.class.getSimpleName();
	
	private int radius = (int) TypedValue
			.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources()
					.getDisplayMetrics());
	private int wCount;
	private int hCount;
	private int widthSegment;
	private int supportedChildern;
	private boolean startIndented = true;
	private int maxColumns = Integer.MAX_VALUE;
	private int maxRows = Integer.MAX_VALUE;

    private int unusableWidth;
    private int unusableHeight;

	public HexGrid(Context context) {
		super(context);
	}

	public HexGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public HexGrid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Hexgrid);

        DisplayMetrics display = getResources().getDisplayMetrics();
                       
        int dim =   (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,48, display);
        
	/*	radius = a.getDimensionPixelOffset(R.styleable.Hexgrid_radius,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
						48, getResources().getDisplayMetrics()));
                                                                          */
		maxColumns = a.getInteger(R.styleable.Hexgrid_max_columns,
				Integer.MAX_VALUE);
		maxRows = a.getInteger(R.styleable.Hexgrid_max_rows, Integer.MAX_VALUE);

		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        hCount = 9;

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		int usableWidth = width - (getPaddingLeft() + getPaddingRight());
		int usableHeight = height - (getPaddingTop() + getPaddingBottom());

        int radiusWidth = (int) (2*usableWidth /(maxColumns*3));

		int radiusHeight = (int) (usableHeight /(maxRows*2));

		radius = Math.min(radiusHeight,radiusWidth);

        unusableWidth = usableWidth - (int)(radius*maxColumns*3/2) -maxColumns*4;
        unusableHeight = usableHeight - radius*maxRows*2;
        widthSegment = (int) (1.5 * radius)+3;

		supportedChildern = 68; // = 8*4 + 9*4 = ...maxColumns* maxRows; //wCount * hCount - (wCount / 2);
//		if (startIndented) {
//			supportedChildern--;
//		}

		// Find out how big everyone wants to be
		//int spec = MeasureSpec.makeMeasureSpec(2 * r, MeasureSpec.AT_MOST);
		int spec = MeasureSpec.makeMeasureSpec(2*radius, MeasureSpec.EXACTLY);
		measureChildren(spec, spec);

		// Check against minimum height and width
		width = Math.max(width, getSuggestedMinimumWidth());
		int h = getSuggestedMinimumHeight();
		height = Math.max(height, getSuggestedMinimumHeight());
		setMeasuredDimension(resolveSize(width, widthMeasureSpec),
				resolveSize(height, heightMeasureSpec));
	}

	@Override
	protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
		int a;
		int b;
		a = b = 0;

		int leftPositionFudge =  getPaddingLeft() + unusableWidth/2 - radius/3;
		int topPositionFudge = getPaddingTop() + unusableHeight/2;

		for (int c = 0; c < supportedChildern; c++) {
			View child = getChildAt(c);
			if (child == null) {
				break;
			}
			if (child.getVisibility() != GONE) {
				int childLeft = a * widthSegment + leftPositionFudge;
				int childTop = b * (int)(2 * (radius-1)) + topPositionFudge;
				if ((a % 2) == 1 ^ startIndented) {
					childTop += radius;
				}
				child.setId(c);
				int mw =child.getMeasuredWidth();
				child.setOnClickListener(ClickOnCellHexa.getInstance());

				child.layout(childLeft, childTop,childLeft + radius*2,	childTop + radius*2);
			}
			b++;
			if ((a % 2) == 0 ^ startIndented) {
				if (b >= hCount) {
					b = 0;
					a++;
				}
			} else {
				if (b >= hCount - 1) {
					b = 0;
					a++;
				}
			}
		}

	}

	@Override
	public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new HexGrid.LayoutParams(getContext(), attrs);
	}

	// Override to allow type-checking of LayoutParams.
	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof HexGrid.LayoutParams;
	}

	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(
			ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	public int getSupportedChildern() {
		return supportedChildern;
	}

	public static class LayoutParams extends ViewGroup.LayoutParams {

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		/**
		 * {@inheritDoc}
		 */
		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}

	}
}
