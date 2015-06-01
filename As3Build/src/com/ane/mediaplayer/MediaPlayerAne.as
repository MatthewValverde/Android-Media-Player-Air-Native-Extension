package com.ane.mediaplayer
{
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;
	
	public class MediaPlayerAne
	{
		private static var _exContext:ExtensionContext;
		private static var _initialized:Boolean;
		private static var _dispatch:EventDispatcher = new EventDispatcher();
		
		public static function startExtensionContext():void
		{
			exContext = ExtensionContext.createExtensionContext("com.ane.mediaplayer", "");
			exContext.addEventListener(StatusEvent.STATUS, statusHandler);
			initialized = true;
		}
		
		public static function disposeNativeExtension():void
		{
			exContext.removeEventListener(StatusEvent.STATUS, statusHandler);
			exContext.dispose();
			exContext = null;
			initialized = false;
		}
		
		public static function loadAndPlay(path:String):void
		{
			autoPlay(true);
			
			if (!initialized)
			{
				startExtensionContext();
			}
			
			try
			{
				exContext.call("mediaPlayer", "playPath", path);
			}
			catch (e:Error)
			{
				trace(e);
			}
		}
		
		public static function load(path:String):void
		{
			if (!initialized)
			{
				startExtensionContext();
			}
			
			try
			{
				exContext.call("mediaPlayer", "playPath", path);
			}
			catch (e:Error)
			{
				trace(e);
			}
		}
		
		public static function centerOnStage():void
		{
			if (!initialized)
			{
				startExtensionContext();
			}
			
			try
			{
				exContext.call("mediaPlayer", "center", "true");
			}
			catch (e:Error)
			{
				trace(e);
			}
		}
		
		public static function orientationChange():void
		{
			if (!initialized)
			{
				return;
			}
			
			try
			{
				exContext.call("mediaPlayer", "orientationChange", "true");
			}
			catch (e:Error)
			{
				trace(e);
			}
		}
		
		public static function goFullScreen():void
		{
			if (!initialized)
			{
				startExtensionContext();
			}
			
			try
			{
				exContext.call("mediaPlayer", "fullScreen", "true");
			}
			catch (e:Error)
			{
				trace(e);
			}
		}
		
		public static function setRectangle(x:int, y:int, width:int, height:int):void
		{
			if (!initialized)
			{
				startExtensionContext();
			}
			
			var rectString:String = String(x) + "," + String(y) + "," + String(width) + "," + String(height);
			
			try
			{
				exContext.call("mediaPlayer", "rectangle", rectString);
			}
			catch (e:Error)
			{
				trace(e);
			}
		}
		
		public static function setFlashStageDimensions(width:int, height:int):void
		{
			if (!initialized)
			{
				startExtensionContext();
			}
			
			var rectString:String = String(width) + "," + String(height);
			
			try
			{
				exContext.call("mediaPlayer", "stageDimensions", rectString);
			}
			catch (e:Error)
			{
				trace(e);
			}
		}
		
		public static function play():void
		{
			if (!initialized)
			{
				return;
			}
			
			try
			{
				exContext.call("mediaPlayer", "start", "true");
			}
			catch (e:Error)
			{
				trace(e);
			}
		}
		
		public static function pause():void
		{
			if (!initialized)
			{
				return;
			}
			
			try
			{
				exContext.call("mediaPlayer", "pause", "true");
			}
			catch (e:Error)
			{
				trace(e);
			}
		}
		
		public static function getCurrentPosition():void
		{
			if (!initialized)
			{
				return;
			}
			
			try
			{
				exContext.call("mediaPlayer", "getCurrentPosition", "true");
			}
			catch (e:Error)
			{
				trace(e);
			}
		}
				
		public static function autoPlay(value:Boolean):void
		{
			if (!initialized)
			{
				startExtensionContext();
			}
			
			try
			{
				exContext.call("mediaPlayer", "autoPlay", String(value));
			}
			catch (e:Error)
			{
				trace(e);
			}
		}
		
		public static function visible(value:Boolean):void
		{
			if (!initialized)
			{
				startExtensionContext();
			}
			
			try
			{
				exContext.call("mediaPlayer", "visible", String(value));
			}
			catch (e:Error)
			{
				trace(e);
			}
		}
		
		public static function seekTo(position:int):void
		{
			if (!initialized)
			{
				return;
			}
			
			try
			{
				exContext.call("mediaPlayer", "seekTo", String(position));
			}
			catch (e:Error)
			{
				trace(e);
			}
		}
		
		public static function disposeMediaPlayer():void
		{
			if (!initialized)
			{
				return;
			}
			
			try
			{
				exContext.call("mediaPlayer", "dispose", "true");
			}
			catch (e:Error)
			{
				trace(e);
			}
		}
		
		private static function statusHandler(e:StatusEvent):void
		{
			dispatchEvent(e);
		}
		
		public static function addEventListener(p_type:String, p_displayListener:Function, p_useCapture:Boolean = false, p_priority:int = 0, p_useWeakReference:Boolean = false):void
		{
			dispatch.addEventListener(p_type, p_displayListener, p_useCapture, p_priority, p_useWeakReference);
		}
		
		public static function removeEventListener(p_type:String, p_displayListener:Function, p_useCapture:Boolean = false):void
		{
			dispatch.removeEventListener(p_type, p_displayListener, p_useCapture);
		}
		
		public static function dispatchEvent(event:*):void
		{
			dispatch.dispatchEvent(event);
		}
		
		static public function get dispatch():EventDispatcher
		{
			return _dispatch;
		}
		
		static public function set dispatch(value:EventDispatcher):void
		{
			_dispatch = value;
		}
		
		static public function get exContext():ExtensionContext
		{
			return _exContext;
		}
		
		static public function set exContext(value:ExtensionContext):void
		{
			_exContext = value;
		}
		
		static public function get initialized():Boolean
		{
			return _initialized;
		}
		
		static public function set initialized(value:Boolean):void
		{
			_initialized = value;
		}
	}
}