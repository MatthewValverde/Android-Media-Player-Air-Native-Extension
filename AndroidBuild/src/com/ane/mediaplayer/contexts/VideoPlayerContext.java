package com.ane.mediaplayer.contexts;

import java.util.Map;

import android.content.Intent;
import android.content.res.Configuration;

import com.adobe.air.ActivityResultCallback;
import com.adobe.air.AndroidActivityWrapper;
import com.adobe.air.AndroidActivityWrapper.ActivityState;
import com.adobe.air.StateChangeCallback;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.ane.mediaplayer.functions.VideoPlayerFunction;

import java.util.HashMap;

public class VideoPlayerContext extends FREContext implements ActivityResultCallback, StateChangeCallback
{
	public VideoPlayerFunction vpFunction;

	private AndroidActivityWrapper aaw;

	private Map<String, FREFunction> functionMap;

	public VideoPlayerContext()
	{

	}

	@Override public void dispose()
	{
		disposeOfPlayer();

		if (aaw != null)
		{
			aaw.removeActivityResultListener(this);
			aaw.removeActivityStateChangeListner(this);
			aaw = null;
		}
	}

	private void disposeOfPlayer()
	{
		functionMap.clear();
		
		if (vpFunction != null)
		{
			vpFunction.dispose();
			vpFunction = null;
		}

	}

	private void startNewPlayer()
	{
		functionMap.put("mediaPlayer", vpFunction = new VideoPlayerFunction());
	}

	@Override public Map<String, FREFunction> getFunctions()
	{
		functionMap = new HashMap<String, FREFunction>();
		functionMap.put("mediaPlayer", vpFunction = new VideoPlayerFunction());

		aaw = AndroidActivityWrapper.GetAndroidActivityWrapper();
		aaw.addActivityResultListener(this);
		aaw.addActivityStateChangeListner(this);

		return functionMap;
	}

	@Override public void onActivityResult(int arg0, int arg1, Intent arg2)
	{

	}

	@Override public void onActivityStateChanged(ActivityState state)
	{
		switch (state)
		{
		case STARTED:
			//startNewPlayer();
		case RESTARTED:
			//startNewPlayer();
		case RESUMED:
			//startNewPlayer();
		case PAUSED:
			dispose();
		case STOPPED:
			dispose();
		case DESTROYED:
			dispose();
		}

	}

	@Override public void onConfigurationChanged(Configuration arg0)
	{
		// TODO Auto-generated method stub

	}

}
