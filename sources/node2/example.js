
const borhan = require('./BorhanClient');


const secret = '@SECRET@';
const partnerId = @PARTNER_ID@;
const entryName = 'test';
const entryDescription = 'test';

let config = new borhan.Configuration();
config.serviceUrl = "http://www.borhan.com";

const client = new borhan.Client(config);

function session_start(){
	var userId = null;
	var type = borhan.enums.SessionType.USER;
	var expiry = null;
	var privileges = null;

	return new Promise((resolve, reject) => {
		borhan.services.session.start(secret, userId, type, partnerId, expiry, privileges)
    	.completion((success, ks) => {
    		if(!success){
    			reject(ks.message);
    			return;
    		}

    		client.setKs(ks);
    		console.log("Seesion started: " + ks);
    		resolve();
    	})
    	.execute(client);
	});
}

function media_add(){
	var entry = new borhan.objects.MediaEntry({
		mediaType: borhan.enums.MediaType.VIDEO,
		name: entryName,
		description: entryDescription
	});

	return new Promise((resolve, reject) => {
		borhan.services.media.add(entry)
		.completion((success, entry) => {
			if(!success){
    			reject(entry.message);
				return;
			}
			
			console.log("Entry created: " + entry.id);
    		resolve(entry.id);
		})
		.execute(client);
	});
}

function media_delete(entryId){
	borhan.services.media.deleteAction(entryId)
	.execute(client);
}

function media_delete_error_with_cb(){
	var entryId = "kishkush";

	return new Promise((resolve, reject) => {
		borhan.services.media.deleteAction(entryId).execute(client, function(success, results){
    		if(success) {
    			reject('Error was expected');
    		}
    		else {
    			resolve("Error received as expected: " + results.message);
    		}
    	});
	});
}

function media_delete_error_without_cb(){
	var entryId = "kishkush";
	borhan.services.media.deleteAction(entryId).execute(client);
}

function multirequest_multi_callback(){
	var entry = new borhan.objects.MediaEntry({
		mediaType: borhan.enums.MediaType.VIDEO,
		name: entryName,
		description: entryDescription
	});

	return new Promise((resolve, reject) => {
		borhan.services.media.add(entry)
		.completion((success, entry) => {
			if(!success){
    			reject(entry.message);
				return;
			}
			
			console.log("Entry created: " + entry.id);
		})
		.add(borhan.services.media.deleteAction("{1:result:id}")
			.completion((success, error) => {
				if(!success) {
					reject(error.message);
				}
				else {
					console.log("Entry deleted");
					resolve();
				}
			}))
		.execute(client);
	});
}

function multirequest_single_callback(){
	var entry = new borhan.objects.MediaEntry({
		mediaType: borhan.enums.MediaType.VIDEO,
		name: entryName,
		description: entryDescription
	});

	return new Promise((resolve, reject) => {
		borhan.services.media.add(entry)
		.add(borhan.services.media.deleteAction("{1:result:id}"))
		.execute(client, (success, results) => {
			if(results.message) { // general transport error
				reject(results.message);
				return;
			}
			
			for(var i = 0; i < results.length; i++){
				if(results[i] && typeof(results[i]) == 'object' && results[i].code && results[i].message) { // request error
					reject(results[i].message);
				}
				else if(i == 0) { // media.add
					console.log("Entry created: " + results[i].id);
				}
				else {
					console.log("Entry deleted");
					resolve();
				}
			}
		});
	});
}

function multirequest_different_style(){
	var entry = new borhan.objects.MediaEntry({
		mediaType: borhan.enums.MediaType.VIDEO,
		name: entryName,
		description: entryDescription
	});

	var addRequestBuilder = borhan.services.media.add(entry);
	var deleteRequestBuilder = borhan.services.media.deleteAction("{1:result:id}").completion();
	
	var multiRequestBuilder = new borhan.MultiRequestBuilder(client);
	multiRequestBuilder.add(addRequestBuilder);
	multiRequestBuilder.add(deleteRequestBuilder);

	return new Promise((resolve, reject) => {
		multiRequestBuilder.execute(client, (success, results) => {
			if(results.message) { // general transport error
				reject(results.message);
				return;
			}
		
    		for(var i = 0; i < results.length; i++) {
    			if(results[i] && results[i].message) { // request error 
    				reject(results[i].message);
    			}
    			else if(i == 0) { // media.add
    				console.log("Entry created: " + results[i].id);
    			}
    			else {
    				console.log("Entry deleted");
					resolve();
    			}
    		}
		});
	});
}

function multirequest_single_promise(){
	var entry = new borhan.objects.MediaEntry({
		mediaType: borhan.enums.MediaType.VIDEO,
		name: entryName,
		description: entryDescription
	});

	return new Promise((resolve, reject) => {
		borhan.services.media.add(entry)
		.add(borhan.services.media.deleteAction("{1:result:id}"))
		.execute(client)
		.then((results) => {
			for(var i = 0; i < results.length; i++){
				if(results[i] && typeof(results[i]) == 'object' && results[i].code && results[i].message) { // request error
					reject(results[i].message);
				}
				else if(i == 0) { // media.add
					console.log("Entry created: " + results[i].id);
				}
				else {
					console.log("Entry deleted");
					resolve();
				}
			}
		}, (error) => {
			if(error.message) { // general transport error
				reject(error.message);
				return;
			}
		});
	});
}

function multirequest_return_promise(){
	var entry = new borhan.objects.MediaEntry({
		mediaType: borhan.enums.MediaType.VIDEO,
		name: entryName,
		description: entryDescription
	});

	return borhan.services.media.add(entry)
	.add(borhan.services.media.deleteAction("{1:result:id}"))
	.execute(client);
}

process.on('uncaughtException', (e) => {
	console.log(e);
});

session_start()
.then(() => Promise.all([
    	media_add().then((entryId) => media_delete(entryId)),
    	media_delete_error_with_cb(),
    	media_delete_error_without_cb(),
    	multirequest_multi_callback(),
    	multirequest_single_callback(),
    	multirequest_different_style(),
    	multirequest_single_promise(),
    	multirequest_return_promise()
]))
.then(() => {
	console.log('All done');
}, (e) => {
	console.error(e);
});
