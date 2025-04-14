// ========== ГЛОБАЛЬНЫЕ ПЕРЕМЕННЫЕ ==========
let currentSort = {
    field: 'date',
    direction: 1 // 1 - по возрастанию, -1 - по убыванию
};

// ========== ИНИЦИАЛИЗАЦИЯ ==========
document.addEventListener('DOMContentLoaded', function() {
    initNavigation();
    initOrdersTable();
    initPasswordChangeModal();
    initEmailVerification();
});

// ========== НАВИГАЦИЯ ПО РАЗДЕЛАМ ==========
function initNavigation() {
    const menuButtons = {
        deliveries: document.getElementById('deliveriesButton'),
        accountEdit: document.getElementById('accountEditButton'),
        addresses: document.getElementById('addressesButton'),
        recipients: document.getElementById('recipientsButton')
    };

    const contentBlocks = {
        deliveries: document.getElementById('deliveries'),
        accountEdit: document.getElementById('accountEdit'),
        addresses: document.getElementById('addresses'),
        contacts: document.getElementById('contacts')
    };

    function hideAllContentBlocks() {
        for (const key in contentBlocks) {
            if (contentBlocks[key]) {
                contentBlocks[key].style.display = 'none';
            }
        }
    }

    function activateMenu(buttonKey, contentKey) {
        hideAllContentBlocks();

        for (const key in menuButtons) {
            if (menuButtons[key]) {
                menuButtons[key].classList.remove('active');
            }
        }

        if (contentBlocks[contentKey]) {
            contentBlocks[contentKey].style.display = 'block';
        }
        if (menuButtons[buttonKey]) {
            menuButtons[buttonKey].classList.add('active');
        }
    }

    // Обработчики для кнопок навигации
    menuButtons.deliveries?.addEventListener('click', function(e) {
        e.preventDefault();
        activateMenu('deliveries', 'deliveries');
    });

    menuButtons.accountEdit?.addEventListener('click', function(e) {
        e.preventDefault();
        activateMenu('accountEdit', 'accountEdit');
    });

    menuButtons.addresses?.addEventListener('click', function(e) {
        e.preventDefault();
        activateMenu('addresses', 'addresses');
        renderAddresses();
    });

    menuButtons.recipients?.addEventListener('click', function(e) {
        e.preventDefault();
        activateMenu('recipients', 'contacts');
        renderContacts();
    });

    activateMenu('deliveries', 'deliveries');
}

// ========== ТАБЛИЦА ЗАКАЗОВ ==========
function initOrdersTable() {
    const ordersData = {
        orders: JSON.parse(document.getElementById('orders-data').textContent)
    };

    const ordersBody = document.getElementById('orders-body');
    const sortableHeaders = document.querySelectorAll('th[data-sort]');

    // Назначаем обработчики кликов на заголовки
    sortableHeaders.forEach(header => {
        header.style.cursor = 'pointer';
        header.addEventListener('click', () => {
            sortOrders(ordersData, header.dataset.sort);
        });
    });

    // Инициализация - сортировка по дате (по возрастанию)
    sortOrders(ordersData, 'date');
}

function formatDate(isoDate) {
    if (!isoDate) return '—';
    try {
        const date = new Date(isoDate);
        if (isNaN(date.getTime())) return isoDate;

        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();

        return `${day}-${month}-${year}`;
    } catch (e) {
        console.error('Ошибка форматирования даты:', e);
        return isoDate;
    }
}

