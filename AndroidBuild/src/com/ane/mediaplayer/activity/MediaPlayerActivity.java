package com.ane.mediaplayer.activity;

import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.MediaController.MediaPlayerControl;

import com.adobe.fre.FREContext;
import com.ane.mediaplayer.R;
import com.ane.mediaplayer.widget.MediaControllerExtension;

public class MediaPlayerActivity extends RelativeLayout implements SurfaceHolder.Callback, MediaPlayerControl, OnPreparedListener, OnCompletionListener
{
	private static final int THRESHOLD = 15;
	private static int CENTER_SURFACE_VIEW = -1000000;
	protected Activity activity;
	private MediaPlayer mediaPlayer;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	FREContext frecontext;
	private MediaControllerExtension mcontroller;
	private Handler handler = new Handler();
	private int surfaceWidth = 0;
	private int surfaceHeight = 0;
	private FrameLayout controllerAnchor;
	private FrameLayout backgroundCover;
	private int androidStageWidth = 0;
	private int androidStageHeight = 0;

	private int flashStageWidth = 0;
	private int flashStageHeight = 0;
	private String surfaceViewRectStringValue;
	OrientationEventListener orientationListener;
	private Boolean isCentered = false;
	private Boolean isFullScreen = true;
	Display display;
	private Boolean isAutoPlay = false;
	private Boolean playerIsVisible = true;

	public MediaPlayerActivity(Context context)
	{
		super(context);

		activity = (Activity) context;

		activity.setTheme(R.style.AppThemeDark);

		Window window = activity.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		display = activity.getWindowManager().getDefaultDisplay();
		androidStageWidth = display.getWidth();
		androidStageHeight = display.getHeight();

		createUI();
	}

	public MediaPlayerActivity(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public MediaPlayerActivity(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void setFreContext(FREContext frecontext)
	{
		this.frecontext = frecontext;
	}

	public void createUI()
	{
		inflate(R.layout.movie_player);
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		backgroundCover = (FrameLayout) findViewById(R.id.backgroundCover);
		controllerAnchor = (FrameLayout) findViewById(R.id.controllerAnchor);

		moveToFront();

		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);

		surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);

		surfaceView.setOnClickListener(ClickListener);
	}

	View.OnClickListener ClickListener = new View.OnClickListener()
	{
		@Override public void onClick(View view)
		{
			if (mcontroller != null)
			{
				if (playerIsVisible)
				{
					if (!isFullScreen)
					{
						new Handler().postDelayed(new Runnable()
						{
							@Override public void run()
							{
								mcontroller.show();
							}
						}, 1500);
					}
					else
					{
						mcontroller.show();
					}
				}
				else
				{
					mcontroller.hide();
				}
			}

			if (frecontext != null)
			{
				frecontext.dispatchStatusEventAsync("MediaPlayer", "ON_TOUCH_DOWN");
			}
		}
	};

	public void getMediaControllerHeight()
	{
		new Handler().postDelayed(new Runnable()
		{
			@Override public void run()
			{
				if (mcontroller.getHeight() > 0)
				{
					orientationChange();
				}
				else
				{
					getMediaControllerHeight();
				}
			}
		}, 100);
	}

	public void startPlay(String mediaPlayerPath)
	{
		if (mediaPlayerPath == null)
		{
			return;
		}

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this);

		mcontroller = new MediaControllerExtension(activity, frecontext);

