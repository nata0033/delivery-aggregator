//Динамическое изменение контенто через боковое меню
document.addEventListener('DOMContentLoaded', function() {
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
    });

    menuButtons.recipients?.addEventListener('click', function(e) {
        e.preventDefault();
        activateMenu('recipients', 'contacts');
    });

    activateMenu('deliveries', 'deliveries');
});

function formatDate(dateString) {
    if (!dateString) return '—';

    try {
        const date = new Date(dateString);
        if (isNaN(date.getTime())) return dateString; // Если дата некорректна

        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();

        return `${day}-${month}-${year}`;
    } catch (e) {
        console.error('Error formatting date:', e);
        return dateString;
    }
}

//Подробная добавление заказа
document.addEventListener('DOMContentLoaded', function() {
    //Получение данных с бека
    const ordersData = {
        orders: JSON.parse(document.getElementById('orders-data').textContent)
    };

    const ordersBody = document.getElementById('orders-body');

    function renderOrders() {
        ordersBody.innerHTML = '';

        ordersData.orders.forEach(order => {
            const mainRow = document.createElement('tr');
            mainRow.dataset.orderNumber = order.number;

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

            const dateCell = document.createElement('td');
            dateCell.textContent = formatDate(order.date);
            mainRow.appendChild(dateCell);

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

            const priceCell = document.createElement('td');
            priceCell.textContent = order.price + '₽';
            mainRow.appendChild(priceCell);

            ordersBody.appendChild(mainRow);

//Детали заказа
            const detailsRow = document.createElement('tr');
            detailsRow.className = 'order-details';
            detailsRow.dataset.orderNumber = order.number;
            detailsRow.style.display = 'none';

            const detailsCell = document.createElement('td');
            detailsCell.colSpan = 4;

            const serviceNameInfo = document.createElement('div');
            serviceNameInfo.className = 'mb-2';
            serviceNameInfo.innerHTML = `<strong>Сервис доставки:</strong> ${order.serviceName}`;
            detailsCell.appendChild(serviceNameInfo);

            const recipientInfo = document.createElement('div');
            recipientInfo.className = 'mb-2';
            recipientInfo.innerHTML = `<strong>Получатель:</strong> ${order.recipient.lastName} ${order.recipient.firstName} ${order.recipient.fatherName}, ${order.recipient.phone}`;
            detailsCell.appendChild(recipientInfo);

            const packagesInfo = document.createElement('div');
            const totalWeight = order.packages.reduce((sum, pkg) => sum + pkg.weight, 0);
            packagesInfo.innerHTML = `<strong>Грузы:</strong> ${order.packages.length} шт., Общий вес: ${totalWeight} кг`;
            detailsCell.appendChild(packagesInfo);

            detailsRow.appendChild(detailsCell);
            ordersBody.appendChild(detailsRow);
        });
    }

    // Toggle order details visibility
    function toggleOrderDetails(orderNumber) {
        const detailsRow = document.querySelector(`tr.order-details[data-order-number="${orderNumber}"]`);
        if (detailsRow) {
            detailsRow.style.display = detailsRow.style.display === 'none' ? 'table-row' : 'none';
        }
    }

    // Initial render
    renderOrders();

    // Sort functionality (placeholder)
    document.getElementById('order-sort').addEventListener('change', function() {
        // Implement sorting logic here based on selected option
        console.log('Sorting by:', this.value);
        renderOrders();
    });
});

//Обновление профиля
function updateUserProfile() {
    // Собираем данные формы
    const formData = new FormData();

    // Добавляем файл аватарки, если выбран
    const avatarInput = document.getElementById('userPic');
    if (avatarInput.files[0]) {
        formData.append('avatar', avatarInput.files[0]);
    }

    // Добавляем остальные данные
    formData.append('lastName', document.getElementById('userLastName').value);
    formData.append('firstName', document.getElementById('userFirstName').value);
    formData.append('fatherName', document.getElementById('userFatherName').value);
    formData.append('email', document.getElementById('userEmail').value);
    formData.append('phone', document.getElementById('userPhone').value);

    // Отправляем AJAX-запрос
    fetch('/account/changeUser', {
        method: 'POST',
        body: formData,
        headers: {
            // Для файлов Content-Type устанавливается автоматически с boundary
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Ошибка сети');
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            // Обновляем данные на странице без перезагрузки
            // Можно добавить уведомление об успешном сохранении
            alert('Данные успешно обновлены!');

            // Если нужно обновить аватарку
            if (data.avatarUrl) {
                document.querySelector('.user-avatar').src = data.avatarUrl;
            }
        } else {
            alert('Ошибка: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Ошибка:', error);
        alert('Произошла ошибка при обновлении данных');
    });
}