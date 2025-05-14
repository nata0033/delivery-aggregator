document.getElementById('order-pay-top').addEventListener('click', sendOrder);
document.getElementById('order-pay-bottom').addEventListener('click', sendOrder);

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

// Функция для отправки заказа
async function sendOrder() {
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
                //date: document.getElementById('from-date').value
            },
            toLocation: {
                state: document.getElementById('to-state').value,
                city: document.getElementById('to-city').value,
                street: document.getElementById('to-street').value,
                house: document.getElementById('to-house').value,
                apartment: document.getElementById('to-apartment').value,
                postalCode: document.getElementById('to-postalCode').value,
                //date: document.getElementById('to-date').value
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

        // Добавляем тариф и упаковки из куки
        orderData.tariff = deliveryData.tariff;
        orderData.packages = deliveryData.packages
        orderData.fromLocation.date = deliveryData.fromLocation.date

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
window.addEventListener('DOMContentLoaded', () => {
    const deliveryDataRaw = getCookie('delivery_data');
    if (!deliveryDataRaw) return;

    let deliveryData;
    try {
        deliveryData = JSON.parse(deliveryDataRaw);
    } catch (e) {
        console.error('Ошибка парсинга delivery_data:', e);
        return;
    }

    // Отправитель
    if (deliveryData.fromLocation) {
        document.getElementById('from-state').value = deliveryData.fromLocation.state || '';
        document.getElementById('from-city').value = deliveryData.fromLocation.city || '';
        document.getElementById('from-street').value = deliveryData.fromLocation.street || '';
        document.getElementById('from-house').value = deliveryData.fromLocation.house || '';
        document.getElementById('from-apartment').value = deliveryData.fromLocation.apartment || '';
        document.getElementById('from-postalCode').value = deliveryData.fromLocation.postalCode || '';
    }

    // Получатель
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

        // Итоговая цена (можно расширить при необходимости)
        const totalPriceEl = document.getElementById('total-price');
        totalPriceEl.textContent = deliveryData.tariff.price ? `${deliveryData.tariff.price} ₽` : '';
    }
});