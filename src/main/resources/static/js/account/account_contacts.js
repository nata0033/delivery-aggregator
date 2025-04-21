class AccountContacts {
    constructor(app) {
        this.app = app;
        this.bindEvents();
    }

    bindEvents() {
        $('#addRecipientBtn').click(() => this.showAddRecipientForm());
    }

    loadRecipients() {
        // Загрузка получателей с сервера
        // В реальном приложении здесь был бы AJAX-запрос
        this.recipients = [];
        this.renderRecipients();
    }

    renderRecipients() {
        const $tbody = $('#recipientsTableBody');
        $tbody.empty();

        if (this.recipients.length === 0) {
            $tbody.append('<tr><td colspan="3" class="text-center py-4">Нет сохраненных получателей</td></tr>');
            return;
        }

        this.recipients.forEach(recipient => {
            $tbody.append(`
                <tr>
                    <td>${recipient.lastName} ${recipient.firstName} ${recipient.fatherName || ''}</td>
                    <td>${recipient.phone || '—'}</td>
                    <td>${recipient.email || '—'}</td>
                </tr>
            `);
        });
    }
}