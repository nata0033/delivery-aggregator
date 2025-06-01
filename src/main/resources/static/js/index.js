/**
 * Основной модуль для работы с формой доставки
 * Обрабатывает города, посылки, валидацию и сохранение данных
 */
class DeliveryForm {
  constructor() {
    // Глобальные переменные для хранения данных
    this.cities = [];
    this.defaultCities = [];

    // Инициализация при загрузке DOM
    document.addEventListener('DOMContentLoaded', () => this.init());
  }

  /**
   * Основная функция инициализации
   */
  init() {
    this.initShipmentDate();
    this.loadCities();
    this.initEventHandlers();
    this.loadHeader();
  }

  // --------------------------
  // Инициализационные методы
  // --------------------------

  /**
   * Загрузка и отображение заголовка
   */
  async loadHeader() {
    try {
      const isAuthenticated = await this.checkAuthStatus();
      const headerTemplate = document.getElementById('header-template');
      const headerClone = document.importNode(headerTemplate.content, true);

      // Добавляем ссылку на личный кабинет если авторизован
      if (isAuthenticated) {
        const navList = headerClone.querySelector('ul');
        const accountLi = document.createElement('li');
        accountLi.innerHTML = '<a href="/account" class="nav-link px-2 text-white">Личный кабинет</a>';
        navList.appendChild(accountLi);
      }

      // Добавляем кнопки входа/выхода
      const headerButtons = headerClone.getElementById('header-buttons-container');
      if (isAuthenticated) {
        headerButtons.innerHTML = '<a href="/logout" class="btn btn-light">Выход</a>';
      } else {
        headerButtons.innerHTML = `
          <a href="/registration" class="btn btn-outline-light me-2">Регистрация</a>
          <a href="/login" class="btn btn-light">Вход</a>
        `;
      }

      // Вставляем в DOM
      document.getElementById("header-container").appendChild(headerClone);
    } catch (error) {
      console.error('Ошибка при загрузке заголовка:', error);
      document.getElementById("header-container").innerHTML = '';
    }
  }

  /**
   * Проверка статуса аутентификации
   */
  async checkAuthStatus() {
    try {
      const response = await fetch('/user/isAuth');
      return response.ok ? await response.json() : false;
    } catch (error) {
      console.error('Ошибка при проверке аутентификации:', error);
      return false;
    }
  }

  /**
   * Инициализирует дату отправки (устанавливает завтрашний день)
   */
  initShipmentDate() {
    const dateInput = document.getElementById("fromLocationDateInput");
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    dateInput.valueAsDate = tomorrow;
  }

  /**
   * Загружает список городов с сервера
   */
  async loadCities() {
    try {
      const response = await fetch('/cities/get');
      if (!response.ok) throw new Error('Ошибка загрузки городов');
      this.cities = await response.json();
      this.initDefaultCities();
    } catch (error) {
      console.error('Ошибка:', error);
      this.cities = [];
      this.defaultCities = [];
      this.showErrorMessage('Не удалось загрузить список городов. Пожалуйста, попробуйте позже.');
    }
  }

  /**
   * Инициализирует список популярных городов по умолчанию
   */
  initDefaultCities() {
    const popularCities = ["Москва", "Санкт-Петербург", "Казань", "Екатеринбург", "Новосибирск"];
    this.defaultCities = popularCities.map(name => this.findCityByName(name)).filter(Boolean);
  }

  /**
   * Инициализирует все обработчики событий
   */
  initEventHandlers() {
    document.getElementById('addPackageButton').addEventListener('click', () => this.addPackage());
    document.getElementById('removePackageButton').addEventListener('click', () => this.removePackage());
    document.getElementById("submitButton1").addEventListener('click', (e) => this.saveFormData(e));
    document.getElementById("submitButton2").addEventListener('click', (e) => this.saveFormData(e));
    this.initCityInputHandlers();
    this.initValidation();
    document.addEventListener('click', (e) => this.closeSuggestionsOnClickOutside(e));
  }