function sortOrders(ordersData, field) {
    // Если сортируем по тому же полю, меняем направление
    if (field === currentSort.field) {
        currentSort.direction *= -1;
    } else {
        currentSort.field = field;
        currentSort.direction = 1;
    }

    // Особый случай для статуса
    if (field === 'status') {
        const statusOrder = {
            'ACCEPTED': currentSort.direction === 1 ? 1 : 2,
            'CANCELED': currentSort.direction === 1 ? 2 : 1,
            'DELIVERED': currentSort.direction === 1 ? 3 : 3
        };

        ordersData.orders.sort((a, b) => {
            return statusOrder[a.status] - statusOrder[b.status] || a.number.localeCompare(b.number);
        });
    } else {
        // Стандартная сортировка
        ordersData.orders.sort((a, b) => {
            const aValue = a[field];
            const bValue = b[field];

            if (aValue < bValue) return -1 * currentSort.direction;
            if (aValue > bValue) return 1 * currentSort.direction;
            return 0;
        });
    }

    // Обновляем индикаторы сортировки
    updateSortIndicators();
    renderOrders(ordersData);
}

function updateSortIndicators() {
    const sortableHeaders = document.querySelectorAll('th[data-sort]');
    sortableHeaders.forEach(header => {
        const sortField = header.dataset.sort;
        header.querySelector('.sort-indicator')?.remove();

        if (sortField === currentSort.field) {
            const indicator = document.createElement('span');
            indicator.className = 'sort-indicator ml-1';
            indicator.textContent = currentSort.direction === 1 ? '▼' : '▲';
            header.appendChild(indicator);
        }
    });
}

function renderOrders(ordersData) {
    const ordersBody = document.getElementById('orders-body');
    ordersBody.innerHTML = '';

    ordersData.orders.forEach(order => {
        // Основная строка заказа
        const mainRow = document.createElement('tr');
        mainRow.dataset.orderNumber = order.number;

        // Номер заказа
        const numberCell = document.createElement('td');
        const numberLink = document.createElement('a');
        numberLink.className = 'navi-link';
        numberLink.href = '#';
        numberLink.dataset.toggle = 'modal';
        numberLink.textContent = order.number;
        numberLink.addEventListener('click', function(e) {
            e.preventDefault();
            toggleOrderDetails(order.number);
        });
        numberCell.appendChild(numberLink);
        mainRow.appendChild(numberCell);

        // Дата заказа
        const dateCell = document.createElement('td');
        dateCell.textContent = formatDate(order.date);
        mainRow.appendChild(dateCell);

        // Статус заказа
        const statusCell = document.createElement('td');
        const statusBadge = document.createElement('span');
        statusBadge.className = 'badge m-0 ';

        let statusText = '';
        switch(order.status) {
            case 'ACCEPTED':
                statusBadge.classList.add('badge-info');
                statusText = 'Принят';
                break;
            case 'DELIVERED':
                statusBadge.classList.add('badge-success');
                statusText = 'Доставлено';
                break;
            case 'CANCELED':
                statusBadge.classList.add('badge-danger');
                statusText = 'Отмена';
                break;
            default:
                statusBadge.classList.add('badge-secondary');
                statusText = order.status;
        }

        statusBadge.textContent = statusText;
        statusCell.appendChild(statusBadge);
        mainRow.appendChild(statusCell);

        // Цена заказа
        const priceCell = document.createElement('td');
        priceCell.textContent = order.price + '₽';
        mainRow.appendChild(priceCell);

        ordersBody.appendChild(mainRow);

        // Детали заказа
        const detailsRow = document.createElement('tr');
        detailsRow.className = 'order-details';
        detailsRow.dataset.orderNumber = order.number;
        detailsRow.style.display = 'none';

        const detailsCell = document.createElement('td');
        detailsCell.colSpan = 4;

        const serviceNameInfo = document.createElement('div');
        serviceNameInfo.className = 'mb-2';
        serviceNameInfo.innerHTML = `<strong>Сервис доставки:</strong> ${order.serviceName || 'Не указан'}`;
        detailsCell.appendChild(serviceNameInfo);

        const recipientInfo = document.createElement('div');
        recipientInfo.className = 'mb-2';
        recipientInfo.innerHTML = `<strong>Получатель:</strong> ${order.recipient.lastName} ${order.recipient.firstName} ${order.recipient.fatherName}, ${order.recipient.phone}`;
        detailsCell.appendChild(recipientInfo);

        const fromLocationInfo = document.createElement('div');
        fromLocationInfo.className = 'mb-2';
        fromLocationInfo.innerHTML = `<strong>Откуда:</strong> ${order.fromLocation}`;
        detailsCell.appendChild(fromLocationInfo);

        const toLocationInfo = document.createElement('div');
        toLocationInfo.className = 'mb-2';
        toLocationInfo.innerHTML = `<strong>Куда:</strong> ${order.toLocation}`;
        detailsCell.appendChild(toLocationInfo);

        const packagesInfo = document.createElement('div');
        const totalWeight = order.packages.reduce((sum, pkg) => sum + (pkg.weight || 0), 0);
        packagesInfo.innerHTML = `<strong>Грузы:</strong> ${order.packages.length} шт., Общий вес: ${totalWeight} кг`;
        detailsCell.appendChild(packagesInfo);

        detailsRow.appendChild(detailsCell);
        ordersBody.appendChild(detailsRow);
    });
}

