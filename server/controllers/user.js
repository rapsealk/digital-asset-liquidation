const db = require('../utils/database');

exports.get = async (req, res, next) => {
    console.log('req.headers.authorization:', req.headers.authorization);
    res.json({ message: req.headers.authorization });
};