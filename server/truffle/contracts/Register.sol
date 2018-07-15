pragma solidity ^0.4.18;

import "./Token.sol";

contract Register {

    Token public tokenReward;

    constructor(address tokenAddress) public {
        tokenReware = Token(tokenAddress);
    }
}