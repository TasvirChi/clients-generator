// ===================================================================================================
//                           _  __     _ _
//                          | |/ /__ _| | |_ _  _ _ _ __ _
//                          | ' </ _` | |  _| || | '_/ _` |
//                          |_|\_\__,_|_|\__|\_,_|_| \__,_|
//
// This file is part of the Borhan Collaborative Media Suite which allows users
// to do with audio, video, and animation what Wiki platfroms allow them to do with
// text.
//
// Copyright (C) 2006-2011  Borhan Inc.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
// @ignore
// ===================================================================================================
﻿package {
	import com.borhan.BorhanClient;
	import com.borhan.commands.session.SessionStart;
	import com.borhan.config.BorhanConfig;
	import com.borhan.events.BorhanEvent;
	import com.borhan.types.BorhanSessionType;
	
	import flash.display.Sprite;

	public class BorhanClientSample extends Sprite
	{
		private const API_SECRET:String = "YOUR_USER_SECRET";
		private const BORHAN_PARTNER_ID:int = 54321;
		
		public function BorhanClientSample()
		{
			var configuration : BorhanConfig = new BorhanConfig();
			var borhan : BorhanClient = new BorhanClient( configuration );	
			var startSession : SessionStart = new SessionStart(API_SECRET, 'testUser', BorhanSessionType.USER, BORHAN_PARTNER_ID);
			startSession.addEventListener(BorhanEvent.COMPLETE, completed);
			startSession.addEventListener(BorhanEvent.FAILED, failed);
			borhan.post( startSession );
		}
		
		private function completed (event:BorhanEvent):void {
			trace ("Session Started: " + event.success);
			trace (event.data);
		}
		
		private function failed (event:BorhanEvent):void {
			trace ("Session Failed: " + event.error.errorMsg);
		}
	}
}
