const db = require('../utils/database');
const keygen = require('../utils/keygen');

exports.get = async (req, res) => {

    let id = req.body.id;
    let password = req.body.password;

    try {
        var conn = await db.getConnection();
        let query = 'SELECT * FROM User WHERE id = ? AND password = ?';
        let user = (await conn.query(query, [id, password])).shift();
        if (!user) {
            res.json({ succeed: false });
        } else {
            res.json({ succeed: true, id: user.id });
        }
    }
    catch (error) {
        console.error(error);
        res.json({ succeed: false });
    }
    finally {
        await db.releaseConnection(conn);
    }
};