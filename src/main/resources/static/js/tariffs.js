// ----------------------- Вспомогательные функции -----------------------

/**
 * Получить значение cookie по имени
 * @param {string} name
 * @returns {string|null}
 */
function getCookie(name) {
    const matches = document.cookie.match(
        new RegExp(
            "(?:^|; )" +
                name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, "\\$1") +
                "=([^;]*)"
        )
    );
    return matches ? decodeURIComponent(matches[1]) : null;
}

/**
 * Формирует объект запроса для /tariffs/get, учитывая selfPickup/selfDelivery
 * @param {boolean} selfPickup
 * @param {boolean} selfDelivery
 * @returns {Object}
 */
function buildRequestData(selfPickup, selfDelivery) {
    const deliveryDataRaw = getCookie('delivery_data');
    if (!deliveryDataRaw) throw new Error('Cookie delivery_data не найдена');
    let deliveryData;
    try {
        deliveryData = JSON.parse(deliveryDataRaw);
    } catch (e) {
        throw new Error('Некорректный формат данных в cookie delivery_data');
    }
    return {
        fromLocation: deliveryData.fromLocation,
        toLocation: deliveryData.toLocation,
        packages: deliveryData.packages,
        selfPickup,
        selfDelivery
    };
}

/**
 * Загружает тарифы с сервера по текущим параметрам
 * @param {boolean} selfPickup
 * @param {boolean} selfDelivery
 * @returns {Promise<Array>} Массив тарифов
 */
async function fetchTariffsWithParams(selfPickup, selfDelivery) {
    const requestData = buildRequestData(selfPickup, selfDelivery);
    const response = await fetch('tariffs/get', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestData)
    });
    if (!response.ok) {
        throw new Error('Ошибка загрузки тарифов: ' + response.status);
    }
    return await response.json();
}

function showSpinner(show) {
    document.getElementById('spinner').style.display = show ? '' : 'none';
}

// Вспомогательные функции для даты доставки
function getDeliveryDateString(minTime) {
    const today = new Date();
    today.setDate(today.getDate() + (minTime || 0));
    return today.toLocaleDateString('ru-RU', { day: 'numeric', month: 'long' });
}
function getDeliveryWeekday(minTime) {
    const today = new Date();
    today.setDate(today.getDate() + (minTime || 0));
    let wd = today.toLocaleDateString('ru-RU', { weekday: 'long' });
    return wd.charAt(0).toUpperCase() + wd.slice(1);
}

// ----------------------- Сортировка и фильтрация -----------------------

/**
 * Сортирует тарифы по цене или скорости
 * @param {Array} tariffs
 * @param {string} sortBy 'price' | 'speed'
 * @returns {Array}
 */
function sortTariffs(tariffs, sortBy) {
    if (sortBy === 'price') {
        return tariffs.slice().sort((a, b) => a.price - b.price);
    }
    if (sortBy === 'speed') {
        return tariffs.slice().sort((a, b) => {
            const aAvg = (a.minTime + a.maxTime) / 2;
            const bAvg = (b.minTime + b.maxTime) / 2;
            return aAvg - bAvg;
        });
    }
    return tariffs;
}

/**
 * Фильтрует тарифы по выбранным службам доставки
 * @param {Array} tariffs
 * @param {Array} selectedServices (например ['CDEK','DPD'])
 * @returns {Array}
 */
function filterTariffsByService(tariffs, selectedServices) {
    if (!selectedServices || selectedServices.length === 0) return tariffs;
    return tariffs.filter(tariff => selectedServices.includes(tariff.service.name));
}

// ----------------------- Отображение тарифов -----------------------

/**
 * Рендерит тарифы в контейнер с id="tariffs-list"
 * @param {Array} tariffs
 */
function renderTariffs(tariffs) {
    const container = document.getElementById('tariffs-list');
    container.innerHTML = '';
    if (!tariffs.length) {
        container.innerHTML = '<div class="alert alert-warning">Нет доступных тарифов.</div>';
        return;
    }
    tariffs.forEach((tariff, idx) => {
        // Сбор сегодня, доставка через minTime дней (можно доработать)
        const today = new Date();
        const deliveryDate = new Date(today);
        deliveryDate.setDate(today.getDate() + (tariff.minTime || 0));
        const deliveryDateStr = deliveryDate.toLocaleDateString('ru-RU', { day: 'numeric', month: 'long' });
        const weekday = deliveryDate.toLocaleDateString('ru-RU', { weekday: 'long' });

        const card = document.createElement('div');
        card.className = 'tariff-card';

        card.innerHTML = `
            <div style="display:flex;align-items:center;min-width:180px;">
                <img class="tariff-logo" src="${tariff.service.logo}" alt="${tariff.service.name}">
                <div>
                    <div class="tariff-service">${tariff.service.name}</div>
                    <div style="font-size:13px;color:#888;">${tariff.name}</div>
                </div>
            </div>
            <div class="tariff-section">
                <b>Срок</b>
                ${tariff.minTime} - ${tariff.maxTime} дня<br>
            </div>
            <div class="tariff-section">
                <b>Доставка</b>
                ${deliveryDateStr}<br>
                <span style="color:#888;font-size:13px;">${weekday.charAt(0).toUpperCase() + weekday.slice(1)}</span>
            </div>
            <div class="tariff-section" style="text-align:right;min-width:110px;">
                <span class="tariff-price">${tariff.price}₽</span>
                <br>
                <button class="tariff-btn" data-tariff-idx="${idx}">Выбрать тариф</button>
            </div>
        `;
        container.appendChild(card);
    });

    // Назначаем обработчик на все кнопки "Выбрать тариф"
    container.querySelectorAll('.tariff-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const idx = this.getAttribute('data-tariff-idx');
            handleChooseTariff(tariffs[idx]);
        });
    });
}
// ----------------------- Основная логика -----------------------

