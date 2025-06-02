/**
 * Модуль для управления страницей личного кабинета
 * Загружает header, данные пользователя и заказы
 */
import { loadHeader } from './common/header.js';
import { showErrorMessage } from './common/error.js';
import { fetchUserContact, fetchUserOrders } from './common/api.js';

class AccountManager {
  constructor() {
    document.addEventListener('DOMContentLoaded', () => this.init());
  }

  /**
   * Основная функция инициализации
   */
  async init() {
    try {
      await loadHeader();
      await this.loadUserData();
      await this.loadOrders();
    } catch (error) {
      showErrorMessage('Ошибка загрузки данных: ' + error.message);
    }
  }

  /**
   * Загружает данные пользователя и заполняет форму профиля
   */
  async loadUserData() {
    try {
      const contact = await fetchUserContact();
      // Заполняем поля формы
      const fields = [
        { name: 'lastName', value: contact.lastName },
        { name: 'firstName', value: contact.firstName },
        { name: 'fatherName', value: contact.fatherName },
        { name: 'email', value: contact.email },
        { name: 'phone', value: contact.phone }
      ];

      fields.forEach(({ name, value }) => {
        const input = document.querySelector(`input[name="${name}"]`);
        if (input) {
          input.value = value || '';
        }
      });
    } catch (error) {
      console.error('Ошибка загрузки данных пользователя:', error);
      showErrorMessage('Не удалось загрузить данные профиля');
    }
  }

  /**
   * Загружает данные заказов и заполняет таблицы
   */
  async loadOrders() {
    try {
      const orders = await fetchUserOrders();
      this.populateOrdersTable('.orders-sent tbody', orders.sentOrders || [], 'sent');
      this.populateOrdersTable('.orders-received tbody', orders.receivedOrders || [], 'received');
    } catch (error) {
      console.error('Ошибка загрузки заказов:', error);
      showErrorMessage('Не удалось загрузить данные заказов');
    }
  }

  /**
   * Заполняет таблицу заказов
   * @param {string} selector - CSS селектор для tbody
   * @param {Array} orders - Массив заказов
   * @param {string} type - Тип заказов ('sent' или 'received')
   */
  populateOrdersTable(selector, orders, type) {
    const tbody = document.querySelector(selector);
    if (!tbody) {
      console.error(`Таблица ${type} не найдена: ${selector}`);
      return;
    }

    // Очищаем таблицу
    tbody.innerHTML = '';

    if (!orders || orders.length === 0) {
      tbody.innerHTML = `<tr><td colspan="4" class="text-center">Нет заказов</td></tr>`;
      // Обновляем счетчик
      const counter = document.querySelector(`.orders-${type} .order-count`);
      if (counter) counter.textContent = '(0)';
      return;
    }

    // Заполняем таблицу
    orders.forEach(order => {
      const row = document.createElement('tr');
      row.innerHTML = `
        <td>${order.id || '-'}</td>
        <td>${order.date || '-'}</td>
        <td>${this.translateStatus(order.status) || '-'}</td>
        <td>${order.price ? order.price.toFixed(2) : '-'}</td>
      `;
      tbody.appendChild(row);
    });

    // Обновляем счетчик
    const counter = document.querySelector(`.orders-${type} .order-count`);
    if (counter) counter.textContent = `(${orders.length})`;
  }

  /**
   * Переводит статус заказа в читаемый вид
   * @param {string} status - Статус заказа
   * @returns {string} Читаемый статус
   */
  translateStatus(status) {
    const statusMap = {
      'PENDING': 'Ожидает',
      'PROCESSING': 'В обработке',
      'SHIPPED': 'Отправлен',
      'DELIVERED': 'Доставлен',
      'CANCELLED': 'Отменен'
    };
    return statusMap[status] || status || 'Неизвестно';
  }
}

new AccountManager();