  // --------------------------
  // Методы работы с городами
  // --------------------------

  /**
   * Находит город по названию
   * @param {string} name - Название города
   * @returns {Object|null} Объект города или null
   */
  findCityByName(name) {
    return this.cities.find(city => city.name === name);
  }

  /**
   * Инициализирует обработчики для полей ввода городов
   */
  initCityInputHandlers() {
    const fromInput = document.getElementById('fromLocationCityInput');
    const toInput = document.getElementById('toLocationCityInput');
    fromInput.addEventListener('input', () => this.handleCityInput('from'));
    toInput.addEventListener('input', () => this.handleCityInput('to'));
  }

  /**
   * Обрабатывает ввод в поле города
   * @param {string} type - Тип поля ('from' или 'to')
   */
  handleCityInput(type) {
    const input = document.getElementById(`${type}LocationCityInput`);
    const value = input.value.toLowerCase();
    const filteredCities = value === ''
      ? this.defaultCities
      : this.cities.filter(city => city.name.toLowerCase().startsWith(value)).slice(0, 5);
    this.displayCitySuggestions(type, filteredCities);
  }

  /**
   * Отображает подсказки для городов
   * @param {string} type - Тип поля ('from' или 'to')
   * @param {Array} cities - Массив городов для отображения
   */
  displayCitySuggestions(type, cities) {
    const suggestElement = document.getElementById(`${type}LocationSuggest`);
    suggestElement.innerHTML = '';

    cities.forEach(city => {
      const button = document.createElement('button');
      button.className = "w-100 btn btn-light";
      button.textContent = `${city.name}, ${city.subject}`;
      button.addEventListener('click', () => {
        document.getElementById(`${type}LocationStateInput`).value = city.subject.split(" ")[0];
        document.getElementById(`${type}LocationCityInput`).value = city.name;
        suggestElement.innerHTML = '';
      });
      suggestElement.appendChild(button);
    });
  }

  /**
   * Закрывает подсказки при клике вне поля ввода
   * @param {Event} event - Событие клика
   */
  closeSuggestionsOnClickOutside(event) {
    const fromElements = ['fromLocationCityInput', 'fromLocationSuggest'];
    const toElements = ['toLocationCityInput', 'toLocationSuggest'];

    if (!event.target.closest(fromElements.join(', '))) {
      document.getElementById('fromLocationSuggest').innerHTML = '';
    }
    if (!event.target.closest(toElements.join(', '))) {
      document.getElementById('toLocationSuggest').innerHTML = '';
    }
  }

  // --------------------------
  // Методы работы с посылками
  // --------------------------

  /**
   * Добавляет новый блок для ввода параметров посылки
   */
  addPackage() {
    const container = document.getElementById('packagesContainer');
    const packageCount = document.querySelectorAll('.package-form').length;
    const template = document.getElementById('package-template');
    const clone = document.importNode(template.content, true);

    clone.querySelectorAll('input').forEach(input => {
      const name = input.getAttribute('name').replace('__index__', packageCount);
      input.setAttribute('name', name);
    });

    container.appendChild(clone);
  }

  /**
   * Удаляет последний блок с параметрами посылки
   */
  removePackage() {
    const packages = document.querySelectorAll('.package-form');
    if (packages.length > 1) packages[packages.length - 1].remove();
  }

  // --------------------------
  // Методы валидации
  // --------------------------

  /**
   * Инициализирует валидацию полей формы
   */
  initValidation() {
    document.querySelectorAll('.location').forEach(input => {
      input.addEventListener('input', () => this.validateCityInput(input));
      input.addEventListener('blur', () => this.validateCityInput(input));
    });

    document.querySelectorAll('.package').forEach(input => {
      this.addPackageValidation(input);
    });

    this.setupPackagesObserver();
  }

