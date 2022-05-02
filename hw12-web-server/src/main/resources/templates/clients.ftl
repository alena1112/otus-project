<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Клиенты</title>
    <script>
        function saveClient() {
            const clientNameTextBox = document.getElementById('clientNameTextBox');
            const clientAddressTextBox = document.getElementById('clientAddressTextBox');
            const clientPhoneTextBox = document.getElementById('clientPhoneTextBox');
            const clientDataContainer = document.getElementById('clientDataContainer');
            fetch('api/client', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    name: clientNameTextBox.value,
                    address: clientAddressTextBox.value,
                    phone: clientPhoneTextBox.value
                })
            }).then(function (response) {
                return response.text().then(function (text) {
                    clientDataContainer.innerHTML = text;
                })
            });
        }
    </script>
</head>

<body>
<h4>Создать клиента</h4>

<label for="clientNameTextBox">Имя:</label>
<input type="text" id="clientNameTextBox">
<br/><br/>
<label for="clientAddressTextBox">Адрес:</label>
<input type="text" id="clientAddressTextBox">
<br/><br/>
<label for="clientPhoneTextBox">Телефон:</label>
<input type="text" id="clientPhoneTextBox">
<br/><br/>
<button onclick="saveClient()">Создать</button>
<pre id="clientDataContainer"></pre>

<h4>Все клиенты</h4>
<table style="width: 400px">
    <thead>
    <tr>
        <td style="width: 50px">Id</td>
        <td style="width: 150px">Имя</td>
        <td style="width: 150px">Адрес</td>
        <td style="width: 150px">Телефон</td>
    </tr>
    </thead>
    <tbody>
    <#list allClients as client>
        <tr>
            <td>${client.id}</td>
            <td>${client.name}</td>
            <td><#if client.address??>${client.address.street}</#if></td>
            <td>
                <#if client.phones??>
                    <#list client.phones as phone>
                        ${phone.number}<#sep>, </#sep>
                    </#list>
                </#if>
            </td>
        </tr>
    </#list>
    </tbody>
</table>
</body>
</html>
