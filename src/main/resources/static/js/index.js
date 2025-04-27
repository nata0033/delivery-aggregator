//Установка даты на завтрашнее число
document.addEventListener('DOMContentLoaded', function() {
    const dateInput = document.getElementById("shipmentDateInput");
    const today = new Date();
    dateInput.valueAsDate = today;

    // Load cities from server
    loadCities();
});

let cities = [];
let defaultCities = [];

async function loadCities() {
    try {
        const response = await fetch('/getCities');
        if (!response.ok) {
            throw new Error('Failed to load cities');
        }
        cities = await response.json();

        // Set default popular cities
        defaultCities = [
            findCityByName("Москва"),
            findCityByName("Санкт-Петербург"),
            findCityByName("Казань"),
            findCityByName("Екатеринбург"),
            findCityByName("Новосибирск")
        ].filter(city => city !== undefined);
    } catch (error) {
        console.error('Error loading cities:', error);
        // Fallback to empty arrays if request fails
        cities = [];
        defaultCities = [];
    }
}

function findCityByName(name) {
    return cities.find(city => city.name === name);
}

//Добавление полей для ввода груза
addPackageButton = document.getElementById('addPackageButton')
addPackageButton.addEventListener('click', () => {
    console.log('inside the newPackage function')
    addedPackageDiv = document.getElementById('package')

    var quantityOfCargo = document.querySelectorAll('#package').length
    newDiv = `
        <div class="form-row" id="package">
            <div class="form-group col-md-2">
                <label for="packageWeight${quantityOfCargo}">Вес (гр)</label>
                <input type="number" name="packages[` + quantityOfCargo + `].weight" class="form-control" id="packageWeight${quantityOfCargo}" placeholder="Вес(гр)" min="1" value="10" required>
            </div>
            <div class="form-group col-md-2">
                <label for="packagrLength${quantityOfCargo}">Длина (см)</label>
                <input type="number" name="packages[` + quantityOfCargo + `].length" class="form-control" id="packagrLength${quantityOfCargo}" placeholder="Длина(см)" min="1" value="10" required>
            </div>
            <div class="form-group col-md-2">
                <label for="packageWidth${quantityOfCargo}">Ширина (см)</label>
                <input type="number" name="packages[` + quantityOfCargo + `].width" class="form-control" id="packageWidth${quantityOfCargo}" placeholder="Ширина(см)" min="1" value="10" required>
            </div>
            <div class="form-group col-md-2">
                <label for="packageHeight${quantityOfCargo}">Высота (см)</label>
                <input type="number" name="packages[` + quantityOfCargo + `].height" class="form-control" id="packageHeight${quantityOfCargo}" placeholder="Высота(см)" min="1" value="10" required>
            </div>
        </div>
    `
    addedPackageDiv.insertAdjacentHTML("beforeBegin", newDiv)
})

//Удаление полей для ввода груза
removePackageButton = document.getElementById('removePackageButton')
removePackageButton.addEventListener('click', () => {
    console.log('inside the removePackage function')
    removedPackageDiv = document.getElementById('package')

    if (document.querySelectorAll('#package').length > 1){
        removedPackageDiv.remove()
    }
})

// Close suggestions when clicking outside
document.addEventListener('click', function(event) {
    if (!event.target.closest('#fromLocationCityInput') && !event.target.closest('#fromLocationSuggest')) {
        fromLocationSuggest.innerHTML = '';
    }
    if (!event.target.closest('#toLocationCityInput') && !event.target.closest('#toLocationSuggest')) {
        toLocationSuggest.innerHTML = '';
    }
});

//Получение изменений в поле ввода города отправки
fromLocationStateInput = document.getElementById('fromLocationStateInput')
fromLocationCityInput = document.getElementById('fromLocationCityInput')
fromLocationSuggest = document.getElementById('fromLocationSuggest')
fromLocationCityInput.addEventListener('input', function() {
    const fromLocationCityInputValue = this.value.toLowerCase()

    let fromLocationFilteredCities;
    if (fromLocationCityInputValue === '') {
        fromLocationFilteredCities = defaultCities;
    } else {
        fromLocationFilteredCities = cities.filter(city =>
            city.name.toLowerCase().startsWith(fromLocationCityInputValue))
            .slice(0, 5); // Limit to 5 suggestions
    }
    displayFromLocationSuggestions(fromLocationFilteredCities)
})

