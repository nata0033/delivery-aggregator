/**
 * Модуль для управления страницей заказа
 */
import { checkAuthStatus, createOrder, fetchUserContact } from './common/api.js';
import { getCookie, setCookie, deleteCookie } from './common/cookies.js';
import { loadHeader } from './common/header.js';
import { showErrorMessage, showSuccessMessage } from './common/messages.js';
import { declOfNum, formatDate } from './common/utils.js';
import { FormValidator } from './common/validation.js';
import { showSpinner } from './common/spinner.js';

class OrderManager {
    constructor() {
        document.addEventListener('DOMContentLoaded', () => this.init());
    }

    async init() {
        await loadHeader();
        this.loadFormData();
        this.initEventHandlers();
        this.initValidation();
        if( new URLSearchParams(window.location.search).has('retryOrder')){
            const fakeEvent = {
                preventDefault: () => {}
            };
            await this.sendOrder(fakeEvent);
        }
    }

    initEventHandlers() {
        document.getElementById('order-pay-top').addEventListener('click', (e) => this.sendOrder(e));
        document.getElementById('order-pay-bottom').addEventListener('click', (e) => this.sendOrder(e));
    }

    loadFormData() {
        const deliveryDataRaw = getCookie('delivery_data');
        if (!deliveryDataRaw) {
            showErrorMessage('Отсутствуют данные о доставке в cookies');
            return;
        }

        try {
            const deliveryData = JSON.parse(deliveryDataRaw);

            if (deliveryData.sender) {
                document.getElementById('sender-firstname').value = deliveryData.sender.firstName || '';
                document.getElementById('sender-lastname').value = deliveryData.sender.lastName || '';
                document.getElementById('sender-fathername').value = deliveryData.sender.fatherName || '';
                document.getElementById('sender-email').value = deliveryData.sender.email || '';
                document.getElementById('sender-phone').value = deliveryData.sender.phone || '';
            }

            if (deliveryData.recipient) {
                document.getElementById('recipient-firstname').value = deliveryData.recipient.firstName || '';
                document.getElementById('recipient-lastname').value = deliveryData.recipient.lastName || '';
                document.getElementById('recipient-fathername').value = deliveryData.recipient.fatherName || '';
                document.getElementById('recipient-email').value = deliveryData.recipient.email || '';
                document.getElementById('recipient-phone').value = deliveryData.recipient.phone || '';
            }

            if (deliveryData.fromLocation) {
                document.getElementById('from-state').value = deliveryData.fromLocation.state || '';
                document.getElementById('from-city').value = deliveryData.fromLocation.city || '';
                document.getElementById('from-street').value = deliveryData.fromLocation.street || '';
                document.getElementById('from-house').value = deliveryData.fromLocation.house || '';
                document.getElementById('from-apartment').value = deliveryData.fromLocation.apartment || '';
                document.getElementById('from-postalCode').value = deliveryData.fromLocation.postalCode || '';
            }

            if (deliveryData.toLocation) {
                document.getElementById('to-state').value = deliveryData.toLocation.state || '';
                document.getElementById('to-city').value = deliveryData.toLocation.city || '';
                document.getElementById('to-street').value = deliveryData.toLocation.street || '';
                document.getElementById('to-house').value = deliveryData.toLocation.house || '';
                document.getElementById('to-apartment').value = deliveryData.toLocation.apartment || '';
                document.getElementById('to-postalCode').value = deliveryData.toLocation.postalCode || '';
            }

            if (deliveryData.tariff) {
                document.getElementById('service-logo').src = deliveryData.tariff.service.logo;
                document.getElementById('service-logo').alt = deliveryData.tariff.service.name || 'Логотип службы доставки';
                document.getElementById('tariff-name').textContent = deliveryData.tariff.name || '';

                const count = deliveryData.packages.length;
                document.getElementById('package-count').textContent = `${count} ${declOfNum(count, ['груз', 'груза', 'грузов'])}`;
                const totalWeight = deliveryData.packages.reduce((sum, pkg) => sum + (pkg.weight || 0), 0);
                document.getElementById('package-weight').textContent = `${totalWeight} г`;

                document.getElementById('tariff-price').textContent = deliveryData.tariff.price ? `${deliveryData.tariff.price} ₽` : '';
                document.getElementById('total-price').textContent = deliveryData.tariff.price ? `${deliveryData.tariff.price} ₽` : '';
            }
            else{
                window.location.href = '/';
            }
        } catch (e) {
            showErrorMessage('Некорректный формат данных в cookie доставки');
        }
    }

