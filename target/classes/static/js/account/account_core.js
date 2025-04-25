/**
 * Основной класс приложения личного кабинета
 * Управляет инициализацией, загрузкой данных и навигацией
 */
class AccountApp {
    constructor() {
        this.userData = null;
        this.sendOrders = [];
        this.receivedOrders = [];
        this.sortStates = {
            sendOrders: { field: 'date', direction: 1 },
            receivedOrders: { field: 'date', direction: 1 }
        };

        this.init();
        this.setupLoadingSpinner();
    }

    /**
     * Инициализирует приложение
     * - Настраивает обработчики событий
     * - Загружает данные пользователя
     */
    init() {
        this.bindEvents();
        this.loadUserData();
        this.orders = new AccountOrders(this);
    }

    // ======================
    // Методы загрузки данных
    // ======================

    /**
     * Загружает данные пользователя с сервера
     */
    loadUserData() {
        $('#loadingOverlay').show();

        $.ajax({
            url: '/account/accountPageData',
            method: 'GET',
            success: (response) => {
                if (response.success) {
                    this.userData = response.data.userData;
                    this.sendOrders = response.data.sendOrders || [];
                    this.receivedOrders = response.data.receivedOrders || [];

                    this.updateUserProfile();
                    this.orders.renderOrders();
                }
            },
            error: (xhr) => {
                if (xhr.status === 401) window.location.href = '/login';
            },
            complete: () => {
                $('#loadingOverlay').hide();
            }
        });
    }

    // ======================
    // Методы UI
    // ======================

    /**
     * Настраивает индикатор загрузки
     */
    setupLoadingSpinner() {
        if (!$('#loadingOverlay').length) {
            $('body').prepend(`
                <div id="loadingOverlay" style="
                    position: fixed;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    background-color: rgba(0,0,0,0.5);
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    z-index: 9999;
                ">
                    <div class="spinner-border text-light" style="width: 3rem; height: 3rem;"></div>
                </div>
            `);
        }
    }

    /**
     * Обновляет информацию профиля в интерфейсе
     */
    updateUserProfile() {
        const user = this.userData;
        $('#userFullName').text(`${user.lastName} ${user.firstName} ${user.fatherName || ''}`);

        const $contactsContainer = $('#userContacts');
        if (!$contactsContainer.length) {
            $('.author-card-details').append('<div id="userContacts" class="mt-3"></div>');
        }

        $('#userContacts').html(`
            <div class="d-flex flex-column">
                <div class="mb-2">
                    <i class="fas fa-phone me-2"></i>
                    <span>${user.phone || 'Не указан'}</span>
                </div>
                <div>
                    <i class="fas fa-envelope me-2"></i>
                    <span>${user.email}</span>
                </div>
            </div>
        `);

        if (user.pic) {
            $('#userAvatar').attr('src', user.pic);
        }

        $('#ordersCount').text(this.sendOrders.length + this.receivedOrders.length);
        $('#sendOrdersCount').text(this.sendOrders.length);
        $('#receivedOrdersCount').text(this.receivedOrders.length);
    }

    /**
     * Переключает отображаемый раздел
     * @param {string} section - Идентификатор раздела
     */
    showSection(section) {
        $('.content-section').hide();
        $(`#${section}Content`).show();
        $('.list-group-item').removeClass('active');
        $(`#${section}Button`).addClass('active');
    }

    // ======================
    // Обработчики событий
    // ======================

    /**
     * Настраивает обработчики событий
     */
    bindEvents() {
        // Навигация
        $('#deliveriesButton').click(() => this.showSection('deliveries'));
        $('#accountEditButton').click(() => this.showSection('accountEdit'));
        $('#addressesButton').click(() => this.showSection('addresses'));
        $('#recipientsButton').click(() => this.showSection('recipients'));
    }
}

// Инициализация приложения после загрузки DOM
$(document).ready(() => new AccountApp());