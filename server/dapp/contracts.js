const Web3 = require('web3');
const web3 = new Web3(new Web3.providers.HttpProvider('http://127.0.0.1:8545'));

const NetworkId = '5777'

const Token = require('../truffle/build/contracts/Token.json');
const TokenContract = new web3.eth.Contract(Token.abi, Token.networks[NetworkId].address);

const AIRDROP_ = 10;

exports.getCoinbase = async (req, res) => {
    console.log('req.query:', req.query);
    console.log('req.body:', req.body);
    // const accounts = await web3.eth.getAccounts();
    res.json({ succeed: true, coinbase: await web3.eth.getCoinbase() });
};

exports.createAccount = async (req, res) => {

    const account = await web3.eth.accounts.create(/*
        @entropy: at least 32 characters
    */);
    const balance = await TokenContract.methods.airdrop(account.address, AIRDROP_).send({
        from: await web3.eth.getCoinbase(),
        gas: web3.utils.toWei('6000000', 'wei')
    });
    console.log('new account:', account);
    console.log('balance:', balance);

    // const base = await TokenContract.methods.balanceOf(await web3.eth.getCoinbase()).call();
    // console.log('base:', base);
    res.json({ succeed: true, account });
};

exports.airdrop = async (req, res) => {
    console.log('req.body:', req.body);
    const { address } = req.body;
    const transaction = await TokenContract.methods.airdrop(address, AIRDROP_).send({
        from: await web3.eth.getCoinbase(),
        gas: web3.utils.toWei('6000000', 'wei')
    });
    console.log('tx:', transaction);
    const balance = await TokenContract.methods.balanceOf(address).call();
    res.json({ succeed: true, balance: balance });
};

exports.balanceOf = async (req, res) => {
    const { address } = req.query;
    const balance = await TokenContract.methods.balanceOf(address).call();
    console.log('address:', address);
    console.log('balance:', balance);
    res.json({ succeed: true, balance: balance });
};