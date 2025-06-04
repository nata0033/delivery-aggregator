/**
 * Модуль для управления страницей личного кабинета
 * Загружает header, данные пользователя и заказы
 */
import { loadHeader } from './common/header.js';
import { showErrorMessage, showSuccessMessage } from './common/messages.js';
import { fetchUserContact, fetchUserOrders, deleteOrder } from './common/api.js';
import { formatDate } from './common/utils.js';

class AccountManager {
  constructor() {
    this.orders = [];
    this.currentSortColumn = null;
    this.sortDirection = 1; // 1 - по возрастанию, -1 - по убыванию
    this.modal = null; // Будет хранить экземпляр модального окна
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
      this.initSortHandlers();
      this.initModal();
    } catch (error) {
      console.error('Ошибка инициализации страницы:', error);
      showErrorMessage('Ошибка загрузки данных: ' + error.message);
    }
  }

  /**
   * Загружает данные пользователя и обновляет профиль
   */
  async loadUserData() {
    try {
      const contact = await fetchUserContact();
      if (!contact) {
        throw new Error('Данные пользователя не получены');
      }
      // Обновляем имя в профиле
      const authorName = document.querySelector('.author-card-name');
      if (authorName) {
        authorName.textContent = `${contact.lastName} ${contact.firstName} ${contact.fatherName || ''}`.trim();
      } else {
        console.warn('Элемент .author-card-name не найден');
      }
      // Обновляем email
      const email = document.querySelector('.author-card-email');
      if (email) {
        email.textContent = `${contact.email}`;
      }
      // Обновляем телефон
      const phone = document.querySelector('.author-card-phone');
      if (phone) {
        phone.textContent = `${contact.phone}`;
      }
    } catch (error) {
      console.error('Ошибка загрузки данных пользователя:', error);
      showErrorMessage('Не удалось загрузить данные профиля: ' + error.message);
    }
  }

  /**
   * Загружает данные заказов и заполняет таблицу
   */
  async loadOrders() {
    try {
      this.orders = await fetchUserOrders() || [];
      this.populateOrdersTable('table tbody', this.orders);
      // Обновляем счетчик заказов
      const counter = document.querySelector('.badge.badge-secondary');
      if (counter) {
        counter.textContent = this.orders.length;
      }
    } catch (error) {
      console.error('Ошибка загрузки заказов:', error);
      showErrorMessage('Не удалось загрузить данные заказов: ' + error.message);
    }
  }

  /**
   * Заполняет таблицу заказов
   * @param {string} selector - CSS селектор для tbody
   * @param {Array} orders - Массив заказов
   */
  populateOrdersTable(selector, orders) {
    const tbody = document.querySelector(selector);
    if (!tbody) {
      console.error(`Таблица заказов не найдена: ${selector}`);
      return;
    }

    // Очищаем таблицу
    tbody.innerHTML = '';

    if (!orders || orders.length === 0) {
      tbody.innerHTML = `<tr><td colspan="4" class="text-center">Нет заказов</td></tr>`;
      return;
    }

    // Заполняем таблицу
    orders.forEach(order => {
      const row = document.createElement('tr');
      row.innerHTML = `
        <td><a class="navi-link" href="" data-bs-toggle="modal" data-order-id="${order.id}">${order.number}</a></td>
        <td>${formatDate(order.date)}</td>
        <td><span class="badge ${this.getStatusClass(order.status)}">${this.translateStatus(order.status)}</span></td>
        <td><span>$${order.price.toFixed(2)}</span></td>
      `;
      // Добавляем обработчик клика на строку
      row.addEventListener('click', (e) => {this.toggleOrderDetails(order);});
      tbody.appendChild(row);
    });
  }

  /**
   * Возвращает CSS-класс для статуса
   * @param {string} status - Статус заказа
   * @returns {string} CSS-класс
   */
