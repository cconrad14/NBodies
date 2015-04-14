// express server
var app = require('express')();
var server = app.listen(80, function() {
	var host = server.address().address
	var port = server.address().port
	console.log('listening at http://%s:%s', host, port)
});


// socket.io
var io = require('socket.io')(server);
app.get('/', function(req, res) {
	res.sendFile(__dirname + '/index.html');
});
app.get('/static/:file', function(req, res) {
	res.sendFile(__dirname + '/www/' + req.params.file);
});

// socket events go here
// gui events are prefixed with 'gui-'
// simulation events are prefixed with 'sim-'
io.on('connection', function(socket) {

	//////////////////
	// events
	//////////////////
	console.log('new connection!');
	//console.log(socket);

	// sim -> gui
	socket.on('update-positions', function(data) {
		//console.log('update-positions');
		io.emit('gui-update-positions', data);
	});

	// gui -> sim
	socket.on('pause', function(data) {
		console.log('pause');
		io.emit('sim-pause', data);
	});
});