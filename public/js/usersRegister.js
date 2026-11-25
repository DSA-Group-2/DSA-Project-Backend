const API_URL = "http://localhost:8080/dsaApp/user";
const REGISTER_PATH = "/register";

async function register() {
    const nif = document.getElementById("nif").value.trim();
    const name = document.getElementById("name").value.trim();
    const surname = document.getElementById("surname").value.trim();
    const mail = document.getElementById("mail").value.trim();
    const password = document.getElementById("pass").value;
    const errM = document.getElementById("registerError");
    errM.textContent = "";

    if (!nif || !name || !surname || !mail || !password)
    {
        errM.textContent = "Rellena todos los campos, por favor";
        return;
    }

    try
    {
        const response = await fetch(API_URL + REGISTER_PATH, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nif, name, surname, mail, password})
        });

        if (response.ok)
        {
            window.location.href = "index2.html";
        }

        else
        {
            errM.textContent = "Error al registrar (" + response.status + ")";
        }

    }
    catch (e)
    {
        errM.textContent = "No se pudo conectar con la API";
    }
}

document.getElementById("registerBtn").addEventListener("click", register);

