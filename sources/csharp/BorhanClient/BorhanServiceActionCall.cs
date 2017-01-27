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
using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace Borhan
{
    public class BorhanServiceActionCall
    {
        #region Private Fields

        private string _Service;
        private string _Action;
        private BorhanParams _Params;
        private BorhanFiles _Files;

        #endregion

        #region Properties

        public string Service
        {
            get { return _Service; }
        }

        public string Action
        {
            get { return _Action; }
        }

        public BorhanParams Params
        {
            get { return _Params; }
        }

        public BorhanFiles Files
        {
            get { return _Files; }
        }

        public BorhanParams GetParamsForMultiRequest(int multiRequestNumber)
        {  
            _Params.Add("service", this._Service);
            _Params.Add("action", this._Action);
          
            BorhanParams multiRequestParams = new BorhanParams();
            multiRequestParams.Add(multiRequestNumber.ToString(), this._Params);

            return multiRequestParams;
        }

        public BorhanFiles GetFilesForMultiRequest(int multiRequestNumber)
        {
            BorhanFiles multiRequestParams = new BorhanFiles();
            foreach (KeyValuePair<string, Stream> param in this._Files)
            {
                multiRequestParams.Add(multiRequestNumber + ":" + param.Key, param.Value);
            }

            return multiRequestParams;
        }

        #endregion

        #region CTor

        public BorhanServiceActionCall(string service, string action, BorhanParams kparams)
            : this(service, action, kparams, new BorhanFiles())
        {

        }

        public BorhanServiceActionCall(string service, string action, BorhanParams kparams, BorhanFiles kfiles)
        {
            this._Service = service;
            this._Action = action;
            this._Params = kparams;
            this._Files = kfiles;
        }

        #endregion
    }
}
