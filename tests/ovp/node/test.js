var borhan = require('borhan');

partner_id=102;
service_url='https://www.borhan.com';
secret='';
var borhan_conf = new borhan.kc.BorhanConfiguration(partner_id);
borhan_conf.serviceUrl = service_url ;
var client = new borhan.kc.BorhanClient(borhan_conf);
var type = borhan.kc.enums.BorhanSessionType.ADMIN;

var expiry = null;
var privileges = null;
var ks = client.session.start(print_ks,secret , 'some@user.com', type, partner_id, expiry, privileges);

function print_ks(result)
{
	console.log(result);
}
