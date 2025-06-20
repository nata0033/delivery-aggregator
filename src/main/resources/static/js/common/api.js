import { showErrorMessage } from './messages.js';
import { getCookie, setCookie } from './cookies.js';

/**
 * Базовый метод для выполнения fetch-запросов
 * @param {string} url - URL для запроса
 * @param {string} method - HTTP метод (GET, POST и т.д.)
 * @param {Object} [body] - Тело запроса (для POST, PUT)
 * @param {Object} [headers] - Дополнительные заголовки
 * @param {string} [contentType] - Тип контента (по умолчанию 'application/json')
 * @returns {Promise} - Промис с ответом сервера
 */
async function fetchRequest(url, method, body = null, headers = {}, contentType = 'application/json') {
    const options = {
        method,
        headers: {
            ...headers,
            'Content-Type': contentType
        },
        credentials: 'same-origin'
    };

    if (body) {
        options.body = contentType === 'application/json' ? JSON.stringify(body) : body;
    }

    try {
        const response = await fetch(url, options);

        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error(`Error in fetchRequest to ${url}:`, error);
        showErrorMessage(error.message);
        throw error;
    }
}

// ==================== Auth Related ====================

/**
 * Проверяет статус аутентификации пользователя
 * @returns {Promise<Object>} - Объект с информацией о пользователе
 */
export async function checkAuthStatus() {
    return fetchRequest('/user/isAuth', 'GET');
}

/**
 * Регистрирует нового пользователя
 * @param {Object} userData - Данные пользователя
 * @returns {Promise<Object>} - Результат регистрации
 */
export async function registerUser(userData) {
    return fetchRequest('/registration', 'POST', userData);
}

// ==================== Email Related ====================

/**
 * Проверяет существование пользователя по email
 * @param {string} email - Email для проверки
 * @returns {Promise<Object>} - {success: boolean, message?: string}
 */
export async function checkUserExists(email) {
    return fetchRequest('/email/check/exist', 'POST', `email=${encodeURIComponent(email)}`, {}, 'application/x-www-form-urlencoded');
}

/**
 * Проверяет уникальность email
 * @param {string} email - Email для проверки
 * @returns {Promise<Object>} - {success: boolean, message?: string}
 */
export async function checkEmailUnique(email) {
    return fetchRequest('/email/check/unique', 'POST', `email=${encodeURIComponent(email)}`, {}, 'application/x-www-form-urlencoded');
}

/**
 * Отправляет код подтверждения на email
 * @param {string} email - Email для отправки кода
 * @returns {Promise<Object>} - {success: boolean, message?: string}
 */
export async function sendVerificationCode(email) {
    return fetchRequest('/email/code/send', 'POST', `email=${encodeURIComponent(email)}`, {}, 'application/x-www-form-urlencoded');
}

/**
 * Подтверждает код верификации
 * @param {string} email - Email пользователя
 * @param {string} code - Код подтверждения
 * @returns {Promise<Object>} - {success: boolean, message?: string}
 */
export async function confirmVerificationCode(email, code) {
    return fetchRequest('/email/code/confirm', 'POST', `email=${encodeURIComponent(email)}&code=${encodeURIComponent(code)}`, {}, 'application/x-www-form-urlencoded');
}

// ==================== Cities Related ====================

/**
 * Получает список городов
 * @returns {Promise<Array>} - Массив городов
 */
export async function getCities() {
    return fetchRequest('/cities/get', 'GET');
}

// ==================== Order Related ====================

/**
 * Создает новый заказ
 * @param {Object} orderData - Данные заказа
 * @returns {Promise<Object>} - Результат создания заказа
 */
export async function createOrder(orderData) {
    return fetchRequest('/order/create', 'POST', orderData);
}

// ==================== Tariffs Related ====================

/**
 * Получает тарифы по параметрам
 * @param {Object} requestData - Параметры запроса
 * @param {Object} requestData.fromLocation - Откуда
 * @param {Object} requestData.toLocation - Куда
 * @param {Array} requestData.packages - Посылки
 * @param {boolean} requestData.selfPickup - Самовывоз
 * @param {boolean} requestData.selfDelivery - Самопривоз
 * @returns {Promise<Array>} - Массив тарифов
 */
