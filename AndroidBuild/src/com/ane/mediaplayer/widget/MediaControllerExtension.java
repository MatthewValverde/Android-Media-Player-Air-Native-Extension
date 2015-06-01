package com.ane.mediaplayer.widget;

import com.adobe.fre.FREContext;
import com.ane.mediaplayer.R;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;

public class MediaControllerExtension extends MediaController
{
	private ImageView fullScreenBtn;
	private Context mContext;
	private FREContext frecontext;

	private MediaController thisValue;

	public int heightValue = 0;

	public MediaControllerExtension(Context context)
	{
		super(context);

		mContext = context;
	}

	public MediaControllerExtension(Context context, FREContext frecontext)
	{
		super(context);

		mContext = context;

		this.frecontext = frecontext;
	}

	@Override public void setAnchorView(View view)
	{
	}

	public void onSizeChanged(View view)
	{
		
		
	}

	public void setCustomAnchorView(View view)
	{
		super.setAnchorView(view);

		createFullScreenBtn();
	}

	private void createFullScreenBtn()
	{
		//thisValue = this;

		FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		frameParams.gravity = Gravity.RIGHT | Gravity.TOP;

		View v = createFullScreenView();
		addView(v, frameParams);

	}

	private View createFullScreenView()
	{
		fullScreenBtn = new ImageView(mContext);
		fullScreenBtn.setImageResource(R.drawable.full_screen);

		fullScreenBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (frecontext != null)
				{
					frecontext.dispatchStatusEventAsync("MediaPlayer", "FULLSCREEN");
				}
			}
		});

		return fullScreenBtn;
	}

	@Override public void show()
	{
		super.show();

		setVisibility(VISIBLE);
	}

	@Override public void hide()
	{
		setVisibility(INVISIBLE);
	}

	public void finishAndHide()
	{
		super.hide();
	}

	/*
	 * @Override protected void onSizeChanged(int xNew, int yNew, int xOld, int
	 * yOld) { super.onSizeChanged(xNew, yNew, xOld, yOld);
	 * 
	 * RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)
	 * anchorView.getLayoutParams(); lp.setMargins(0, 0, 0, yNew);
	 * 
	 * if (anchorView != null) { anchorView.setLayoutParams(lp);
	 * anchorView.requestLayout(); }
	 * 
	 * }
	 */
}
