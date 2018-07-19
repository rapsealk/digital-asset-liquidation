pragma solidity ^0.4.18;

import "../../node_modules/openzeppelin-solidity/contracts/token/ERC20/BasicToken.sol";

contract Token is BasicToken {

    string public name = "Digital Asset Liquidation Token";
    string public symbol = "DAL";
    uint8 public decimals = 18;

    address public owner;

    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }

    constructor(address _owner, uint256 _totalSupply) public {
        owner = _owner;
        totalSupply_ = _totalSupply;
        balances[owner] = _totalSupply;
    }

    function airdrop(address _to, uint256 _amount) public payable /*onlyOwner*/ returns (uint256) {
        require(_to != address(0));
        require(_amount <= balances[owner]);

        balances[owner] = balances[owner].sub(_amount);
        balances[_to] = balances[_to].add(_amount);
        emit Transfer(owner, _to, _amount);
        return balances[_to];
    }

    function transfer(address _from, address _to, uint256 _amount) public returns (bool) {
        require(_from != address(0));
        require(_to != address(0));
        
        balances[_from] -= _amount;
        balances[_to] += _amount;

        emit Transfer(_from, _to, _amount);
        return true;
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