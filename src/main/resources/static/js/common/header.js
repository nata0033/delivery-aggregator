import { checkAuthStatus } from './api.js';

export async function loadHeader() {
    try {
        const isAuthenticated = await checkAuthStatus();
        const headerTemplate = document.getElementById('header-template');
        const headerClone = document.importNode(headerTemplate.content, true);

        if (isAuthenticated) {
            const navList = headerClone.querySelector('ul');
            const accountLi = document.createElement('li');
            accountLi.innerHTML = '<a href="/account" class="nav-link px-2 text-white">Личный кабинет</a>';
            navList.appendChild(accountLi);
        }

        const headerButtons = headerClone.getElementById('header-buttons-container');
        if (isAuthenticated) {
            headerButtons.innerHTML = '<a href="/logout" class="btn btn-light">Выход</a>';
        } else {
            headerButtons.innerHTML = `
                <a href="/registration" class="btn btn-outline-light me-2">Регистрация</a>
                <a href="/login" class="btn btn-light">Вход</a>
            `;
        }

        document.getElementById("header-container").appendChild(headerClone);
    } catch (error) {
        console.error('Ошибка при загрузке заголовка:', error);
        document.getElementById("header-container").innerHTML = '';
    }
}