  /**
   * Настраивает MutationObserver для отслеживания новых полей посылок
   */
  setupPackagesObserver() {
    const observer = new MutationObserver((mutations) => {
      mutations.forEach((mutation) => {
        mutation.addedNodes.forEach((node) => {
          if (node.nodeType === 1 && node.classList.contains('package-form')) {
            node.querySelectorAll('.package').forEach(input => {
              this.addPackageValidation(input);
            });
          }
        });
      });
    });

    const packagesContainer = document.getElementById('packagesContainer');
    observer.observe(packagesContainer, { childList: true, subtree: true });
  }

  /**
   * Добавляет обработчики валидации для поля посылки
   * @param {HTMLElement} input - Поле ввода
   */
  addPackageValidation(input) {
    const validate = () => this.validatePackageField(input);
    input.addEventListener('input', validate);
    input.addEventListener('blur', validate);
  }


  /**
   * Проверяет корректность ввода города
   * @param {HTMLElement} input - Поле ввода города
   */
  validateCityInput(input) {
    const value = input.value.trim();
    const errorMessageId = `${input.id}-error`;
    let errorMessage = document.getElementById(errorMessageId);

    if (errorMessage) errorMessage.remove();
    if (value === '') {
      this.resetInputStyle(input);
      return;
    }

    const isValid = this.cities.some(city => city.name === value);
    if (isValid) {
      this.setInputSuccess(input);
    } else {
      this.setInputError(input, 'Укажите город из списка');
      this.createErrorMessage(input, errorMessageId, 'Укажите город из списка');
    }
  }

  /**
   * Проверяет корректность поля посылки
   * @param {HTMLElement} input - Поле ввода посылки
   */
  validatePackageField(input) {
    const value = parseFloat(input.value);
    const errorMessageId = `${input.name}-error`;
    let errorMessage = document.getElementById(errorMessageId);

    if (errorMessage) errorMessage.remove();
    if (isNaN(value) || value <= 0) {
      this.setInputError(input, 'Введите положительное число');
      this.createErrorMessage(input, errorMessageId, 'Введите положительное число');
    } else {
      this.setInputSuccess(input);
    }
  }

  /**
   * Проверяет, все ли поля формы валидны
   * @returns {boolean} Результат проверки
   */
  isFormValid() {
    const cityInputsValid = Array.from(document.querySelectorAll('.location')).every(input => {
      const value = input.value.trim();
      return value !== '' && this.cities.some(city => city.name === value);
    });

    const packageInputsValid = Array.from(document.querySelectorAll('.package')).every(input => {
      const value = parseFloat(input.value);
      return !isNaN(value) && value > 0;
    });

    const dateInput = document.getElementById('fromLocationDateInput');
    const dateValid = dateInput.value !== '';

    return cityInputsValid && packageInputsValid && dateValid;
  }

  /**
   * Обновляет состояние кнопок отправки
   */
  updateSubmitButtons() {
    const isValid = this.isFormValid();
    document.querySelectorAll('[type="submit"]').forEach(button => {
      button.disabled = !isValid;
    });
  }

  // --------------------------
  // Вспомогательные методы
  // --------------------------

  /**
   * Устанавливает стиль успешного ввода
   * @param {HTMLElement} input - Поле ввода
   */
  setInputSuccess(input) {
    input.classList.remove('input-error');
    input.classList.add('input-success');
  }

  /**
   * Устанавливает стиль ошибочного ввода
   * @param {HTMLElement} input - Поле ввода
   * @param {string} message - Сообщение об ошибке
   */
  setInputError(input, message) {
    input.classList.remove('input-success');
    input.classList.add('input-error');
    input.setAttribute('title', message);
  }

  /**
   * Сбрасывает стили валидации
   * @param {HTMLElement} input - Поле ввода
   */
  resetInputStyle(input) {
    input.classList.remove('input-success', 'input-error');
    input.removeAttribute('title');
  }

