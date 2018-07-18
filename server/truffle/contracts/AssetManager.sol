pragma solidity ^0.4.18;

import "./Token.sol";

contract AssetManager {

    Token public tokenReward;

    mapping (uint256 => address) public ownerOfAsset;

    constructor(address tokenAddress) public {
        tokenReward = Token(tokenAddress);
    }
}