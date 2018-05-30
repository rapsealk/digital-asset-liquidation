// const keypair = require('keypair');
const crypto2 = require('crypto2');

// console.log(keypair());

const private = // '-----BEGIN RSA PRIVATE KEY-----\n'
    'MIIEowIBAAKCAQEAl7GaxlF3WQhXlC3pRHsUrqjODoiBSA0YxfZPlgDcdJd6in/GhM5XoAyT\njbOpblwl6VH9MWsz3smhFqCVFn/UA6m4RWmNb2gXj4nUnUS7Z1DG+tdu+MGmHql30QSe5/ik\nCrF/HN4lajDIbqeCK/j3rcXqaOVvGjJyHYVhqThFmvzkehCchRQONAOcnSNox8hUSPfsgf//\n+3HhZX7xUmWXbGkELXhS0n0pXIsA2ZUGdwZBnCfitf0eckhNvtN5OimVT/UOJms70+AmLlYs\n0JXKFnVvrfskYOu/Tou0TykS/yl1DBr6f5wB+onJPgFRTfxQHU4teVsjXsRgGZit806LFwID\nAQABAoIBACn21gxa6RtWNXQmp0tsiTIwPKDEno8iIGJTW3zxzArtBNdftblzo8YA9KjUtOB0\n2YAMUHDlaToOqWOrFNNelXZV80YwhxH0x9/Lj2faZcWVERa/llF2sM/2rdDC6Hrfy/GxXKrN\nwaf4ytnhuR8VBt+pyskt/2hN4EfOaE/54zeWpLNTWW8GcuoUFpcldO34PG6RIJY6QqfNCo7O\neI9D6gnW63BNLw6uwgd6Tbd1T1n4IiZWR/ndB4PiRKQtK+tKVYz18sNJoQdZoUaOpZ3gm8mj\nNSGPR2YkJe14HC5dRxp2DPA51v6MJcUbykUkjm79YqUiv1xkF0ChPq2LcqyQLBECgYEA2u/s\nhbsxzoeyw8XtyE59eYqPRVz9yh6a1m5LXR6dfbNfRqxh2ADGeTcFQ2hjhwk8k6njF3prIau3\nosBrcS7idpbgsDADUwtgTReBIXjGrjzLCfhy7x5vUB+ubI4IDxy5ulxQqqaRPbhn7ObaPQmT\no2zXdNNZrjBdZiuM2vKTcXUCgYEAsV+NudgIr1QbSytAo7rEAB9zB3QnndffvF/f2tXgFiPG\nB55HmVla8/LasuIygMQux1stEURd+iok5RQDUxKPbpjWBBV3A3Tl9O6BganJuDbfl24FeCQw\nv7O8Pm2oYRwsBFy5WbLClFhAMyN1/TN+0fzVqKX0+wCzvmNFSmAPDNsCgYAaUH4IMNCytyKo\nXzDOQ4jXQ+0mdB/Y3EbWARmzzos1YYrRDsUP1kAEr7VFPdXAlD6grOmrl74qpN4EhMYlK3Jy\nuH23D35Gy/Y+LdYKdN7EcHAi+aau0OSmJ7iqOYQEdPppyNC1QiHw3io3kiOm9HC3UsyusPx1\ncEC5mABtDHzmRQKBgQCUnJjkgysTq4aKvpyUEiGycKcyMZIX4A1niXTHp7bvFd4P9//JbmKU\nToAKrRbfaIBBLBVgiEWtGq56+P+oFL70TT3TgiCLVuBtDYCYoSeQHvnLqLUQalIl7HPmYn5X\njwEaTo5Fp078d8yijC2JjiHN0uc5PIM74uS2ufoOTNmPTQKBgEMv8Fxt7Fxj9RUnhCc7tPZF\nhOOT5f///MpOIS8pD2cO45hVlHmlHezYgTgsRKDPJ5L9zXH8FKAW9JkaPvJrvW4Aoh6B8ug4\nWPU1CuDGIosaVS2JYp+nQN/Pn2rwOSAkNbKaZBz02J5jG0dUjZk+XeCpW5loJzyOo+DNbsWb\nZ8si\n';
    // '-----END RSA PRIVATE KEY-----\n'

exports.privateKey = private;

exports.decrypt = async (encrypted) => {
    return await crypto2.decrypt.rsa(encrypted, private);
};