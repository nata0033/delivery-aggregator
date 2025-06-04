/**
 * Модуль для управления тарифами доставки
 * Обрабатывает загрузку, сортировку, фильтрацию и отображение тарифов
 */
import { getTariffs } from './common/api.js';
import { getCookie } from './common/cookies.js';
import { loadHeader } from './common/header.js';
import { showErrorMessage } from './common/messages.js';
import { formatDate } from './common/utils.js';
import { createSpinner, showSpinner } from './common/spinner.js';

class TariffsManager {
  constructor() {
    this.allTariffs = [];
    this.currentSelfPickup = false;
    this.currentSelfDelivery = false;
    document.addEventListener('DOMContentLoaded', () => this.init());
  }

  /**
   * Основная функция инициализации
   */
  init() {
    this.fillDeliveryHeader();
    this.loadAndDisplayTariffs(this.currentSelfPickup, this.currentSelfDelivery);
    this.initEventHandlers();
    loadHeader();
  }

  /**
   * Инициализирует обработчики событий
   */
  initEventHandlers() {
    document.getElementById('sort-options').addEventListener('change', () => this.updateTariffsDisplay());
    document.querySelectorAll('input[type="checkbox"][name="delivery-method"]').forEach(cb => {
      cb.addEventListener('change', () => this.handleDeliveryMethodChange(cb));
    });
    document.querySelectorAll('input[type="radio"][name="delivery-method"]').forEach(radio => {
      radio.addEventListener('change', () => this.handleDeliveryTypeChange(radio));
    });
  }

  /**
   * Заполняет заголовок доставки из cookie
   */
  fillDeliveryHeader() {
    const deliveryDataRaw = getCookie('delivery_data');
    if (!deliveryDataRaw) return;

    try {
      const data = JSON.parse(deliveryDataRaw);
      document.getElementById('dateCell').textContent = formatDate(data.fromLocation.date);
      document.getElementById('fromCity').textContent = data.fromLocation.city;
      document.getElementById('toCity').textContent = data.toLocation.city;
      const totalWeight = data.packages.reduce((sum, p) => sum + (p.weight || 0), 0);
      document.getElementById('packageInfo').textContent = `${totalWeight} г, ${data.packages.length} шт.`;
    } catch (e) {
      showErrorMessage('Некорректный формат данных в cookie delivery_data');
    }
  }

  /**
   * Формирует объект запроса для /tariffs/get
   * @param {boolean} selfPickup
   * @param {boolean} selfDelivery
   * @returns {Object}
   */
  buildRequestData(selfPickup, selfDelivery) {
    const deliveryDataRaw = getCookie('delivery_data');
    if (!deliveryDataRaw) throw new Error('Cookie delivery_data не найдена');
    try {
      const deliveryData = JSON.parse(deliveryDataRaw);
      return {
        fromLocation: deliveryData.fromLocation,
        toLocation: deliveryData.toLocation,
        packages: deliveryData.packages,
        selfPickup,
        selfDelivery
      };
    } catch (e) {
      throw new Error('Некорректный формат данных в cookie delivery_data');
    }
  }

  /**
   * Загружает тарифы и обновляет отображение
   * @param {boolean} selfPickup
   * @param {boolean} selfDelivery
   */
  async loadAndDisplayTariffs(selfPickup, selfDelivery) {
    showSpinner(true);
    const container = document.getElementById('tariffs-list');
    try {
      this.allTariffs = await getTariffs(this.buildRequestData(selfPickup, selfDelivery));
      this.updateTariffsDisplay();
    } catch (e) {
      container.innerHTML = `<div class="alert alert-danger">Ошибка: ${e.message}</div>`;
    }
    showSpinner(false);
  }

