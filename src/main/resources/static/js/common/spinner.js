/**
 * Создает элемент спиннера из шаблона
 * @returns {HTMLElement}
 */
export function createSpinner() {
  const template = document.getElementById('spinner-template');
  const spinner = document.importNode(template.content, true).firstElementChild;
  spinner.id = 'spinner';
  spinner.style.display = 'none';
  return spinner;
}

/**
 * Показывает или скрывает спиннер, управляет затемнением фона и блокировкой кнопок
 * @param {boolean} show - Показать или скрыть спиннер
 */
export function showSpinner(show) {
  let spinner = document.getElementById('spinner');
  if (!spinner) {
    spinner = createSpinner();
    document.body.appendChild(spinner);
  }
  spinner.style.display = show ? 'flex' : 'none';

  // Управление затемнением фона
  let overlay = document.getElementById('spinner-overlay');
  if (!overlay && show) {
    overlay = document.createElement('div');
    overlay.id = 'spinner-overlay';
    overlay.style.position = 'fixed';
    overlay.style.top = '0';
    overlay.style.left = '0';
    overlay.style.width = '100%';
    overlay.style.height = '100%';
    overlay.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
    overlay.style.zIndex = '999';
    document.body.appendChild(overlay);
  }
  if (overlay) {
    overlay.style.display = show ? 'block' : 'none';
  }

  // Блокировка/разблокировка кнопок
  document.querySelectorAll('button, input[type="submit"], input[type="button"]').forEach(button => {
    button.disabled = show;
  });
}