let allTariffs = []; // Все тарифы, полученные с сервера
let currentSelfPickup = false;
let currentSelfDelivery = false;

function fillDeliveryHeader() {
    const cookie = document.cookie.match('(^|;)\\s*delivery_data\\s*=\\s*([^;]+)')?.pop();
    if (!cookie) return;

    const data = JSON.parse(decodeURIComponent(cookie));
    // Форматируем дату в ДД.ММ.ГГГГ
    function formatDate(dateStr) {
        if (!dateStr) return "";
        const d = new Date(dateStr);
        return d.toLocaleDateString('ru-RU');
    }
    document.getElementById('dateCell').textContent = formatDate(data.fromLocation.date);
    document.getElementById('fromCity').textContent = data.fromLocation.city;
    document.getElementById('toCity').textContent = data.toLocation.city;
    const totalWeight = data.packages.reduce((sum, p) => sum + (p.weight || 0), 0);
    document.getElementById('packageInfo').textContent = `${totalWeight} г, ${data.packages.length} шт.`;
}

/**
 * Обновляет отображение тарифов с учетом фильтрации и сортировки
 */
function updateTariffsDisplay() {
    // Получаем выбранную сортировку
    const sortBy = document.getElementById('sort-options').value;

    // Получаем выбранные службы доставки
    const serviceCheckboxes = document.querySelectorAll('input[type="checkbox"][name="delivery-method"]:not(#allServices)');
    let selectedServices = [];
    serviceCheckboxes.forEach(cb => {
        if (cb.checked) selectedServices.push(cb.id.toUpperCase());
    });
    // Если "Все компании" отмечено, показываем все
    if (document.getElementById('allServices').checked) {
        selectedServices = [];
    }
    // Фильтрация и сортировка
    let filtered = filterTariffsByService(allTariffs, selectedServices);
    let sorted = sortTariffs(filtered, sortBy);

    renderTariffs(sorted);
}

/**
 * Загружает тарифы и обновляет отображение
 * @param {boolean} selfPickup
 * @param {boolean} selfDelivery
 */
async function loadAndDisplayTariffs(selfPickup, selfDelivery) {
    // Показываем лоадер
    const container = document.getElementById('tariffs-list');
    showSpinner(true);
    try {
        allTariffs = await fetchTariffsWithParams(selfPickup, selfDelivery);
        updateTariffsDisplay();
    } catch (e) {
        container.innerHTML = `<div class="alert alert-danger">Ошибка: ${e.message}</div>`;
    }
    showSpinner(false)
}

/**
 * Выбор тарифа
 */
function handleChooseTariff(tariff) {
    // Получаем cookie delivery_data
    const cookie = document.cookie.match('(^|;)\\s*delivery_data\\s*=\\s*([^;]+)')?.pop();
    if (!cookie) {
        alert('Ошибка: не найдены данные доставки');
        return;
    }
    let data;
    try {
        data = JSON.parse(decodeURIComponent(cookie));
    } catch (e) {
        alert('Ошибка чтения данных доставки');
        return;
    }

    // Формируем объект тарифа согласно классу TariffDto
    const selectedTariff = {
        service: {
            name: tariff.service.name,
            logo: tariff.service.logo
        },
        code: tariff.code,
        name: tariff.name,
        minTime: tariff.minTime,
        maxTime: tariff.maxTime,
        price: tariff.price
    };

    // Записываем тариф в delivery_data
    data.tariff = selectedTariff;

    // Сохраняем обратно в cookie (на 1 день)
    document.cookie = 'delivery_data=' + encodeURIComponent(JSON.stringify(data)) +
        ';path=/;max-age=86400';

    // Переходим на страницу заказа
    window.location.href = '/order';
}

// ----------------------- События управления -----------------------

/**
 * Обработчик смены сортировки
 */
document.getElementById('sort-options').addEventListener('change', updateTariffsDisplay);

/**
 * Обработчик чекбоксов служб доставки
 */
document.querySelectorAll('input[type="checkbox"][name="delivery-method"]').forEach(cb => {
    cb.addEventListener('change', function() {
        // Если "Все компании" отмечено, снимаем остальные
        if (cb.id === 'allServices' && cb.checked) {
            document.querySelectorAll('input[type="checkbox"][name="delivery-method"]:not(#allServices)').forEach(other => other.checked = false);
        } else if (cb.id !== 'allServices' && cb.checked) {
            document.getElementById('allServices').checked = false;
        }
        updateTariffsDisplay();
    });
});

/**
 * Обработчик смены способа доставки (радиокнопки)
 */
document.querySelectorAll('input[type="radio"][name="delivery-method"]').forEach(radio => {
    radio.addEventListener('change', function() {
        let selfPickup = false, selfDelivery = false;
        switch (radio.id) {
            case 'door-to-door':
                selfPickup = false; selfDelivery = false; break;
            case 'door-to-pickup':
                selfPickup = false; selfDelivery = true; break;
            case 'pickup-to-door':
                selfPickup = true; selfDelivery = false; break;
            case 'pickup-to-pickup':
                selfPickup = true; selfDelivery = true; break;
        }
        currentSelfPickup = selfPickup;
        currentSelfDelivery = selfDelivery;
        loadAndDisplayTariffs(selfPickup, selfDelivery);
    });
});

// ----------------------- Инициализация -----------------------

/**
 * Загружаем тарифы при первой загрузке страницы
 */
window.addEventListener('DOMContentLoaded', () => {
    // По умолчанию: Дверь - Дверь (оба false)
    currentSelfPickup = false;
    currentSelfDelivery = false;
    fillDeliveryHeader()
    loadAndDisplayTariffs(currentSelfPickup, currentSelfDelivery);
});