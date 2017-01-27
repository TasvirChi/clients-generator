
var config = new BorhanConfiguration();
config.serviceUrl = serviceUrl;
config.setLogger(new IBorhanLogger());

var client = new BorhanClient(config);

describe("Start session", function() {
    describe("User KS", function() {
    	var userId = null;
    	var type = 0; // BorhanSessionType.USER
    	var expiry = null;
    	var privileges = null;

    	it('not null', function(done) {
    		BorhanSessionService.start(secret, userId, type, partnerId, expiry, privileges)
        	.completion(function(success, ks) {
        		expect(success).toBe(true);
        		expect(ks).not.toBe(null);
        		client.setKs(ks);
        		done();
        	})
        	.execute(client);
        });
    });
});


describe("Add media", function() {
    describe("Multiple requests", function() {

    	var entry = {
    		mediaType: 1, // BorhanMediaType.VIDEO
    		name: 'test'
    	};

    	var uploadToken = {};

    	var createdEntry;
    	var createdUploadToken;

    	jasmine.DEFAULT_TIMEOUT_INTERVAL = 60000;
    	it('entry created', function(done) {
    		BorhanMediaService.add(entry)
    		.completion(function(success, entry) {
        		expect(success).toBe(true);
        		expect(entry).not.toBe(null);
        		expect(entry.id).not.toBe(null);
        		expect(entry.status.toString()).toBe('7'); // BorhanEntryStatus.NO_CONTENT

        		createdEntry = entry;
        		BorhanUploadTokenService.add(uploadToken)
        		.completion(function(success, uploadToken) {
            		expect(success).toBe(true);
            		expect(uploadToken).not.toBe(null);
            		expect(uploadToken.id).not.toBe(null);
            		expect(uploadToken.status).toBe(0); // BorhanUploadTokenStatus.PENDING

            		createdUploadToken = uploadToken;
            		
            		var mediaResource = {
            			objectType: 'BorhanUploadedFileTokenResource',
            			token: uploadToken.id
                	};
            		
            		BorhanMediaService.addContent(createdEntry.id, mediaResource)
            		.completion(function(success, entry) {
                		expect(success).toBe(true);
                		expect(entry.status.toString()).toBe('0'); // BorhanEntryStatus.IMPORT

                		done();
            		})
            		.execute(client);
        		})
        		.execute(client);
    		})
    		.execute(client)
    	});
    });
    

    describe("Single multi-request", function() {
    	var entry = {
    		mediaType: 1, // BorhanMediaType.VIDEO
    		name: 'test'
    	};

    	var uploadToken = {
    	};

    	var mediaResource = {
    		objectType: 'BorhanUploadedFileTokenResource',
			token: '{2:result:id}'
    	};

    	jasmine.DEFAULT_TIMEOUT_INTERVAL = 60000;
    	it('entry created', function(done) {
    		BorhanMediaService.add(entry)
    		.add(BorhanUploadTokenService.add(uploadToken))
    		.add(BorhanMediaService.addContent('{1:result:id}', mediaResource))
    		.completion(function(success, results) {
        		expect(success).toBe(true);
        		
    			entry = results[0];
        		expect(entry).not.toBe(null);
        		expect(entry.id).not.toBe(null);
        		expect(entry.status.toString()).toBe('7'); // BorhanEntryStatus.NO_CONTENT

    			uploadToken = results[1];
        		expect(uploadToken).not.toBe(null);
        		expect(uploadToken.id).not.toBe(null);
        		expect(uploadToken.status).toBe(0); // BorhanUploadTokenStatus.PENDING

    			entry = results[2];
        		expect(entry.status.toString()).toBe('0'); // BorhanEntryStatus.IMPORT
        		
        		done();
    		})
    		.execute(client);
    	});
    });
});
