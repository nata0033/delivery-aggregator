export function getCookie(name) {
    const matches = document.cookie.match(
        new RegExp("(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)")
    );
    return matches ? decodeURIComponent(matches[1]) : null;
}

export function setCookie(name, value, days) {
    let expires = "";
    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + encodeURIComponent(value) + expires + "; path=/";
}

/**
 * Удаляет cookie по имени
 * @param {string} name - Имя cookie для удаления
 */
export function deleteCookie(name) {
    // Устанавливаем дату истечения в прошлое (1 января 1970)
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;

    // Для дополнительной надежности добавляем domain, если он используется
    const domain = window.location.hostname;
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=${domain};`;
}