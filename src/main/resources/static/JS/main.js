fetch("http://localhost:7890/tasks/readAll")
    .then
    (
        function(response)
        {
            if(response.status !== 200)
            {
                console.log("Failed. Error Code: " + response.status);
                return;
            }
            response.json().then(function(tasksData)
            {
            let table = document.querySelector("table");
            let data = Object.keys(tasksData[0]);
            createTableHeader(table, data);
            createTableBody(table, tasksData);
            });
        }
    )
    .catch
    (
        function(fetchError)
        {
        console.log("Fetch Error: ", fetchError);
        }
    );

    function createTableHeader(table, data)
    {
        let tableHead = table.createTHead();
        let tableRow = tableHead.insertRow();
        let keys;
        for(keys of data)
        {
            if(keys == "Tasks")
            {
                console.log("skip");
            }
            else
            {
                let th = document.createElement("th");
                let text = document.createTextNode(keys);
                th.appendChild(text);
                tableRow.appendChild(th);
            }
        }
        let th2 = document.createElement("th");
        let text2 = document.createTextNode("View");
        let th3 = document.createElement("th");
        let text3 = document.createTextNode("Delete");
        let th4 = document.createElement("th");
        let text4 = document.createTextNode("Completed");
        th2.appendChild(text2);
        tableRow.appendChild(th2);
        th3.appendChild(text3);
        tableRow.appendChild(th3);
        th4.appendChild(text4);
        tableRow.appendChild(th4);
    }

    function createTableBody(table,tasksData)
    {
        for(let tasksRecord of tasksData)
        {
            let row = table.insertRow();
            for(let values in tasksRecord)
            {
              if(values === "Tasks")
              {
                console.log("skipped 2");
              }
              else
              {
                let cell = row.insertCell();
                let text = document.createTextNode(tasksRecord[values]);
                cell.appendChild(text);
              }
            }
            let newDataCell = row.insertCell();
            let viewButton = document.createElement("a");
            let buttonValue = document.createTextNode("View/Edit");
            viewButton.className = "btn btn-dark";
            viewButton.href = "records.html?id=" + tasksRecord.id;
            viewButton.appendChild(buttonValue);
            newCell.appendChild(viewButton);
            let newCellDelete = row.insertCell();
            let deleteButton = document.createElement("button");
            let buttonValue1 = document.createTextNode("Delete");
            deleteButton.className = "btn btn-dark";
            deleteButton.onclick = function()
            {
              deleteTasks(tasksRecord.id);
              return false;
            };
            deleteButton.appendChild(buttonValue1);
            newCellDelete.appendChild(deleteButton);
            let newCellCompleted = row.insertCell();
            let completedTickBox = document.createElement("checkbox");
            completedTickBox.className = "form-check-input";
        }
    }

    function deleteTasks(id)
    {
        fetch("http://localhost:7890/Tasks/delete/" + id, 
        {
            method: "delete",
            headers: 
            {
              "Content-type": "application/json"
            },
        })
        .then
        (
            function (data) 
            {
                console.log("Request succeeded with JSON response", data);
                let div = document.getElementById("create");
                div.className ="alert alert-primary"
                div.textContent ="Task Deleted";
                removeElement(div)
            }
        )
        .catch
        (
            function (error) 
            {
                console.log("Request failed", error);
                let div = document.getElementById("create");
                div.className ="alert alert-danger"
                div.textContent ="Error Deleting Task";
                removeElement(div)
            }
        );
    }

    function removeElement(element)
    {
        setTimeout
        (
            function () 
            {
                element.style ="display:none";
                location.reload();
            }       
        ,1000
        );
    }