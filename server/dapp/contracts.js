const Web3 = require('web3');
const web3 = new Web3(new Web3.providers.HttpProvider('http://127.0.0.1:8545'));

const NetworkId = '1'; // '5777'

const Token = require('../truffle/build/contracts/Token.json');
const TokenContract = new web3.eth.Contract(Token.abi, Token.networks[NetworkId].address);
const AssetManager = require('../truffle/build/contracts/AssetManager.json');
const AssetManagerContract = new web3.eth.Contract(AssetManager.abi, AssetManager.networks[NetworkId].address);

const AIRDROP_ = 10;

exports.getCoinbase = async (req, res) => {
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
    res.json({ succeed: true, address: account.address });
};

exports.airdrop = async (req, res) => {
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
    res.json({ succeed: true, balance: balance });
};

exports.registerAsset = async (req, res) => {
    const { address, assetId } = req.body;
    // TODO("Contract")
    const tx = await AssetManagerContract.methods.registerAsset(assetId).send({
        from: address,
        gas: web3.utils.toWei('6000000', 'wei')
    });
    console.log('transaction:', tx);
    res.json({ succeed: true });
};

exports.buyAsset = async (req, res) => {
    const { address, amount } = req.body;
    console.log('address:', address, ', amount:', amount);
    const price = await AssetManagerContract.methods.getCurrentPrice().call();
    console.log('price:', price);
    const buyableShare = await AssetManagerContract.methods.getBuyableShare().call();
    console.log('buyableShare:', buyableShare);
    if (buyableShare < amount) return res.json({ succeed: false });
    const tx = await AssetManagerContract.methods.buyAsset(price, amount).send({
        from: address,
        gas: web3.utils.toWei('6000000', 'wei')
    });
    console.log('tx:', tx);
    const share = await AssetManagerContract.methods.getOwnShare(address).call();
    console.log('share:', share);
    res.json({ succeed: true, share: share });
};