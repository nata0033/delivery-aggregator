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
   * Допустимы форматы: +7XXXXXXXXXX, 8XXXXXXXXXX, с пробелами, дефисами и скобками
   * @param {string} input
   * @returns {boolean}
   */
  static validatePhone(input) {
    // Удаляем все символы, кроме цифр
    const digits = input.replace(/\D/g, '');

    // Российские номера: 11 цифр, начинаются с 7 или 8
    if (digits.length === 11 && (digits.startsWith('7') || digits.startsWith('8'))) {
      return true;
    }

    return false;
  }

  static validatePostalCode(input) {
      const pattern = /^\d{6}$/;
      return pattern.test(input.trim());
    }
}
