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

document.addEventListener('DOMContentLoaded', function() {
    const dateCell = document.getElementById("dateCell")
    dateCell.textContent = formatDate(dateCell.textContent);
});

//Добавление данных из формы в виде json в cookie
submitButton1 = document.getElementById("submitButton")
submitButton1.addEventListener('click', () => {
    const deliveryDataJson = JSON.parse(getCookie('delivery_data'));

    const tariffCode = document.getElementById("tariffCodeInput").value;
    const tariffName = document.getElementById("tariffNameInput").value;
    const tariffMinTime = document.getElementById("tariffMinTimeInput").value;
    const tariffMaxTime = document.getElementById("tariffMaxTimeInput").value;
    const tariffPrice = document.getElementById("tariffPriceInput").value;

    const tariffDataJson = {
        code: tariffCode,
        name: tariffName,
        minTime: tariffMinTime,
        maxTime: tariffMaxTime,
        price: tariffPrice
    };
    deliveryDataJson['tariff'] = tariffDataJson
    document.cookie = 'delivery_data' + '=' +  encodeURIComponent(JSON.stringify(deliveryDataJson))
});

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return decodeURIComponent(parts.pop().split(';').shift());
}