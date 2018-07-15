const Web3 = require('web3');
const web3 = new Web3(new Web3.providers.HttpProvider('http://127.0.0.1:8545'));

/*
const CONTRACT = require('../truffle/build/contracts/CONTRACT.json');
const CONTRACT_ADDRESS = CONTRACT.address;
const CONTRACT_ABI = CONTRACT.abi;
*/

exports.createAccount = async (req, res) => {
    const account = await web3.eth.accounts.create(/*
        @entropy: at least 32 characters
    */);
    console.log('new account:', account);
    res.json(account);
};