  /**
   * Создает сообщение об ошибке под полем ввода
   * @param {HTMLElement} input - Поле ввода
   * @param {string} id - ID элемента сообщения
   * @param {string} message - Текст сообщения
   */
  createErrorMessage(input, id, message) {
    const errorMessage = document.createElement('div');
    errorMessage.id = id;
    errorMessage.className = 'field-error-message';
    errorMessage.textContent = message;
    input.insertAdjacentElement('afterend', errorMessage);
  }

  /**
   * Отображает сообщение об ошибке внизу справа
   * @param {string} message - Текст сообщения
   */
  showErrorMessage(message) {
    let errorContainer = document.getElementById('error-message-container');
    if (!errorContainer) {
      errorContainer = document.createElement('div');
      errorContainer.id = 'error-message-container';
      errorContainer.classList.add('error-message-container');
      document.body.appendChild(errorContainer);
    }

    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-danger alert-dismissible fade show';
    alertDiv.setAttribute('role', 'alert');
    alertDiv.textContent = message;

    const closeBtn = document.createElement('button');
    closeBtn.type = 'button';
    closeBtn.className = 'btn-close';
    closeBtn.setAttribute('aria-label', 'Close');
    closeBtn.onclick = () => {
      alertDiv.remove();
      if (errorContainer.children.length === 0) errorContainer.remove();
    };

    alertDiv.appendChild(closeBtn);
    errorContainer.appendChild(alertDiv);
  }

  // --------------------------
  // Методы работы с данными
  // --------------------------

  /**
   * Сохраняет данные формы в cookie (с проверкой валидности)
   * @param {Event} event - Событие отправки формы
   */
  saveFormData(event) {
    event.preventDefault();
    if (!this.isFormValid()) {
      this.showErrorMessage('Пожалуйста, заполните все поля корректно');
      return;
    }

    const formData = {
      fromLocation: this.getLocationData('from'),
      toLocation: this.getLocationData('to'),
      packages: this.getPackagesData(),
      sender: null,
      recipient: null,
      tariff: null
    };

    this.setCookie('delivery_data', JSON.stringify(formData), 86400);
    document.getElementById('deliveryDataForm').submit();
  }

  /**
   * Получает данные о местоположении
   * @param {string} type - Тип местоположения ('from' или 'to')
   * @returns {Object} Данные местоположения
   */
  getLocationData(type) {
    return {
      country: document.getElementById(`${type}LocationCountryInput`).value,
      state: document.getElementById(`${type}LocationStateInput`).value,
      city: document.getElementById(`${type}LocationCityInput`).value,
      street: "",
      house: "",
      apartment: "",
      postalCode: "",
      date: type === 'from' ? document.getElementById('fromLocationDateInput').value : ""
    };
  }

  /**
   * Получает данные о посылках
   * @returns {Array} Массив данных о посылках
   */
  getPackagesData() {
    const packages = [];
    const inputs = document.querySelectorAll('[name^="packages"]');
    const packagesMap = {};

    inputs.forEach(input => {
      const [, index, field] = input.name.match(/packages\[(\d+)\]\.(\w+)/) || [];
      if (index && field) {
        if (!packagesMap[index]) packagesMap[index] = {};
        packagesMap[index][field] = parseInt(input.value) || 0;
      }
    });

    for (const index in packagesMap) {
      packages.push({
        weight: packagesMap[index].weight,
        length: packagesMap[index].length,
        width: packagesMap[index].width,
        height: packagesMap[index].height
      });
    }

    return packages;
  }

  /**
   * Устанавливает cookie
   * @param {string} name - Имя cookie
   * @param {string} value - Значение cookie
   * @param {number} seconds - Время жизни в секундах
   */
  setCookie(name, value, seconds) {
    const date = new Date();
    date.setTime(date.getTime() + (seconds * 1000));
    document.cookie = `${name}=${encodeURIComponent(value)};path=/;expires=${date.toUTCString()}`;
  }
}

// Создаем экземпляр класса при загрузке страницы
new DeliveryForm();