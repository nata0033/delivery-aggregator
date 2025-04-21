class AccountAddresses {
    constructor(app) {
        this.app = app;
        this.bindEvents();
    }

    bindEvents() {
        $('#addAddressBtn').click(() => this.showAddAddressForm());
    }

    loadAddresses() {
        // Загрузка адресов с сервера
        // В реальном приложении здесь был бы AJAX-запрос
        this.addresses = [];
        this.renderAddresses();
    }

    renderAddresses() {
        const $tbody = $('#addressesTableBody');
        $tbody.empty();

        if (this.addresses.length === 0) {
            $tbody.append('<tr><td colspan="3" class="text-center py-4">Нет сохраненных адресов</td></tr>');
            return;
        }

        this.addresses.forEach(address => {
            $tbody.append(`
                <tr>
                    <td>${address.city || 'Не указан'}</td>
                    <td>
                        ${address.country || ''} ${address.state || ''}
                        ${address.street || ''} ${address.house || ''} ${address.apartment || ''}
                    </td>
                    <td>${address.postalCode || '—'}</td>
                </tr>
            `);
        });
    }
}