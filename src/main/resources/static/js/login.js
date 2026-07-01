/*loginform*/
function togglePassword() {

    const password = document.getElementById("password");
    const eye = document.getElementById("eyeIcon");

    if (!password || !eye) return;

    if (password.type === "password") {
        password.type = "text";
        eye.classList.replace("bi-eye", "bi-eye-slash");
    } else {
        password.type = "password";
        eye.classList.replace("bi-eye-slash", "bi-eye");
    }
}