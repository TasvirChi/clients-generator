# ===================================================================================================
#                           _  __     _ _
#                          | |/ /__ _| | |_ _  _ _ _ __ _
#                          | ' </ _` | |  _| || | '_/ _` |
#                          |_|\_\__,_|_|\__|\_,_|_| \__,_|
#
# This file is part of the Borhan Collaborative Media Suite which allows users
# to do with audio, video, and animation what Wiki platfroms allow them to do with
# text.
#
# Copyright (C) 2006-2011  Borhan Inc.
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http:#www.gnu.org/licenses/>.
#
# @ignore
# ===================================================================================================
require 'rubygems'
require 'test/unit'
require 'shoulda'
require 'yaml'
require 'logger'

require 'borhan'

class Test::Unit::TestCase

  # read the borhan config file
  # initiate a borhan configuration object
  # initiate borhan client object
  # get the sesion object and assigns it to the client
  def setup
    config_file = YAML.load_file("borhan.yml")
        
    partner_id = config_file["test"]["partner_id"]
    service_url = config_file["test"]["service_url"]
    username = config_file["test"]["username"]
    password = config_file["test"]["password"]
    timeout = config_file["test"]["timeout"]
	
    @media_asset_id = config_file["test"]["media_asset_id"]
    
    config = Borhan::BorhanConfiguration.new()
    config.service_url = service_url
    config.logger = Logger.new(STDOUT)
    config.timeout = timeout
    
    @client = Borhan::BorhanClient.new(config)
	response = @client.ott_user_service.login(partner_id, username, password)
	
    @client.ks = response.login_session.ks
  end

end
