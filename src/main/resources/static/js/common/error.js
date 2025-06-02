/**
 * Модуль для обработки сообщений об ошибках и валидации формы
 */

/**
 * Показывает ошибку валидации для поля ввода
 * @param {HTMLElement} input - Поле ввода
 * @param {string} errorMessageId - ID для сообщения об ошибке
 * @param {string} message - Текст сообщения об ошибке
 */
export function showValidateFormError(input, errorMessageId, message) {
  input.classList.remove('input-success');
  input.classList.add('input-error');
  input.setAttribute('title', message);

  let errorMessage = document.getElementById(errorMessageId);
  if (errorMessage) errorMessage.remove();

  errorMessage = document.createElement('div');
  errorMessage.id = errorMessageId;
  errorMessage.className = 'field-error-message';
  errorMessage.textContent = message;
  input.insertAdjacentElement('afterend', errorMessage);
}

/**
 * Показывает успешную валидацию для поля ввода
 * @param {HTMLElement} input - Поле ввода
 */
export function showValidateFormSuccess(input) {
  input.classList.remove('input-error');
  input.classList.add('input-success');
  input.removeAttribute('title');

  const errorMessageId = `${input.name || input.id}-error`;
  const errorMessage = document.getElementById(errorMessageId);
  if (errorMessage) errorMessage.remove();
}

/**
 * Отображает общее сообщение об ошибке
 * @param {string} message - Текст сообщения
 */
export function showErrorMessage(message) {
  let errorContainer = document.getElementById('error-message-container');
  if (!errorContainer) {
    errorContainer = document.createElement('div');
    errorContainer.id = 'error-message-container';
    errorContainer.className = 'error-message-container';
    document.body.appendChild(errorContainer);
  }

  const alertDiv = document.createElement('div');
  alertDiv.className = 'alert alert-danger alert-dismissible fade show';
  alertDiv.setAttribute('role', 'alert');
  alertDiv.textContent = message;
  alertDiv.innerHTML += '<button type="button" class="btn-close" aria-label="Close"></button>';
  alertDiv.querySelector('.btn-close').addEventListener('click', () => {
    alertDiv.remove();
    if (errorContainer.children.length === 0) errorContainer.remove();
  });
  errorContainer.appendChild(alertDiv);
}