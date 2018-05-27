const axios = require('axios');
const moment = require('moment');

const db = require('./database');

// https://apidocs.korbit.co.kr/ko/
const currencyTags = ['BTC', 'ETH', 'XRP'];

class Ticker {
    constructor(interval = 5000) {
        this.coin = {
            price: 10000,
            tag: 'DAL'
        },
        this.begin = () => {
            console.log(`Ticker begins. - ${Date.now()}`);
            setInterval(() => {
                currencyTags.forEach(tag => {
                    let _tag = tag.toLowerCase();
                    axios.get(`https://api.korbit.co.kr/v1/ticker/detailed?currency_pair=${_tag}_krw`)
                        .then(response => {
                            let { timestamp, last } = response.data;
                            // console.log(`[${tag}] last: ${last} at ${timestamp}`);
                            this.update(last, timestamp, tag);
                        })
                        .catch(error => {
                            console.error(error);
                        });                    
                });
                this.update(this.coin.price, Date.now(), this.coin.tag);
                this.event();
            }, interval);
        },
        this.update = async (price, timestamp, tag) => {
            try {
                var conn = await db.getConnection();
                await conn.beginTransaction();
                try {

                }
                catch (error) {
                    throw error;
                }
                let query = 'INSERT INTO Price SET ?';
                let priceData = { tag: tag, value: price, timestamp: new Date(timestamp) };
                await conn.query(query, priceData);
                console.log(`[Query] ${tag} price: ${price} at ${moment(timestamp).format('YYYY-MM-DD HH:mm:ss')}`);
            }
            catch (error) {
                console.error(error);
            }
            finally {
                await db.releaseConnection(conn);
            }
        },
        this.event = () => {
            // if (this.coin.price === 0) this.coin.price = (Math.random() >= 0.75) ? 10000 : 1000;
            let rate = ((0.5 - Math.random()) / 50);
            if (Math.random() >= 0.75) {
                let isNegative = (rate < 0);
                rate = Math.sqrt(Math.abs(rate));
                if (isNegative) rate *= -1;
            }
            console.log('rate: %f%%', rate);
            this.coin.price *= (1 + rate);
            console.log('new price:', this.coin.price);
        },

        this.reset = async () => {
            try {
                var conn = await db.getConnection();
                let query = 'DELETE FROM Price';
                let result = await conn.query(query);
                console.log('[Reset] result:', result);
            }
            catch (error) {
                console.error(error);
            }
            finally {
                await db.releaseConnection(conn);
            }
        }
    }
}

module.exports = Ticker;