function toggleOrderDetails(orderNumber) {
    const detailsRow = document.querySelector(`tr.order-details[data-order-number="${orderNumber}"]`);
    if (detailsRow) {
        detailsRow.style.display = detailsRow.style.display === 'none' ? 'table-row' : 'none';
    }
}

// ========== ИЗМЕНЕНИЕ ПАРОЛЯ ==========
function initPasswordChangeModal() {
    // Обработчик для кнопки "Изменить пароль"
    const changePasswordBtn = document.getElementById('changePasswordButton');
    if (changePasswordBtn) {
        changePasswordBtn.addEventListener('click', function(e) {
            e.preventDefault();
            $('#passwordChangeModal').modal('show');
        });
    }

    // Обработчик формы изменения пароля
    const passwordChangeForm = document.getElementById('passwordChangeForm');
    if (passwordChangeForm) {
        passwordChangeForm.addEventListener('submit', function(e) {
            e.preventDefault();
            changeUserPassword();
        });
    }

    // Очистка формы при закрытии модального окна
    $('#passwordChangeModal').on('hidden.bs.modal', function() {
        passwordChangeForm.reset();
        document.getElementById('passwordChangeError').style.display = 'none';
    });
}

function changeUserPassword() {
    const oldPassword = document.getElementById('oldPassword').value;
    const newPassword = document.getElementById('newPassword').value;
    const repeatNewPassword = document.getElementById('repeatNewPassword').value;

    // Валидация
    if (newPassword !== repeatNewPassword) {
        document.getElementById('passwordChangeError').textContent = 'Пароли не совпадают';
        document.getElementById('passwordChangeError').style.display = 'block';
        return;
    }

    fetch('/account/changeUserPassword', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `oldPassword=${encodeURIComponent(oldPassword)}&newPassword=${encodeURIComponent(newPassword)}`
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Ошибка сети');
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            // Показываем сообщение об успехе
            const successMessage = document.createElement('div');
            successMessage.className = 'alert alert-success';
            successMessage.textContent = 'Пароль обновлен';
            passwordChangeForm.appendChild(successMessage);

            // Закрываем модальное окно через 2 секунды
            setTimeout(() => {
                $('#passwordChangeModal').modal('hide');
                passwordChangeForm.reset();
                successMessage.remove();
            }, 2000);
        } else {
            document.getElementById('passwordChangeError').textContent = data.message || 'Ошибка при изменении пароля';
            document.getElementById('passwordChangeError').style.display = 'block';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('passwordChangeError').textContent = 'Ошибка при изменении пароля';
        document.getElementById('passwordChangeError').style.display = 'block';
    });
}

