pragma solidity ^0.4.18;

import "./Token.sol";

contract AssetManager {

    Token public tokenReward;
    uint256 public id;
    mapping (address => uint8) public owningShareOf;
    uint8 public totalShare = 100;
    uint8 public buyableShare = 100;
    uint256 public price = 1;

    mapping (uint256 => address) public ownerOfAsset;

    constructor(address tokenAddress) public {
        tokenReward = Token(tokenAddress);
    }

    function registerAsset(uint256 _id) public {
        id = _id;
    }

    function buyAsset(uint256 _price, uint8 _amount) public payable {
        
        if (buyableShare < _amount) revert();
        if (!tokenReward.transfer(msg.sender, this, _price * _amount)) revert();

        owningShareOf[msg.sender] += _amount;
        buyableShare -= _amount;
    }

    function getOwnShare(address _owner) public view returns (uint8) {
        return owningShareOf[_owner];
    }

    function getBuyableShare() public view returns (uint8) {
        return buyableShare;
    }

    function getCurrentPrice() public view returns (uint256) {
        return price;
    }
}