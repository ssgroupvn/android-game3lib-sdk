package com.ssgroup.game3sdk;

import java.util.ArrayList;

import com.ssgroup.libgame3.Game3Lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("NewApi")
public class HUDView extends LinearLayout
{

	int count = 0;
	public ArrayList<View> itemList;
	public ArrayList<View> itemList_right;
	public View hint_left;
	View hint_right;
	public View BtnExpand;
	View bg_left;
	View bg_right;
	
	public boolean btnExpandIdle = true;
	
	final int MIN_GAP = 0;
	final int MAX_GAP = 30;
	
	private int mHeight;
	
	private int mWidthList = 0;
	private int mHeightList;

	/**
	 * children will be set the same size.
	 */
	private int mChildSize;

	/* the distance between child Views */
	private int mChildGap = 20;

	/* space to place the switch button */
	public int mHolderWidth;	
	
	/* width screen of device */
	private int Screen_width;
	
	/* expand from left */
	public boolean isLeft = true;

	boolean mExpanded = false;
	
	private boolean mSwitching = false;		
	
	static boolean rotate = false;

	float density;
	
	static float X;
	static float Y;
	
	static float globalX;
	static float globalY;		
	
	static boolean isClick = false;
	
	static public int SCREEN_WIDTH;
	static public int SCREEN_HEIGHT;
	
	private int dpToPx(int dp)
	{
	    //float density = getContext().getResources().getDisplayMetrics().density;
	    return Math.round((float)dp * density);
	}
	
	public HUDView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		density = getContext().getResources().getDisplayMetrics().density;
		
		itemList = new ArrayList<View>();
		itemList_right = new ArrayList<View>();
		
		//LayoutInflater li = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		//li.inflate(R.layout.hud_layout, this, true);
		View.inflate(context, R.layout.hud_layout, this);
		
		BtnExpand = findViewById(R.id.expand);
		
		itemList.add(findViewById(R.id.item1));
		itemList.add(findViewById(R.id.item2));
		itemList.add(findViewById(R.id.item3));
		itemList.add(findViewById(R.id.item4));
		itemList.add(findViewById(R.id.item5));
		
		itemList_right.add(findViewById(R.id.item1_r));
		itemList_right.add(findViewById(R.id.item2_r));
		itemList_right.add(findViewById(R.id.item3_r));
		itemList_right.add(findViewById(R.id.item4_r));
		itemList_right.add(findViewById(R.id.item5_r));
		
