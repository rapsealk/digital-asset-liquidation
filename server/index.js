const restify = require('restify');

const server = restify.createServer();

server.use(restify.plugins.queryParser());
server.use(restify.plugins.bodyParser());

const contracts = require('./dapp/contracts');

/*
const controller = require('./controllers');
const authController = require('./controllers/auth');
const userController = require('./controllers/user');
*/
// const Ticker = require('./utils/ticker');

// const ticker = new Ticker();
// ticker.begin();
// ticker.reset();

server.use((req, res, next) => {
    console.log(`GET/POST [${req.method}] ${req.href()}`);
    return next();
});

server.get('/', contracts.getCoinbase);
server.post('/', (req, res) => {
    console.log('req.body:', req.body);
    res.json(req.body);
})
// server.get('/', controller.get);
server.post('/accounts/create', contracts.createAccount);
server.get('/accounts/balance', contracts.balanceOf);
server.post('/accounts/airdrop', contracts.airdrop);
server.post('/assets/register', contracts.registerAsset);
server.post('/assets/buy', contracts.buyAsset);
// server.get('/auth', authController.get);
// server.post('/auth', authController.post);
//server.post('/auth/signup', authController.signUp);
//server.post('/auth/signin', authController.signIn, authController.issue);
// server.get('/auth/new', authController.new);
//server.get('/user', authController.authenticate, userController.get);

const portNumber = 3000;

server.listen(portNumber, () => {
    console.log('%s listening at %s (port: %d)', server.name, server.url, portNumber);
});