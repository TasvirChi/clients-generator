var Unit = require('deadunit')
var kc = require('../BorhanClient');
var ktypes = require('../BorhanTypes');
var vo = require ('../BorhanVO.js');
var config = require ('./config.js');

    var testme = Unit.test('Create -2 Admin session', function () {
	this.count(1);
	this.log('aga');
    });
var cb = function (results)
{
    if(results){
	    if(results.code && results.message){
		test.log(results.message);
		test.log(results.code);
		test.ok(false);
	    }else{
		//test.log('KS is: '+results);
		//test.ok(true);

	    }
	    testme.writeConsole(); // writes colorful output!
    }else{
	console.log('Something went wrong here :(');
    }
}

var borhan_conf = new kc.BorhanConfiguration(config.minus2_partner_id);
borhan_conf.serviceUrl = config.service_url ;
var client = new kc.BorhanClient(borhan_conf);
var type = ktypes.BorhanSessionType.ADMIN;

var expiry = null;
var privileges = null;
var ks = client.session.start(cb, config.minus2_admin_secret, config.user_id, type, config.minus2_partner_id, expiry, privileges);
var partner = new vo.BorhanPartner();
partner.name = "MBP";
partner.appearInSearch = null;
partner.adminName = "MBP";
partner.adminEmail = "mbp@example.com";
partner.description = "MBP";
var cms_password = 'testit';
var template_partner_id = null;
var silent = null;
//var result = client.partner.register(cb, partner, cms_password, template_partner_id, silent);
