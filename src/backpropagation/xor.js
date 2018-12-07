// create the network
var synaptic = require('synaptic'); // this line is not needed in the browser
var Neuron = synaptic.Neuron,
	Layer = synaptic.Layer,
	Network = synaptic.Network,
	Trainer = synaptic.Trainer,
	Architect = synaptic.Architect;

/*  Descripción: Esta arquitectura pretender resolver el
problema XOR el cual no es linealmente separable.
    x1 x2 y
    0  0  0
    0  1  1
    1  0  1
    1  1  0
*/

// Mapeamos la arquitectura del la red neuronal:

// En la capa de entrada hay dos entradas
var inputLayer = new Layer(2);
// En la capa oculta hay 3 neuronas
var hiddenLayer = new Layer(30);
// En la capa de salida hay una sola salida
var outputLayer = new Layer(1);

// Ahora conectamos esas capas
inputLayer.project(hiddenLayer);
hiddenLayer.project(outputLayer);

// Se instancia una nueva red neuronal, mapeando el rol de cada capa
var myNetwork = new Network({
	input: inputLayer,
	hidden: [hiddenLayer],
	output: outputLayer
});

// Ahora se entrena la red neuronal para el XOR
var alpha = 0.2; // factor de aprendizaje
var epocas = 20000;
/*
* Aquí se va a iterar 20,000 veces.
* En cada iteración se va a propagar hacia adelante
* y hacia atrás (actualizar pesos y el bias) 4 veces
*/
for (var i = 0; i < epocas; i++) {
    // [x1 => 0, x2 => 0] => 0
    myNetwork.activate([0,0]); // forward propagation
    myNetwork.propagate(alpha, [0]); // backpropagation

    // [0, 1] => 1
    myNetwork.activate([0,1]);
    myNetwork.propagate(alpha, [1]);

    // [1, 0] => 1
    myNetwork.activate([1,0]);
    myNetwork.propagate(alpha, [1]);

    // [1, 1] => 0
    myNetwork.activate([1,1]);
    myNetwork.propagate(alpha, [0]);
}

// Ahora se va a testear
console.log(myNetwork.activate([0,0]));
console.log(myNetwork.activate([0,1]));
console.log(myNetwork.activate([1,0]));
console.log(myNetwork.activate([1,1]));