  /**
   * Сортирует тарифы
   * @param {Array} tariffs
   * @param {string} sortBy - 'price' | 'speed'
   * @returns {Array}
   */
  sortTariffs(tariffs, sortBy) {
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
   * Фильтрует тарифы по службам доставки
   * @param {Array} tariffs
   * @param {Array} selectedServices
   * @returns {Array}
   */
  filterTariffsByService(tariffs, selectedServices) {
    if (!selectedServices || selectedServices.length === 0) return tariffs;
    return tariffs.filter(tariff => selectedServices.includes(tariff.service.name));
  }

  /**
   * Обновляет отображение тарифов
   */
  updateTariffsDisplay() {
    const sortBy = document.getElementById('sort-options').value;
    const serviceCheckboxes = document.querySelectorAll('input[type="checkbox"][name="delivery-method"]:not(#allServices)');
    let selectedServices = Array.from(serviceCheckboxes)
      .filter(cb => cb.checked)
      .map(cb => cb.id.toUpperCase());
    if (document.getElementById('allServices').checked) {
      selectedServices = [];
    }
    const filtered = this.filterTariffsByService(this.allTariffs, selectedServices);
    const sorted = this.sortTariffs(filtered, sortBy);
    this.renderTariffs(sorted);
  }

  /**
   * Рендерит тарифы в контейнер
   * @param {Array} tariffs
   */
  renderTariffs(tariffs) {
    const container = document.getElementById('tariffs-list');
    container.innerHTML = '';
    if (!tariffs.length) {
      container.innerHTML = '<div class="alert alert-warning">Нет доступных тарифов.</div>';
      return;
    }
    const template = document.getElementById('tariffs-template');
    tariffs.forEach((tariff, idx) => {
      const clone = document.importNode(template.content, true);
      clone.querySelector('.tariff-card').setAttribute('data-tariff-idx', idx);
      clone.querySelector('.tariff-logo').src = tariff.service.logo;
      clone.querySelector('.tariff-logo').alt = tariff.service.name;
      clone.querySelector('.tariff-service').textContent = tariff.service.name;
      clone.querySelector('.tariff-name').textContent = tariff.name;
      clone.querySelector('.tariff-time').textContent = `${tariff.minTime} - ${tariff.maxTime} дня`;
      clone.querySelector('.tariff-delivery-date').textContent = this.getDeliveryDateString(tariff.minTime);
      clone.querySelector('.tariff-weekday').textContent = this.getDeliveryWeekday(tariff.minTime);
      clone.querySelector('.tariff-price').textContent = `${tariff.price}₽`;
      clone.querySelector('.tariff-btn').addEventListener('click', () => this.handleChooseTariff(tariffs[idx]));
      container.appendChild(clone);
    });
  }

  /**
   * Форматирует дату доставки
   * @param {number} minTime
   * @returns {string}
   */
  getDeliveryDateString(minTime) {
    const today = new Date();
    today.setDate(today.getDate() + (minTime || 0));
    return formatDate(today);
  }

  /**
   * Получает день недели доставки
   * @param {number} minTime
   * @returns {string}
   */
  getDeliveryWeekday(minTime) {
    const today = new Date();
    today.setDate(today.getDate() + (minTime || 0));
    const wd = today.toLocaleDateString('ru-RU', { weekday: 'long' });
    return wd.charAt(0).toUpperCase() + wd.slice(1);
  }

  /**
   * Обработчик выбора службы доставки
   * @param {HTMLElement} checkbox
   */
  handleDeliveryMethodChange(checkbox) {
    if (checkbox.id === 'allServices' && checkbox.checked) {
      document.querySelectorAll('input[type="checkbox"][name="delivery-method"]:not(#allServices)').forEach(other => {
        other.checked = false;
      });
    } else if (checkbox.id !== 'allServices' && checkbox.checked) {
      document.getElementById('allServices').checked = false;
    }
    this.updateTariffsDisplay();
  }

  /**
   * Обработчик смены типа доставки
   * @param {HTMLElement} radio
   */
  handleDeliveryTypeChange(radio) {
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
    this.currentSelfPickup = selfPickup;
    this.currentSelfDelivery = selfDelivery;
    this.loadAndDisplayTariffs(selfPickup, selfDelivery);
  }

  /**
   * Выбор тарифа и сохранение в cookie
   * @param {Object} tariff
   */
  handleChooseTariff(tariff) {
    const deliveryDataRaw = getCookie('delivery_data');
    if (!deliveryDataRaw) {
      showErrorMessage('Ошибка: не найдены данные доставки');
      return;
    }
    try {
      const data = JSON.parse(deliveryDataRaw);
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
      data.tariff = selectedTariff;
      document.cookie = `delivery_data=${encodeURIComponent(JSON.stringify(data))};path=/;max-age=86400`;
      window.location.href = '/order';
    } catch (e) {
      showErrorMessage('Ошибка чтения данных доставки');
    }
  }
}

new TariffsManager();