
/*signupform*/

function togglePassword(inputId, eyeId) {

    const input = document.getElementById(inputId);
    const eye = document.getElementById(eyeId);

    if (!input || !eye) return;

    if (input.type === "password") {

        input.type = "text";
        eye.classList.replace("bi-eye", "bi-eye-slash");

    } else {

        input.type = "password";
        eye.classList.replace("bi-eye-slash", "bi-eye");

    }

}

const signupForm = document.getElementById("signupForm");

if (signupForm) {

    signupForm.addEventListener("submit", function(e) {

        const password =
            document.getElementById("password").value;

        const confirm =
            document.getElementById("confirmPassword").value;

        if (password !== confirm) {

            e.preventDefault();

            alert("Passwords do not match.");

        }

    });

}

const confirmPassword =
    document.getElementById("confirmPassword");

if (confirmPassword) {

    confirmPassword.addEventListener("keyup", function() {

        const password =
            document.getElementById("password").value;

        const message =
            document.getElementById("passwordMessage");

        if (this.value.length === 0) {

            message.innerHTML = "";
            return;

        }

        if (password === this.value) {

            message.innerHTML = "✅ Passwords match";
            message.className = "text-success mt-2 d-block";

        } else {

            message.innerHTML = "❌ Passwords do not match";
            message.className = "text-danger mt-2 d-block";

        }

    });

}