		try
		{
			mediaPlayer.setDataSource(mediaPlayerPath);
		}
		catch (IllegalArgumentException e)
		{
			if (frecontext != null)
			{
				frecontext.dispatchStatusEventAsync("MediaPlayerError", e.getMessage());
			}
		}
		catch (SecurityException e)
		{
			if (frecontext != null)
			{
				frecontext.dispatchStatusEventAsync("MediaPlayerError", e.getMessage());
			}
		}
		catch (IllegalStateException e)
		{
			if (frecontext != null)
			{
				frecontext.dispatchStatusEventAsync("MediaPlayerError", e.getMessage());
			}
		}
		catch (IOException e)
		{
			if (frecontext != null)
			{
				frecontext.dispatchStatusEventAsync("MediaPlayerError", e.getMessage());
			}
		}
	}

	public void orientationChange()
	{
		if (mediaPlayer == null)
		{
			return;
		}

		androidStageWidth = display.getWidth();
		androidStageHeight = display.getHeight();

		if (!isFullScreen)
		{
			boolean centerSurfaceView = isCentered;

			if (surfaceViewRectStringValue != null)
			{
				setSurfaceViewRect(surfaceViewRectStringValue);

				if (centerSurfaceView)
				{
					centerSurfaceView();
				}
			}
		}
		else
		{
			goFullScreen();
		}

		if (frecontext != null)
		{
			frecontext.dispatchStatusEventAsync("MediaPlayer", "ORIENTATION_CHANGE");
		}
	}

	public void setFlashDims(String value)
	{
		String[] split = value.split(",");
		flashStageWidth = Integer.parseInt(split[0]);
		flashStageHeight = Integer.parseInt(split[1]);

		orientationChange();
	}

	public void setVideoVisiblity(String value)
	{
		/*
		 * if(value.equals("true")) { surfaceView.setVisibility(VISIBLE);
		 * playerIsVisible = true; if(isFullScreen) {
		 * backgroundCover.setVisibility(VISIBLE); }
		 * 
		 * surfaceHolder = surfaceView.getHolder();
		 * surfaceHolder.addCallback(this); } else {
		 * surfaceView.setVisibility(INVISIBLE);
		 * backgroundCover.setVisibility(INVISIBLE); playerIsVisible = false;
		 * mcontroller.hide(); }
		 */
	}

	public void setSurfaceViewRect(String value)
	{
		isCentered = false;
		isFullScreen = false;

		backgroundCover.setVisibility(INVISIBLE);

		surfaceViewRectStringValue = value;

		String[] split = value.split(",");

		int x = Integer.parseInt(split[0]);
		int y = Integer.parseInt(split[1]);
		int width = Integer.parseInt(split[2]);
		int height = Integer.parseInt(split[3]);
		surfaceWidth = width;
		surfaceHeight = height;
		LayoutParams layParams = new LayoutParams(surfaceWidth, surfaceHeight);
		layParams.setMargins(x, y, 0, 0);
		surfaceView.setLayoutParams(layParams);

		LayoutParams layParamsAnchor = (LayoutParams) controllerAnchor.getLayoutParams();
		layParamsAnchor.height = 1;// surfaceHeight - mcontroller.getHeight();
		layParamsAnchor.width = 1;
		// layParamsAnchor.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		// layParamsAnchor.setMargins(0, y, 0, 0);
		layParamsAnchor.setMargins(0, 0, 0, 0);
		controllerAnchor.setLayoutParams(layParamsAnchor);

		setMediaControllerRect(x, y);
	}

	public void centerSurfaceView()
	{
		isCentered = true;
		isFullScreen = false;
		backgroundCover.setVisibility(INVISIBLE);

		LayoutParams layParams = new LayoutParams(surfaceWidth, surfaceHeight);
		layParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		surfaceView.setLayoutParams(layParams);

		// LayoutParams surfaceViewParams = (LayoutParams)
		// surfaceView.getLayoutParams();

		LayoutParams layParamsAnchor = (LayoutParams) controllerAnchor.getLayoutParams();
		layParamsAnchor.height = 1;// surfaceHeight - mcontroller.getHeight();
		layParamsAnchor.width = 1;
		// layParamsAnchor.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		// layParamsAnchor.setMargins(0, (androidStageHeight - surfaceHeight) /
		// 2, 0, 0);
		layParamsAnchor.setMargins(0, 0, 0, 0);
		controllerAnchor.setLayoutParams(layParamsAnchor);

		setMediaControllerRect(CENTER_SURFACE_VIEW, CENTER_SURFACE_VIEW);
	}

	public void goFullScreen()
	{
		isFullScreen = true;

		backgroundCover.setVisibility(VISIBLE);

		setVideoToCorrectApectRatio();
	}

	public void autoPlay(String value)
	{
		isAutoPlay = Boolean.parseBoolean(value);
	}

	public void setMediaControllerRect(int x, int y)
	{
		if (mediaPlayer == null)
		{
			return;
		}

		int stageWidth = androidStageWidth;
		int stageHeight = androidStageHeight;

		if (flashStageWidth > 0 && flashStageHeight > 0)
		{
			stageWidth = flashStageWidth;
			stageHeight = flashStageHeight;
		}

		if (x == CENTER_SURFACE_VIEW && y == CENTER_SURFACE_VIEW)
		{
			x = (stageWidth - surfaceWidth) / 2;
			y = (stageHeight - surfaceHeight) / 2;
		}

		int rightMargin = stageWidth - (x + surfaceWidth);
		int bottomMargin = stageHeight - (y + surfaceHeight);

		// mcontroller.setPadding(x, 0, rightMargin, bottomMargin);
		mcontroller.setPadding(x, 0, rightMargin, 0);
	}

	public void moveToFront()
	{
		this.bringToFront();
		surfaceView.setZOrderOnTop(true);
	}

	public void moveToBack()
	{
		surfaceView.setZOrderOnTop(false);
		pause();
	}

	public void completeAndDisposeMediaPlayer()
	{
		if (mediaPlayer != null)
		{
			mediaPlayer.release();
			mediaPlayer = null;

			if (frecontext != null)
			{
				frecontext.dispatchStatusEventAsync("MediaPlayer", "RELEASED");
			}
		}

		if (mcontroller != null)
		{
			mcontroller.finishAndHide();
			mcontroller.removeAllViews();
			mcontroller = null;
		}

		if (surfaceHolder != null)
		{
			surfaceHolder.removeCallback(this);
			surfaceHolder = null;
		}

		ViewGroup vg = (ViewGroup) (surfaceView.getParent());

		if (surfaceView != null)
		{
			vg.removeView(surfaceView);
			surfaceView = null;
		}

		vg.removeAllViews();

		if (frecontext != null)
		{
			frecontext.dispatchStatusEventAsync("MediaPlayer", "DISPOSED");
		}
	}

	protected void inflate(int resource)
	{
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(resource, this, true);
	}

	public void onPrepared(MediaPlayer mediaplayer)
	{
		mediaPlayer.getVideoHeight();
		mediaPlayer.getVideoWidth();

		mcontroller.setMediaPlayer(this);
		mcontroller.setCustomAnchorView(controllerAnchor);

		if (surfaceViewRectStringValue != null)
		{
			setSurfaceViewRect(surfaceViewRectStringValue);
		}
		else
		{
			goFullScreen();
		}

		getMediaControllerHeight();

		handler.post(new Runnable()
		{
			public void run()
			{
				try
				{
					mcontroller.setEnabled(true);
					mcontroller.show();
				}
				catch (Exception e)
				{
					if (frecontext != null)
					{
						frecontext.dispatchStatusEventAsync("MediaPlayerError", e.getMessage());
					}
				}
			}
		});
	}

	@Override public boolean onTouchEvent(MotionEvent event)
	{
		/*
		 * if (mcontroller != null) { if(playerIsVisible) { mcontroller.show();
		 * } else { mcontroller.hide(); } }
		 * 
		 * if (frecontext != null) {
		 * frecontext.dispatchStatusEventAsync("MediaPlayer", "ON_TOUCH_DOWN");
		 * }
		 */

		return false;
	}

	public void onCompletion(MediaPlayer mp)
	{
		seekTo(0);

		pause();

		if (frecontext != null)
		{
			frecontext.dispatchStatusEventAsync("MediaPlayer", "COMPLETE");
		}

		// completeAndDisposeMediaPlayer();
	}

	@Override public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{
	}

	@Override public void surfaceCreated(SurfaceHolder holder)
	{
		try
		{
			mediaPlayer.setDisplay(holder);
			mediaPlayer.setScreenOnWhilePlaying(true);

			mediaPlayer.prepare();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

			if (isAutoPlay)
			{
				mediaPlayer.start();
			}
			else
			{
				seekTo(0);

				if (frecontext != null)
				{
					frecontext.dispatchStatusEventAsync("MediaPlayer", "PREPARED");
				}
			}
		}
		catch (Exception e)
		{
			if (frecontext != null)
			{
				frecontext.dispatchStatusEventAsync("MediaPlayerError", e.getMessage());
			}
		}
	}

	@Override public void surfaceDestroyed(SurfaceHolder arg0)
	{
		// surfaceHolder.removeCallback(this);

		if (mediaPlayer != null)
		{
			mediaPlayer.release();

			if (frecontext != null)
			{
				frecontext.dispatchStatusEventAsync("MediaPlayer", "RELEASED");
			}
		}

		if (mcontroller != null)
		{
			mcontroller.finishAndHide();
			mcontroller.removeAllViews();
			mcontroller = null;
		}
	}

	@Override public boolean canPause()
	{
		return true;
	}

	@Override public boolean canSeekBackward()
	{
		return true;
	}

	@Override public boolean canSeekForward()
	{
		return true;
	}

	@Override public int getAudioSessionId()
	{
		return 0;
	}

	@Override public int getBufferPercentage()
	{
		return 0;
	}

	@Override public int getCurrentPosition()
	{
		if (mediaPlayer == null)
		{
			return 0;
		}
		else
		{
			return mediaPlayer.getCurrentPosition();
		}
	}

	@Override public int getDuration()
	{
		if (mediaPlayer == null)
		{
			return 0;
		}
		else
		{
			return mediaPlayer.getDuration();
		}
	}

	@Override public boolean isPlaying()
	{
		if (mediaPlayer == null)
		{
			return false;
		}
		else
		{
			return mediaPlayer.isPlaying();
		}
	}

	@Override public void pause()
	{
		if (mediaPlayer != null)
		{
			mediaPlayer.pause();

			if (frecontext != null)
			{
				frecontext.dispatchStatusEventAsync("MediaPlayer", "PAUSED");
			}
		}
	}

	@Override public void seekTo(int pos)
	{
		if (mediaPlayer != null)
		{
			mediaPlayer.seekTo(pos);

			if (frecontext != null)
			{
				frecontext.dispatchStatusEventAsync("MediaPlayerSeeked", String.valueOf(pos));
			}
		}
	}

	@Override public void start()
	{
		if (mediaPlayer != null)
		{
			mediaPlayer.start();

			if (frecontext != null)
			{
				frecontext.dispatchStatusEventAsync("MediaPlayer", "STARTED");
			}
		}
	}

	private void setVideoToCorrectApectRatio()
	{
		int videoWidth = mediaPlayer.getVideoWidth();
		int videoHeight = mediaPlayer.getVideoHeight();
		float videoProportion = (float) videoWidth / (float) videoHeight;

		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		float screenProportion = (float) screenWidth / (float) screenHeight;
		int width = 0;
		int height = 0;

		if (videoProportion > screenProportion)
		{
			width = screenWidth;
			height = (int) ((float) screenWidth / videoProportion);
		}
		else
		{
			width = (int) (videoProportion * (float) screenHeight);
			height = screenHeight;
		}

		LayoutParams layParams = new LayoutParams(width, height);
		layParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		surfaceView.setLayoutParams(layParams);

		LayoutParams layParamsAnchor = (LayoutParams) controllerAnchor.getLayoutParams();
		layParamsAnchor.height = androidStageHeight;
		layParamsAnchor.width = LayoutParams.MATCH_PARENT;
		layParamsAnchor.setMargins(0, 0, 0, 0);
		controllerAnchor.setLayoutParams(layParamsAnchor);

		mcontroller.setPadding(0, 0, 0, 0);
	}
}
