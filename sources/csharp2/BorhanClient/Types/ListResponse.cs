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
// Copyright (C) 2006-2016  Borhan Inc.
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
using System;
using System.Xml;
using System.Collections.Generic;
using Borhan.Enums;
using Borhan.Request;

namespace Borhan.Types
{
    public class ListResponse<T> : ObjectBase, IListResponse where T : ObjectBase
	{
		#region Private Fields

        private int _TotalCount = Int32.MinValue;

        private IList<T> _Objects;

		#endregion

		#region Properties

		public int TotalCount
		{
			get { return _TotalCount; }
		}

        public IList<T> Objects
        {
            get { return _Objects; }
        }

		#endregion

		#region CTor

		public ListResponse()
		{
		}

		public ListResponse(XmlElement node) : base(node)
		{
			foreach (XmlElement propertyNode in node.ChildNodes)
			{
				string txt = propertyNode.InnerText;
				switch (propertyNode.Name)
				{
					case "totalCount":
						this._TotalCount = ParseInt(txt);
                        continue;
                    case "objects":
                        this._Objects = new List<T>();
                        foreach (XmlElement arrayNode in propertyNode.ChildNodes)
                        {
                            this._Objects.Add(ObjectFactory.Create<T>(arrayNode));
                        }
                        continue;
				}
			}
		}

		#endregion
	}
}
