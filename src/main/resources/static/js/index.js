/**
 * Основной модуль для работы с формой доставки
 * Обрабатывает города, посылки и сохранение данных
 */
document.addEventListener('DOMContentLoaded', function() {
    // Инициализация даты отправки (завтрашний день)
    initShipmentDate();

    // Загрузка списка городов с сервера
    loadCities();

    // Инициализация обработчиков событий
    initEventHandlers();
});

// Глобальные переменные для хранения городов
let cities = [];
let defaultCities = [];

/**
 * Инициализирует дату отправки (устанавливает завтрашний день)
 */
function initShipmentDate() {
    const dateInput = document.getElementById("fromLocationDateInput");
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    dateInput.valueAsDate = tomorrow;
}

/**
 * Загружает список городов с сервера
 */
async function loadCities() {
    try {
        const response = await fetch('/getCities');
        if (!response.ok) {
            throw new Error('Ошибка загрузки городов');
        }
        cities = await response.json();
        initDefaultCities();
    } catch (error) {
        console.error('Ошибка:', error);
        cities = [];
        defaultCities = [];
    }
}

/**
 * Инициализирует список популярных городов по умолчанию
 */
function initDefaultCities() {
    defaultCities = [
        "Москва", "Санкт-Петербург", "Казань",
        "Екатеринбург", "Новосибирск"
    ].map(name => findCityByName(name)).filter(Boolean);
}

/**
 * Находит город по названию
 */
function findCityByName(name) {
    return cities.find(city => city.name === name);
}

/**
 * Инициализирует все обработчики событий
 */
function initEventHandlers() {
    // Обработчики для добавления/удаления посылок
    document.getElementById('addPackageButton').addEventListener('click', addPackage);
    document.getElementById('removePackageButton').addEventListener('click', removePackage);

    // Обработчики для полей городов
    initCityInputHandlers();

    // Обработчики для кнопок отправки
    document.getElementById("submitButton1").addEventListener('click', saveFormData);
    document.getElementById("submitButton2").addEventListener('click', saveFormData);

    // Закрытие подсказок при клике вне поля
    document.addEventListener('click', closeSuggestionsOnClickOutside);
}

/**
 * Добавляет новый блок для ввода параметров посылки
 */
function addPackage() {
    const container = document.getElementById('packagesContainer');
    const packageCount = document.querySelectorAll('.package-form').length;

    const packageHTML = `
        <div class="package-form form-row">
            <div class="form-group col-md-2">
                <label>Вес (гр)</label>
                <input type="number" name="packages[${packageCount}].weight"
                       class="form-control" placeholder="Вес(гр)" min="1" value="10" required>
            </div>
            <div class="form-group col-md-2">
                <label>Длина (см)</label>
                <input type="number" name="packages[${packageCount}].length"
                       class="form-control" placeholder="Длина(см)" min="1" value="10" required>
            </div>
            <div class="form-group col-md-2">
                <label>Ширина (см)</label>
                <input type="number" name="packages[${packageCount}].width"
                       class="form-control" placeholder="Ширина(см)" min="1" value="10" required>
            </div>
            <div class="form-group col-md-2">
                <label>Высота (см)</label>
                <input type="number" name="packages[${packageCount}].height"
                       class="form-control" placeholder="Высота(см)" min="1" value="10" required>
            </div>
        </div>
    `;

    container.insertAdjacentHTML('beforeend', packageHTML);
}

/**
 * Удаляет последний блок с параметрами посылки
 */
function removePackage() {
    const packages = document.querySelectorAll('.package-form');
    if (packages.length > 1) {
        packages[packages.length - 1].remove();
    }
}

/**
 * Инициализирует обработчики для полей ввода городов
 */
function initCityInputHandlers() {
    const fromInput = document.getElementById('fromLocationCityInput');
    const toInput = document.getElementById('toLocationCityInput');

    fromInput.addEventListener('input', () => handleCityInput('from'));
    toInput.addEventListener('input', () => handleCityInput('to'));
}

/**
 * Обрабатывает ввод в поле города
 */
function handleCityInput(type) {
    const input = document.getElementById(`${type}LocationCityInput`);
    const value = input.value.toLowerCase();

    const filteredCities = value === ''
        ? defaultCities
        : cities.filter(city =>
            city.name.toLowerCase().startsWith(value)
          ).slice(0, 5);

    displayCitySuggestions(type, filteredCities);
}

/**
 * Отображает подсказки для городов
 */
function displayCitySuggestions(type, cities) {
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
 */
function closeSuggestionsOnClickOutside(event) {
    const fromElements = ['fromLocationCityInput', 'fromLocationSuggest'];
    const toElements = ['toLocationCityInput', 'toLocationSuggest'];

    if (!event.target.closest(fromElements.join(', '))) {
        document.getElementById('fromLocationSuggest').innerHTML = '';
    }

    if (!event.target.closest(toElements.join(', '))) {
        document.getElementById('toLocationSuggest').innerHTML = '';
    }
}

/**
 * Сохраняет данные формы в cookie
 */
function saveFormData() {
    const formData = {
        fromLocation: getLocationData('from'),
        toLocation: getLocationData('to'),
        packages: getPackagesData(),
        recipient: null,
        tariff: null
    };

    setCookie('delivery_data', JSON.stringify(formData), 86400); // 24 часа
}

/**
 * Получает данные о местоположении
 */
function getLocationData(type) {
    return {
        country: document.getElementById(`${type}LocationCountryInput`).value,
        state: document.getElementById(`${type}LocationStateInput`).value,
        city: document.getElementById(`${type}LocationCityInput`).value,
        street: "",
        house: "",
        apartment: "",
        postalCode: "",
        date: type === 'from'
            ? document.getElementById('fromLocationDateInput').value
            : ""
    };
}

/**
 * Получает данные о посылках
 */
function getPackagesData() {
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
 */
function setCookie(name, value, seconds) {
    const date = new Date();
    date.setTime(date.getTime() + (seconds * 1000));
    document.cookie = `${name}=${encodeURIComponent(value)};path=/;expires=${date.toUTCString()}`;
}