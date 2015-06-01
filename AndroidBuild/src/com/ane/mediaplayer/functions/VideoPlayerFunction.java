package com.ane.mediaplayer.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREObject;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREWrongThreadException;
import com.ane.mediaplayer.activity.MediaPlayerActivity;

import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.app.Activity;

public class VideoPlayerFunction implements FREFunction
{
	private Activity activity;
	private MediaPlayerActivity mediaPlayer;
	FREContext context;
	String videoPath;
	String method;
	String parameters;

	@Override public FREObject call(FREContext frecontext, FREObject[] values)
	{
		context = frecontext;
		activity = context.getActivity();

		try
		{
			method = values[0].getAsString();
		}
		catch (IllegalStateException e)
		{
			frecontext.dispatchStatusEventAsync("MediaPlayerError", e.getMessage());
		}
		catch (FRETypeMismatchException e)
		{
			frecontext.dispatchStatusEventAsync("MediaPlayerError", e.getMessage());
		}
		catch (FREInvalidObjectException e)
		{
			frecontext.dispatchStatusEventAsync("MediaPlayerError", e.getMessage());
		}
		catch (FREWrongThreadException e)
		{
			frecontext.dispatchStatusEventAsync("MediaPlayerError", e.getMessage());
		}

		try
		{
			parameters = values[1].getAsString();
		}
		catch (IllegalStateException e)
		{
			frecontext.dispatchStatusEventAsync("MediaPlayerError", e.getMessage());
		}
		catch (FRETypeMismatchException e)
		{
			frecontext.dispatchStatusEventAsync("MediaPlayerError", e.getMessage());
		}
		catch (FREInvalidObjectException e)
		{
			frecontext.dispatchStatusEventAsync("MediaPlayerError", e.getMessage());
		}
		catch (FREWrongThreadException e)
		{
			frecontext.dispatchStatusEventAsync("MediaPlayerError", e.getMessage());
		}

		if (mediaPlayer == null)
		{
			mediaPlayer = new MediaPlayerActivity(activity);
			mediaPlayer.setFreContext(context);

			LayoutParams layParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			activity.addContentView(mediaPlayer, layParams);
		}

		if (method.equals("playPath"))
		{
			mediaPlayer.startPlay(parameters);
		}

		if (method.equals("fullScreen"))
		{
			mediaPlayer.goFullScreen();
		}

		if (method.equals("rectangle"))
		{
			mediaPlayer.setSurfaceViewRect(parameters);
		}
		
		if (method.equals("stageDimensions"))
		{
			mediaPlayer.setFlashDims(parameters);
		}

		if (method.equals("center"))
		{
			mediaPlayer.centerSurfaceView();
		}
		
		if (method.equals("orientationChange"))
		{
			mediaPlayer.orientationChange();
		}

		if (method.equals("start"))
		{
			mediaPlayer.start();
		}

		if (method.equals("pause"))
		{
			mediaPlayer.pause();
		}
		
		if (method.equals("seekTo"))
		{
			mediaPlayer.seekTo(Integer.parseInt(parameters));
		}
		
		if (method.equals("autoPlay"))
		{
			mediaPlayer.autoPlay(parameters);
		}
		
		if (method.equals("moveToFront"))
		{
			mediaPlayer.start();
		}

		if (method.equals("moveToBack"))
		{
			mediaPlayer.start();
		}
		
		if (method.equals("dispose"))
		{
			dispose();
		}
		
		if (method.equals("visible"))
		{
			mediaPlayer.setVideoVisiblity(parameters);
		}
		
		if (method.equals("getCurrentPosition"))
		{
			context.dispatchStatusEventAsync("MediaPlayerCurrentPosition", String.valueOf(mediaPlayer.getCurrentPosition()));
		}

		context.dispatchStatusEventAsync("MediaPlayer", "Received call - " + method + " with parameters - " + parameters);

		return null;
	}
	
	public void dispose()
	{
		mediaPlayer.completeAndDisposeMediaPlayer();

		ViewGroup vg = (ViewGroup) (mediaPlayer.getParent());
		vg.removeView(mediaPlayer);
		mediaPlayer = null;
	}
}
