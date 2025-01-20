require('dotenv').config();

keys = {
  ticketmaster_key: process.env.TICKETMASTER_KEY,
  geocoding_key: process.env.GEOCODING_KEY,
};

module.exports = keys;
