# Run a Geth client
```
~/ethereum$ geth --datadir . --rpcapi personal,db,eth,net,web3 console --rpc --dev
```

* --networkid 5777
* init genesis.json
* --testnet (for Ropsten)
* --rinkeby (for Rinkeby)

# Run on Ganache client
```
$ Ganache.exe
$ truffle deploy
$ npm run dev
```