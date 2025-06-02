/**
 * Модуль для управления страницей логина
 * Формирует заголовок, заполняет поле логина из cookie, валидирует форму и отправляет данные
 */
import { getCookie, setCookie } from './common/cookies.js';
import { loadHeader } from './common/header.js';
import { showErrorMessage, showValidateFormError, showValidateFormSuccess } from './common/error.js';
import { FormValidator } from './common/validation.js';
import { showSpinner } from './common/spinner.js';
import { submitLogin } from './common/api.js';

class LoginManager {
  constructor() {
    document.addEventListener('DOMContentLoaded', () => this.init());
  }

  /**
   * Основная функция инициализации
   */
  async init() {
    await loadHeader();
    this.loadFormData();
    this.initEventHandlers();
    this.initValidation();
  }

  /**
   * Инициализирует обработчики событий
   */
  initEventHandlers() {
    document.getElementById('submit').addEventListener('click', (e) => this.sendLogin(e));
  }

  /**
   * Загружает данные из куки и заполняет форму
   */
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

  /**
   * Инициализирует валидацию полей формы
   */
  initValidation() {
    const fields = [
      { id: 'loginInput', validator: FormValidator.validateEmail, error: 'Введите корректный email' },
      { id: 'passwordInput', validator: (value) => value.trim() !== '', error: 'Пароль не может быть пустым' },
    ];

    fields.forEach(({ id, validator, error }) => {
      const input = document.getElementById(id);
      const validate = () => this.validateField(input, validator, error);
      input.addEventListener('input', validate);
      input.addEventListener('blur', validate);
    });
  }

  /**
   * Проверяет корректность поля
   * @param {HTMLElement} input
   * @param {Function} validator
   * @param {string} errorMessage
   */
  validateField(input, validator, errorMessage) {
    const value = input.value.trim();
    const errorMessageId = `${input.id}-error`;

    if (!value) {
      input.classList.remove('input-error', 'input-success');
      input.removeAttribute('title');
      const existingError = document.getElementById(errorMessageId);
      if (existingError) existingError.remove();
      return;
    }

    const isValid = validator(value);
    if (isValid) {
      showValidateFormSuccess(input);
    } else {
      showValidateFormError(input, errorMessageId, errorMessage);
    }
  }

  /**
   * Проверяет, все ли поля формы валидны
   * @returns {boolean}
   */
  isFormValid() {
    const fields = [
      { id: 'loginInput', validator: FormValidator.validateEmail, error: 'Введите корректный email' },
      { id: 'passwordInput', validator: (value) => value.trim() !== '', error: 'Пароль не может быть пустым' },
    ];

    let isValid = true;
    fields.forEach(({ id, validator, error }) => {
      const input = document.getElementById(id);
      const value = input.value.trim();
      if (!value || !validator(value)) {
        showValidateFormError(input, `${id}-error`, error);
        isValid = false;
      }
    });
    return isValid;
  }

  /**
   * Отправляет форму логина
   * @param {Event} event
   */
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

      // Проверяем совпадение email с sender.email в cookie
      if (deliveryData.sender && deliveryData.sender.email !== enteredEmail) {
        // Обновляем sender.email и очищаем остальные поля sender
        deliveryData.sender = {
          email: enteredEmail,
          firstName: '',
          lastName: '',
          fatherName: '',
          phone: ''
        };
        setCookie('delivery_data', JSON.stringify(deliveryData), 7);
      }

      // Отправляем данные через API
      await submitLogin(enteredEmail, password);
    } catch (error) {
      showErrorMessage(error.message || 'Произошла ошибка при отправке формы');
    } finally {
      showSpinner(false);
    }
  }
}

new LoginManager();