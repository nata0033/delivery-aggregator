//Добавление полей для ввода груза
addPackageButton = document.getElementById('addPackageButton')
addPackageButton.addEventListener('click', () => {
    console.log('inside the newPackage function')
    addedPackageDiv = document.getElementById('package')

    var quantityOfCargo = document.querySelectorAll('#package').length
    newDiv = `
        <div class="form-row" id="package">
            <div class="form-group col-md-2">
                <label>
                    <input type="number" name="packages[` + quantityOfCargo + `].weight" class="form-control" placeholder="Вес" required>
                </label>
            </div>
            <div class="form-group col-md-2">
                <label>
                    <input type="number" name="packages[` + quantityOfCargo + `].length" class="form-control" placeholder="Длина">
                </label>
            </div>
            <div class="form-group col-md-2">
                <label>
                    <input type="number" name="packages[` + quantityOfCargo + `].width" class="form-control" placeholder="Ширина">
                </label>
            </div>
            <div class="form-group col-md-2">
                <label>
                    <input type="number" name="packages[` + quantityOfCargo + `].height" class="form-control" placeholder="Высота">
                </label>
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

//Полу работающий ридер джисона
//function readTextFile(file, callback) {
//    var rawFile = new XMLHttpRequest()
//    rawFile.overrideMimeType("application/json")
//    rawFile.open("GET", file, true)
//    rawFile.onreadystatechange = function() {
//        if (rawFile.readyState === 4 && rawFile.status == "200") {
//            callback(rawFile.responseText)
//        }
//    }
//    rawFile.send(null)
//}
//readTextFile("/delivery-aggregator/src/main/resources/static/json/russian-cities.json", function(text){
//    data = JSON.parse(text)
//})
//"https://gist.github.com/gorborukov/0722a93c35dfba96337b.js"

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
fromLocationInput = document.getElementById('fromLocationInput')
fromLocationInput.addEventListener('input', function() {
    const fromLocationInputValue = this.value.toLowerCase()

    const fromLocationFilteredCities = cities.filter(city =>
        city.name.toLowerCase().startsWith(fromLocationInputValue)
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
        fromLocationSuggestCitiesButton.className = "btn btn-light"

//Обработка нажатия на город из подсказки для города отправки
        fromLocationSuggestCitiesButton.addEventListener('click', function() {
        fromLocationInput.value = city.name
        fromLocationSuggest.innerHTML = ''
        })
        fromLocationSuggest.appendChild(fromLocationSuggestCitiesButton)
    })
}

//Получение изменений в поле ввода города получения
toLocationInput = document.getElementById('toLocationInput')
toLocationInput.addEventListener('input', function() {
    const toLocationInputValue = this.value.toLowerCase()
    const toLocationFilteredCities = cities.filter(city =>
        city.name.toLowerCase().startsWith(toLocationInputValue)
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
        toLocationSuggestCitiesButton.className = "btn btn-light"

//Обработка нажатия на город из подсказки для города получения
        toLocationSuggestCitiesButton.addEventListener('click', function() {
        toLocationInput.value = city.name
        toLocationSuggest.innerHTML = ''
        })
        toLocationSuggest.appendChild(toLocationSuggestCitiesButton)
    })
}