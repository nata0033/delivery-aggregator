document.addEventListener('DOMContentLoaded', function() {
    // Основные элементы формы
    const loginForm = document.getElementById("loginForm");
    if (!loginForm) return;

    const loginInput = document.getElementById('loginInput');
    const passwordInput = document.getElementById('passwordInput');
    const submitButton = document.getElementById('loginButton');

    // Контейнер для отображения ошибок
    const errorContainer = document.createElement('div');
    errorContainer.id = 'loginErrorContainer';
    errorContainer.style.width = '100%';
    errorContainer.style.marginTop = '15px';
    submitButton.insertAdjacentElement('afterend', errorContainer);

    // Хранилище элементов ошибок
    const errorElements = {
        login: null,
        password: null
    };

    /**
     * Обновляет или создает элемент ошибки для указанного поля
     * @param {string} field - Название поля ('login' или 'password')
     * @param {string|null} message - Текст ошибки (null для удаления)
     * @param {HTMLElement} errorContainer - Контейнер для ошибок
     * @param {Object} errorElements - Хранилище элементов ошибок
     */
    function updateErrorElement(field, message) {
        // Если ошибки нет - удаляем элемент
        if (!message) {
            if (errorElements[field]) {
                errorElements[field].remove();
                errorElements[field] = null;
            }
            return;
        }

        // Обновляем существующий элемент
        if (errorElements[field]) {
            errorElements[field].textContent = message;
            return;
        }

        // Создаем новый элемент ошибки
        const errorElement = document.createElement('div');
        errorElement.className = `error-message ${field}-error`;
        errorElement.textContent = message;
        errorElement.style.width = '100%';
        errorElement.style.marginBottom = '10px';
        errorElement.style.padding = '10px';
        errorElement.style.borderRadius = '4px';
        errorElement.style.animation = 'fadeIn 0.3s ease-out';
        errorElement.style.backgroundColor = '#fff3cd';
        errorElement.style.color = '#856404';
        errorElement.style.border = '1px solid #ffeeba';

        errorContainer.appendChild(errorElement);
        errorElements[field] = errorElement;
    }

    /**
     * Валидирует поле логина
     * @param {string} value - Значение поля
     * @param {function} updateErrorElement - Функция обновления ошибок
     * @returns {boolean} - Результат валидации
     */
    function validateLogin(value) {
        if (!value.trim()) {
            updateErrorElement('login', 'Введите почту');
            return false;
        }

        const isEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
        const isPhone = /^\+\d{11}$/.test(value);

        if (!isEmail && !isPhone) {
            updateErrorElement('login', 'Пример: example@example.com');
            return false;
        }

        updateErrorElement('login', null);
        return true;
    }

    /**
     * Валидирует поле пароля
     * @param {string} value - Значение поля
     * @param {function} updateErrorElement - Функция обновления ошибок
     * @returns {boolean} - Результат валидации
     */
    function validatePassword(value) {
        if (!value.trim()) {
            updateErrorElement('password', 'Введите пароль');
            return false;
        }

        updateErrorElement('password', null);
        return true;
    }

    /**
     * Проверяет существование пользователя по email
     * @param {string} email - Email для проверки
     * @returns {Promise<Object>} - Результат проверки {success, message}
     */
    async function checkUserExists(email) {
        try {
            const response = await fetch('/check-email-exist', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Accept': 'application/json'
                },
                body: `email=${encodeURIComponent(email)}`
            });
            if (!response.ok) {
                throw new Error('Ошибка сервера');
            }
            return await response.json();
        } catch (error) {
            console.error('Ошибка при проверке пользователя:', error);
            return {
                success: false,
                message: 'Ошибка при проверке пользователя'
            };
        }
    }

    // Обработчики событий полей формы
    loginInput.addEventListener('input', function() {
        validateLogin(loginInput.value);
    });

    passwordInput.addEventListener('input', function() {
        validatePassword(passwordInput.value);
    });

    loginInput.addEventListener('blur', function() {
        if (!loginInput.value.trim()) {
            updateErrorElement('login', 'Введите почту');
        }
    });

    passwordInput.addEventListener('blur', function() {
        if (!passwordInput.value.trim()) {
            updateErrorElement('password', 'Введите пароль');
        }
    });

    // Обработчик отправки формы
    loginForm.addEventListener('submit', async function(event) {
        event.preventDefault();

        const email = loginInput.value.trim();
        const password = passwordInput.value.trim();

        // Валидация полей
        const isLoginValid = validateLogin(email);
        const isPasswordValid = validatePassword(password);

        if (!isLoginValid || !isPasswordValid) {
            return;
        }

        // Проверка существования пользователя
        const checkResult = await checkUserExists(email);

        if (!checkResult.success) {
            updateErrorElement('login', checkResult.message || 'Пользователь с такой почтой не существует');
            return;
        }

        // Отправка формы если все проверки пройдены
        this.submit();
    });
});