getStatusClass(status) {
    const statusClasses = {
        'ACCEPTED': 'badge rounded-pill bg-primary',
        'IN_PROGRESS': 'badge rounded-pill bg-primary',
        'DELAYED': 'badge rounded-pill bg-warning text-dark',
        'CANCELED': 'badge rounded-pill bg-danger',
        'DELIVERED': 'badge rounded-pill bg-success'
    };

    return statusClasses[status] || 'badge rounded-pill bg-secondary'; // По умолчанию серый цвет
}

  /**
   * Переводит статус заказа в читаемый вид
   * @param {string} status - Статус заказа
   * @returns {string} Читаемый статус
   */
  translateStatus(status) {
    const statusMap = {
        'ACCEPTED': 'Принят',
        'IN_PROGRESS': 'Доставляется',
        'DELAYED': 'Задержвается',
        'CANCELED': 'Отменен',
        'DELIVERED': 'Доставлен'
    };
    return statusMap[status] || status || 'Неизвестно';
  }

  /**
   * Инициализирует обработчики сортировки
   */
  initSortHandlers() {
    const headers = document.querySelectorAll('table thead th');
    headers.forEach((header, index) => {
      header.style.cursor = 'pointer';
      header.addEventListener('click', () => {
        // Если уже сортируем по этому столбцу, меняем направление
        if (this.currentSortColumn === index) {
          this.sortDirection *= -1;
        } else {
          this.currentSortColumn = index;
          this.sortDirection = 1;
        }
        this.sortOrders(index);
      });
    });
  }

  /**
   * Сортирует заказы по указанному полю
   * @param {number} columnIndex - Индекс столбца (0: number, 1: date, 2: status, 3: price)
   */
  sortOrders(columnIndex) {
    const sortFunctions = [
      (a, b) => a.number.localeCompare(b.number) * this.sortDirection, // Номер #
      (a, b) => (new Date(a.date) - new Date(b.date)) * this.sortDirection, // Дата
      (a, b) => this.translateStatus(a.status).localeCompare(this.translateStatus(b.status)) * this.sortDirection, // Статус
      (a, b) => (a.price - b.price) * this.sortDirection // Цена
    ];

    this.orders.sort(sortFunctions[columnIndex]);
    this.populateOrdersTable('table tbody', this.orders);
  }

  /**
   * Инициализирует модальное окно
   */
  initModal() {
    const modalElement = document.getElementById('orderDetailsModal');
    if (!modalElement) {
      console.error('Модальное окно orderDetailsModal не найдено');
      showErrorMessage('Не удалось инициализировать модальное окно');
      return;
    }

    // Создаем экземпляр модального окна Bootstrap
    this.modal = new bootstrap.Modal(modalElement);

    // Обработчик скрытия модального окна
    modalElement.addEventListener('hidden.bs.modal', () => {
      const modalBody = modalElement.querySelector('.modal-body');
      if (modalBody) {
        modalBody.removeAttribute('data-order-id');
      }
    });
  }

  /**
   * Показывает/скрывает подробную информацию о заказе
   * @param {Object} order - Данные заказа
   */
  toggleOrderDetails(order) {
      if (!this.modal) {
          console.error('Модальное окно не инициализировано');
          return;
      }

    const modalElement = document.getElementById('orderDetailsModal');

    if (!modalElement) {
      console.error('Модальное окно orderDetailsModal не найдено');
      return;
    }

    const modalBody = modalElement.querySelector('.modal-body');
    if (!modalBody) {
      console.error('Элемент .modal-body не найден в модальном окне');
      return;
    }

    // Если модальное окно уже открыто и показывает этот заказ - закрываем его
    if (modalBody.dataset.orderId === order.id && this.modal) {
      this.modal.hide();
      return;
    }

    // Рассчитываем общий вес и количество посылок
    const totalWeight = order.packages.reduce((sum, pkg) => sum + (pkg.weight || 0), 0);
    const packageCount = order.packages.length;

    modalBody.innerHTML = `
      <p><strong>Получатель:</strong> ${order.recipient.lastName} ${order.recipient.firstName} ${order.recipient.fatherName || ''}</p>
      <p><strong>Email:</strong> ${order.recipient.email}</p>
      <p><strong>Телефон:</strong> ${order.recipient.phone}</p>
      <p><strong>Адрес отправления:</strong> ${order.fromLocation}</p>
      <p><strong>Адрес получения:</strong> ${order.toLocation}</p>
      <p><strong>Посылка:</strong> ${packageCount}, ${totalWeight} кг</p>
    `;
    modalBody.dataset.orderId = order.id;

    // Проверяем, можно ли отменить заказ (только для определенных статусов)
    const canCancel = ['ACCEPTED', 'DELAYED', 'IN_PROGRESS'].includes(order.status);

    // Добавляем обработчик кнопки отмены, если она есть
    if (canCancel) {
        const modalFooter = modalElement.querySelector('.modal-footer')
        const cancelButton = modalFooter.querySelector('#cancelOrderButton');

        cancelButton.disabled = false;
        if (cancelButton) {
            cancelButton.addEventListener('click', async (e) => {
                e.stopPropagation();
                await this.cancelOrder(order.id);
            });
        }
    }

    // Показываем модальное окно
    if (this.modal) {
      this.modal.show();
    }
  }

  /**
   * Отменяет заказ по UUID
   * @param {string} uuid - UUID заказа
   */
  async cancelOrder(uuid) {
      if (!uuid) {
          showErrorMessage('Не указан идентификатор заказа');
          return false;
      }

      try {
          // Подтверждение отмены
          const isConfirmed = confirm('Вы уверены, что хотите отменить заказ?');
          if (!isConfirmed) return false;

        const response = await deleteOrder(uuid);

          if (response.ok) {
              showSuccessMessage('Заказ успешно отменен');
              await this.loadOrders();
              if (this.modal) {
                  this.modal.hide();
              }
              return true;
          } else {
              const error = await response.json();
              throw new Error(error.message || 'Не удалось отменить заказ');
          }
      } catch (error) {
          console.error('Ошибка отмены заказа:', error);
          showErrorMessage('Ошибка при отмене заказа: ' + error.message);
          return false;
      }
  }
}

new AccountManager();