/**
 * Класс для управления отображением заказов в личном кабинете
 */
class AccountOrders {
    constructor(app) {
        this.app = app;
        this.bindEvents();
    }

    // ======================
    // Основные методы
    // ======================

    /**
     * Отрисовывает все заказы (отправляемые и получаемые)
     */
    renderOrders() {
        this.renderOrdersTable('sendOrders', this.app.sendOrders);
        this.renderOrdersTable('receivedOrders', this.app.receivedOrders);
    }

    /**
     * Отрисовывает таблицу заказов
     * @param {string} tableType - Тип таблицы ('sendOrders' или 'receivedOrders')
     * @param {Array} orders - Массив заказов
     */
    renderOrdersTable(tableType, orders) {
        const $tbody = $(`#${tableType}TableBody`);
        $tbody.empty();

        if (!orders || orders.length === 0) {
            $tbody.append('<tr><td colspan="4" class="text-center py-4">Нет данных о доставках</td></tr>');
            return;
        }

        orders.forEach(order => {
            const statusClass = this.getStatusClass(order.status);
            const statusText = this.getStatusText(order.status);
            const formattedDate = this.formatDateToDDMMYYYY(order.date);

            $tbody.append(`
                <tr data-order-id="${order.id}">
                    <td>${order.number || '—'}</td>
                    <td>${formattedDate}</td>
                    <td><span class="badge ${statusClass}">${statusText}</span></td>
                    <td>${order.price ? order.price + '₽' : '—'}</td>
                </tr>
                <tr class="order-details" data-order-id="${order.id}" style="display: none;">
                    <td colspan="4" class="p-0">
                        <div class="order-details-content">
                            ${this.getOrderDetailsHtml(order)}
                        </div>
                    </td>
                </tr>
            `);
        });
    }

    /**
     * Сортирует заказы по указанному полю
     * @param {string} tableType - Тип таблицы ('sendOrders' или 'receivedOrders')
     * @param {string} field - Поле для сортировки
     */
    sortOrders(tableType, field) {
        const currentSort = this.app.sortStates[tableType];

        if (field === currentSort.field) {
            currentSort.direction *= -1;
        } else {
            currentSort.field = field;
            currentSort.direction = 1;
        }

        const orders = tableType === 'sendOrders' ? this.app.sendOrders : this.app.receivedOrders;
        orders.sort((a, b) => this.compareItems(a, b, currentSort.field, currentSort.direction));

        this.updateSortIndicators(tableType, currentSort.field, currentSort.direction);
        this.renderOrdersTable(tableType, orders);
    }

    // ======================
    // Вспомогательные методы
    // ======================

    /**
     * Сравнивает два элемента для сортировки
     */
    compareItems(a, b, field, direction) {
        if (field === 'date') {
            return (new Date(a.date) - new Date(b.date)) * direction;
        }
        if (field === 'price') {
            return ((a.price || 0) - (b.price || 0)) * direction;
        }
        return String(a[field] || '').localeCompare(String(b[field] || '')) * direction;
    }

    /**
     * Обновляет индикаторы сортировки в заголовках таблицы
     */
    updateSortIndicators(tableType, field, direction) {
        $(`#${tableType}Table .sortable i`).remove();
        $(`#${tableType}Table th[data-sort="${field}"]`).append(
            `<i class="fas fa-sort-${direction === 1 ? 'up' : 'down'} ms-2"></i>`
        );
    }

    /**
     * Генерирует HTML для деталей заказа
     */
    getOrderDetailsHtml(order) {
        const totalWeight = order.packages?.reduce((sum, pkg) => sum + (pkg.weight || 0), 0) || 0;

        return `
            <div class="details-row">
                <span class="details-label">Сервис:</span>
                <span>${order.serviceName || '—'}</span>
            </div>
            <div class="details-row">
                <span class="details-label">Получатель:</span>
                <span>${order.recipient?.lastName} ${order.recipient?.firstName}</span>
            </div>
            <div class="details-row">
                <span class="details-label">Телефон:</span>
                <span>${order.recipient?.phone || '—'}</span>
            </div>
            <div class="details-row">
                <span class="details-label">Откуда:</span>
                <span>${order.fromLocation || '—'}</span>
            </div>
            <div class="details-row">
                <span class="details-label">Куда:</span>
                <span>${order.toLocation || '—'}</span>
            </div>
            <div class="details-row">
                <span class="details-label">Посылки:</span>
                <span>${order.packages?.length} шт. (${totalWeight} кг)</span>
            </div>
        `;
    }

    /**
     * Возвращает класс CSS для статуса заказа
     */
    getStatusClass(status) {
        switch(status) {
            case 'ACCEPTED': return 'bg-primary';
            case 'DELIVERED': return 'bg-success';
            case 'CANCELED': return 'bg-danger';
            default: return 'bg-secondary';
        }
    }

    /**
     * Возвращает текст для статуса заказа
     */
    getStatusText(status) {
        switch(status) {
            case 'ACCEPTED': return 'Принят';
            case 'DELIVERED': return 'Доставлен';
            case 'CANCELED': return 'Отменён';
            default: return status;
        }
    }

    /**
     * Форматирует дату в формате DD.MM.YYYY
     */
    formatDateToDDMMYYYY(dateString) {
        const date = new Date(dateString);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        return `${day}.${month}.${year}`;
    }

    /**
     * Переключает видимость подраздела заказов
     */
    toggleSubsection(subsection) {
        $(`#${subsection}Content`).slideToggle();
        $(`#${subsection}Header i`).toggleClass('fa-chevron-down fa-chevron-up');
    }

    // ======================
    // Обработчики событий
    // ======================

    /**
     * Настраивает обработчики событий
     */
    bindEvents() {
        // Сортировка таблиц
        $(document).on('click', '#sendOrdersTable .sortable', (e) => {
            const field = $(e.currentTarget).data('sort');
            this.sortOrders('sendOrders', field);
        });

        $(document).on('click', '#receivedOrdersTable .sortable', (e) => {
            const field = $(e.currentTarget).data('sort');
            this.sortOrders('receivedOrders', field);
        });

        // Подразделы заказов
        $('#sendOrdersHeader').click(() => this.toggleSubsection('sendOrders'));
        $('#receivedOrdersHeader').click(() => this.toggleSubsection('receivedOrders'));

        // Клик по строке заказа
        $(document).on('click', '[data-order-id]', (e) => {
            if (!$(e.target).closest('.order-details-content').length) {
                const orderId = $(e.currentTarget).data('order-id');
                $(`.order-details[data-order-id="${orderId}"]`).toggle();
            }
        });
    }
}