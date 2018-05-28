const restify = require('restify');

const server = restify.createServer();
const portNumber = 3000;

const controller = require('./controllers');
const Ticker = require('./utils/ticker');

const ticker = new Ticker();
// ticker.begin();
// ticker.reset();

server.use((req, res, next) => {
    console.log(`GET/POST ${req.href()}`);
    return next();
});

server.get('/', controller.get);
server.get('/auth', controller.auth.get);
server.post('/auth', controller.auth.post);
server.get('/user', controller.user.get);

server.listen(portNumber, () => {
    console.log('%s listening at %s (port: %d)', server.name, server.url, portNumber);
});