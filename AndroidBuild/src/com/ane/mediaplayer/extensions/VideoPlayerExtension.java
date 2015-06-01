package com.ane.mediaplayer.extensions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;
import com.ane.mediaplayer.contexts.VideoPlayerContext;

public class VideoPlayerExtension implements FREExtension
{
	public static VideoPlayerContext context;

	@Override public FREContext createContext(String arg0)
	{
		return context = new VideoPlayerContext();
	}

	@Override public void dispose()
	{
		context.dispose();
		context = null;
	}

	@Override public void initialize()
	{
	}
}
