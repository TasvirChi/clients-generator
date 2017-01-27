#!/usr/bin/env escript
%% -*- erlang -*-
%%! -pa ./ebin -pa ./deps/jsx/ebin -pa ./deps/erlsom/ebin

-module(test1).
-import(io, [format/2]).

-include_lib("../src/borhan_client.hrl").

main(_) ->
    application:start(inets),
    
    ClientConfiguration = #borhan_configuration{
    	client_options = [{verbose, debug}]
    }, 
    ClientRequest = #borhan_request{
    	ks = <<"KS Place Holder">>
    },
    Entry = #borhan_media_entry{name = <<"test entry">>, mediaType = 2},
    Results = borhan_media_service:add(ClientConfiguration, ClientRequest, Entry),

	io:format("Created entry: ~p~n", [Results]).
	