    initValidation() {
        const fields = [
            { id: 'sender-firstname', validator: FormValidator.validateRussianText, error: 'Введите имя русскими буквами' },
            { id: 'sender-lastname', validator: FormValidator.validateRussianText, error: 'Введите фамилию русскими буквами' },
            { id: 'sender-fathername', validator: FormValidator.validateRussianText, error: 'Введите отчество русскими буквами' },
            { id: 'sender-email', validator: FormValidator.validateEmail, error: 'Введите корректный email' },
            { id: 'sender-phone', validator: FormValidator.validatePhone, error: 'Введите корректный номер телефона' },
            { id: 'recipient-firstname', validator: FormValidator.validateRussianText, error: 'Введите имя русскими буквами' },
            { id: 'recipient-lastname', validator: FormValidator.validateRussianText, error: 'Введите фамилию русскими буквами' },
            { id: 'recipient-fathername', validator: FormValidator.validateRussianText, error: 'Введите отчество русскими буквами' },
            { id: 'recipient-email', validator: FormValidator.validateEmail, error: 'Введите корректный email' },
            { id: 'recipient-phone', validator: FormValidator.validatePhone, error: 'Введите корректный номер телефона' },
            { id: 'from-state', validator: FormValidator.validateRussianText, error: 'Введите регион русскими буквами' },
            { id: 'from-city', validator: FormValidator.validateRussianText, error: 'Введите город русскими буквами' },
            { id: 'from-street', validator: FormValidator.validateRussianText, error: 'Введите улицу русскими буквами' },
            { id: 'from-house', validator: FormValidator.validateNonNegativeNumber, error: 'Введите номер дома' },
            { id: 'from-apartment', validator: FormValidator.validateNonNegativeNumber, error: 'Введите номер квартиры' },
            { id: 'from-postalCode', validator: FormValidator.validatePostalCode, error: 'Введите корректный почтовый индекс' },
            { id: 'to-state', validator: FormValidator.validateRussianText, error: 'Введите регион русскими буквами' },
            { id: 'to-city', validator: FormValidator.validateRussianText, error: 'Введите город русскими буквами' },
            { id: 'to-street', validator: FormValidator.validateRussianText, error: 'Введите улицу русскими буквами' },
            { id: 'to-house', validator: FormValidator.validateNonNegativeNumber, error: 'Введите номер дома' },
            { id: 'to-apartment', validator: FormValidator.validateNonNegativeNumber, error: 'Введите номер квартиры' },
            { id: 'to-postalCode', validator: FormValidator.validatePostalCode, error: 'Введите корректный почтовый индекс' }
        ];

        fields.forEach(({ id, validator, error }) => {
            const input = document.getElementById(id);
            if (input) {
                const validate = () => FormValidator.validateAndShow(input, validator, error);
                input.addEventListener('input', validate);
                input.addEventListener('blur', validate);
            }
        });
    }

    isFormValid() {
        const fields = [
            { id: 'sender-firstname', validator: FormValidator.validateRussianText },
            { id: 'sender-lastname', validator: FormValidator.validateRussianText },
            { id: 'sender-fathername', validator: FormValidator.validateRussianText },
            { id: 'sender-email', validator: FormValidator.validateEmail },
            { id: 'sender-phone', validator: FormValidator.validatePhone },
            { id: 'recipient-firstname', validator: FormValidator.validateRussianText },
            { id: 'recipient-lastname', validator: FormValidator.validateRussianText },
            { id: 'recipient-fathername', validator: FormValidator.validateRussianText },
            { id: 'recipient-email', validator: FormValidator.validateEmail },
            { id: 'recipient-phone', validator: FormValidator.validatePhone },
            { id: 'from-state', validator: FormValidator.validateRussianText },
            { id: 'from-city', validator: FormValidator.validateRussianText },
            { id: 'from-street', validator: FormValidator.validateRussianText },
            { id: 'from-house', validator: FormValidator.validateNonNegativeNumber },
            { id: 'from-apartment', validator: FormValidator.validateNonNegativeNumber },
            { id: 'from-postalCode', validator: FormValidator.validatePostalCode },
            { id: 'to-state', validator: FormValidator.validateRussianText },
            { id: 'to-city', validator: FormValidator.validateRussianText },
            { id: 'to-street', validator: FormValidator.validateRussianText },
            { id: 'to-house', validator: FormValidator.validateNonNegativeNumber },
            { id: 'to-apartment', validator: FormValidator.validateNonNegativeNumber },
            { id: 'to-postalCode', validator: FormValidator.validatePostalCode }
        ];

        return fields.every(({ id, validator }) => {
            const input = document.getElementById(id);
            return input.value.trim() && validator(input.value.trim());
        });
    }

    async sendOrder(event) {
        event.preventDefault();
        if (!this.isFormValid()) {
            showErrorMessage('Пожалуйста, заполните все поля корректно');
            return;
        }

        showSpinner(true);
        try {
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
                    date: getCookie('delivery_data') ? JSON.parse(getCookie('delivery_data')).fromLocation.date : ''
                },
                toLocation: {
                    state: document.getElementById('to-state').value,
                    city: document.getElementById('to-city').value,
                    street: document.getElementById('to-street').value,
                    house: document.getElementById('to-house').value,
                    apartment: document.getElementById('to-apartment').value,
                    postalCode: document.getElementById('to-postalCode').value
                },
                comment: document.getElementById('order-comment').value
            };

            const deliveryDataRaw = getCookie('delivery_data');
            if (!deliveryDataRaw) {
                throw new Error('Отсутствуют данные о доставке в cookies');
            }

            let deliveryData;
            try {
                deliveryData = JSON.parse(deliveryDataRaw);
            } catch (e) {
                throw new Error('Некорректный формат данных в cookie доставки');
            }

            const updatedDeliveryData = {
                ...deliveryData,
                sender: orderData.sender,
                recipient: orderData.recipient,
                fromLocation: { ...deliveryData.fromLocation, ...orderData.fromLocation },
                toLocation: { ...deliveryData.toLocation, ...orderData.toLocation }
            };

            setCookie('delivery_data', JSON.stringify(updatedDeliveryData), 7);
            orderData.tariff = updatedDeliveryData.tariff;
            orderData.packages = updatedDeliveryData.packages;

            const isAuthenticated = await checkAuthStatus();
            if (!isAuthenticated) {
                window.location.href = '/login';
                return;
            }

            const response = await createOrder(orderData);

            showSuccessMessage('Заказ успешно создан'); // Показываем уведомление
            deleteCookie('delivery_data') //Удаляем куки
            setTimeout(() => {
            window.location.href = '/account'; // Переход на страницу аккаунта
            }, 2000);
        } catch (error) {
            console.error('Ошибка отправки заказа:', error);
        } finally {
            showSpinner(false);
        }
    }
}

new OrderManager();