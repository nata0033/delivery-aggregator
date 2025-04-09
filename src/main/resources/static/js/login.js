document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.querySelector('form[th\\:action="@{/login}"]');
    if (!loginForm) return;

    // Получаем элементы формы
    const loginInput = document.getElementById('loginInput');
    const passwordInput = document.getElementById('passwordInput');
    const submitButton = document.getElementById('loginButton');

    // Создаем контейнер для ошибок
    const errorContainer = document.createElement('div');
    errorContainer.id = 'loginErrorContainer';
    errorContainer.style.width = '100%';
    errorContainer.style.marginTop = '15px';
    submitButton.insertAdjacentElement('afterend', errorContainer);

    // Объект для хранения текущих ошибок
    const errors = {
        login: null,
        password: null
    };

    // Функция отображения ошибок
    function displayErrors() {
        errorContainer.innerHTML = '';

        // Добавляем ошибки в контейнер (новые сверху)
        Object.values(errors).filter(error => error !== null).forEach(error => {
            const errorElement = document.createElement('div');
            errorElement.className = 'alert alert-warning';
            errorElement.style.width = '100%';
            errorElement.style.marginBottom = '10px';
            errorElement.style.padding = '10px';
            errorElement.style.borderRadius = '4px';
            errorElement.style.animation = 'fadeIn 0.3s ease-out';
            errorElement.textContent = error;
            errorContainer.appendChild(errorElement);
        });
    }

    // Валидация логина (email или телефон)
    function validateLogin(value) {
        if (!value.trim()) {
            errors.login = 'Введите почту или телефон';
            return false;
        }

        const isEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
        const isPhone = /^\+\d{11}$/.test(value);

        if (!isEmail && !isPhone) {
            errors.login = 'Некорректный формат. Примеры:\nПочта: example@example.com\nТелефон: +78007008078';
            return false;
        }

        errors.login = null;
        return true;
    }

    // Валидация пароля
    function validatePassword(value) {
        if (!value.trim()) {
            errors.password = 'Введите пароль';
            return false;
        }

        errors.password = null;
        return true;
    }

    // Обработчики событий для полей ввода
    loginInput.addEventListener('input', function() {
        validateLogin(loginInput.value);
        displayErrors();
    });

    passwordInput.addEventListener('input', function() {
        validatePassword(passwordInput.value);
        displayErrors();
    });

    // Обработчики потери фокуса
    loginInput.addEventListener('blur', function() {
        if (!loginInput.value.trim()) {
            errors.login = 'Введите почту или телефон';
            displayErrors();
        }
    });

    passwordInput.addEventListener('blur', function() {
        if (!passwordInput.value.trim()) {
            errors.password = 'Введите пароль';
            displayErrors();
        }
    });

    // Валидация при отправке формы
    loginForm.addEventListener('submit', function(event) {
        const isLoginValid = validateLogin(loginInput.value);
        const isPasswordValid = validatePassword(passwordInput.value);

        displayErrors();

        if (!isLoginValid || !isPasswordValid) {
            event.preventDefault();
        }
    });
});