// ========== ВАЛИДАЦИЯ EMAIL ==========
function initEmailVerification() {
    // Обработчик для кнопки подтверждения email
    document.getElementById('verifyEmailButton')?.addEventListener('click', verifyEmailCode);

    // Очистка формы при закрытии модального окна
    $('#emailVerificationModal').on('hidden.bs.modal', function() {
        document.getElementById('verificationCodeInput').value = '';
        document.getElementById('verificationError').style.display = 'none';
    });
}

function verifyEmailCode() {
    const code = document.getElementById('verificationCodeInput').value.trim();
    const email = document.getElementById('userEmail').value.trim();

    if (!code) {
        document.getElementById('verificationError').textContent = 'Введите код подтверждения';
        document.getElementById('verificationError').style.display = 'block';
        return;
    }

    fetch('/confirm-code', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `email=${encodeURIComponent(email)}&code=${encodeURIComponent(code)}`
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Ошибка сети');
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            document.getElementById('verificationError').style.display = 'none';
            $('#emailVerificationModal').modal('hide');

            // Показываем сообщение об успехе
            const successMessage = document.createElement('div');
            successMessage.className = 'alert alert-success';
            successMessage.textContent = 'Email успешно подтвержден';
            document.getElementById('changeUserFormSubmitButton').before(successMessage);

            setTimeout(() => successMessage.remove(), 3000);
        } else {
            document.getElementById('verificationError').textContent = data.message || 'Неверный код';
            document.getElementById('verificationError').style.display = 'block';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('verificationError').textContent = 'Ошибка при проверке кода';
        document.getElementById('verificationError').style.display = 'block';
    });
}

// ========== ОБНОВЛЕНИЕ ПРОФИЛЯ ==========
function updateUserProfile() {
    const formData = new FormData();
    const avatarInput = document.getElementById('userPic');
    const emailChanged = document.getElementById('userEmail').value !== document.getElementById('userEmail').defaultValue;

    if (avatarInput.files[0]) {
        formData.append('avatar', avatarInput.files[0]);
    }

    formData.append('lastName', document.getElementById('userLastName').value);
    formData.append('firstName', document.getElementById('userFirstName').value);
    formData.append('fatherName', document.getElementById('userFatherName').value);
    formData.append('email', document.getElementById('userEmail').value);
    formData.append('phone', document.getElementById('userPhone').value);

    fetch('/account/changeUser', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Ошибка сети');
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            alert('Данные успешно обновлены!');

            if (data.avatarUrl) {
                document.querySelector('.user-avatar').src = data.avatarUrl;
            }

            if (emailChanged) {
                // Показываем модальное окно подтверждения email
                document.getElementById('verificationEmailDisplay').textContent =
                    document.getElementById('userEmail').value;
                $('#emailVerificationModal').modal('show');
            }
        }
    })
    .catch(error => {
        console.error('Ошибка:', error);
        alert('Произошла ошибка при обновлении данных');
    });
}

// ========== ВЫВОД АДРЕСОВ ==========
function renderAddresses() {
    const addressesData = JSON.parse(document.getElementById('addresses-data')?.textContent || '[]');
    const addresses = JSON.parse(addressesData);
    const addressesBody = document.getElementById('addresses-body');

    addressesBody.innerHTML = '';

    addresses.forEach(address => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${address.city || 'Не указан'}</td>
            <td>${address.street || ''} ${address.house || ''} ${address.apartment || ''}</td>
        `;
        addressesBody.appendChild(row);
    });
}

// ========== ВЫВОД КОНТАКТОВ ==========
function renderContacts() {
    const contactsData = JSON.parse(document.getElementById('contacts-data')?.textContent || '[]');
    const contacts = JSON.parse(contactsData);
    const contactsBody = document.getElementById('contacts-body');

    contactsBody.innerHTML = '';

    contacts.forEach(contact => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${contact.lastName} ${contact.firstName} ${contact.fatherName || ''}</td>
            <td>${contact.phone}</td>
            <td>${contact.email}</td>
        `;
        contactsBody.appendChild(row);
    });
}