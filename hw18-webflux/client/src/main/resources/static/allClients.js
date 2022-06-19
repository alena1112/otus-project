const streamErr = e => {
    console.warn("error");
    console.warn(e);
}

fetch("http://localhost:8081/api/client").then((response) => {
    return can.ndjsonStream(response.body);
}).then(dataStream => {
    const reader = dataStream.getReader();
    const read = result => {
        if (result.done) {
            return;
        }
        render(result.value);
        reader.read().then(read, streamErr);
    }
    reader.read().then(read, streamErr);
});

const render = value => {
    const table = document.getElementById("clients");

    for (let i = 0; i < value.length; i++) {
        let client = value[i];

        let newRow = table.insertRow();
        newRow.insertCell().appendChild(document.createTextNode(client.id));
        newRow.insertCell().appendChild(document.createTextNode(client.name));
        newRow.insertCell().appendChild(document.createTextNode(client.address));
        newRow.insertCell().appendChild(document.createTextNode(client.phone));
    }
};