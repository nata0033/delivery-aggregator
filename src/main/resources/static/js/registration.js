/**
 * Модуль для управления страницей регистрации
 */
import { loadHeader } from './common/header.js';
import { showErrorMessage } from './common/error.js';
import { checkAuthStatus, checkEmailUnique, sendVerificationCode, confirmVerificationCode, registerUser } from './common/api.js';
import { showSpinner } from './common/spinner.js';

class RegistrationManager {
  constructor() {
    this.form = null;
    this.modal = null;
    this.countdownInterval = null;
    this.init = this.init.bind(this);
    this.initForm = this.initForm.bind(this);
    this.initModal = this.initModal.bind(this);

    // Ждём полной загрузки DOM
    if (document.readyState === 'complete' || document.readyState === 'interactive') {
      setTimeout(this.init, 0);
    } else {
      document.addEventListener('DOMContentLoaded', this.init);
    }
  }

  /**
   * Основная функция инициализации
   */
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

  /**
   * Загружает header с учетом статуса аутентификации
   */
  async loadHeader() {
    try {
      const isAuthenticated = await checkAuthStatus();
      await loadHeader(isAuthenticated);
    } catch (error) {
      console.error('Ошибка проверки статуса аутентификации:', error);
    }
  }

  /**
   * Инициализирует форму регистрации
   */
  initForm() {
    this.form = document.getElementById('registrationForm');
    if (!this.form) {
      console.error('Форма регистрации не найдена');
      return;
    }

    // Загружаем данные из куки
    this.loadFormFromCookies();

    // Добавляем обработчики событий
    this.form.addEventListener('submit', (e) => this.handleSubmit(e));

    // Валидация при вводе
    const inputs = this.form.querySelectorAll('input');
    inputs.forEach(input => {
      input.addEventListener('input', () => this.validateField(input));
      input.addEventListener('blur', () => this.validateField(input));
    });
  }

  /**
   * Загружает данные формы из куки
   */
  loadFormFromCookies() {
    const cookies = document.cookie.split(';').reduce((acc, cookie) => {
      const [key, value] = cookie.trim().split('=');
      acc[key] = value;
      return acc;
    }, {});

    const fields = ['lastName', 'firstName', 'fatherName', 'phone', 'email', 'password', 'passwordConfirm'];
    fields.forEach(field => {
      const input = this.form.querySelector(`#${field}`);
      if (input && cookies[field]) {
        input.value = decodeURIComponent(cookies[field]);
        this.validateField(input);
      }
    });
  }

  /**
   * Валидирует отдельное поле
   * @param {HTMLElement} input - Поле ввода
   * @returns {boolean} - Результат валидации
   */
  validateField(input) {
    let isValid = true;
    const errorElement = input.nextElementSibling;

    // Создаем элемент для ошибки, если его нет
    if (!errorElement || !errorElement.classList.contains('invalid-feedback')) {
      const newErrorElement = document.createElement('div');
      newErrorElement.className = 'invalid-feedback';
      input.parentNode.appendChild(newErrorElement);
    }

    if (input.required && !input.value.trim()) {
      errorElement.textContent = 'Это поле обязательно';
      isValid = false;
    } else if (input.id === 'email') {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(input.value)) {
        errorElement.textContent = 'Введите корректный email';
        isValid = false;
      }
    } else if (input.id === 'phone') {
      const phoneRegex = /^\+?[1-9]\d{1,14}$/;
      if (!phoneRegex.test(input.value)) {
        errorElement.textContent = 'Введите корректный номер телефона';
        isValid = false;
      }
    } else if (input.id === 'password') {
      if (input.value.length < 6) {
        errorElement.textContent = 'Пароль должен содержать минимум 6 символов';
        isValid = false;
      }
    } else if (input.id === 'repeatPasswordInput') {
      const password = this.form.querySelector('#passwordInput').value;
      if (input.value !== password) {
        errorElement.textContent = 'Пароли не совпадают';
        isValid = false;
      }
    }

