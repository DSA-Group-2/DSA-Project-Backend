const API_URL = "http://localhost:8080/dsaApp/user";

async function fetchUsers(path, containerId) {
    const container = document.getElementById(containerId);
    //Container es como se ha llamado en index.html ha esa div.
    try {
        const response = await fetch(API_URL + path);

        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }

        const data = await response.json();
        container.innerHTML = "";
        console.log(data);

        Object.values(data).forEach(user => {
            const card = document.createElement('div');
            card.classList.add('track-card');

            card.innerHTML = `
                    <p><span class="label">Name:</span> ${user.name || 'Unknown Product'}</p>
                    

                `;
            container.appendChild(card);
        });

    } catch (error) {
        console.error("Error al cargar usuarios:", error);
        container.innerHTML = `<p style="color: red; text-align: center;">No se pudo conectar con la API en ${API_URL}</p>`;
    }
}

fetchUsers("/", "users");
//fetchProducts("/byPrice", "byPrice");
//fetchProducts("/bySales", "bySales");