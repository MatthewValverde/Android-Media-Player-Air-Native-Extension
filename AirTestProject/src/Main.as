package
{
	import flash.events.Event;
	import flash.display.Sprite;
	import flash.display.StageAlign;
	import flash.display.StageScaleMode;
	import flash.events.MouseEvent;
	import flash.filesystem.File;
	import flash.text.TextField;
	import flash.events.StatusEvent;
	import fl.controls.Button;
	
	import com.ane.mediaplayer.MediaPlayerAne;
	
	public class Main extends Sprite
	{
		private var videoName:String = "test.mp4";
		private var path:String;
		private var txt:TextField;
		private var isPlaying:Boolean;
		private var buttonIsClicked:Boolean;
		private var fullscreenClicked:Boolean;
		private var button:Button
		private var playerIsVisible:Boolean = true;
		private var buttonBig:Button
		
		public function Main():void
		{
			stage.scaleMode = StageScaleMode.NO_SCALE;
			stage.align = StageAlign.TOP_LEFT;
			
			createUI();
		}
		
		private function createUI():void
		{
			
			var buttonBig2:Button = new Button();
			buttonBig2.label = " ";
			buttonBig2.width = stage.stageWidth;
			buttonBig2.height = stage.stageHeight;
			addChild(buttonBig2);
			
			buttonBig = new Button();
			buttonBig.label = "Get Position and Stop";
			buttonBig.width = 300;
			buttonBig.height = 60;
			buttonBig.x = 400;
			buttonBig.y = 20;
			//addChild(buttonBig);
			buttonBig.addEventListener(MouseEvent.CLICK, onBtnBigClick);
			buttonBig.visible = false;
			
			button = new Button();
			button.label = "Click to Play";
			button.width = 200;
			button.height = 60;
			button.x = 20;
			button.y = 20;
			addChild(button);
			button.addEventListener(MouseEvent.CLICK, onBtnClick);
			
			txt = new TextField();
			txt.y = button.height + button.y + 20;
			txt.x = 20;
			txt.width = 500;
			txt.height = stage.stageHeight - txt.y;
			txt.multiline = true;
			txt.wordWrap = true;
			txt.textColor = 0x666666;
			addChild(txt);
			txt.mouseEnabled = false;
			
			startMediaPlayer();
		}
		
		private function startMediaPlayer():void
		{
			var file:File = File.applicationDirectory.resolvePath(videoName);
			var fileTo:File = File.applicationStorageDirectory.resolvePath(videoName);
			
			if (!fileTo.exists)
			{
				file.copyTo(fileTo, true);
				txt.text = "Copying the video '" + file.name + "' to local storage.";
			}
			else
			{
				txt.text = "Video file '" + fileTo.name + "' already exists in local storage.";
			}
			
			path = fileTo.nativePath;
			
			MediaPlayerAne.addEventListener(StatusEvent.STATUS, statusUpdate);
			
			// You can add you own resize event to the stage and call the MediaPlayerAne.orientationChange() from there.
			// You must have stage.scaleMode = StageScaleMode.NO_SCALE; set if you want to have a resize event firing correctly in your app.
			// Don't use a StageOrientationEvent.ORIENTATION_CHANGE or StageOrientationEvent.ORIENTATION_CHANGING in the same way that you use a Event.Resize event.
			// The StageOrientationEvent is called prior to actually changing the oreintation of the screen and therefore has adverse effects on the mediacontroller.
			
			stage.addEventListener(Event.RESIZE, resizeListener);
		}
		
		private function resizeListener(event:Event):void
		{
			MediaPlayerAne.orientationChange();
		}
		
		private function onBtnBigClick(event:MouseEvent = null):void
		{
			txt.appendText("\n..........");
			MediaPlayerAne.getCurrentPosition();
		}
		
		private function onBtnClick(event:MouseEvent = null):void
		{
			if (buttonIsClicked)
			{
				MediaPlayerAne.disposeNativeExtension();
				button.label = "Click to Play";
				buttonBig.visible = false;
			}
			else
			{
				MediaPlayerAne.setRectangle(100, 100, 540, 360);
				
				// MediaPlayerAne.load(path); // Loads the video but does not start playing automatically.
				// You then call MediaPlayer.play to start.
				// You will receive a PREPARED callback when calling the load method and after the Media Player is ready to play.
				// After receivng an PREPARED callback, you can call a MediaPlayerAne.seekTo(10000);, if so desired.
				// If you call the method MediaPlayerAne.autoPlay(true); before you call a MediaPlayerAne.load(path);, 
				// it is the same as calling the method MediaPlayerAne.loadAndPlay(path);
				
				MediaPlayerAne.load(path);
				//MediaPlayerAne.loadAndPlay(path); // Loads the video and starts playing automaticaly.
				
				button.label = "Click to of Dispose ANE";
				
				buttonBig.visible = true;
			}
			
			buttonIsClicked = !buttonIsClicked;
		
		}
		
		private var currentPosition:int;
		
		private function statusUpdate(event:StatusEvent):void
		{
			if (event.code == "MediaPlayer")
			{
				if (event.level == "PREPARED")
				{
					MediaPlayerAne.seekTo(currentPosition); // MediaPlayerAne seekTo is calculated in milliseconds.
						//MediaPlayerAne.play();
						// The MediaPlayer automatically seeks to 0. 
				}
				
				if (event.level == "STARTED")
				{
					
				}
				
				if (event.level == "PAUSED")
				{
					
				}
				
				if (event.level == "RELEASED")
				{
					
				}
				
				if (event.level == "COMPLETE")
				{
					// Calling the method MediaPlayerAne.play(); is going to create a playing loop here.
					// MediaPlayerAne.play();
				}
				
				if (event.level == "ON_TOUCH_DOWN")
				{
					if (!fullscreenClicked)
					{
						fullscreenClicked = true;
						MediaPlayerAne.goFullScreen();
						MediaPlayerAne.play();
					}
				}
				
				if (event.level == "FULLSCREEN")
				{
					MediaPlayerAne.setRectangle(0, 0, 360, 240);
					MediaPlayerAne.centerOnStage();
					MediaPlayerAne.pause();
					fullscreenClicked = false;
					
					/*if (!fullscreenClicked)
					   {
					   MediaPlayerAne.goFullScreen();
					   }
					   else
					   {
					   MediaPlayerAne.setRectangle(0, 0, 360, 240);
					   MediaPlayerAne.centerOnStage();
					   }
					 fullscreenClicked = !fullscreenClicked;*/
				}
				
				if (event.level == "DISPOSED")
				{
					// Recommended to dispose completely of Native Extension.
					onBtnClick();
						// onBtnClick Basically calling the method:
						// MediaPlayerAne.disposeNativeExtension();
				}
			}
			
			if (event.code == "MediaPlayerError")
			{
				trace(event.level);
			}
			
			if (event.code == "MediaPlayerSeeked")
			{
				trace(event.level);
			}
			
			if (event.code == "MediaPlayerCurrentPosition")
			{
				trace(event.level);
				
				currentPosition = int(event.level);
				
				onBtnClick();
			}
			
			txt.appendText("\n" + event.code + " - " + event.level);
		}
	
	}
}