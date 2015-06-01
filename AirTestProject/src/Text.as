package 
{
	import flash.text.TextField;
	import flash.events.Event;
	import flash.text.TextFormat;
	
	/**
	 * ...
	 * @author Matthew C. Valverde
	 */
	
	dynamic public class Text extends TextField
	{
		private var _id:String = "";
		private var _text:String = "";
		
		override public function get text():String
		{
			return super.text;
		}
		
		override public function set text(value:String):void
		{
			if (value != null)
			{
				_text = value;
			}
			
			super.text = _text;
		}
		
		private var _font:String = "Arial";
		
		public function get font():String
		{
			return _font;
		}
		
		public function set font(value:String):void
		{
			_font = value;
			embedFonts = true;
			textFormat.font = value;
			updateText();
		}
		
		private var _fontSize:Number;
		
		public function get fontSize():Number
		{
			return _fontSize;
		}
		
		public function set fontSize(value:Number):void
		{
			_fontSize = value;
			textFormat.size = value;
			updateText();
		}
		
		private var _lineSpacing:int;
		
		public function get lineSpacing():int
		{
			return _lineSpacing;
		}
		
		public function set lineSpacing(value:int):void
		{
			_lineSpacing = value;
			textFormat.leading = value;
			updateText();
		}
		
		private var _align:String = "left";
		
		public function get align():String
		{
			return _align;
		}
		
		public function set align(value:String):void
		{
			_align = value;
			textFormat.align = value;
			updateText();
		}
		
		private var _underline:Boolean;
		
		public function get underline():Boolean
		{
			return _underline;
		}
		
		public function set underline(value:Boolean):void
		{
			_underline = value;
			textFormat.underline = value;
			updateText();
		}
		
		private var _bold:Boolean;
		
		public function get bold():Boolean
		{
			return _bold;
		}
		
		public function set bold(value:Boolean):void
		{
			_bold = value;
			textFormat.bold = value;
			updateText();
		}
		
		private var _leading:Number = 0;
		
		public function get leading():Number
		{
			return _leading
		}
		
		public function set leading(value:Number):void
		{
			_leading = value;
			textFormat.leading = value;
			updateText();
		}
		
		private var _letterSpacing:Number = 0;
		
		public function get letterSpacing():Number
		{
			return _letterSpacing;
		}
		
		public function set letterSpacing(value:Number):void
		{
			_letterSpacing = value;
			textFormat.letterSpacing = value;
			updateText();
		}
		
		public function get id():String
		{
			return _id;
		}
		
		public function set id(value:String):void
		{
			_id = value;
		}
		
		public var textFormat:TextFormat = new TextFormat();
		
		public function Text(text:String = "", fontSize:Number = 12, font:String = "Arial", fontColor:uint = 0x000000)
		{
			this.text = text;
			this.fontSize = fontSize;
			this.font = font;
			this.textColor = fontColor;
			
			if (stage)
			{
				init();
			}
			else
			{
				addEventListener(Event.ADDED_TO_STAGE, init);
			}
		}
		
		private function init(e:Event = null):void
		{
			removeEventListener(Event.ADDED_TO_STAGE, init);
			//selectable = false;
			updateText();
		}
		
		private function updateText():void
		{
			defaultTextFormat = textFormat;
			text = _text;
		}
	}
}