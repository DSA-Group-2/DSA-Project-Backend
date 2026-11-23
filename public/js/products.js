const API_URL = "http://localhost:8080/dsaApp/product";

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
            const card = document.createElement('div');
            card.classList.add('track-card');

            card.innerHTML = `
                    <p><span class="label">Name:</span> ${product.name || 'Unknown Product'}</p>
                    <p><span class="label">Price:</span> ${product.price || 'Unknown Price'}</p>
                    <p><span class="label">Sales:</span> ${product.sales || 'Unknown Sales'}</p>
                    <img src="${product.image_url}" alt=""></img>
                    

                    <small>Ref: ${product.id}</small>
                `;
            container.appendChild(card);
        });

    } catch (error) {
        console.error("Error al cargar products:", error);
        container.innerHTML = `<p style="color: red; text-align: center;">No se pudo conectar con la API en ${API_URL}</p>`;
    }
}

fetchProducts("/", "products");
//fetchProducts("/byPrice", "byPrice");
//fetchProducts("/bySales", "bySales");