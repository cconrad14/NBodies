// Heavily adapted version of below
//// http://threejs.org/examples/#misc_controls_orbit


if (!Detector.webgl)
	Detector.addGetWebGLMessage();


var container;
//var stats;
var camera, controls, scene, renderer;


init();
render();


var bodyHash = {};


var socket = io.connect('http://localhost');
//console.log(socket);
setupSocketEvents();


function animate() {

	requestAnimationFrame(animate);
	controls.update();

}

function init() {

	camera = new THREE.PerspectiveCamera( 60, window.innerWidth / window.innerHeight, 1, 100000 );
	camera.position.z = 500;

	controls = new THREE.OrbitControls( camera );
	controls.damping = 0.2;
	controls.addEventListener( 'change', render );

	scene = new THREE.Scene();

	// renderer

	renderer = new THREE.WebGLRenderer( { antialias: false } );
	//renderer.setClearColor( scene.fog.color );
	renderer.setPixelRatio( window.devicePixelRatio );
	renderer.setSize( window.innerWidth, window.innerHeight );

	container = document.getElementById( 'container' );
	container.appendChild( renderer.domElement );

/*
	stats = new Stats();
	stats.domElement.style.position = 'absolute';
	stats.domElement.style.top = '0px';
	stats.domElement.style.zIndex = 100;
	container.appendChild( stats.domElement );
*/
	//

	window.addEventListener( 'resize', onWindowResize, false );

	animate();

	var frameRate = 33; // milliseconds
	window.setInterval(render, frameRate);

}

function onWindowResize() {

	camera.aspect = window.innerWidth / window.innerHeight;
	camera.updateProjectionMatrix();
	renderer.setSize( window.innerWidth, window.innerHeight );

	render();

}

function render() {

	renderer.render( scene, camera );
	//stats.update();

}

function addBody(body)
{
	var geo = new THREE.SphereGeometry(body.radius, 8, 8);
    var mat = new THREE.MeshBasicMaterial( { color: 0xff0000, wireframe: true } );
	var mesh = new THREE.Mesh(geo, mat);

	updateBodyPosition(mesh, body);
	scene.add(mesh);
	bodyHash[body.id] = mesh;
}

function updateBodyPosition(mesh, body)
{
	mesh.position.x = body.x;
	mesh.position.y = body.y;
	mesh.position.z = body.z;
}

function setupSocketEvents() {
	socket.on('gui-update-positions', function(data) {
		//console.log('gui-update-positions');
		//console.log(data);

		data.forEach(function(element, index, arr) {
			var entry = bodyHash[element.id]; 
			if(typeof entry === 'undefined')
				addBody(element);
			else
				updateBodyPosition(entry, element);
		});

		// ensure the gui updates
		//render();
	});
}

function pauseSim() {
	socket.emit('pause');
}