pragma solidity ^0.4.18;

import "./Token.sol";

contract AssetManager {

    Token public tokenReward;

    constructor(address tokenAddress) public {
        tokenReward = Token(tokenAddress);
    }
}