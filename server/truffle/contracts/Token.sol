pragma solidity ^0.4.18;

import "openzeppelin-solidity/contracts/token/ERC20/BasicToken.sol";

contract Token is BasicToken {

    string public name = "Digital Asset Liquidation Token";
    string public symbol = "DAL";
    uint8 public decimals = 18;

    address public owner;

    constructor(address _owner, uint256 _totalSupply) public {
        owner = _owner; // msg.sender;
        totalSupply_ = _totalSupply;
        balances[_owner] = _totalSupply;
    }
}

/*
contract BasicToken is ERC20Basic {
    using SafeMath for uint256;

    mapping(address => uint256) balances;

    uint256 totalSupply_;

    function totalSupply() public view returns (uint256) {
        return totalSupply_;
    }

    function transfer(address _to, uint256 _value) public returns (bool) {
        require(_to != address(0));
        require(_value <= balances[msg.sender]);

        balances[msg.sender] = balances[msg.sender].sub(_value);
        balances[_to] = balances[_to].add(_value);
        emit Transfer(msg.sender, _to, _value);
        return true;
    }

    function balanceOf(address _owner) public view returns (uint256) {
        return balances[_owner];
    }
}
*/