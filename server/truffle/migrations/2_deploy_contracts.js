const Web3 = require('web3');
const web3 = new Web3(new Web3.providers.HttpProvider('http://127.0.0.1:8545'));

const Token = artifacts.require("Token.sol");
const AssetManager = artifacts.require("AssetManager.sol");

module.exports = deployer => {
    deployer.then(async () => {
        await deployer.deploy(Token, await web3.eth.getCoinbase(), 1000000000);
        await deployer.deploy(AssetManager, Token.address);
    });
};