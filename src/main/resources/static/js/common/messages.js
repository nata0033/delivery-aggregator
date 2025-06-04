/**
 * Показывает успешную валидацию для поля ввода
 * @param {HTMLElement} input - Поле ввода
 */
export function showValidateFormSuccess(input) {
    input.classList.remove('is-invalid');
    input.classList.add('is-valid');

    // Удаляем сообщение об ошибке, если оно есть
    const errorMessage = input.nextElementSibling;
    if (errorMessage && errorMessage.classList.contains('invalid-feedback')) {
        errorMessage.remove();
    }
}

/**
 * Показывает ошибку валидации для поля ввода
 * @param {HTMLElement} input - Поле ввода
 * @param {string} message - Текст сообщения об ошибке
 */

export function showValidateFormError(input, message) {
    input.classList.remove('is-valid');
    input.classList.add('is-invalid');

    let errorMessage = input.nextElementSibling;
    if (errorMessage && errorMessage.classList.contains('invalid-feedback')) {
        errorMessage.textContent = message; // Обновляем текст существующего сообщения
    } else {
        errorMessage = document.createElement('div');
        errorMessage.className = 'invalid-feedback';
        errorMessage.textContent = message;
        input.parentNode.appendChild(errorMessage); // Добавляем новое сообщение, если его нет
    }
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

/**
 * Отображает общее сообщение о успешной работе
 * @param {string} message - Текст сообщения
 */
export function showSuccessMessage(message) {
    let successContainer = document.getElementById('success-message-container');
    if (!successContainer) {
        successContainer = document.createElement('div');
        successContainer.id = 'success-message-container';
        successContainer.className = 'success-message-container';
        document.body.appendChild(successContainer);
    }

    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-success alert-dismissible fade show';
    alertDiv.setAttribute('role', 'alert');
    alertDiv.textContent = message;
    alertDiv.innerHTML += '<button type="button" class="btn-close" aria-label="Close"></button>';
    alertDiv.querySelector('.btn-close').addEventListener('click', () => {
        alertDiv.remove();
        if (successContainer.children.length === 0) successContainer.remove();
    });
    successContainer.appendChild(alertDiv);
}