const db = require('../utils/database');

exports.get = async (req, res, next) => {
    res.json(200, { path: req.href() });
};

exports.post = async (req, res, next) => {
    res.json(200, { path: req.href() });
};