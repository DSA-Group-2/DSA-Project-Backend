const API_URL = "http://localhost:8080/dsaApp/product";

let usuario = sessionStorage.getItem("nif");
let nombreUsuario = sessionStorage.getItem("usuario");
let monedas = 0;
let inventario = {};
let nombresProductos = {};

//cargar las monedas
async function cargarUsuario() {
    const response = await fetch("http://localhost:8080/dsaApp/user/" + usuario);
    if (response.ok) {
        const datos = await response.json();
        monedas = datos.dinero;
        document.getElementById("monedas").textContent = monedas;
        inventario = datos.inventario || {};

        mostrarInventario();
    }
}

document.getElementById("usuario").textContent = nombreUsuario;
document.getElementById("monedas").textContent = monedas;
cargarUsuario();


async function fetchProducts(path, containerId) {
    const container = document.getElementById(containerId);

    //Container es como se ha llamado en index.html ha esa div.
    try {
        const response = await fetch(API_URL + path);

        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }

        const data = await response.json();
        container.innerHTML = "";

        data.forEach(product => {
            nombresProductos[product.id] = product.name;
            const card = document.createElement('div');
            card.classList.add('track-card');

            card.innerHTML = `
                    <p><span class="label">Name:</span> ${product.name || 'Unknown Product'}</p>
                    <p><span class="label">Price:</span> ${product.price || 'Unknown Price'}</p>
                    <p><span class="label">Sales:</span> ${product.sales || 'Unknown Sales'}</p>
                    <img src="${product.image_url}" alt=""></img>
                    

                    <small>Ref: ${product.id}</small>
                    <button onclick="comprar('${product.id}', ${product.price}, '${product.name}')">Comprar</button>                `;
            container.appendChild(card);
        });
        mostrarInventario();

    } catch (error) {
        console.error("Error al cargar products:", error);
        container.innerHTML = `<p style="color: red; text-align: center;">No se pudo conectar con la API en ${API_URL}</p>`;
    }
}

fetchProducts("/", "products");

async function comprar(idProducto, price,name) {
    if (monedas < price) {
        alert("No tienes suficientes monedas");
        return;
    }
    const pedido = {
        dni: usuario,
        productList: {
            [idProducto]: 1
        }
    };

    console.log(pedido);
    const response = await fetch("http://localhost:8080/dsaApp/user/order", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(pedido)
    });

    if (response.status === 201) {
        await cargarUsuario();
        alert("Has comprado " + name);
    } else if (response.status === 409) {
        alert("No tienes suficientes monedas");
    } else {
        alert("Error al realizar la compra");
    }
}

function mostrarInventario() {
    const lista = document.getElementById("inventario");
    lista.innerHTML = ""; //limpia inventario anterior
    for (let idProducto in inventario) {
        const elemento = document.createElement("li");
        let nombre = nombresProductos[idProducto] || idProducto;
        elemento.textContent = nombre + " x" + inventario[idProducto];
        lista.appendChild(elemento);
    }
}

//fetchProducts("/byPrice", "byPrice");
//fetchProducts("/bySales", "bySales");