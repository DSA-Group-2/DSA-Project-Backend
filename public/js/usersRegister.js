const API_URL = "http://localhost:8080/dsaApp/user";
const REGISTER_PATH = "/register";

async function register() {
    const name = document.getElementById("name").value.trim();
    const surname = document.getElementById("surname").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("pass").value;
    const errM = document.getElementById("registerError");
    errM.textContent = "";

    if (!name || !surname || !email || !password)
    {
        errM.textContent = "Rellena todos los campos, por favor";
        return;
    }

    try {
        const response = await fetch(API_URL + REGISTER_PATH, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email })
        });

        if (response.ok)
        {
            document.getElementById("registerBox").style.display = "none";
            document.getElementById("app").style.display = "block";
            document.getElementById("wellcome").textContent = "Bienvenido a nuestro juego";
        }

        else if (response.status === 401) {
            errM.textContent = "Usuario o contraseña incorrectos";
        }

        else
        {
            errM.textContent = `Error del servidor (${response.status})`;
        }

    } catch (e) {
        errM.textContent = "No se pudo conectar con la API";
    }
}

document.getElementById("loginBtn").addEventListener("click", login);

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
