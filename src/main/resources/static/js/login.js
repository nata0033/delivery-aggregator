/**
 * Модуль для управления страницей логина
 */
import { getCookie, setCookie } from './common/cookies.js';
import { loadHeader } from './common/header.js';
import { showErrorMessage } from './common/messages.js';
import { FormValidator } from './common/validation.js';
import { showSpinner } from './common/spinner.js';
import { submitLogin } from './common/api.js';

class LoginManager {
    constructor() {
        document.addEventListener('DOMContentLoaded', () => this.init());
    }

    async init() {
        await loadHeader();
        this.loadFormData();
        this.initEventHandlers();
        this.initValidation();
    }

    initEventHandlers() {
        document.getElementById('submit').addEventListener('click', (e) => this.sendLogin(e));
    }

    loadFormData() {
        const deliveryDataRaw = getCookie('delivery_data');
        if (!deliveryDataRaw) return;

        try {
            const deliveryData = JSON.parse(deliveryDataRaw);
            if (deliveryData.sender && deliveryData.sender.email) {
                document.getElementById('loginInput').value = deliveryData.sender.email;
            }
        } catch (e) {
            console.error('Ошибка парсинга delivery_data:', e);
        }
    }

    initValidation() {
        const fields = [
            { id: 'loginInput', validator: FormValidator.validateEmail, error: 'Введите корректный email' },
            { id: 'passwordInput', validator: (value) => value.trim() !== '', error: 'Пароль не может быть пустым' }
        ];

        fields.forEach(({ id, validator, error }) => {
            const input = document.getElementById(id);
            if (input) {
                const validate = () => FormValidator.validateAndShow(input, validator, error);
                input.addEventListener('input', validate);
                input.addEventListener('blur', validate);
            }
        });
    }

    isFormValid() {
        const fields = [
            { id: 'loginInput', validator: FormValidator.validateEmail },
            { id: 'passwordInput', validator: (value) => value.trim() !== '' }
        ];

        return fields.every(({ id, validator }) => {
            const input = document.getElementById(id);
            return input.value.trim() && validator(input.value.trim());
        });
    }

    async sendLogin(event) {
        event.preventDefault();
        if (!this.isFormValid()) {
            showErrorMessage('Пожалуйста, заполните все поля корректно');
            return;
        }

        showSpinner(true);
        try {
            const enteredEmail = document.getElementById('loginInput').value.trim();
            const password = document.getElementById('passwordInput').value.trim();
            const deliveryDataRaw = getCookie('delivery_data');
            let deliveryData = {};

            if (deliveryDataRaw) {
                try {
                    deliveryData = JSON.parse(deliveryDataRaw);
                } catch (e) {
                    console.error('Ошибка парсинга delivery_data:', e);
                }
            }

            if (deliveryData.sender && deliveryData.sender.email !== enteredEmail) {
                deliveryData.sender = {
                    email: enteredEmail,
                    firstName: '',
                    lastName: '',
                    fatherName: '',
                    phone: ''
                };
                setCookie('delivery_data', JSON.stringify(deliveryData), 7);
            }

            await submitLogin(enteredEmail, password);
        } catch (error) {
            console.error('Ошибка отправки формы:', error);
        } finally {
            showSpinner(false);
        }
    }
}

new LoginManager();