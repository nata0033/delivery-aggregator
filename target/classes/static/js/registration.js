document.addEventListener('DOMContentLoaded', function() {
    // ========== ЭЛЕМЕНТЫ ФОРМЫ ==========
    const form = document.getElementById('registrationForm');
    const registrationButton = document.getElementById('registrationButton');
    const inputs = {
        lastName: document.getElementById('lastNameInput'),
        firstName: document.getElementById('firstNameInput'),
        fatherName: document.getElementById('fatherNameInput'),
        phone: document.getElementById('phoneInput'),
        email: document.getElementById('emailInput'),
        password: document.getElementById('passwordInput'),
        repeatPassword: document.getElementById('repeatPasswordInput')
    };

    // ========== ЭЛЕМЕНТЫ ВЕРИФИКАЦИИ ==========
    const verificationModal = document.getElementById('verificationModal');
    const emailDisplay = document.getElementById('emailDisplay');
    const verificationCodeInput = document.getElementById('verificationCodeInput');
    const resendLink = document.getElementById('resendLink');
    const countdownElement = document.getElementById('countdown');
    const confirmButton = document.getElementById('confirmButton');
    const verificationError = document.getElementById('verificationError');

    // ========== ДИНАМИЧЕСКИ СОЗДАВАЕМЫЕ ЭЛЕМЕНТЫ ==========
    const errorContainer = document.createElement('div');
    errorContainer.id = 'errorContainer';
    errorContainer.style.width = '100%';
    errorContainer.style.marginTop = '15px';
    registrationButton.insertAdjacentElement('afterend', errorContainer);

    // ========== ПЕРЕМЕННЫЕ СОСТОЯНИЯ ==========
    const errorElements = {
        lastName: null,
        firstName: null,
        fatherName: null,
        phone: null,
        email: null,
        password: null,
        repeatPassword: null
    };

    let countdownInterval;
    let countdown = 60;

    // ========== ВАЛИДАЦИОННЫЕ ФУНКЦИИ ==========

    /**
     * Валидирует ФИО (фамилию, имя, отчество)
     * @param {string} value - Значение поля
     * @param {string} fieldName - Название поля ('фамилию', 'имя', 'отчество')
     * @returns {string|null} - Сообщение об ошибке или null если валидно
     */
    function validateName(value, fieldName) {
        if (!value.trim()) return null;
        const regex = fieldName === 'отчество' ? /^[а-яА-ЯёЁ-]+$/ : /^[а-яА-ЯёЁ]+$/;
        return !regex.test(value) ?
            `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} может содержать только буквы${fieldName === 'отчество' ? ' или прочерк' : ''}` :
            null;
    }

    /**
     * Валидирует телефонный номер
     * @param {string} value - Номер телефона
     * @returns {string|null} - Сообщение об ошибке или null если валидно
     */
    function validatePhone(value) {
        if (!value.trim()) return null;
        if (/[a-zA-Z]/.test(value)) return 'Телефон не может содержать буквы';
        if (!/^\+\d{11}$/.test(value)) return 'Пример: +78006005040';
        return null;
    }

    /**
     * Валидирует email
     * @param {string} value - Email адрес
     * @returns {string|null} - Сообщение об ошибке или null если валидно
     */
    function validateEmail(value) {
        if (!value.trim()) return null;
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) return 'Пример: example@example.example';
        return null;
    }

    /**
     * Валидирует пароль
     * @param {string} value - Пароль
     * @returns {string|null} - Сообщение об ошибке или null если валидно
     */
    function validatePassword(value) {
        if (!value.trim()) return null;
        return null; // Дополнительные проверки пароля можно добавить здесь
    }

    /**
     * Проверяет совпадение паролей
     * @param {string} value - Повторный пароль
     * @param {string} passwordValue - Оригинальный пароль
     * @returns {string|null} - Сообщение об ошибке или null если совпадают
     */
    function validateRepeatPassword(value, passwordValue) {
        if (!value.trim()) return null;
        return value !== passwordValue ? 'Пароли не совпадают' : null;
    }

    // Объект валидаторов для каждого поля
    const validators = {
        lastName: (value) => validateName(value, 'фамилию'),
        firstName: (value) => validateName(value, 'имя'),
        fatherName: (value) => validateName(value, 'отчество'),
        phone: validatePhone,
        email: validateEmail,
        password: validatePassword,
        repeatPassword: (value) => validateRepeatPassword(value, inputs.password.value)
    };

    // ========== ФУНКЦИИ РАБОТЫ С ОШИБКАМИ ==========

    /**
     * Обновляет или создает элемент ошибки для поля
     * @param {string} field - Имя поля (ключ из errorElements)
     * @param {string|null} message - Текст ошибки (null для удаления)
     */
    function updateErrorElement(field, message) {
        if (!message) {
            if (errorElements[field]) {
                errorElements[field].remove();
                errorElements[field] = null;
            }
            return;
        }

        if (errorElements[field]) {
            errorElements[field].textContent = message;
            return;
        }

        const errorElement = document.createElement('div');
        errorElement.className = `error-message ${field}-error`;
        errorElement.textContent = message;
        errorElement.style.animation = 'fadeIn 0.3s ease-out';

        errorContainer.appendChild(errorElement);
        errorElements[field] = errorElement;
    }

    /**
     * Проверяет валидность всей формы
     * @returns {boolean} - true если форма валидна, false если есть ошибки
     */
    function isFormValid() {
        let isValid = true;

        Object.entries(inputs).forEach(([field, input]) => {
            if (!input) return;

            const value = input.value.trim();
            let errorMessage = null;

            if (!value) {
                errorMessage = field === 'repeatPassword' ? 'Повторите пароль' :
                             field === 'fatherName' ? 'Введите отчество или прочерк' :
                              `Введите ${field === 'phone' ? 'телефон' : field === 'email' ? 'почту' : field === 'firstName' ? 'имя' : field === 'lastName' ? 'фамилию' : field === 'fatherName' ? 'отчество' : field}`;
                isValid = false;
            } else {
                errorMessage = validators[field](value);
                if (errorMessage) isValid = false;
            }

            updateErrorElement(field, errorMessage);
        });

        return isValid;
    }

    // ========== ФУНКЦИИ ВЕРИФИКАЦИИ ==========

    /**
     * Обновляет текст таймера обратного отсчета
     */
    function updateCountdownText() {
        countdownElement.textContent = countdown;
        resendLink.innerHTML = `Отправить код повторно через <span id="countdown">${countdown}</span> секунд`;
    }

    /**
     * Запускает таймер обратного отсчета для повторной отправки кода
     */
    function startCountdown() {
        countdown = 60;
        resendLink.classList.add('disabled');
        updateCountdownText();

        countdownInterval = setInterval(() => {
            countdown--;
            updateCountdownText();

            if (countdown <= 0) {
                clearInterval(countdownInterval);
                resendLink.classList.remove('disabled');
                resendLink.textContent = 'Отправить код повторно';
            }
        }, 1000);
    }

    /**
     * Показывает модальное окно верификации
     * @param {string} email - Email для отображения
     */
    function showVerificationModal(email) {
        emailDisplay.textContent = email;
        verificationModal.style.display = 'flex';
        document.body.style.overflow = 'hidden';
        startCountdown();
    }

    /**
     * Отправляет код подтверждения на email
     * @returns {Promise} - Promise с результатом отправки
     */
    function sendVerificationCode() {
        const email = inputs.email.value.trim();

        return fetch('/send-code', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `email=${encodeURIComponent(email)}`
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка при отправке кода подтверждения в контроллере');
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                showVerificationModal(email);
            } else {
                updateErrorElement('email', data.message || 'Ошибка при отправке кода подтверждения');
            }
            return data;
        })
        .catch(error => {
            console.error('Error:', error);
            updateErrorElement('email', 'Ошибка при отправке кода подтверждения');
            throw error;
        })
        .finally(() => {
            document.getElementById('registrationSpinner').style.display = 'none';
        });
    }

    /**
     * Подтверждает введенный код верификации
     */
    function confirmVerificationCode() {
        const email = inputs.email.value.trim();
        const code = verificationCodeInput.value.trim();

        if (!code) {
            verificationError.textContent = 'Введите код подтверждения';
            verificationError.style.display = 'block';
            return;
        }

        fetch('/confirm-code', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `email=${encodeURIComponent(email)}&code=${encodeURIComponent(code)}`
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                form.submit();
            } else {
                verificationError.textContent = data.message || 'Неверный код';
                verificationError.style.display = 'block';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            verificationError.textContent = 'Ошибка при проверке кода';
            verificationError.style.display = 'block';
        });
    }

    // ========== ОБРАБОТЧИКИ СОБЫТИЙ ==========

    // Обработчики ввода и потери фокуса для полей формы
    Object.entries(inputs).forEach(([field, input]) => {
        if (!input) return;

        input.addEventListener('input', () => {
            const errorMessage = validators[field](input.value.trim());
            updateErrorElement(field, errorMessage);
        });

        input.addEventListener('blur', () => {
            if (!input.value.trim()) {
                const message = field === 'repeatPassword' ? 'Повторите пароль' :
                              field === 'fatherName' ? 'Введите отчество или прочерк' :
                              `Введите ${field === 'phone' ? 'телефон' : field === 'email' ? 'почту' : field === 'firstName' ? 'имя' : field === 'lastName' ? 'фамилию' : field === 'fatherName' ? 'отчество' : field}`;
                updateErrorElement(field, message);
            }
        });
    });

    // Обработчик кнопки регистрации
    registrationButton.addEventListener('click', function(event) {
        event.preventDefault();

        if (isFormValid()) {
            const email = inputs.email.value.trim();

            // Проверка уникальности email перед отправкой кода
            fetch('/check-email-unique', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `email=${encodeURIComponent(email)}`
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Ошибка при проверке email');
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    document.getElementById('registrationSpinner').style.display = 'block';
                    return sendVerificationCode();
                } else {
                    updateErrorElement('email', data.message || 'Пользователь с такой почтой уже существует');
                    document.getElementById('registrationSpinner').style.display = 'none';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                updateErrorElement('email', 'Ошибка при проверке email');
                document.getElementById('registrationSpinner').style.display = 'none';
            });
        }
    });

    // Обработчик кнопки подтверждения кода
    confirmButton.addEventListener('click', confirmVerificationCode);

    // Обработчик повторной отправки кода
    resendLink.addEventListener('click', function(event) {
        if (!resendLink.classList.contains('disabled')) {
            event.preventDefault();
            document.getElementById('registrationSpinner').style.display = 'block';
            sendVerificationCode();
        }
    });
});