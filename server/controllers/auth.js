const crypto = require('crypto');
const jwt = require('jsonwebtoken');

const db = require('../utils/database');

const keygen = require('../utils/keygen');

const hashPassword = (password) => {
	// 1 sec
	for (let i = 0; i < 100; i++) {
		const hmac = crypto.createHmac('sha256', keygen.privateKey);
		password = hmac.update(password).digest('hex');
	}
	return password;
};

exports.signUp = async (req, res) => {

	let id = req.body.id;
	let password = hashPassword(req.body.password);

	console.log('id:', id);
	console.log('password:', password);

	try {
		var conn = await db.getConnection();
		await conn.beginTransaction();
		try {
			let query = 'SELECT * FROM User WHERE id = ?';
			let users = await conn.query(query, id);
			if (users.length > 0) {
				res.json({ succeed: false });
			} else {
				let query2 = 'INSERT INTO User SET ?';
				let user = { id: id, password: password };
				await conn.query(query2, user);
				res.json({ succeed: true });
			}
			await conn.commit();
		}
		catch (error) {
			throw error;
		}
	}
	catch (error) {
		await conn.rollback();
		console.error(error);
	}
	finally {
		await db.releaseConnection(conn);
	}
};

exports.signIn = async (req, res, next) => {

	let id = req.body.id;
	let password = hashPassword(req.body.password);

	console.log('id:', id);
	console.log('password:', password);

	try {
		var conn = await db.getConnection();
		let query = 'SELECT * FROM User WHERE id = ? AND password = ?';
		let user = (await conn.query(query, [id, password])).shift();
		if (!user) {
			res.json({ succeed: false });
		} else {
			next();
		}
	}
	catch (error) {
		console.error(error);
	}
	finally {
		await db.releaseConnection(conn);
	}
};

exports.issue = async (req, res) => {

	let timestamp = Date.now();
	let payload = {
		// registered claim
		iss: 'digital-asset-liquidation',
		iat: timestamp, exp: timestamp + (24 * 60 * 60 * 1000),
		// private claim
		user: req.body.id,
		password: hashPassword(req.body.password)
	};
	
	let token = jwt.sign(payload, keygen.privateKey);

	res.json({ succeed: true, token: token });
};

exports.authenticate = async (req, res, next) => {

	let token = req.headers.authorization;

	try {
		let decoded = jwt.verify(token, keygen.privateKey);
		console.log('decoded:', decoded);
		req.body.id = decoded.user;
		req.body.password = decoded.password;

		try {
			var conn = await db.getConnection();
			let query = 'SELECT * FROM User WHERE id = ? AND password = ?';
			let user = (await conn.query(query, [req.body.id, req.body.password])).shift();
			if (!user) {
				res.json({ succeed: false });
			} else {
				next();
			}
		}
		catch (error) {
			throw error;
		}
		finally {
			await db.releaseConnection(conn);
		}
	}
	catch (error) {
		console.error(error);
		res.json({ succeed: false });
	}
};
/*
exports.get = async (req, res) => {
	res.json(200, { path: req.href() });
};

exports.post = async (req, res) => {
	res.json(200, { path: req.href() });
};
*/