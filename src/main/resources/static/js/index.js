//Установка даты на завтрашнее число
document.addEventListener('DOMContentLoaded', function() {
    const dateInput = document.getElementById("shipmentDateInput");
    const today = new Date();
    dateInput.valueAsDate = today;
});

//Добавление полей для ввода груза
addPackageButton = document.getElementById('addPackageButton')
addPackageButton.addEventListener('click', () => {
    console.log('inside the newPackage function')
    addedPackageDiv = document.getElementById('package')

    var quantityOfCargo = document.querySelectorAll('#package').length
    newDiv = `
        <div class="form-row" id="package">
            <div class="form-group col-md-2">
                <input type="number" name="packages[` + quantityOfCargo + `].packageParams.weight" class="form-control" id="packageWeight" placeholder="Вес(гр)" min="1" required>
            </div>
            <div class="form-group col-md-2">
                <input type="number" name="packages[` + quantityOfCargo + `].packageParams.length" class="form-control" id="packagrLength" placeholder="Длина(см)" min="1" required>
            </div>
            <div class="form-group col-md-2">
                <input type="number" name="packages[` + quantityOfCargo + `].packageParams.width" class="form-control" id="packageWidth" placeholder="Ширина(см)" min="1" required>
            </div>
            <div class="form-group col-md-2">
                <input type="number" name="packages[` + quantityOfCargo + `].packageParams.height" class="form-control" id="packageHeight" placeholder="Высота(см)" min="1" required>
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

let cities = [
               { "name": "Москва", "subject": "Москва" },
               { "name": "Пушкин", "subject": "Санкт-Петербург" },
               { "name": "Новосибирск", "subject": "Новосибирская область" },
               { "name": "Екатеринбург", "subject": "Уральский" },
               { "name": "Казань", "subject": "Поволжский" },
               { "name": "Оренбург", "subject": "Оренбургская область" },
               { "name": "Нижний Новгород", "subject": "Нижегородская область" },
               { "name": "Челябинск", "subject": "Челябинская область" },
               { "name": "Красноярск", "subject": "Красноярский край" },
               { "name": "Самара", "subject": "Самарская область" },
               { "name": "Уфа", "subject": "Башкортостан" },
               { "name": "Ростов-на-Дону", "subject": "Ростовская область" },
               { "name": "Краснодар", "subject": "Краснодарский край" },
               { "name": "Омск", "subject": "Омская область" },
               { "name": "Воронеж", "subject": "Воронежская область" },
               { "name": "Пермь", "subject": "Пермский край" },
               { "name": "Волгоград", "subject": "Волгоградская область" },
               { "name": "Саратов", "subject": "Саратовская область" },
               { "name": "Тюмень", "subject": "Тюменская область" },
               { "name": "Тольятти", "subject": "Самарская область" },
               { "name": "Ижевск", "subject": "Удмуртия" },
               { "name": "Барнаул", "subject": "Алтайский край" },
               { "name": "Иркутск", "subject": "Иркутская область" },
               { "name": "Ульяновск", "subject": "Ульяновская область" },
               { "name": "Хабаровск", "subject": "Хабаровский край" },
               { "name": "Владивосток", "subject": "Приморский край" },
               { "name": "Ярославль", "subject": "Ярославская область" },
               { "name": "Махачкала", "subject": "Дагестан" },
               { "name": "Томск", "subject": "Томская область" },
               { "name": "Новокузнецк", "subject": "Кемеровская область" },
               { "name": "Элиста", "subject": "Калмыкия" },
               { "name": "Южно-Сахалинск", "subject": "Сахалинская область" },
               { "name": "Чита", "subject": "Забайкальский край" },
               { "name": "Щелково", "subject": "Московская область" },
               { "name": "Грозный", "subject": "Чечня" },
               { "name": "Рыбинск", "subject": "Ярославская область" },
               { "name": "Фрязино", "subject": "Московская область" },
               { "name": "Йошкар-Ола", "subject": "Марий Эл" },
               { "name": "Абакан", "subject": "Хакасия" },
               { "name": "Брянск", "subject": "Брянская область" },
               { "name": "Дзержинск", "subject": "Нижегородская область" },
               { "name": "Клин", "subject": "Московская область" },
               { "name": "Липецк", "subject": "Липецкая область" }
             ]

//Получение изменений в поле ввода города отправки
fromLocationStateInput = document.getElementById('fromLocationStateInput')
fromLocationCityInput = document.getElementById('fromLocationCityInput')
fromLocationCityInput.addEventListener('input', function() {
    const fromLocationCityInputValue = this.value.toLowerCase()

    const fromLocationFilteredCities = cities.filter(city =>
        city.name.toLowerCase().startsWith(fromLocationCityInputValue)
    )
    displayFromLocationSuggestions(fromLocationFilteredCities)
})

//Создание подсказок и обработка нажатия на город из подсказки для города отправки
function displayFromLocationSuggestions(cities) {
    fromLocationSuggest.innerHTML = ''

//Создание подсказок
    cities.forEach(city => {

        const fromLocationSuggestCitiesButton = document.createElement('button')
        fromLocationSuggestCitiesButton.textContent = city.name + ', ' + city.subject
        fromLocationSuggestCitiesButton.id = "fromLocationSuggestCitiesButton"
        fromLocationSuggestCitiesButton.className = "w-100 btn btn-light"

//Обработка нажатия на город из подсказки для города отправки
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
toLocationCityInput.addEventListener('input', function() {
    const toLocationCityInputValue = this.value.toLowerCase()
    const toLocationFilteredCities = cities.filter(city =>
        city.name.toLowerCase().startsWith(toLocationCityInputValue)
    )
    displayToLocationSuggestions(toLocationFilteredCities)
})

//Создание подсказок и обработка нажатия на город из подсказки для города получения
function displayToLocationSuggestions(cities) {
    toLocationSuggest.innerHTML = ''

//Создание подсказок
    cities.forEach(city => {

        const toLocationSuggestCitiesButton = document.createElement('button')
        toLocationSuggestCitiesButton.textContent = city.name + ', ' + city.subject
        toLocationSuggestCitiesButton.id = "toLocationSuggestCitiesButton"
        toLocationSuggestCitiesButton.className = "w-100 btn btn-light"

//Обработка нажатия на город из подсказки для города получения
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
                packageParams: {}
            };
        }

        const paramName = input.name.split('.').pop(); // Извлекаем имя параметра (weight, length, width, height)
        deliveryDataJson.packages[packageIndex].packageParams[paramName] = parseInt(input.value);
    });
    document.cookie = 'delivery_data' + '=' +  encodeURIComponent(JSON.stringify(deliveryDataJson))
};