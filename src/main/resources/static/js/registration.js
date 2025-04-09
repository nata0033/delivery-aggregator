document.addEventListener('DOMContentLoaded', function() {
    // Получаем элементы формы
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

    // Verification elements
    const verificationModal = document.getElementById('verificationModal');
    const emailDisplay = document.getElementById('emailDisplay');
    const verificationCodeInput = document.getElementById('verificationCodeInput');
    const resendLink = document.getElementById('resendLink');
    const countdownElement = document.getElementById('countdown');
    const confirmButton = document.getElementById('confirmButton');
    const verificationError = document.getElementById('verificationError');

    // Создаем контейнер для ошибок
    const errorContainer = document.createElement('div');
    errorContainer.id = 'errorContainer';
    errorContainer.style.width = '100%';
    errorContainer.style.marginTop = '15px';
    registrationButton.insertAdjacentElement('afterend', errorContainer);

    // Объект для хранения текущих ошибок и их элементов
    const errorElements = {
        lastName: null,
        firstName: null,
        fatherName: null,
        phone: null,
        email: null,
        password: null,
        repeatPassword: null
    };

    // Таймер для повторной отправки кода
    let countdownInterval;
    let countdown = 60;

    // Функция для создания/обновления элемента ошибки
    function updateErrorElement(field, message) {
        // Если сообщения нет, удаляем элемент ошибки
        if (!message) {
            if (errorElements[field]) {
                errorElements[field].remove();
                errorElements[field] = null;
            }
            return;
        }

        // Если элемент ошибки уже существует, обновляем текст
        if (errorElements[field]) {
            errorElements[field].textContent = message;
            return;
        }

        // Создаем новый элемент ошибки
        const errorElement = document.createElement('div');
        errorElement.className = `error-message ${field}-error`;
        errorElement.textContent = message;
        errorElement.style.animation = 'fadeIn 0.3s ease-out';

        // Добавляем в контейнер
        errorContainer.appendChild(errorElement);
        errorElements[field] = errorElement;
    }

    // Валидаторы для каждого поля
    const validators = {
        lastName: (value) => validateName(value, 'фамилию'),
        firstName: (value) => validateName(value, 'имя'),
        fatherName: (value) => validateName(value, 'отчество'),
        phone: validatePhone,
        email: validateEmail,
        password: validatePassword,
        repeatPassword: (value) => validateRepeatPassword(value, inputs.password.value)
    };

    // Общие функции валидации
    function validateName(value, fieldName) {
        if (!value.trim()) return null;
        const regex = fieldName === 'отчество' ? /^[а-яА-ЯёЁ-]+$/ : /^[а-яА-ЯёЁ]+$/;
        return !regex.test(value) ?
            `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} может содержать только буквы${fieldName === 'отчество' ? ' или прочерк' : ''}` :
            null;
    }

    function validatePhone(value) {
        if (!value.trim()) return null;
        if (/[a-zA-Z]/.test(value)) return 'Телефон не может содержать буквы';
        if (!/^\+\d{11}$/.test(value)) return 'Пример: +78006005040';
        return null;
    }

    function validateEmail(value) {
        if (!value.trim()) return null;
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) return 'Пример: example@example.example';
        return null;
    }

    function validatePassword(value) {
        if (!value.trim()) return null;
        return null; // Дополнительные проверки пароля можно добавить здесь
    }

    function validateRepeatPassword(value, passwordValue) {
        if (!value.trim()) return null;
        return value !== passwordValue ? 'Пароли не совпадают' : null;
    }

    // Проверка всей формы на валидность
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

    // Отправка кода подтверждения
    function sendVerificationCode() {
        const email = inputs.email.value.trim();

        fetch('/send-code', {
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
        }).finally(() => {
            document.getElementById('registrationSpinner').style.display = 'none';
        });
    }

    // Показать модальное окно подтверждения
    function showVerificationModal(email) {
        emailDisplay.textContent = email;
        verificationModal.style.display = 'flex';
        document.body.style.overflow = 'hidden'; // Блокируем прокрутку страницы

        // Запускаем таймер
        startCountdown();
    }

    // Запуск таймера для повторной отправки
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

    // Обновление текста таймера
    function updateCountdownText() {
        countdownElement.textContent = countdown;
        resendLink.innerHTML = `Отправить код повторно через <span id="countdown">${countdown}</span> секунд`;
    }

    // Подтверждение кода
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
                // Если код верный, отправляем форму регистрации
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

    // Обработчики событий
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
            // Показываем индикатор загрузки
            document.getElementById('registrationSpinner').style.display = 'block';
            sendVerificationCode()
        }
    });

    // Обработчик кнопки подтверждения
    confirmButton.addEventListener('click', confirmVerificationCode);

    // Обработчик ссылки повторной отправки
    resendLink.addEventListener('click', function(event) {
        if (!resendLink.classList.contains('disabled')) {
            event.preventDefault();
            document.getElementById('registrationSpinner').style.display = 'block';
            sendVerificationCode();
        }
    });

//    // Закрытие модального окна при клике вне его
//    verificationModal.addEventListener('click', function(event) {
//        if (event.target === verificationModal) {
//            verificationModal.style.display = 'none';
//            document.body.style.overflow = 'auto'; // Разблокируем прокрутку страницы
//            clearInterval(countdownInterval);
//        }
//    });

});