class AccountProfile {
    constructor(app) {
        this.app = app;
        this.bindEvents();
    }

    bindEvents() {
        $('#profileForm').submit(e => this.handleProfileFormSubmit(e));
        $('#changePasswordBtn').click(() => $('#passwordModal').modal('show'));
        $('#passwordForm').submit(e => this.handlePasswordFormSubmit(e));
    }

    handleProfileFormSubmit(e) {
        e.preventDefault();
        // ... (логика валидации и отправки формы)
    }
}