const express = require('express');
const cors = require('cors');
const keys = require('./keys');
const axios = require('axios');
const geohash = require('ngeohash');
var SpotifyWebApi = require('spotify-web-api-node');

// credentials are optional
var spotifyApi = new SpotifyWebApi({
  clientId: 'a9172a3d558d4444977390458f8f6244',
  clientSecret: 'ab0ca724dc8f4500aef41e4b59b72d3f',
  redirectUri: 'http://www.example.com/callback'
});

// Retrieve an access token.
spotifyApi.clientCredentialsGrant().then(
    function(data) {
      console.log('The access token expires in ' + data.body['expires_in']);
      console.log('The access token is ' + data.body['access_token']);
  
      // Save the access token so that it's used in future calls
      spotifyApi.setAccessToken(data.body['access_token']);
    },
    function(err) {
      console.log('Something went wrong when retrieving an access token', err);
    }
  );

app = express();
app.use(cors());

app.get('/', (req, res) => {
    res.send('Im Alive');
});


//converts location to Coordinates and then to geohash 
async function geocoding(location) {
    url = `https://maps.googleapis.com/maps/api/geocode/json?address=${location}&key=${keys.geocoding_key}`;
    response = await axios.get(url);
    dictionary = response.data;
    latitude = 0;
    longitude = 0;
    if(dictionary.results.length > 0){
        latitude = dictionary.results[0].geometry.location.lat;
        longitude= dictionary.results[0].geometry.location.lng;
        console.log(latitude,longitude);
        var latlon = geohash.encode(latitude,longitude);
        geoPoint = latlon.substring(0, 6);
        return geoPoint
    }else{
        return 'Not a valid Address';
    }
};

app.get('/geocoding',async (req,res) => {
    console.log(req.query);
    url = `https://maps.googleapis.com/maps/api/geocode/json?address=${req.query.location}&key=${keys.geocoding_key}`;
    response = await axios.get(url);
    dictionary = response.data;
    latitude = 0;
    longitude = 0;
    if(dictionary.results.length > 0){
        latitude = dictionary.results[0].geometry.location.lat;
        longitude= dictionary.results[0].geometry.location.lng;
        console.log(latitude,longitude);}
    Data = {'latitude': latitude,
            'longitude': longitude}
    res.send(Data);
});

// get all events
app.get('/allEvents', async (req,res) => {
    let Data = {};
    console.log(req.query);
    geoPoint = await geocoding(req.query.location);
    if(geoPoint === 'Not a valid Address'){
        res.send(Data);
    }else{
        segmentID = {'Music': 'KZFzniwnSyZfZ7v7nJ', 'Sports':'KZFzniwnSyZfZ7v7nE', 'Arts':'KZFzniwnSyZfZ7v7na', 'Film':'KZFzniwnSyZfZ7v7nn', 'Miscellanious': 'KZFzniwnSyZfZ7v7n1','Default':''}
    req = `https://app.ticketmaster.com/discovery/v2/events.json?apikey=${keys.ticketmaster_key}&keyword=${req.query.keyword}&segmentId=${segmentID[req.query.category]}&radius=${req.query.radius}&unit=miles&geoPoint=${geoPoint}`;
    response = await axios.get(req);
    var dictionary = response.data;

    
    if ('_embedded' in dictionary){
        
        let date = '';
        let time = '';
        let icon = '';
        let event_name = '';
        let genre = '';
        let venue = '';
        let count = 0;
        if(dictionary['_embedded'].hasOwnProperty('events')){
            for(let i in dictionary['_embedded']['events']){
                let event = dictionary['_embedded']['events'][i]
                if((event.hasOwnProperty('dates')) && (event['dates'].hasOwnProperty('start')) && (event['dates']['start'].hasOwnProperty('localDate'))){
                    date = event['dates']['start']['localDate'];
                }
                if((event.hasOwnProperty('dates')) && (event['dates'].hasOwnProperty('start')) && (event['dates']['start'].hasOwnProperty('localTime'))){
                    time = event['dates']['start']['localTime'];
                }
                if(event.hasOwnProperty('images') && event['images'][0].hasOwnProperty('url')){
                    icon = event['images'][0]['url'];
                }
                if(event.hasOwnProperty('name')){
                    event_name = event['name'];
                }
                if(event.hasOwnProperty('classifications') && event['classifications'][0].hasOwnProperty('segment') && event['classifications'][0]['segment'].hasOwnProperty('name')){
                    genre = event['classifications'][0]['segment']['name'];
                }
                if(event.hasOwnProperty('_embedded') && event['_embedded'].hasOwnProperty('venues') && event['_embedded']['venues'][0].hasOwnProperty('name')){
                    venue = event['_embedded']['venues'][0]['name'];
                }

               Data[event['id']]={
                    'id': event['id'],
                    'Date': date,
                    'Time': time,
                    'Icon': icon,
                    'Name': event_name,
                    'Genre': genre,
                    'Venue': venue
               }
               count += 1;
               if(count>20){
                break;
               }
            }
        }
    }
    res.send(Data);
    }
    

});

