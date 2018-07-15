const restify = require('restify');

const server = restify.createServer();

const bodyParser = require('body-parser');

server.use(bodyParser.urlencoded({ extended: false }));
server.use(bodyParser.json());

const contracts = require('./dapp/contracts');

const controller = require('./controllers');
const authController = require('./controllers/auth');
const userController = require('./controllers/user');
// const Ticker = require('./utils/ticker');

// const ticker = new Ticker();
// ticker.begin();
// ticker.reset();

server.use((req, res, next) => {
    console.log(`GET/POST [${req.method}] ${req.href()}`);
    return next();
});

server.get('/', controller.get);
server.post('/accounts/create', contracts.createAccount);
// server.get('/auth', authController.get);
// server.post('/auth', authController.post);
server.post('/auth/signup', authController.signUp);
server.post('/auth/signin', authController.signIn, authController.issue);
// server.get('/auth/new', authController.new);
server.get('/user', authController.authenticate, userController.get);

const portNumber = 3000;

server.listen(portNumber, () => {
    console.log('%s listening at %s (port: %d)', server.name, server.url, portNumber);
});