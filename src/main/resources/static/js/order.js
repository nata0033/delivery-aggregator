document.getElementById('order-pay-top').addEventListener('click', sendOrder);
document.getElementById('order-pay-bottom').addEventListener('click', sendOrder);

// Функция для установки куки
function setCookie(name, value, days) {
    let expires = "";
    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + encodeURIComponent(value) + expires + "; path=/";
}

// Функция для чтения куки по имени
function getCookie(name) {
    const matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

/**
 * Функция для правильного склонения слова "груз" в зависимости от числа
 * @param {number} number
 * @param {Array} titles - массив из 3 форм слова, например ['груз', 'груза', 'грузов']
 * @returns {string}
 */
function declOfNum(number, titles) {
    number = Math.abs(number) % 100;
    const n1 = number % 10;
    if (number > 10 && number < 20) return titles[2];
    if (n1 > 1 && n1 < 5) return titles[1];
    if (n1 === 1) return titles[0];
    return titles[2];
}

/**
 * Проверка статуса аутентификации
 */
async function checkAuthStatus() {
    try {
        const response = await fetch('/user/isAuth');
        return response.ok ? await response.json() : false;
    } catch (error) {
        console.error('Ошибка при проверке аутентификации:', error);
        return false;
    }
}

/**
 * Загрузка и отображение заголовка
 */
async function loadHeader() {
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

// Функция для отправки заказа
async function sendOrder(event) {
    event.preventDefault();
    try {
        // Получаем данные из формы
        const orderData = {
            sender: {
                firstName: document.getElementById('sender-firstname').value,
                lastName: document.getElementById('sender-lastname').value,
                fatherName: document.getElementById('sender-fathername').value,
                email: document.getElementById('sender-email').value,
                phone: document.getElementById('sender-phone').value
            },
            recipient: {
                firstName: document.getElementById('recipient-firstname').value,
                lastName: document.getElementById('recipient-lastname').value,
                fatherName: document.getElementById('recipient-fathername').value,
                email: document.getElementById('recipient-email').value,
                phone: document.getElementById('recipient-phone').value
            },
            fromLocation: {
                state: document.getElementById('from-state').value,
                city: document.getElementById('from-city').value,
                street: document.getElementById('from-street').value,
                house: document.getElementById('from-house').value,
                apartment: document.getElementById('from-apartment').value,
                postalCode: document.getElementById('from-postalCode').value,
            },
            toLocation: {
                state: document.getElementById('to-state').value,
                city: document.getElementById('to-city').value,
                street: document.getElementById('to-street').value,
                house: document.getElementById('to-house').value,
                apartment: document.getElementById('to-apartment').value,
                postalCode: document.getElementById('to-postalCode').value,
            },
            comment: document.getElementById('order-comment').value
        };

        // Получаем данные из куки
        const deliveryDataRaw = getCookie('delivery_data');
        if (!deliveryDataRaw) {
            throw new Error('Отсутствуют данные о доставке в cookies');
        }

        let deliveryData;
        try {
            deliveryData = JSON.parse(deliveryDataRaw);
        } catch (e) {
            console.error('Ошибка парсинга delivery_data:', e);
            throw new Error('Неверный формат данных о доставке');
        }

        // Обновляем данные в куки перед отправкой
        const updatedDeliveryData = {
            ...deliveryData,
            sender: orderData.sender,
            recipient: orderData.recipient,
            fromLocation: {
                ...deliveryData.fromLocation,
                state: orderData.fromLocation.state,
                city: orderData.fromLocation.city,
                street: orderData.fromLocation.street,
                house: orderData.fromLocation.house,
                apartment: orderData.fromLocation.apartment,
                postalCode: orderData.fromLocation.postalCode
            },
            toLocation: {
                ...deliveryData.toLocation,
                state: orderData.toLocation.state,
                city: orderData.toLocation.city,
                street: orderData.toLocation.street,
                house: orderData.toLocation.house,
                apartment: orderData.toLocation.apartment,
                postalCode: orderData.toLocation.postalCode
            }
        };

        // Сохраняем обновленные данные в куки
        setCookie('delivery_data', JSON.stringify(updatedDeliveryData), 7);

        // Добавляем тариф и упаковки из обновленных куки
        orderData.tariff = updatedDeliveryData.tariff;
        orderData.packages = updatedDeliveryData.packages;
        orderData.fromLocation.date = updatedDeliveryData.fromLocation.date;

        // Отправляем данные на сервер
        const response = await fetch('/order/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(orderData)
        });

        if (!response.ok) {
            throw new Error('Ошибка при создании заказа');
        }

        const result = await response.json();
        if (result.redirectUrl) {
            window.location.href = result.redirectUrl;
        }
    } catch (error) {
        console.error('Ошибка при отправке заказа:', error);
        alert(error.message || 'Произошла ошибка при отправке заказа');
    }
}

// Заполнение страницы при загрузке
window.addEventListener('DOMContentLoaded', async () => {
    // Загружаем заголовок
    await loadHeader();

    const deliveryDataRaw = getCookie('delivery_data');
    if (!deliveryDataRaw) return;

    let deliveryData;
    try {
        deliveryData = JSON.parse(deliveryDataRaw);
    } catch (e) {
        console.error('Ошибка парсинга delivery_data:', e);
        return;
    }

    // Заполняем данные отправителя
    if (deliveryData.sender) {
        document.getElementById('sender-firstname').value = deliveryData.sender.firstName || '';
        document.getElementById('sender-lastname').value = deliveryData.sender.lastName || '';
        document.getElementById('sender-fathername').value = deliveryData.sender.fatherName || '';
        document.getElementById('sender-email').value = deliveryData.sender.email || '';
        document.getElementById('sender-phone').value = deliveryData.sender.phone || '';
    }

    // Заполняем данные получателя
    if (deliveryData.recipient) {
        document.getElementById('recipient-firstname').value = deliveryData.recipient.firstName || '';
        document.getElementById('recipient-lastname').value = deliveryData.recipient.lastName || '';
        document.getElementById('recipient-fathername').value = deliveryData.recipient.fatherName || '';
        document.getElementById('recipient-email').value = deliveryData.recipient.email || '';
        document.getElementById('recipient-phone').value = deliveryData.recipient.phone || '';
    }

    // Адрес отправления
    if (deliveryData.fromLocation) {
        document.getElementById('from-state').value = deliveryData.fromLocation.state || '';
        document.getElementById('from-city').value = deliveryData.fromLocation.city || '';
        document.getElementById('from-street').value = deliveryData.fromLocation.street || '';
        document.getElementById('from-house').value = deliveryData.fromLocation.house || '';
        document.getElementById('from-apartment').value = deliveryData.fromLocation.apartment || '';
        document.getElementById('from-postalCode').value = deliveryData.fromLocation.postalCode || '';
    }

    // Адрес получения
    if (deliveryData.toLocation) {
        document.getElementById('to-state').value = deliveryData.toLocation.state || '';
        document.getElementById('to-city').value = deliveryData.toLocation.city || '';
        document.getElementById('to-street').value = deliveryData.toLocation.street || '';
        document.getElementById('to-house').value = deliveryData.toLocation.house || '';
        document.getElementById('to-apartment').value = deliveryData.toLocation.apartment || '';
        document.getElementById('to-postalCode').value = deliveryData.toLocation.postalCode || '';
    }

    // Тариф
    if (deliveryData.tariff) {
        // Логотип и название службы доставки
        const logoEl = document.getElementById('service-logo');
        logoEl.src = deliveryData.tariff.service.logo;
        logoEl.alt = deliveryData.tariff.service.name || 'Логотип службы доставки';

        // Название тарифа
        const tariffNameEl = document.getElementById('tariff-name');
        tariffNameEl.textContent = deliveryData.tariff.name || '';

        // Количество грузов и общий вес
        const packageCountEl = document.getElementById('package-count');
        const packageWeightEl = document.getElementById('package-weight');

        const count = deliveryData.packages.length;
        packageCountEl.textContent = `${count} ${declOfNum(count, ['груз', 'груза', 'грузов'])}`;

        const totalWeight = deliveryData.packages.reduce((sum, pkg) => sum + (pkg.weight || 0), 0);
        packageWeightEl.textContent = `${totalWeight} г`;

        // Цена тарифа
        const tariffPriceEl = document.getElementById('tariff-price');
        tariffPriceEl.textContent = deliveryData.tariff.price ? `${deliveryData.tariff.price} ₽` : '';

        // Итоговая цена
        const totalPriceEl = document.getElementById('total-price');
        totalPriceEl.textContent = deliveryData.tariff.price ? `${deliveryData.tariff.price} ₽` : '';
    }
});