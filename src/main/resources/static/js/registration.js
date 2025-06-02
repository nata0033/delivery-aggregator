/**
 * Модуль для управления страницей регистрации
 */
import { loadHeader } from './common/header.js';
import { showErrorMessage } from './common/error.js';
import { checkAuthStatus, checkEmailUnique, sendVerificationCode, confirmVerificationCode, registerUser } from './common/api.js';
import { showSpinner } from './common/spinner.js';
import { FormValidator } from './common/validation.js';

class RegistrationManager {
    constructor() {
        this.form = null;
        this.modal = null;
        this.countdownInterval = null;
        this.init = this.init.bind(this);
        this.initForm = this.initForm.bind(this);
        this.initModal = this.initModal.bind(this);

        if (document.readyState === 'complete' || document.readyState === 'interactive') {
            setTimeout(this.init, 0);
        } else {
            document.addEventListener('DOMContentLoaded', this.init);
        }
    }

    async init() {
        try {
            showSpinner(true);
            await this.loadHeader();
            this.initForm();
            this.initModal();
        } catch (error) {
            console.error('Ошибка инициализации страницы:', error);
            showErrorMessage('Ошибка загрузки страницы: ' + error.message);
        } finally {
            showSpinner(false);
        }
    }

    async loadHeader() {
        try {
            const isAuthenticated = await checkAuthStatus();
            await loadHeader(isAuthenticated);
        } catch (error) {
            console.error('Ошибка проверки статуса аутентификации:', error);
        }
    }

    initForm() {
        this.form = document.getElementById('registrationForm');
        if (!this.form) {
            console.error('Форма регистрации не найдена');
            return;
        }

        this.loadFormFromCookies();

        this.form.addEventListener('submit', (e) => this.handleSubmit(e));

        const fields = [
            { input: this.form.querySelector('#lastNameInput'), validator: FormValidator.validateRussianText, error: 'Фамилия должна содержать только русские буквы' },
            { input: this.form.querySelector('#firstNameInput'), validator: FormValidator.validateRussianText, error: 'Имя должно содержать только русские буквы' },
            { input: this.form.querySelector('#fatherNameInput'), validator: FormValidator.validateRussianText, error: 'Отчество должно содержать только русские буквы' },
            { input: this.form.querySelector('#phoneInput'), validator: FormValidator.validatePhone, error: 'Введите корректный номер телефона' },
            { input: this.form.querySelector('#emailInput'), validator: FormValidator.validateEmail, error: 'Введите корректный email' },
            { input: this.form.querySelector('#passwordInput'), validator: (value) => value.length >= 6, error: 'Пароль должен содержать минимум 6 символов' },
            { input: this.form.querySelector('#repeatPasswordInput'), validator: (value) => value === this.form.querySelector('#passwordInput').value, error: 'Пароли не совпадают' }
        ];

        fields.forEach(({ input, validator, error }) => {
            if (input) {
                const validate = () => FormValidator.validateAndShow(input, validator, error);
                input.addEventListener('input', validate);
                input.addEventListener('blur', validate);
            }
        });
    }

    loadFormFromCookies() {
        const cookies = document.cookie.split(';').reduce((acc, cookie) => {
            const [key, value] = cookie.trim().split('=');
            acc[key] = value;
            return acc;
        }, {});

        const fields = ['lastNameInput', 'firstNameInput', 'fatherNameInput', 'phoneInput', 'emailInput', 'passwordInput', 'repeatPasswordInput'];
        fields.forEach(field => {
            const input = this.form.querySelector(`#${field}`);
            if (input && cookies[field]) {
                input.value = decodeURIComponent(cookies[field]);
            }
        });
    }

    async checkEmailUniqueAsync(email) {
        try {
            const response = await checkEmailUnique(email);
            if (!response.success) {
                const emailInput = this.form.querySelector('#emailInput');
                FormValidator.validateAndShow(emailInput, () => false, response.message || 'Пользователь с такой почтой уже существует');
                return false;
            }
            return true;
        } catch (error) {
            console.error('Ошибка проверки уникальности email:', error);
            showErrorMessage('Ошибка проверки email: ' + error.message);
            return false;
        }
    }

    async handleSubmit(e) {
        e.preventDefault();
        showSpinner(true);

        try {
            const inputs = this.form.querySelectorAll('input');
            const isFormValid = Array.from(inputs).every(input => {
                const validator = {
                    'lastNameInput': FormValidator.validateRussianText,
                    'firstNameInput': FormValidator.validateRussianText,
                    'fatherNameInput': FormValidator.validateRussianText,
                    'phoneInput': FormValidator.validatePhone,
                    'emailInput': FormValidator.validateEmail,
                    'passwordInput': (value) => value.length >= 6,
                    'repeatPasswordInput': (value) => value === this.form.querySelector('#passwordInput').value
                }[input.id];
                return input.value.trim() && validator(input.value.trim());
            });

            const emailInput = this.form.querySelector('#emailInput');
            if (!await this.checkEmailUniqueAsync(emailInput.value)) {
                return;
            }

            if (!isFormValid) {
                showErrorMessage('Пожалуйста, исправьте ошибки в форме, форма должна быть зеленой и с зеленой галочкой ');
                return;
            }

            inputs.forEach(input => {
                document.cookie = `${input.id}=${encodeURIComponent(input.value)}; path=/; max-age=86400`;
            });

            const response = await sendVerificationCode(emailInput.value);
            if (!response.success) {
                showErrorMessage(response.message || 'Ошибка отправки кода подтверждения');
                return;
            }

            this.showVerificationModal(emailInput.value);
        } catch (error) {
            console.error('Ошибка отправки кода:', error);
        } finally {
            showSpinner(false);
        }
    }

