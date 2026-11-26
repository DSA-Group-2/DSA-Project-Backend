const API_URL = "http://localhost:8080/dsaApp/user";
const LOGIN_PATH = "/login";

async function login() {
    const name = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value;
    const errormessage = document.getElementById("loginError");
    errormessage.textContent = "";
    document.getElementById("registerHint").style.display = "none";

    if (!name || !password)
    {
        errormessage.textContent = "Rellena usuario y contraseña";
        return;
    }

    try {
        const response = await fetch(API_URL + LOGIN_PATH, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, password })
        });

        if (response.ok)
        {
            const datos = await response.json();
            sessionStorage.setItem("nif", datos.nif);
            sessionStorage.setItem("usuario", datos.name);
            document.getElementById("loginBox").style.display = "none";
            document.getElementById("app").style.display = "block";
            fetchUsers("/", "users");
        }

        else if (response.status === 401) {
            errormessage.textContent = "Usuario o contraseña incorrectos";
            document.getElementById("registerHint").style.display = "block";
        }

        else
        {
            errormessage.textContent = `Error del servidor (${response.status})`;
        }

    } catch (e) {
        errormessage.textContent = "No se pudo conectar con la API";
    }
}

document.getElementById("loginBtn").addEventListener("click", login);
document.getElementById("goRegisterBtn").addEventListener("click", () => { window.location.href = "index3.html";});

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

//fetchUsers("/", "users");
//fetchProducts("/byPrice", "byPrice");
//fetchProducts("/bySales", "bySales");