// get event details
app.get('/eventDetails', async(req,res) => {
    console.log(req.query);
    let id = req.query.event_id;
    req = `https://app.ticketmaster.com/discovery/v2/events/${req.query.event_id}?apikey=${keys.ticketmaster_key}`;
    response = await axios.get(req);
    dictionary = response.data;
    // res.send(response.data);
    Data = {};
    let event_name = ''
    let date = '';
    let time = '';
    let artist = [];
    let artist_string = '';
    let venue = '';
    let genre = '';
    let prices = '';
    let ticket = '';
    let buy = '';
    let seat_map = '';
    

    if(dictionary.hasOwnProperty('name')){
        event_name = dictionary.name;
    }

    if (dictionary.hasOwnProperty('dates') && dictionary.dates.hasOwnProperty('start')){
        date = dictionary.dates.start.localDate;
        time = dictionary.dates.start.localTime;
    }

    if (dictionary.hasOwnProperty('_embedded')){
        if(dictionary['_embedded'].hasOwnProperty('attractions')){
            for(let i in dictionary._embedded.attractions){
                art = dictionary._embedded.attractions[i]
                let lst = [];
                lst.push(art.name);
                if(art.name != 'Undefined'){
                   artist_string += art.name + ' | '; 
                }
                if (art.hasOwnProperty('classifications') &&  art.classifications[0].hasOwnProperty('segment') && art.classifications[0].segment.hasOwnProperty('name')){
                    if (art.classifications[0].segment.name == 'Music'){
                        lst.push(1);
                    }else{
                        lst.push(0);
                    }
                }
                artist.push(lst);
                }
            }
        if(dictionary._embedded.hasOwnProperty('venues')){
            venue = dictionary._embedded.venues[0].name;
        }           
    }
    artist_string = artist_string.slice(0,-3);
    
    if (dictionary.hasOwnProperty('classifications')){
        if (dictionary.classifications[0].hasOwnProperty('subGenre') && dictionary.classifications[0].subGenre.hasOwnProperty('name')){
            if(dictionary.classifications[0].subGenre.name != 'Undefined'){
                genre += dictionary.classifications[0].subGenre.name + ' | ';
            }
            
        }
        if (dictionary.classifications[0].hasOwnProperty('genre') && dictionary.classifications[0].genre.hasOwnProperty('name')){
            if(dictionary.classifications[0].genre.name != 'Undefined'){
                genre += dictionary.classifications[0].genre.name + ' | ';
            }
        }
        if (dictionary.classifications[0].hasOwnProperty('segment') && dictionary.classifications[0].segment.hasOwnProperty('name')){
            if(dictionary.classifications[0].segment.name != 'Undefined'){
                genre += dictionary.classifications[0].segment.name + ' | ';
            }
        }
        if (dictionary.classifications[0].hasOwnProperty('subType') && dictionary.classifications[0].subType.hasOwnProperty('name')){
            if(dictionary.classifications[0].subType.name != 'Undefined'){
                genre += dictionary.classifications[0].subType.name + ' | ';
            }
        }
        if (dictionary.classifications[0].hasOwnProperty('type') && dictionary.classifications[0].type.hasOwnProperty('name')){
            if(dictionary.classifications[0].type.name != 'Undefined'){
               genre += dictionary.classifications[0].type.name + ' | '; 
            }
        }
    }

    genre = genre.slice(0,-3);
    
    if(dictionary.hasOwnProperty('priceRanges')){
        prices = String(dictionary.priceRanges[0].min) + '-' + String(dictionary.priceRanges[0].max) + ' USD';
    }
    if(dictionary.hasOwnProperty('dates') && dictionary.dates.hasOwnProperty('status')){
        ticket = dictionary.dates.status.code;
    }
    if(dictionary.hasOwnProperty('url')){
        buy = dictionary.url;
    }
    if(dictionary.hasOwnProperty('seatmap')){
        seat_map = dictionary.seatmap.staticUrl;
    }
    Data ={
        'Name': event_name,
        'Id': id,
        'Date': date,
        'Time': time,
        'Artist': artist,
        'ArtistString': artist_string,
        'Venue': venue,
        'Genre': genre,
        'Prices': prices,
        'Ticket': ticket,
        'Buy': buy,
        'SeatMap': seat_map
    }
    res.send(Data);
});