    initModal() {
        const modalElement = document.getElementById('emailVerificationModal');
        if (!modalElement) {
            console.error('Модальное окно emailVerificationModal не найдено');
            return;
        }

        modalElement.querySelector('.modal-body').innerHTML = `
            <h4>Подтверждение email</h4>
            <p id="verificationText">Введите код из письма отправленного на адрес <span id="emailDisplay"></span></p>
            <div class="form-group">
                <input type="text" class="form-control" id="verificationCodeInput" placeholder="Введите код">
                <div id="verificationError" class="invalid-feedback" style="display: none;"></div>
            </div>
            <div class="form-group">
                <a id="resendLink" class="resend-link disabled">Отправить код повторно через <span id="countdown">60</span></a>
            </div>
        `;

        this.modal = new bootstrap.Modal(modalElement);

        const confirmButton = modalElement.querySelector('#confirmEmailButton');
        const resendLink = modalElement.querySelector('#resendLink');

        if (confirmButton) {
            confirmButton.addEventListener('click', () => this.handleCodeConfirmation());
        }

        if (resendLink) {
            resendLink.addEventListener('click', () => this.handleResendCode());
        }

        modalElement.addEventListener('hidden.bs.modal', () => {
            if (this.countdownInterval) {
                clearInterval(this.countdownInterval);
            }
            const countdown = modalElement.querySelector('#countdown');
            if (countdown) countdown.textContent = '60';
            if (resendLink) resendLink.classList.add('disabled');
            const verificationInput = modalElement.querySelector('#verificationCodeInput');
            if (verificationInput) {
                verificationInput.value = '';
                verificationInput.classList.remove('is-invalid');
            }
            const errorElement = modalElement.querySelector('#verificationError');
            if (errorElement) errorElement.style.display = 'none';
        });
    }

    showVerificationModal(email) {
        const emailDisplay = document.querySelector('#emailDisplay');
        if (emailDisplay) emailDisplay.textContent = email;

        this.startCountdown();
        if (this.modal) this.modal.show();
    }

    startCountdown() {
        const countdownElement = document.querySelector('#countdown');
        const resendLink = document.querySelector('#resendLink');
        let timeLeft = 60;

        if (this.countdownInterval) clearInterval(this.countdownInterval);

        this.countdownInterval = setInterval(() => {
            timeLeft--;
            if (countdownElement) countdownElement.textContent = timeLeft;
            if (resendLink && timeLeft <= 0) {
                clearInterval(this.countdownInterval);
                resendLink.classList.remove('disabled');
            }
        }, 1000);
    }

    async handleCodeConfirmation() {
        const email = document.querySelector('#emailDisplay')?.textContent;
        const codeInput = document.querySelector('#verificationCodeInput');
        const errorElement = document.querySelector('#verificationError');

        if (!codeInput?.value.trim()) {
            codeInput?.classList.add('is-invalid');
            if (errorElement) {
                errorElement.textContent = 'Введите код';
                errorElement.style.display = 'block';
            }
            return;
        }

        showSpinner(true);
        try {
            const response = await confirmVerificationCode(email, codeInput.value);
            if (!response.success) {
                codeInput?.classList.add('is-invalid');
                if (errorElement) {
                    errorElement.textContent = response.message || 'Неверный код';
                    errorElement.style.display = 'block';
                }
                return;
            }

            await this.registerUser();
        } catch (error) {
            console.error('Ошибка подтверждения кода:', error);
            codeInput?.classList.add('is-invalid');
            if (errorElement) {
                errorElement.textContent = 'Ошибка подтверждения кода';
                errorElement.style.display = 'block';
            }
        } finally {
            showSpinner(false);
        }
    }

    async handleResendCode() {
        const resendLink = document.querySelector('#resendLink');
        if (resendLink?.classList.contains('disabled')) return;

        const email = document.querySelector('#emailDisplay')?.textContent;
        if (!email) return;

        showSpinner(true);
        try {
            const response = await sendVerificationCode(email);
            if (!response.success) {
                showErrorMessage(response.message || 'Ошибка отправки кода');
                return;
            }
            this.startCountdown();
            resendLink.classList.add('disabled');
        } catch (error) {
            console.error('Ошибка повторной отправки кода:', error);
        } finally {
            showSpinner(false);
        }
    }

    async registerUser() {
        const formData = {
            lastName: this.form.querySelector('#lastNameInput')?.value,
            firstName: this.form.querySelector('#firstNameInput')?.value,
            fatherName: this.form.querySelector('#fatherNameInput')?.value,
            phone: this.form.querySelector('#phoneInput')?.value,
            email: this.form.querySelector('#emailInput')?.value,
            password: this.form.querySelector('#passwordInput')?.value
        };

        showSpinner(true);
        try {
            const response = await registerUser(formData);
            if (response.id) {
                this.modal.hide();
                window.location.href = '/account';
            } else {
                showErrorMessage('Ошибка регистрации пользователя');
            }
        } catch (error) {
            console.error('Ошибка регистрации:', error);
        } finally {
            showSpinner(false);
        }
    }
}

new RegistrationManager();