    input.classList.toggle('is-invalid', !isValid);
    input.classList.toggle('is-valid', isValid);
    return isValid;
  }

  /**
   * Проверяет уникальность email
   * @param {string} email - Email для проверки
   * @returns {Promise<boolean>} - Результат проверки
   */
  async checkEmailUniqueAsync(email) {
    try {
      const response = await checkEmailUnique(email);
      if (!response.success) {
        const emailInput = this.form.querySelector('#emailInput');
        const errorElement = emailInput.nextElementSibling;
        if (errorElement && errorElement.classList.contains('invalid-feedback')) {
          errorElement.textContent = response.message || 'Пользователь с такой почтой уже существует';
        }
        emailInput.classList.add('is-invalid');
        return false;
      }
      return true;
    } catch (error) {
      console.error('Ошибка проверки уникальности email:', error);
      showErrorMessage('Ошибка проверки email: ' + error.message);
      return false;
    }
  }

  /**
   * Обрабатывает отправку формы
   * @param {Event} e - Событие отправки формы
   */
  async handleSubmit(e) {
    e.preventDefault();
    showSpinner(true);

    try {
      // Валидация всех полей
      const inputs = this.form.querySelectorAll('input');
      let isFormValid = true;
      inputs.forEach(input => {
        if (!this.validateField(input)) {
          isFormValid = false;
        }
      });

      // Проверка уникальности email
      const emailInput = this.form.querySelector('#emailInput');
      if (!await this.checkEmailUniqueAsync(emailInput.value)) {
        isFormValid = false;
      }

      if (!isFormValid) {
        showErrorMessage('Пожалуйста, исправьте ошибки в форме');
        return;
      }

      // Сохраняем данные в куки
      inputs.forEach(input => {
        document.cookie = `${input.id}=${encodeURIComponent(input.value)}; path=/; max-age=86400`; // 1 день
      });

      // Отправляем код подтверждения
      const response = await sendVerificationCode(emailInput.value);
      if (!response.success) {
        showErrorMessage(response.message || 'Ошибка отправки кода подтверждения');
        return;
      }

      // Показываем модальное окно
      this.showVerificationModal(emailInput.value);
    } catch (error) {
      console.error('Ошибка отправки кода:', error);
      showErrorMessage('Ошибка отправки кода: ' + error.message);
    } finally {
      showSpinner(false);
    }
  }

  /**
   * Инициализирует модальное окно
   */
  initModal() {
    const modalElement = document.getElementById('emailVerificationModal');
    if (!modalElement) {
      console.error('Модальное окно emailVerificationModal не найдено');
      return;
    }

    // Добавляем содержимое модального окна
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

    // Инициализируем обработчики кнопок модального окна
    const confirmButton = modalElement.querySelector('#confirmEmailButton');
    const resendLink = modalElement.querySelector('#resendLink');

    if (confirmButton) {
      confirmButton.addEventListener('click', () => this.handleCodeConfirmation());
    }

    if (resendLink) {
      resendLink.addEventListener('click', () => this.handleResendCode());
    }

    // Обработчик скрытия модального окна
    modalElement.addEventListener('hidden.bs.modal', () => {
      if (this.countdownInterval) {
        clearInterval(this.countdownInterval);
      }
      const countdown = modalElement.querySelector('#countdown');
      if (countdown) {
        countdown.textContent = '60';
      }
      if (resendLink) {
        resendLink.classList.add('disabled');
      }
      const verificationInput = modalElement.querySelector('#verificationCodeInput');
      if (verificationInput) {
        verificationInput.value = '';
        verificationInput.classList.remove('is-invalid');
      }
      const errorElement = modalElement.querySelector('#verificationError');
      if (errorElement) {
        errorElement.style.display = 'none';
      }
    });
  }

  /**
   * Показывает модальное окно верификации
   * @param {string} email - Email пользователя
   */
  showVerificationModal(email) {
    const emailDisplay = document.querySelector('#emailDisplay');
    if (emailDisplay) {
      emailDisplay.textContent = email;
    }

    // Запускаем таймер
    this.startCountdown();

    // Показываем модальное окно
    if (this.modal) {
      this.modal.show();
    }
  }

  /**
   * Запускает таймер обратного отсчета
   */
  startCountdown() {
    const countdownElement = document.querySelector('#countdown');
    const resendLink = document.querySelector('#resendLink');
    let timeLeft = 60;

    if (this.countdownInterval) {
      clearInterval(this.countdownInterval);
    }

    this.countdownInterval = setInterval(() => {
      timeLeft--;
      if (countdownElement) countdownElement.textContent = timeLeft;
      if (resendLink && timeLeft <= 0) {
        clearInterval(this.countdownInterval);
        resendLink.classList.remove('disabled');
      }
    }, 1000);
  }

  /**
   * Обрабатывает подтверждение кода
   */
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

      // Код подтвержден, регистрируем пользователя
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

  /**
   * Обрабатывает повторную отправку кода
   */
  async handleResendCode() {
    const resendLink = document.querySelector('#resendLink');
    if (resendLink?.classList.contains('disabled')) {
      return;
    }

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
      showErrorMessage('Ошибка отправки кода: ' + error.message);
    } finally {
      showSpinner(false);
    }
  }

  /**
   * Регистрирует пользователя
   */
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
      if (response.id) { // Предполагаем, что успешный ответ содержит ID пользователя
        this.modal.hide();
        window.location.href = '/account'; // Перенаправляем на личный кабинет
      } else {
        showErrorMessage('Ошибка регистрации пользователя');
      }
    } catch (error) {
      console.error('Ошибка регистрации:', error);
      showErrorMessage('Ошибка регистрации: ' + error.message);
    } finally {
      showSpinner(false);
    }
  }
}

// Инициализация менеджера регистрации
new RegistrationManager();