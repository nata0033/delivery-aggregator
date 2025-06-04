/**
 * Основной модуль для работы с формой доставки
 */
import { getCities } from './common/api.js';
import { FormValidator } from './common/validation.js';
import { setCookie } from './common/cookies.js';
import { loadHeader } from './common/header.js';
import { showErrorMessage } from './common/messages.js';

class DeliveryForm {
    constructor() {
        this.cities = [];
        this.defaultCities = ["Москва", "Санкт-Петербург", "Казань", "Екатеринбург", "Новосибирск"];
        document.addEventListener('DOMContentLoaded', () => this.init());
    }

    async init() {
        await this.loadCities();
        this.initShipmentDate();
        loadHeader();
        this.initEventHandlers();
    }

    async loadCities() {
        try {
            this.cities = await getCities();
            this.defaultCities = this.defaultCities
                .map(name => this.cities.find(city => city.name === name))
                .filter(Boolean);
        } catch (error) {
            console.error('Ошибка загрузки городов:', error);
        }
    }

    initShipmentDate() {
        const dateInput = document.getElementById("fromLocationDateInput");
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        dateInput.valueAsDate = tomorrow;
    }

    initEventHandlers() {
        document.getElementById('addPackageButton').addEventListener('click', () => this.addPackage());
        document.getElementById('removePackageButton').addEventListener('click', () => this.removePackage());
        document.getElementById('submitButton1').addEventListener('click', (e) => this.saveFormData(e));
        document.getElementById('submitButton2').addEventListener('click', (e) => this.saveFormData(e));
        this.initCityInputHandlers();
        this.initValidation();
        document.addEventListener('click', (e) => this.closeSuggestionsOnClickOutside(e));
    }

    initCityInputHandlers() {
        const inputs = ['fromLocationCityInput', 'toLocationCityInput'];
        inputs.forEach(id => {
            const input = document.getElementById(id);
            input.addEventListener('input', () => this.handleCityInput(id.split('Location')[0]));
        });
    }

    handleCityInput(type) {
        const input = document.getElementById(`${type}LocationCityInput`);
        const value = input.value.trim().toLowerCase();
        const filteredCities = value
            ? this.cities.filter(city => city.name.toLowerCase().startsWith(value)).slice(0, 5)
            : this.defaultCities;
        this.displayCitySuggestions(type, filteredCities);
    }

    displayCitySuggestions(type, cities) {
        const suggestElement = document.getElementById(`${type}LocationSuggest`);
        suggestElement.innerHTML = '';
        cities.forEach(city => {
            const button = document.createElement('button');
            button.className = 'w-100 btn btn-light';
            button.textContent = `${city.name}, ${city.subject}`;
            button.addEventListener('click', () => {
                const cityInput = document.getElementById(`${type}LocationCityInput`);
                const stateInput = document.getElementById(`${type}LocationStateInput`);
                cityInput.value = city.name;
                stateInput.value = city.subject;
                FormValidator.validateAndShow(cityInput, (val) => FormValidator.validateInList(val, this.cities), 'Укажите город из списка');
                suggestElement.innerHTML = '';
            });
            suggestElement.appendChild(button);
        });
    }

    closeSuggestionsOnClickOutside(event) {
        ['from', 'to'].forEach(type => {
            if (!event.target.closest(`#${type}LocationCityInput, #${type}LocationSuggest`)) {
                document.getElementById(`${type}LocationSuggest`).innerHTML = '';
            }
        });
    }

    addPackage() {
        const container = document.getElementById('packagesContainer');
        const packageCount = document.querySelectorAll('.package-form').length;
        const template = document.getElementById('package-template');
        const clone = document.importNode(template.content, true);

        clone.querySelectorAll('input').forEach(input => {
            const name = input.getAttribute('name').replace('__index__', packageCount);
            input.setAttribute('name', name);
            this.addPackageValidation(input);
        });

        container.appendChild(clone);
    }

    removePackage() {
        const packages = document.querySelectorAll('.package-form');
        if (packages.length > 1) packages[packages.length - 1].remove();
    }

    initValidation() {
        const cityInputs = document.querySelectorAll('.location');
        cityInputs.forEach(input => {
            const validate = () => FormValidator.validateAndShow(input, (val) => FormValidator.validateInList(val, this.cities), 'Укажите город из списка');
            input.addEventListener('input', validate);
            input.addEventListener('blur', validate);
        });

        const packageInputs = document.querySelectorAll('.package');
        packageInputs.forEach(input => this.addPackageValidation(input));

        this.setupPackagesObserver();
    }

    setupPackagesObserver() {
        const observer = new MutationObserver(mutations => {
            mutations.forEach(mutation => {
                mutation.addedNodes.forEach(node => {
                    if (node.nodeType === 1 && node.classList.contains('package-form')) {
                        node.querySelectorAll('.package').forEach(input => this.addPackageValidation(input));
                    }
                });
            });
        });

        observer.observe(document.getElementById('packagesContainer'), { childList: true, subtree: true });
    }

    addPackageValidation(input) {
        const validate = () => FormValidator.validateAndShow(input, FormValidator.validateNonNegativeNumber, 'Введите положительное число');
        input.addEventListener('input', validate);
        input.addEventListener('blur', validate);
    }

    isFormValid() {
        const cityInputsValid = Array.from(document.querySelectorAll('.location')).every(input =>
            input.value.trim() && FormValidator.validateInList(input.value.trim(), this.cities)
        );

        const packageInputsValid = Array.from(document.querySelectorAll('.package')).every(input =>
            FormValidator.validateNonNegativeNumber(input.value.trim())
        );

        const dateInput = document.getElementById('fromLocationDateInput');
        return cityInputsValid && packageInputsValid && dateInput.value !== '';
    }

    saveFormData(event) {
        event.preventDefault();

        if (!this.isFormValid()) {
            showErrorMessage('Пожалуйста, заполните все поля корректно');
            return;
        }

        const formData = {
            fromLocation: this.getLocationData('from'),
            toLocation: this.getLocationData('to'),
            packages: this.getPackagesData(),
            sender: null,
            recipient: null,
            tariff: null
        };

        setCookie('delivery_data', JSON.stringify(formData), 1);
        document.getElementById('deliveryDataForm').submit();
    }

    getLocationData(type) {
        return {
            country: document.getElementById(`${type}LocationCountryInput`).value,
            state: document.getElementById(`${type}LocationStateInput`).value,
            city: document.getElementById(`${type}LocationCityInput`).value,
            street: "",
            house: "",
            apartment: "",
            postalCode: "",
            date: type === 'from' ? document.getElementById('fromLocationDateInput').value : ""
        };
    }

    getPackagesData() {
        const packages = [];
        const inputs = document.querySelectorAll('[name^="packages"]');
        const packagesMap = {};

        inputs.forEach(input => {
            const [, index, field] = input.name.match(/packages\[(\d+)\]\.(\w+)/) || [];
            if (index && field) {
                if (!packagesMap[index]) packagesMap[index] = {};
                packagesMap[index][field] = parseFloat(input.value) || 0;
            }
        });

        for (const index in packagesMap) {
            packages.push({
                weight: packagesMap[index].weight,
                length: packagesMap[index].length,
                width: packagesMap[index].width,
                height: packagesMap[index].height
            });
        }

        return packages;
    }
}

new DeliveryForm();