//Создание подсказок и обработка нажатия на город из подсказки для города отправки
function displayFromLocationSuggestions(cities) {
    fromLocationSuggest.innerHTML = ''

    cities.forEach(city => {
        const fromLocationSuggestCitiesButton = document.createElement('button')
        fromLocationSuggestCitiesButton.textContent = city.name + ', ' + city.subject
        fromLocationSuggestCitiesButton.id = "fromLocationSuggestCitiesButton"
        fromLocationSuggestCitiesButton.className = "w-100 btn btn-light"

        fromLocationSuggestCitiesButton.addEventListener('click', function() {
            fromLocationStateInput.value = city.subject.split(" ")[0]
            fromLocationCityInput.value = city.name
            fromLocationSuggest.innerHTML = ''
        })
        fromLocationSuggest.appendChild(fromLocationSuggestCitiesButton)
    })
}

//Получение изменений в поле ввода города получения
toLocationStateInput = document.getElementById('toLocationStateInput')
toLocationCityInput = document.getElementById('toLocationCityInput')
toLocationSuggest = document.getElementById('toLocationSuggest')
toLocationCityInput.addEventListener('input', function() {
    const toLocationCityInputValue = this.value.toLowerCase()

    let toLocationFilteredCities;
    if (toLocationCityInputValue === '') {
        toLocationFilteredCities = defaultCities;
    } else {
        toLocationFilteredCities = cities.filter(city =>
            city.name.toLowerCase().startsWith(toLocationCityInputValue))
            .slice(0, 5); // Limit to 5 suggestions
    }
    displayToLocationSuggestions(toLocationFilteredCities)
})

//Создание подсказок и обработка нажатия на город из подсказки для города получения
function displayToLocationSuggestions(cities) {
    toLocationSuggest.innerHTML = ''

    cities.forEach(city => {
        const toLocationSuggestCitiesButton = document.createElement('button')
        toLocationSuggestCitiesButton.textContent = city.name + ', ' + city.subject
        toLocationSuggestCitiesButton.id = "toLocationSuggestCitiesButton"
        toLocationSuggestCitiesButton.className = "w-100 btn btn-light"

        toLocationSuggestCitiesButton.addEventListener('click', function() {
            toLocationStateInput.value = city.subject.split(" ")[0]
            toLocationCityInput.value = city.name
            toLocationSuggest.innerHTML = ''
        })
        toLocationSuggest.appendChild(toLocationSuggestCitiesButton)
    })
}

submitButton1 = document.getElementById("submitButton1")
submitButton1.addEventListener('click', addJson)
submitButton2 = document.getElementById("submitButton2")
submitButton2.addEventListener('click', addJson)

//Добавление данных из формы в виде json в cookie
function addJson(){
    const fromLocationState = document.getElementById("fromLocationStateInput").value;
    const fromLocationCity = document.getElementById("fromLocationCityInput").value;
    const toLocationState = document.getElementById("toLocationStateInput").value;
    const toLocationCity = document.getElementById("toLocationCityInput").value;

    const deliveryDataJson = {
        fromLocation: {
            state: fromLocationState,
            city: fromLocationCity
        },
        toLocation: {
            state: toLocationState,
            city: toLocationCity
        },
        packages: [],
        tariff: {}
    };
    const packageInputs = document.querySelectorAll('[name^="packages"]');
    packageInputs.forEach(input => {
        const packageName = input.name.match(/\[(\d+)\]/)[1];
        const packageIndex = parseInt(packageName);

        if (!deliveryDataJson.packages[packageIndex]) {
            deliveryDataJson.packages[packageIndex] = {
            };
        }

        const paramName = input.name.split('.').pop(); // Извлекаем имя параметра (weight, length, width, height)
        deliveryDataJson.packages[packageIndex][paramName] = parseInt(input.value);
    });
    document.cookie = 'delivery_data' + '=' +  encodeURIComponent(JSON.stringify(deliveryDataJson))
};