export async function getTariffs(requestData) {
    return fetchRequest('/tariffs/get', 'POST', requestData);
}

/**
 * Отправляет данные формы логина на сервер
 * @param {string} email - Email пользователя
 * @param {string} password - Пароль пользователя
 * @throws {Error} Если запрос не удался
 */
export async function submitLogin(email, password) {
    try {
        const formData = new URLSearchParams();
        formData.append('username', email);
        formData.append('password', password);

        const response = await fetch('http://localhost:8080/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8'
            },
            body: formData,
            credentials: 'include',
            redirect: 'follow'
        });

        if (response.redirected) {
            window.location.href = response.url;
            return;
        }

        if (!response.ok) {
            if (response.status === 401) {
                throw new Error('Неверный email или пароль');
            }
            throw new Error(`Ошибка авторизации: ${response.statusText}`);
        }

        const isAuthenticated = await checkAuthStatus();
        if (!isAuthenticated) {
            throw new Error('Авторизация не удалась, сессия не установлена');
        }

        const text = await response.text();
        try {
            const data = JSON.parse(text);
            return data;
        } catch {
            return { success: true };
        }
    } catch (error) {
        console.error('Ошибка при отправке формы логина:', error);
        showErrorMessage(error.message);
        throw error;
    }
}

/**
 * Запрашивает данные пользователя (Contact) и сохраняет их в cookie delivery_data
 * @throws {Error} Если запрос не удался
 */
export async function fetchUserContact() {
    try {
        const response = await fetch('/account/user', {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            },
            credentials: 'include'
        });

        if (!response.ok) {
            if (response.status === 401) {
                throw new Error('Не аутентифицирован');
            }
            throw new Error(`Ошибка получения данных пользователя: ${response.statusText}`);
        }

        const contact = await response.json();

        const sender = {
            email: contact.email || '',
            firstName: contact.firstName || '',
            lastName: contact.lastName || '',
            fatherName: contact.fatherName || '',
            phone: contact.phone || '',
            pic: contact.pic || 'https://i.pinimg.com/736x/97/55/60/975560b7e586c4b0c1c4ce0e0eeac1.jpg'
        };

// Получаем текущие данные из cookie
        let delivery_data = getCookie('delivery_data');

        // Если cookie не существует или пустое, инициализируем как объект
        if (!delivery_data) {
            delivery_data = {};
        } else {
            // Пробуем распарсить строку cookie в объект
            try {
                delivery_data = JSON.parse(delivery_data);
            } catch (e) {
                console.error('Ошибка парсинга delivery_data:', e);
                delivery_data = {}; // Если парсинг не удался, используем пустой объект
            }
        }

        // Обновляем свойство sender
        delivery_data.sender = sender;

        // Сохраняем обновлённые данные в cookie как строку
        setCookie('delivery_data', JSON.stringify(delivery_data), 1);

        return sender;
    } catch (error) {
        console.error('Ошибка при получении данных пользователя:', error);
        showErrorMessage(error.message);
        throw error;
    }
}

/**
 * Запрашивает данные заказов пользователя
 * @throws {Error} Если запрос не удался
 */
export async function fetchUserOrders() {
    try {
        const response = await fetch('/account/orders', {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            },
            credentials: 'include'
        });

        if (!response.ok) {
            if (response.status === 401) {
                throw new Error('Не аутентифицирован');
            }
            throw new Error(`Ошибка получения данных заказов: ${response.statusText}`);
        }

        // Обрабатываем случай 204 No Content
        if (response.status === 204) {
            return [];
        }

        // Проверяем наличие контента перед парсингом
        const contentLength = response.headers.get('Content-Length');
        if (contentLength === '0') {
            return [];
        }

        return await response.json();
    } catch (error) {
        console.error('Ошибка при получении данных заказов:', error);
        showErrorMessage(error.message);
        throw error;
    }
}

export async function deleteOrder(uuid) {
    return await fetch(`/order/${uuid}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        }
    });
}