// get venue details
app.get('/venueDetails', async(req,res) => {
    console.log(req.query);
    req = `https://app.ticketmaster.com/discovery/v2/venues?apikey=${keys.ticketmaster_key}&keyword=${req.query.keyword}`;
    response = await axios.get(req);
    dictionary = response.data;

    Data = {};
    let venue_name = '';
    let address = '';
    let city = '';
    let phone_no = '';
    let openHours = '';
    let generalRule = '';
    let childRule = '';

    if(dictionary.hasOwnProperty('_embedded') && dictionary._embedded.hasOwnProperty('venues')){
        ven = dictionary._embedded.venues[0];
        if(ven.hasOwnProperty('name')){
            venue_name = ven.name;
        }
        if(ven.hasOwnProperty('address') && ven.address.hasOwnProperty('line1')){
            address = ven.address.line1;
        }
        if(ven.hasOwnProperty('city') && ven.city.hasOwnProperty('name')){
            city = ven.city.name + ',' + ven.state.name;
        }
        if(ven.hasOwnProperty('boxOfficeInfo') && ven.boxOfficeInfo.hasOwnProperty('phoneNumberDetail')){
            phone_no = ven.boxOfficeInfo.phoneNumberDetail;
        }
        if(ven.hasOwnProperty('boxOfficeInfo') && ven.boxOfficeInfo.hasOwnProperty('openHoursDetail')){
            openHours = ven.boxOfficeInfo.openHoursDetail;
        }
        if(ven.hasOwnProperty('generalInfo') && ven.generalInfo.hasOwnProperty('generalRule')){
            generalRule = ven.generalInfo.generalRule;
        }
        if(ven.hasOwnProperty('generalInfo') && ven.generalInfo.hasOwnProperty('childRule')){
            childRule = ven.generalInfo.childRule;
        }
        Data = {
            'Name': venue_name,
            'Address': address,
            'City': city,
            'PhoneNumber': phone_no,
            'OpenHours': openHours,
            'GeneralRule': generalRule,
            'ChildRule': childRule
        }
    }
    
    res.send(Data);

});

//get artist info
app.get('/artistDetails', async(req,res) => {
    console.log(req.query);
    
    spotifyApi.searchArtists(req.query.name)
    .then(function(response) {
        // res.send(response.body);
        
        dictionary = response.body;
        Data = {}
        let name = '';
        let followers = '';
        let popularity = '';
        let spotify = '';
        let image = '';
        let id = '';
        if(dictionary.hasOwnProperty('artists')){
            if(dictionary.artists.hasOwnProperty('items')){
                artist = dictionary.artists.items[0];
                if(artist.hasOwnProperty('name')){
                name = artist.name;  
                }
                if(artist.hasOwnProperty('followers') && artist.followers.hasOwnProperty('total')){
                    followers = artist.followers.total;
                    followers = followers.toLocaleString("en-US");
                }
                if(artist.hasOwnProperty('popularity')){
                    popularity = artist.popularity;
                }
                if(artist.hasOwnProperty('external_urls') && artist.external_urls.hasOwnProperty('spotify')){
                    spotify = artist.external_urls.spotify;
                }
                if(artist.hasOwnProperty('images') && artist.images[0].hasOwnProperty('url')){
                    image = artist.images[0].url;
                }
                if(artist.hasOwnProperty('id')){
                    id = artist.id;
                }
            }
            Data = {
            'Name': name,
            'ID': id,
            'Followers': followers,
            'Popularity': popularity,
            'spotify': spotify,
            'Image': image,
            }
        }

        res.send(Data);
    }, function(err) {
        console.error(err);
    });
});

app.get('/albumDetails', async(req,res) => {
    console.log(req.query);
    spotifyApi.getArtistAlbums(req.query.id, { limit: 3})
    .then(function(response) {
        // res.send(response.body);
        dictionary = response.body;  
        album_urls = [];
        Data = {}
        if(dictionary.hasOwnProperty('items')){
            for(let i in dictionary.items){
                album = dictionary.items;
                if(album[i].hasOwnProperty('images') && album[i].images[0].hasOwnProperty('url')){
                    album_urls.push(album[i].images[0].url);
                }
            }
            Data = { 'Album':album_urls}
        }
        res.send(Data);
    }, function(err) {
        console.error(err);
    });
});

app.get('/suggest',async(req, res) => {
    console.log(req.query);
    req = `https://app.ticketmaster.com/discovery/v2/suggest?apikey=${keys.ticketmaster_key}&keyword=${req.query.keyword}`;
    response = await axios.get(req);
    let dictionary = response.data;
    let Data_s = {};
    suggestions = [];
    if(dictionary.hasOwnProperty('_embedded') && dictionary._embedded.hasOwnProperty('attractions')){
            for(let key in dictionary._embedded.attractions){
                if(dictionary._embedded.attractions[key].hasOwnProperty('name')){
                   suggestions.push(dictionary._embedded.attractions[key].name); 
                }
            }
            Data_s = {
                'suggestions': suggestions
            }
        }   
    res.send(Data_s);
});

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => console.log(`Server running on ${PORT}`));