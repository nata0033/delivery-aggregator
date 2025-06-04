import { showValidateFormError, showValidateFormSuccess } from './messages.js';

export class FormValidator {
    /**
     * Проверяет, что строка содержит только русские буквы и пробелы
     * @param {string} input
     * @returns {boolean}
     */
    static validateRussianText(input) {
        const pattern = /^[а-яё\s]+$/i;
        return pattern.test(input.trim());
    }

    /**
     * Проверяет, что строка — неотрицательное число (целое или дробное)
     * @param {string} input
     * @returns {boolean}
     */
    static validateNonNegativeNumber(input) {
        const pattern = /^\d+(\.\d+)?$/;
        return pattern.test(input.trim());
    }

    /**
     * Проверяет, что строка содержится в списке допустимых значений
     * @param {string} input
     * @param {Array<string>} validOptions
     * @returns {boolean}
     */
    static validateInList(input, jsonList) {
        const trimmedInput = input.trim();
        return jsonList.some(item => item.name === trimmedInput);
    }

    /**
     * Проверяет валидность email адреса
     * @param {string} input
     * @returns {boolean}
     */
    static validateEmail(input) {
        const pattern = /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;
        return pattern.test(input.trim());
    }

    /**
     * Проверяет валидность номера телефона (российский формат)
     * @param {string} input
     * @returns {boolean}
     */
    static validatePhone(input) {
        const digits = input.replace(/\D/g, '');
        return digits.length === 11 && (digits.startsWith('7') || digits.startsWith('8'));
    }

    /**
     * Проверяет валидность почтового индекса
     * @param {string} input
     * @returns {boolean}
     */
    static validatePostalCode(input) {
        const pattern = /^\d{6}$/;
        return pattern.test(input.trim());
    }

    /**
     * Валидирует поле и показывает результат
     * @param {HTMLElement} input - Поле ввода
     * @param {Function} validator - Функция валидации
     * @param {string} errorMessage - Сообщение об ошибке
     */
    static validateAndShow(input, validator, errorMessage) {
        const value = input.value.trim();
        if (!value) {
            input.classList.remove('is-invalid', 'is-valid');
            const errorElement = input.nextElementSibling;
            if (errorElement && errorElement.classList.contains('invalid-feedback')) {
                errorElement.remove();
            }
            return;
        }

        const isValid = validator(value);
        if (isValid) {
            showValidateFormSuccess(input);
        } else {
            showValidateFormError(input, errorMessage);
        }
    }
}