		for(int i = 0; i < itemList.size(); i++)
		{
			final String link = Game3Lib.HUDLinks.get(i);
			itemList.get(i).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Toast.makeText(getContext(), "KKKK click", Toast.LENGTH_SHORT).show();
					Page_Activity.link = link;
					Intent i = new Intent(getContext(), Page_Activity.class);
					getContext().startActivity(i);
				}
			});
		}
		
		for(int i = 0; i < itemList_right.size(); i++)
		{
			final String link = Game3Lib.HUDLinks.get(i);
			itemList_right.get(i).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Toast.makeText(getContext(), "KKKK click", Toast.LENGTH_SHORT).show();
					Page_Activity.link = link;
					Intent i = new Intent(getContext(), Page_Activity.class);
					getContext().startActivity(i);
				}
			});
		}
		
		hint_left = findViewById(R.id.hint_left);
		hint_right = findViewById(R.id.hint_right);
		
		bg_left = findViewById(R.id.item_bg_left);
		bg_right = findViewById(R.id.item_bg_right);
		
		mHeight = hint_left.getLayoutParams().height;				
		mHeightList = bg_left.getLayoutParams().height;
		
		hint_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switchState(true);				
			}
		});
		
		hint_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switchState(true);				
			}
		});
				
	}
	
	@Override
	protected int getSuggestedMinimumHeight() {
		return mHeight;
	}

	@Override
	protected int getSuggestedMinimumWidth() {
		int widthHint = hint_left.getLayoutParams().width;
		int widthList = 0;
		if(mExpanded)
		{
			widthList = dpToPx(255);	
			return widthHint + widthList;
		}
		return BtnExpand.getLayoutParams().width + widthList;		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	{		
		//System.out.println("KKKK onMeasure");		
		final int count = getChildCount();
		//mChildGap = computeChildGap(Screen_width - mHolderWidth, count, mChildSize, MIN_GAP, MAX_GAP);
		super.onMeasure(MeasureSpec.makeMeasureSpec(getSuggestedMinimumWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(getSuggestedMinimumHeight(), MeasureSpec.EXACTLY));		
					
		//final int count = getChildCount();		
		//mChildGap = computeChildGap(Screen_width - mHolderWidth, count, mChildSize, MIN_GAP, MAX_GAP);

		/*for (int i = 0; i < itemList.size(); i++) {
			itemList.get(i).measure(MeasureSpec.makeMeasureSpec(itemList.get(i).getMeasuredWidth(), MeasureSpec.EXACTLY), mHeightList);
		}*/			
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) 
	{
		//System.out.println("KKKK onLayout");
		final int paddingLeft = 300;//mHolderWidth;
		//final int childCount = getChildCount();		

		//if(!mSwitching)
		/*{
			for (int i = 0; i < itemList.size(); i++) 
			{
				Rect frame = computeChildFrame(mExpanded, paddingLeft, i, mChildGap, mChildSize);
				itemList.get(i).layout(frame.left, frame.top, frame.right, frame.bottom);				
			}
		}*/
		super.onLayout(changed, l, t, r, b);		
		if(mExpanded)
		{
			bg_left.setBackgroundResource(R.drawable.bg_tab);						
			BtnExpand.setVisibility(View.GONE);			
		}
		else
		{
			bg_left.setBackgroundColor(Color.TRANSPARENT);
			bg_right.setBackgroundColor(Color.TRANSPARENT);
			hint_left.setVisibility(View.GONE);
			hint_right.setVisibility(View.GONE);
			BtnExpand.setVisibility(View.VISIBLE);	
			if(android.os.Build.VERSION.SDK_INT > 10 )
				BtnExpand.setAlpha(1);
			if(btnExpandIdle)
			{
				Task task = new Task();
				task.execute(System.currentTimeMillis());
			}			
			else
			{
				if(android.os.Build.VERSION.SDK_INT > 10 )
					BtnExpand.setAlpha(1);
			}
		}
	}		
	
	/**
	 * switch between expansion and shrinkage
	 * 
	 * @param showAnimation
	 */
	@SuppressLint("NewApi")
	public void switchState(final boolean showAnimation) {
		System.out.println("KKKK width: " + Screen_width);
		
		mSwitching = true;		
		
		if (showAnimation) 
		{						
			final int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) 
			{
				getChildAt(i).setVisibility(View.VISIBLE);
				//bindChildAnimation(getChildAt(i), i, 300);
			}			
		}
		
		mExpanded = !mExpanded;				
		
		if(isLeft)
		{
			bg_left.setBackgroundResource(R.drawable.bg_tab);
			bg_left.setVisibility(View.VISIBLE);
			bg_right.setVisibility(View.GONE);
			hint_left.setVisibility(View.VISIBLE);
			hint_right.setVisibility(View.GONE);			
		}
		else
		{
			bg_right.setBackgroundResource(R.drawable.bg_tab);
			bg_right.setVisibility(View.VISIBLE);
			bg_left.setVisibility(View.GONE);
			hint_right.setVisibility(View.VISIBLE);
			hint_left.setVisibility(View.GONE);			
		}
		
		requestLayout();
		invalidate();				
		
		System.out.println("KKKK after invalidate");
	}					
	
	class Task extends AsyncTask<Long, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(Long... params) {
			// TODO Auto-generated method stub
			while(System.currentTimeMillis() - params[0] < 5000)
			{
				if(btnExpandIdle == false)
				{
					return false;
				}								
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result)
			{
				if(android.os.Build.VERSION.SDK_INT > 10 )
					BtnExpand.setAlpha(0.3f);
			}
		}
	}
}
