// Heavily adapted version of below
//// http://threejs.org/examples/#misc_controls_orbit


if (!Detector.webgl)
	Detector.addGetWebGLMessage();


var container, stats;
var camera, controls, scene, renderer;


init();
render();


var socket = io.connect('http://localhost');
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
	//scene.fog = new THREE.FogExp2( 0xcccccc, 0.002 );

	// world
	/*
	var geometry = new THREE.CylinderGeometry( 0, 10, 30, 4, 1 );
	var material =  new THREE.MeshLambertMaterial( { color:0xffffff, shading: THREE.FlatShading } );

	for ( var i = 0; i < 500; i ++ ) {

		var mesh = new THREE.Mesh( geometry, material );
		mesh.position.x = ( Math.random() - 0.5 ) * 1000;
		mesh.position.y = ( Math.random() - 0.5 ) * 1000;
		mesh.position.z = ( Math.random() - 0.5 ) * 1000;
		mesh.updateMatrix();
		mesh.matrixAutoUpdate = false;
		scene.add( mesh );

	}
	*/


	// lights
	/*
	light = new THREE.DirectionalLight( 0xffffff );
	light.position.set( 1, 1, 1 );
	scene.add( light );

	light = new THREE.DirectionalLight( 0x002288 );
	light.position.set( -1, -1, -1 );
	scene.add( light );

	light = new THREE.AmbientLight( 0x222222 );
	scene.add( light );
	*/

	// renderer

	renderer = new THREE.WebGLRenderer( { antialias: false } );
	//renderer.setClearColor( scene.fog.color );
	renderer.setPixelRatio( window.devicePixelRatio );
	renderer.setSize( window.innerWidth, window.innerHeight );

	container = document.getElementById( 'container' );
	container.appendChild( renderer.domElement );

	stats = new Stats();
	stats.domElement.style.position = 'absolute';
	stats.domElement.style.top = '0px';
	stats.domElement.style.zIndex = 100;
	container.appendChild( stats.domElement );

	//

	window.addEventListener( 'resize', onWindowResize, false );

	animate();

}

function onWindowResize() {

	camera.aspect = window.innerWidth / window.innerHeight;
	camera.updateProjectionMatrix();

	renderer.setSize( window.innerWidth, window.innerHeight );

	render();

}

function render() {

	renderer.render( scene, camera );
	stats.update();

}

function addBody(body)
{
	var geo = new THREE.SphereGeometry(body.radius, 32, 32);
	var mat = new THREE.MeshBasicMaterial();
	var mesh = new THREE.Mesh(geo, mat);

	updateBodyPosition(mesh, body.position);
	scene.add(mesh);
	bodyHash[body.id] = mesh;
}

function updateBodyPosition(mesh, position)
{
	mesh.position.x = position[0];
	mesh.position.y = position[1];
	mesh.position.z = position[2];
}

function setupSocketEvents() {
	socket.on('gui-update-positions', function(data) {
		console.log('gui-update-positions');
		console.log(data);
	});
}

function pauseSim() {
	socket.emit('pause');
}