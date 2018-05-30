const crypto = require('crypto');

exports.issue = async (req, res) => {

	let header = { typ: 'JWT', alg: 'HS256' };
	let encodedHeader = new Buffer(JSON.stringify(header))
										.toString('base64')
										.replace('=', '');

	let timestamp = Date.now();
	let payload = {
		// registered claim
		iss: 'digital-asset-liquidation',
		iat: timestamp, exp: timestamp + (24 * 60 * 60 * 1000),
		// private claim
		user: req.body.id
	};
	let encodedPayload = new Buffer(JSON.stringify(payload))
										.toString('base64')
										.replace('=', '');

	let signature = crypto.createHmac('sha256', keygen.privateKey)
							.update(`${encodedHeader}.${encodedPayload}`)
							.digest('base64')
							.replace('=', '');

	let token = `${encodedHeader}.$${encodedPayload}.${signature}`;

	res.json({ succeed: true, token: token });
};